<a name="topage"></a>

Here’s a simple yet effective **Bash script** that backs up a specific user’s home directory and compresses it into a `.tar.gz` archive. It’s perfect for daily snapshots or archiving user data before system changes.

---

## 🛠️ Script: `user_backup.sh`
```bash
#!/bin/bash

# === CONFIGURATION ===
USERNAME="yourusername"  # Replace with actual username
USER_HOME="/home/$USERNAME"
BACKUP_DIR="/var/backups/users"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
ARCHIVE_NAME="${USERNAME}_backup_${TIMESTAMP}.tar.gz"

# === CREATE BACKUP DIRECTORY IF NEEDED ===
mkdir -p "$BACKUP_DIR"

# === CHECK IF USER HOME EXISTS ===
if [ ! -d "$USER_HOME" ]; then
    echo "❌ Error: Home directory for user '$USERNAME' not found."
    exit 1
fi

# === CREATE ARCHIVE ===
echo "📦 Creating backup for $USERNAME..."
tar -czf "$BACKUP_DIR/$ARCHIVE_NAME" -C "/home" "$USERNAME"

# === VERIFY SUCCESS ===
if [ $? -eq 0 ]; then
    echo "✅ Backup successful: $BACKUP_DIR/$ARCHIVE_NAME"
else
    echo "❌ Backup failed."
fi
```

---

### 🧪 Notes:
* Replace `"yourusername"` with the actual username you want to back up.
* The script compresses the entire `/home/username` directory.
* You can run this manually or automate it with `cron`.

---

### 🕒 Automate with Cron
To run this script daily at 1 AM:
```bash
crontab -e
```
Add:
```
0 1 * * * /path/to/user_backup.sh
```

---


##### online cheat sheet
* www

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
