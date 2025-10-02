
# samba_with_users.md

Here's a Bash script that installs Samba on Ubuntu, creates the specified users and directories, sets permissions, and configures Samba to allow remote access to each user's folder:

```bash
#!/bin/bash

# Update and install Samba
sudo apt update
sudo apt install -y samba

# Create users (no shell access, no home directory)
for user in jaron noam ofir home; do
    sudo useradd -M -s /sbin/nologin $user
    echo "Set Samba password for $user:"
    sudo smbpasswd -a $user
done

# Create shared directories
sudo mkdir -p /kess/{jaron,noam,ofir,home}
sudo chmod -R 777 /kess
sudo chown -R root:root /kess

# Backup original Samba config
sudo cp /etc/samba/smb.conf /etc/samba/smb.conf.bak

# Append Samba shares to config
cat <<EOL | sudo tee -a /etc/samba/smb.conf

[kess_jaron]
   path = /kess/jaron
   valid users = jaron
   read only = no
   browsable = yes
   guest ok = no
   create mask = 0777
   directory mask = 0777

[kess_noam]
   path = /kess/noam
   valid users = noam
   read only = no
   browsable = yes
   guest ok = no
   create mask = 0777
   directory mask = 0777

[kess_ofir]
   path = /kess/ofir
   valid users = ofir
   read only = no
   browsable = yes
   guest ok = no
   create mask = 0777
   directory mask = 0777

[kess_home]
   path = /kess/home
   valid users = home
   read only = no
   browsable = yes
   guest ok = no
   create mask = 0777
   directory mask = 0777
EOL

# Restart Samba service
sudo systemctl restart smbd

echo "Samba setup complete. Users can now access their folders remotely."
```

🛠️ **Usage Notes:**
- Run this script with `sudo` privileges.
- You'll be prompted to set Samba passwords for each user.
- Ensure your firewall allows Samba traffic (`sudo ufw allow 'Samba'` if using UFW).

Would you like a version that auto-generates passwords or restricts access further?
