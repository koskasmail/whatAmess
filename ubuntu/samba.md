<a name="topage"></a>

# samba.md

* **Bash script**
1. installs Samba
2. creates a shared directory
3. adds a group and users
4. configures **per-user subfolders** with full permissions
* Note: This is perfect for daily backups or team collaboration setups.

---

## 🛠️ Script: `setup_samba_share.sh`
```bash
#!/bin/bash

# === CONFIGURATION ===
SHARE_DIR="/srv/samba/shared"
GROUP_NAME="smbgroup"
USERS=("alice" "bob" "charlie")  # Add usernames here
SAMBA_CONF="/etc/samba/smb.conf"

# === INSTALL SAMBA ===
echo "Installing Samba..."
sudo apt update
sudo apt install -y samba

# === CREATE GROUP ===
echo "Creating group: $GROUP_NAME"
sudo groupadd $GROUP_NAME

# === CREATE MAIN SHARE DIRECTORY ===
echo "Creating shared directory: $SHARE_DIR"
sudo mkdir -p "$SHARE_DIR"
sudo chown root:$GROUP_NAME "$SHARE_DIR"
sudo chmod 2770 "$SHARE_DIR"

# === CREATE USER SUBFOLDERS ===
for USER in "${USERS[@]}"; do
    echo "Setting up user: $USER"

    # Create user without home directory
    sudo useradd -M -s /usr/sbin/nologin -G $GROUP_NAME $USER

    # Create Samba password
    echo "Set Samba password for $USER:"
    sudo smbpasswd -a $USER
    sudo smbpasswd -e $USER

    # Create subfolder
    USER_DIR="$SHARE_DIR/$USER"
    sudo mkdir -p "$USER_DIR"
    sudo chown $USER:$GROUP_NAME "$USER_DIR"
    sudo chmod 2770 "$USER_DIR"

    # Add share config
    sudo tee -a "$SAMBA_CONF" > /dev/null <<EOF

[$USER]
   path = $USER_DIR
   valid users = $USER
   read only = no
   browseable = yes
   force user = $USER
   force group = $GROUP_NAME
   create mask = 0770
   directory mask = 0770
EOF
done

# === RESTART SAMBA ===
echo "Restarting Samba service..."
sudo systemctl restart smbd

echo "✅ Samba setup complete. Each user has full access to their folder."
```

---

### 🧪 Notes:
- This script assumes you're on a **Debian-based system** (like Ubuntu).
- Each user gets a private folder under `/srv/samba/shared/username`.
- You can access the shares from Windows using `\\<server-ip>\<username>`.

Want to extend this to include daily backups or email alerts when a user logs in? I can help you build that next.

---

##### online cheat sheet
* www

----
