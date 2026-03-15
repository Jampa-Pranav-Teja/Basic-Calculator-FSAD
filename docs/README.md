# Security Scanner for Hardcoded Secrets & Outdated Dependencies

A lightweight Python-based security tool that scans project files for **hardcoded secrets** (API keys, tokens, passwords) and identifies **outdated dependencies** in Python and Node.js projects.

## 🔍 Features

- **Secret Detection**: Scans for 20+ types of hardcoded secrets including:
  - Cloud provider keys (AWS, Google, Heroku)
  - Social media tokens (Facebook, Slack, Twitter)
  - Payment API keys (Stripe, Twilio)
  - Generic passwords and API keys
  - SSH private keys
  - URLs with embedded credentials

- **Dependency Analysis**:
  - Detects outdated Python packages from `requirements.txt`
  - Detects outdated Node.js packages from `package.json`
  - Lists package names with available updates

- **Multi-language Support**: Scans Python, JavaScript, TypeScript, Java, Go, Rust, Ruby, PHP, and configuration files

## 📦 Supported File Types

| Category | Extensions |
|----------|------------|
| **Source Code** | `.py`, `.js`, `.ts`, `.java`, `.go`, `.rs`, `.rb`, `.php` |
| **Configuration** | `.json`, `.txt`, `.cfg`, `.ini`, `.yaml`, `.yml`, `.toml`, `.env` |

## 🚀 Quick Start

### Prerequisites
- Python 3.6+
- `pip` (for Python package checks)
- `npm` (for Node.js package checks - optional)

### Installation
```bash
# Clone or download the script
git clone <repository-url>
cd security-scanner

# Make executable (Linux/Mac)
chmod +x scanner.py
```

### Usage
```bash
# Scan current directory
python scanner.py

# Scan specific directory
cd /path/to/project && python /path/to/scanner.py
```

### Output Format
Results are printed as **JSON** with two main sections:

```json
{
  "secrets": [
    {
      "file": "config/settings.py",
      "type": "AWS Access Key",
      "value": "AKIAIOSFODNN7EXAMPLE...",
      "line": "AWS_KEY = 'AKIAIOSFODNN7EXAMPLE'"
    }
  ],
  "outdated": [
    {
      "file": "requirements.txt",
      "packages": ["django", "requests"]
    }
  ],
  "files_analyzed": 42
}
```

## 🔎 What Gets Scanned

### Secret Patterns (Partial List)
| Secret Type | Example Pattern |
|-------------|-----------------|
| AWS Access Key | `AKIA[0-9A-Z]{16}` |
| GitHub Token | `ghp_[0-9a-zA-Z]{36}` |
| Google API Key | `AIza[0-9A-Za-z-_]{35}` |
| Stripe API Key | `sk_live_[0-9a-zA-Z]{24}` |
| Slack Token | `xox[baprs]-[0-9a-zA-Z]{10,48}` |
| SSH Private Key | `-----BEGIN PRIVATE KEY-----` |
| Password in URL | `https://user:pass@example.com` |
| Generic API Key | `api_key['"]?[:=]['"]?[^\s'"]{8,}` |

### Dependency Files Analyzed
- **Python**: `requirements.txt`, `requirements/*.txt`, `setup.cfg`, `Pipfile`, `pyproject.toml`
- **Node.js**: `package.json` (all dependency types)

## ⚠️ Limitations & Notes

1. **False Positives Possible**:
   - The "Generic API Key" pattern may match non-secret strings
   - Review all findings manually
   - Add legitimate keys to `.gitignore` or environment variables

2. **External Commands**:
   - Outdated checks require `pip`/`npm` to be in PATH
   - Network connection needed for version lookups
   - Timeout after 30 seconds per command

3. **Scope**:
   - Scans only files with specified extensions
   - Case-insensitive matching for most patterns
   - Only analyzes first 20 characters of matched secrets in output

4. **Performance**:
   - Recursive directory scanning (may be slow on large projects)
   - Uses `errors='ignore'` for file encoding issues

## 🛠️ Customization

### Add Custom Secret Patterns
Edit the `patterns` dictionary in `check_hardcoded_secrets()`:
```python
patterns = {
    'Custom Secret': r'YOUR_REGEX_HERE',
    # ... existing patterns
}
```

### Adjust Scanned Extensions
Modify the `file_extensions` set in `main()`:
```python
file_extensions = {'.py', '.js', '.ts', '.java', '.go', ...}
```

### Exclude Directories
Add skip logic before `filepath.rglob('*')`:
```python
exclude_dirs = {'.git', 'node_modules', 'venv', '__pycache__'}
if any(part in exclude_dirs for part in filepath.parts):
    continue
```

## 📊 Example Scan Results

```json
{
  "secrets": [
    {
      "file": ".env",
      "type": "Google API Key",
      "value": "AIzaSyBOti4mMtCCv3y6EAqL9...",
      "line": "GOOGLE_API_KEY=AIzaSyBOti4mMtCCv3y6EAqL9"
    }
  ],
  "outdated": [
    {
      "file": "package.json",
      "packages": ["express@4.17.1"]
    }
  ],
  "files_analyzed": 156
}
```

## 🔒 Security Best Practices

1. **Never commit secrets**:
   - Use `.gitignore` for `.env`, `secrets.json`, etc.
   - Store keys in environment variables or secret managers

2. **Regular updates**:
   - Run this scanner weekly
   - Automate in CI/CD pipelines (see below)

3. **Secret rotation**:
   - Immediately rotate any exposed secrets
   - Use temporary credentials where possible

## 🧪 CI/CD Integration

### GitHub Actions Example
```yaml
name: Security Scan
on: [push, pull_request]
jobs:
  scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up Python
        uses: actions/setup-python@v4
        with:
          python-version: '3.9'
      - name: Install dependencies
        run: pip install -r requirements.txt
      - name: Run scanner
        run: python scanner.py
      - name: Check for secrets
        run: |
          if python scanner.py | grep -q '"secrets": \[\]'; then
            echo "✅ No secrets found"
          else
            echo "❌ Secrets detected!"
            exit 1
          fi
```

## 📝 License

This tool is provided as-is for security scanning purposes. Use at your own responsibility.

## 🤝 Contributing

Improvements welcome! Please:
1. Test with diverse project structures
2. Add support for more dependency files (Cargo.toml, composer.json, etc.)
3. Optimize performance for large codebases
4. Reduce false positives in generic patterns

---

**⚠️ Important**: This scanner helps find *potential* security issues. Always verify findings manually and never share detected secrets publicly. Rotate exposed credentials immediately.