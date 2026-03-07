<a name="topage"></a>

# powershell

PowerShell runs **natively on Ubuntu** because modern PowerShell (PowerShell 7+) is fully **cross‑platform** and supported on Linux, macOS, and Windows. Ubuntu is one of the primary supported distributions.   [LinuxCapable](https://linuxcapable.com/how-to-install-powershell-on-ubuntu-linux/)

---

## 🧩 What PowerShell on Ubuntu Actually Means
PowerShell on Linux is the same engine used on Windows, but compiled for .NET 7/8 on Linux. It gives you:

- A consistent **object‑based shell** across Windows and Linux  
- The ability to automate **Azure**, **cloud**, and **hybrid environments**  
- Access to modules that support cross‑platform operations  
- A familiar scripting environment if you come from Windows administration  

This makes it useful for mixed Windows–Linux infrastructures.   [LinuxCapable](https://linuxcapable.com/how-to-install-powershell-on-ubuntu-linux/)

---

## 🛠️ How to Install PowerShell on Ubuntu (Short Manual)

### 1. Install via Microsoft’s APT Repository (Recommended)
Ubuntu 22.04 LTS and 24.04 LTS are officially supported.   [LinuxCapable](https://linuxcapable.com/how-to-install-powershell-on-ubuntu-linux/)

**Step 1 — Install prerequisites**
```bash
sudo apt update
sudo apt install wget apt-transport-https software-properties-common
```

**Step 2 — Add Microsoft’s repository**
```bash
wget -q https://packages.microsoft.com/config/ubuntu/22.04/packages-microsoft-prod.deb
sudo dpkg -i packages-microsoft-prod.deb
```

**Step 3 — Install PowerShell**
```bash
sudo apt update
sudo apt install powershell
```

**Step 4 — Run PowerShell**
```bash
pwsh
```

---

### 2. Install via Direct Download (Alternative)
Microsoft also provides `.deb` packages for manual installation.  
This method is useful if you need a specific version.   [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)

---

### 3. Install via Snap (Ubuntu 24.04 Option)
Some guides show installation via Snap packages.   [Ubuntu Free](https://www.ubuntufree.com/powershell-for-ubuntu/)

```bash
sudo snap install powershell --classic
```

---

## 📚 Documentation & Resources

### Official Microsoft Documentation
- **Installing PowerShell on Ubuntu (Microsoft Learn)**  
  Comprehensive instructions for repository, direct download, ARM support, and uninstall steps.  
    [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)



### Community Guides
- **LinuxCapable: How to Install PowerShell on Ubuntu** — explains cross‑platform benefits and installation for 22.04/24.04.   [LinuxCapable](https://linuxcapable.com/how-to-install-powershell-on-ubuntu-linux/)  
- **IT’s FOSS: Installing PowerShell on Ubuntu 20.04/22.04** — step‑by‑step with explanations of why PowerShell is useful on Linux.   [itsfoss.gitlab.io](https://itsfoss.gitlab.io/post/how-to-install-powershell-on-ubuntu-2204-or-2004/)  
- **Ubuntu Free: PowerShell on Ubuntu 24.04 Guide** — includes Snap installation and usage basics.   [Ubuntu Free](https://www.ubuntufree.com/powershell-for-ubuntu/)  

---

PowerShell on Ubuntu can install modules, run scripts, and integrate with Linux services like **systemd**, giving you a unified automation layer across Windows and Linux. The sections below walk through each capability in depth, with practical examples and considerations.

---

## 🧩 Installing PowerShell Modules on Ubuntu
PowerShell on Ubuntu uses the same module ecosystem as Windows, but with some platform‑specific limitations. Modules are installed from the PowerShell Gallery using `Install-Module`.

### Common module installation workflow
```powershell
pwsh
Install-Module Az -Scope CurrentUser
Install-Module Pester -Scope CurrentUser
Install-Module PSReadLine -Scope CurrentUser
```

### What to know before installing
- Modules that depend on **Windows-only APIs** (e.g., ActiveDirectory, WMI-based modules) will not work on Linux.
- Cross‑platform modules like **Az**, **Pester**, **PSReadLine**, **PowerShellGet**, and many DevOps modules work normally.
- You may need to trust the PSGallery repository the first time:
```powershell
Set-PSRepository -Name PSGallery -InstallationPolicy Trusted
```

### Verifying module availability
```powershell
Get-Module -ListAvailable
```

---

## 📜 Running PowerShell Scripts on Ubuntu
PowerShell scripts (`.ps1`) run the same way as on Windows, with a few Linux‑specific considerations.

### Running a script
```bash
pwsh ./myscript.ps1
```

### Making a script executable
```bash
chmod +x myscript.ps1
pwsh myscript.ps1
```

### Execution policy on Linux
Execution policies are advisory on Linux, not enforced by the OS. You can still set them:
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Using Linux paths inside PowerShell
PowerShell understands Linux paths:
```powershell
Get-ChildItem /etc
```

And environment variables:
```powershell
$env:PATH
```

---

## 🔧 Integrating PowerShell with Linux Services (systemd)
PowerShell can run background services on Ubuntu using **systemd**, allowing scheduled tasks, daemons, or automation agents.

### Example: Running a PowerShell script as a systemd service

#### 1. Create a PowerShell script
`/usr/local/bin/mysvc.ps1`:
```powershell
while ($true) {
    "Service heartbeat: $(Get-Date)" | Out-File /var/log/mysvc.log -Append
    Start-Sleep -Seconds 30
}
```

#### 2. Create a systemd service file
`/etc/systemd/system/mysvc.service`:
```ini
[Unit]
Description=PowerShell Background Service

[Service]
ExecStart=/usr/bin/pwsh /usr/local/bin/mysvc.ps1
Restart=always

[Install]
WantedBy=multi-user.target
```

#### 3. Enable and start the service
```bash
sudo systemctl daemon-reload
sudo systemctl enable mysvc
sudo systemctl start mysvc
```

#### 4. Check status and logs
```bash
systemctl status mysvc
journalctl -u mysvc
```

### Why this matters
- Lets you build cross‑platform automation agents.
- Replaces Windows Task Scheduler jobs with Linux-native equivalents.
- Enables long‑running PowerShell processes on Ubuntu servers.

---

## 📚 Documentation and References
- Microsoft Learn explains installation and supported versions of PowerShell on Ubuntu.   [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)
- LinuxVox provides a comprehensive overview of PowerShell usage on Ubuntu, including common practices.   [linuxvox.com](https://linuxvox.com/blog/powershell-ubuntu/)
- IT’s FOSS covers installation and cross‑platform benefits for Ubuntu 20.04/22.04.   [itsfoss.gitlab.io](https://itsfoss.gitlab.io/post/how-to-install-powershell-on-ubuntu-2204-or-2004/)
- Ubuntu Free offers a guide for Ubuntu 24.04 with additional usage notes.   [Ubuntu Free](https://www.ubuntufree.com/powershell-for-ubuntu/)

---

A PowerShell automation environment on Ubuntu works best when you treat it like a small, modular platform: one part for **modules**, one for **script execution**, one for **logging**, and one for **scheduled or persistent tasks**. PowerShell itself installs cleanly on Ubuntu through Microsoft’s package repository, and newer versions replace older ones unless installed side‑by‑side using archive methods.   [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)

---

## Building the automation foundation

### 🧱 Core components you need
- A stable PowerShell installation (from Microsoft’s APT repo or direct `.deb` package).   [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)  [Lindevs](https://lindevs.com/install-powershell-on-ubuntu)
- A directory structure for scripts, logs, and modules.
- A consistent module versioning strategy.
- Integration with Ubuntu’s native automation tools such as `systemd` and `cron`.

---

## Installing and pinning module versions

### Installing modules
PowerShell on Ubuntu uses the same module system as Windows. Modules install from the PowerShell Gallery:

```powershell
Install-Module Az -RequiredVersion 11.3.0 -Scope CurrentUser
Install-Module Pester -RequiredVersion 5.5.0 -Scope CurrentUser
```

### Pinning versions
Pinning ensures automation doesn’t break when a module updates.

Two common strategies:

- **Explicit version pinning in scripts**  
  ```powershell
  Import-Module Az -RequiredVersion 11.3.0
  ```

- **Local module repository**  
  Store approved versions in a local folder and register it:
  ```powershell
  Register-PSRepository -Name LocalRepo -SourceLocation /opt/psmodules
  ```

This avoids accidental upgrades and keeps automation predictable.

---

## Running scripts reliably

### Directory layout
A simple, maintainable structure:

```
/opt/ps-automation/
  scripts/
  modules/
  logs/
```

### Script execution basics
PowerShell scripts run directly:

```bash
pwsh /opt/ps-automation/scripts/backup.ps1
```

Execution policies on Linux are advisory, so they won’t block scripts the way they might on Windows.

---

## Logging and observability

### Logging inside scripts
Use structured logging to a file:

```powershell
$log = "/opt/ps-automation/logs/backup.log"
"[$(Get-Date)] Starting backup" | Out-File $log -Append
```

For more advanced logging, you can serialize objects to JSON:

```powershell
$data | ConvertTo-Json | Out-File $log -Append
```

### System-level logging
When scripts run as services, logs also appear in `journalctl`, which gives you timestamps, restarts, and error traces.

---

## Scheduling and background execution

### Using systemd for persistent automation
Systemd is the most reliable way to run long‑lived or repeating PowerShell tasks.

**Service file example:**

`/etc/systemd/system/ps-backup.service`
```ini
[Unit]
Description=Nightly PowerShell Backup

[Service]
ExecStart=/usr/bin/pwsh /opt/ps-automation/scripts/backup.ps1
WorkingDirectory=/opt/ps-automation
Restart=on-failure
```

**Timer file example:**

`/etc/systemd/system/ps-backup.timer`
```ini
[Unit]
Description=Run PowerShell backup nightly

[Timer]
OnCalendar=*-*-* 02:00:00
Persistent=true

[Install]
WantedBy=timers.target
```

Enable and start:

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now ps-backup.timer
```

### Using cron for simpler tasks
Cron works well for lightweight jobs:

```bash
crontab -e
```

Add:

```
0 3 * * * pwsh /opt/ps-automation/scripts/cleanup.ps1 >> /opt/ps-automation/logs/cleanup.log 2>&1
```

---

## Putting it all together

A complete automation environment on Ubuntu typically includes:

- **PowerShell installed and updated** via Microsoft’s repository.   [Microsoft Learn](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)
- **Pinned module versions** stored in a controlled repository.
- **Scripts organized** in a predictable directory structure.
- **Logging** to both file and systemd journals.
- **Scheduled tasks** using systemd timers for reliability.
- **Optional cron jobs** for lightweight periodic tasks.
- **Version control** (Git) for scripts and module manifests.

This setup gives you a stable, reproducible automation platform that behaves consistently across updates and environments.

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
