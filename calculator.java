import subprocess
import json
import re
import os
import sys
from pathlib import Path

def check_hardcoded_secrets(content):
    patterns = {
        'AWS Access Key': r'(?:A3T[A-Z0-9]|AKIA|AGPA|AIDA|AROA|AIPA|ANPA|ANVA|ASIA)[A-Z0-9]{16}',
        'AWS Secret Key': r'(?i)aws(.{0,20})?[\'\"][0-9a-zA-Z/+]{40}[\'\"]',
        'Facebook Access Token': r'EAACEdEose0cBA[0-9A-Za-z]+',
        'Facebook OAuth': r'[fF][aA][cC][eE][bB][oO][oO][kK].*[\'\"][0-9a-f]{32}[\'\"]',
        'GitHub Token': r'ghp_[0-9a-zA-Z]{36}',
        'GitHub OAuth': r'[gG][iI][tT][hH][uU][bB].*[\'\"][0-9a-f]{40}[\'\"]',
        'Google API Key': r'AIza[0-9A-Za-z-_]{35}',
        'Google OAuth': r'[0-9]+-[0-9A-Za-z_]{32}\.apps\.googleusercontent\.com',
        'Heroku API Key': r'[hH][eE][rR][oO][kK][uU].*[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}',
        'Mailgun API Key': r'key-[0-9a-f]{32}',
        'Mailchimp API Key': r'[0-9a-f]{32}-us[0-9]{1,2}',
        'Password in URL': r'[://][^:]+:[^@]+@',
        'Private IP': r'\b(?:10|127|172\.(?:1[6-9]|2[0-9]|3[01])|192\.168)(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}\b',
        'SSH Private Key': r'-----BEGIN ((RSA|DSA|EC|OPENSSH) )?PRIVATE KEY-----',
        'Slack Token': r'xox[baprs]-([0-9]{10,13}-)?[0-9a-zA-Z]{10,48}',
        'Slack Webhook': r'https://hooks\.slack\.com/services/T[a-zA-Z0-9_]+/B[a-zA-Z0-9_]+/[a-zA-Z0-9_]+',
        'Stripe API Key': r'sk_live_[0-9a-zA-Z]{24}',
        'Stripe Restricted Key': r'rk_live_[0-9a-zA-Z]{24}',
        'Twilio API Key': r'SK[0-9a-fA-F]{32}',
        'Generic API Key': r'(?i)^(?:api[_-]?key|apikey|secret|token|pass|password|credential)s?[\'\"\s:=]+[^\s\'\"=]{8,}[\'\"\s]?$'
    }
    
    findings = []
    for secret_type, pattern in patterns.items():
        matches = re.finditer(pattern, content, re.IGNORECASE)
        for match in matches:
            line_start = content.rfind('\n', 0, match.start()) + 1
            line_end = content.find('\n', match.start())
            if line_end == -1:
                line_end = len(content)
            line = content[line_start:line_end].strip()
            findings.append({
                'type': secret_type,
                'value': match.group()[:20] + '...' if len(match.group()) > 20 else match.group(),
                'line': line
            })
    return findings

def parse_requirements(content):
    packages = []
    for line in content.splitlines():
        line = line.strip()
        if line and not line.startswith('#'):
            parts = re.split(r'[<>=!~]', line, 1)
            if parts:
                package_name = parts[0].strip()
                version = line[len(package_name):].strip() if len(parts) > 1 else ''
                packages.append({'name': package_name, 'version': version, 'raw': line})
    return packages

def parse_package_json(content):
    try:
        data = json.loads(content)
        dependencies = {}
        for dep_type in ['dependencies', 'devDependencies', 'peerDependencies']:
            if dep_type in data:
                for pkg, ver in data[dep_type].items():
                    dependencies[pkg] = {'version': ver, 'type': dep_type}
        return dependencies
    except json.JSONDecodeError:
        return {}

def check_outdated_python(packages):
    outdated = []
    try:
        result = subprocess.run([sys.executable, '-m', 'pip', 'list', '--outdated', '--format=json'], 
                              capture_output=True, text=True, timeout=30)
        if result.returncode == 0:
            outdated_data = json.loads(result.stdout)
            outdated_names = {item['name'].lower() for item in outdated_data}
            for pkg in packages:
                if pkg['name'].lower() in outdated_names:
                    outdated.append(pkg['name'])
    except Exception:
        pass
    return outdated

def check_outdated_node(dependencies):
    outdated = []
    try:
        result = subprocess.run(['npm', 'outdated', '--json'], 
                              capture_output=True, text=True, timeout=30)
        if result.returncode == 0 and result.stdout.strip():
            outdated_data = json.loads(result.stdout)
            for pkg in outdated_data:
                if pkg in dependencies:
                    outdated.append(pkg)
    except Exception:
        pass
    return outdated

def analyze_file(filepath, file_type):
    try:
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
    except Exception:
        return {'secrets': [], 'outdated': []}
    
    secrets = check_hardcoded_secrets(content)
    outdated = []
    
    if file_type in ['.txt', '.cfg', '.ini'] and 'requirements' in filepath.name.lower():
        packages = parse_requirements(content)
        if packages:
            outdated = check_outdated_python(packages)
    
    elif file_type == '.json' and filepath.name.lower() == 'package.json':
        dependencies = parse_package_json(content)
        if dependencies:
            outdated = check_outdated_node(dependencies)
    
    return {'secrets': secrets, 'outdated': outdated}

def main():
    current_dir = Path.cwd()
    results = {'secrets': [], 'outdated': [], 'files_analyzed': 0}
    
    file_extensions = {'.py', '.js', '.ts', '.java', '.go', '.rs', '.rb', '.php', '.json', 
                       '.txt', '.cfg', '.ini', '.yaml', '.yml', '.toml', '.env'}
    
    for filepath in current_dir.rglob('*'):
        if filepath.is_file() and filepath.suffix in file_extensions:
            results['files_analyzed'] += 1
            file_result = analyze_file(filepath, filepath.suffix)
            if file_result['secrets']:
                for secret in file_result['secrets']:
                    results['secrets'].append({
                        'file': str(filepath.relative_to(current_dir)),
                        **secret
                    })
            if file_result['outdated']:
                results['outdated'].append({
                    'file': str(filepath.relative_to(current_dir)),
                    'packages': file_result['outdated']
                })
    
    print(json.dumps(results, indent=2))

if __name__ == '__main__':
    main()