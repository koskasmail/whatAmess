<a name="topage"></a>

# backup_script.md

* **Bash backup script**
1. creates compressed archives of important directories
2. logs the operation
3. optionally cleans up old backups
* Note: It’s perfect for daily use by system administrators or anyone who wants peace of mind.

---

## 🛠️ Script: `daily_backup.sh`
```bash
#!/bin/bash

# === CONFIGURATION ===
SOURCE_DIRS=("/etc" "/home" "/var/log")   # Add more paths as needed
BACKUP_DIR="/var/backups"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
HOSTNAME=$(hostname -s)
ARCHIVE_NAME="${HOSTNAME}_backup_${TIMESTAMP}.tar.gz"
LOG_FILE="$BACKUP_DIR/backup.log"
RETENTION_DAYS=30

# === CREATE BACKUP DIRECTORY IF NEEDED ===
mkdir -p "$BACKUP_DIR"

# === CREATE ARCHIVE ===
echo "[$(date)] Starting backup..." >> "$LOG_FILE"
tar -czf "$BACKUP_DIR/$ARCHIVE_NAME" "${SOURCE_DIRS[@]}" && \
echo "[$(date)] Backup successful: $ARCHIVE_NAME" >> "$LOG_FILE" || \
echo "[$(date)] Backup failed!" >> "$LOG_FILE"

# === CLEAN OLD BACKUPS ===
find "$BACKUP_DIR" -type f -name "*.tar.gz" -mtime +$RETENTION_DAYS -exec rm -f {} \; && \
echo "[$(date)] Old backups cleaned (>$RETENTION_DAYS days)" >> "$LOG_FILE"

# === DONE ===
echo "[$(date)] Backup script completed." >> "$LOG_FILE"
```

---

### 🧪 Features:
* Compresses multiple directories into a single `.tar.gz` archive
* Names the archive with hostname and timestamp
* Logs each run to `backup.log`
* Deletes backups older than 30 days

---

### 🕒 Automate with Cron
To run this script daily at 2 AM:
```bash
crontab -e
```
Add this line:
```
0 2 * * * /path/to/daily_backup.sh
```

---

##### online cheat sheet
* www

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
