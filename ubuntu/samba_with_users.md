
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

-----

# show computer "tv" to network

To set your Ubuntu machine's hostname to "tv" and make it visible on your local network, follow these steps:

### 🖥️ 1. Set the Hostname
Run the following command to permanently set the hostname:

```bash
sudo hostnamectl set-hostname tv
```

Then update your `/etc/hosts` file to reflect the new hostname:

```bash
sudo nano /etc/hosts
```

Change or add this line:

```
127.0.1.1   tv
```

Save and exit (`Ctrl+O`, `Enter`, then `Ctrl+X`).

### 🌐 2. Make It Discoverable on the Network
Ubuntu uses **Avahi** (a mDNS responder) to broadcast its hostname on the local network. This allows other devices to access it using `tv.local`.

To ensure Avahi is installed and running:

```bash
sudo apt install avahi-daemon
sudo systemctl enable avahi-daemon
sudo systemctl start avahi-daemon
```

### 🧪 3. Test from Another Device
From another computer on the same network, try:

```bash
ping tv.local
```

Or access services (like Samba or a web server) via:

```
http://tv.local
```

This works across most modern operating systems including Windows, macOS, and Linux.

-----

# check and fix

If you've configured Samba with a user and share point on Ubuntu, but Windows users on the same local network still can't see the share, here’s a complete checklist to troubleshoot and fix the issue safely:

---

### ✅ 1. **Verify Samba Is Installed and Running**
```bash
sudo systemctl status smbd
```
If it's not running, start it:
```bash
sudo systemctl start smbd
```

---

### ✅ 2. **Check Your Samba Configuration**
Edit your Samba config:
```bash
sudo nano /etc/samba/smb.conf
```
Make sure your share looks like this:
```ini
[Shared]
   path = /home/yourusername/Shared
   browseable = yes
   read only = no
   guest ok = yes
```
Also ensure:
```ini
workgroup = WORKGROUP
server string = Samba Server
netbios name = ubuntu-machine
wins support = yes
```
> Replace `yourusername` and `ubuntu-machine` with your actual values.

---

### ✅ 3. **Set Proper Permissions**
```bash
chmod -R 0777 /home/yourusername/Shared
```
If using user authentication:
```bash
sudo smbpasswd -a yourusername
```

---

### ✅ 4. **Install WSDD for Windows Discovery**
Windows uses WS-Discovery, not NetBIOS. Install `wsdd`:
```bash
sudo apt install wsdd
sudo systemctl enable wsdd
sudo systemctl start wsdd
```
This makes your Samba server visible in Windows Network Neighborhood.

---

### ✅ 5. **Allow Samba Through the Firewall**
```bash
sudo ufw allow samba
sudo ufw reload
```

---

### ✅ 6. **Restart Samba and Network Services**
```bash
sudo systemctl restart smbd nmbd
```

---

### ✅ 7. **Access the Share from Windows**
Instead of browsing, try direct access:
- Press `Win + R` → type `\\10.0.0.123\Shared` → press Enter

If it works, the issue is with network discovery, not the share itself.

---

### ✅ 8. **Enable Guest Access in Windows (if needed)**
If you're using guest access:
- Run `gpedit.msc`
- Go to:  
  `Computer Configuration → Administrative Templates → Network → Lanman Workstation`
- Enable **"Enable insecure guest logons"**

---

Once these steps are complete, your Samba share should be visible and accessible from Windows devices on the same local network.

Would you like help setting up auto-mounting on Windows or securing the share with user permissions?

Let me know if you want to assign a static IP or integrate with a local DNS server for more advanced setups.

-----

# service fix

Thanks for sharing the error—this means the `wsdd` service isn’t installed yet. Here's how to **safely install and enable WSDD** on Ubuntu so your Samba shares become visible to Windows machines:

---

### 🛠️ Step-by-Step: Install WSDD on Ubuntu

#### 1. **Install Required Packages**
First, install Python 3 and Git (if not already installed):
```bash
sudo apt update
sudo apt install python3 git
```

#### 2. **Download WSDD**
Clone the official WSDD repository:
```bash
git clone https://github.com/christgau/wsdd.git
```

#### 3. **Install WSDD Systemd Service**
Navigate into the folder:
```bash
cd wsdd
```
Copy the service file:
```bash
sudo cp wsdd.service /etc/systemd/system/
```

Install the script:
```bash
sudo cp src/wsdd.py /usr/bin/wsdd
sudo chmod +x /usr/bin/wsdd
```

#### 4. **Enable and Start the Service**
```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable wsdd
sudo systemctl start wsdd
```

#### 5. **Verify It's Running**
```bash
sudo systemctl status wsdd
```

---

### ✅ After Setup
Once WSDD is running, your Samba server should appear in Windows Network Neighborhood. You can still access it directly via `\\your-ip\sharename`, but now it should also show up automatically.

Would you like help making this setup persistent across reboots or securing it with user authentication?

-----



