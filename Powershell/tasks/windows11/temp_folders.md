
<a name="topage"></a>

# windows 11 - temp folders


Windows 11 uses the **same Task Scheduler engine as Windows 10**, so the automation pattern you already built carries over directly. What changes is mostly the environment around it: Windows 11 has stricter execution‑policy defaults, more aggressive UAC elevation prompts, and a slightly different security model for SYSTEM‑level tasks. The core mechanics—PowerShell script + scheduled task + logging—remain identical.

---

## 🧭 How scheduled tasks behave on Windows 11
Windows 11 keeps the same task types, triggers, and principals as Windows 10, but with a few practical differences:

- Running tasks as **SYSTEM** is still the most reliable way to access all user temp folders.
- PowerShell 5.1 is still present, but Windows 11 prefers PowerShell 7 for automation.
- ExecutionPolicy is more locked down, so tasks should explicitly bypass it.
- Windows 11’s virtualization-based security can block scripts stored in user profiles, so scripts should live in a neutral path like `C:\Scripts`.

These differences shape how you configure the task.

---

## 🛠️ Step-by-step: scheduled task for Windows 11 cleanup

### 1. Place your cleanup script in a stable location
A neutral directory avoids permission issues:

```
C:\Scripts\CleanTemp.ps1
```

The script can be the same cleanup logic you already built.

---

### 2. Create the scheduled task using PowerShell
This version is tuned for Windows 11’s security defaults.

```powershell
$Action = New-ScheduledTaskAction -Execute "powershell.exe" `
    -Argument "-NoProfile -ExecutionPolicy Bypass -File `"C:\Scripts\CleanTemp.ps1`""

$Trigger = New-ScheduledTaskTrigger -Daily -At 2:00am

$Principal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -RunLevel Highest

Register-ScheduledTask -TaskName "CleanTempFiles" `
    -Action $Action -Trigger $Trigger -Principal $Principal `
    -Description "Daily cleanup of temporary files on Windows 11"
```

### Why this works well on Windows 11
- **SYSTEM** avoids UAC prompts and has full access to all temp directories.
- **RunLevel Highest** ensures the task runs even under tightened security.
- **ExecutionPolicy Bypass** avoids Windows 11’s stricter script-blocking behavior.

---

## 📋 Verifying and testing the task

### Check that the task exists
```powershell
Get-ScheduledTask -TaskName "CleanTempFiles"
```

### Run it manually
```powershell
Start-ScheduledTask -TaskName "CleanTempFiles"
```

### Check last run result
```powershell
Get-ScheduledTaskInfo -TaskName "CleanTempFiles"
```

### Check logs
If your script writes to a log file:

```
C:\Scripts\CleanTemp.log
```

Windows 11 also logs scheduled task output in Event Viewer under:

```
Applications and Services Logs → Microsoft → Windows → TaskScheduler
```

---

## 🧾 Optional: add a Windows 11‑friendly logging pattern
Windows 11’s security model prefers logs in non‑user paths:

```powershell
$log = "C:\Scripts\CleanTemp.log"
"[$(Get-Date)] Cleanup started" | Out-File $log -Append
```

This avoids virtualization and permission issues.

---

## 🔄 Optional: add a second scheduled task for reporting
If you want a daily CSV or JSON report of temp folder sizes, you can schedule a second script:

```powershell
$Action = New-ScheduledTaskAction -Execute "powershell.exe" `
    -Argument "-NoProfile -ExecutionPolicy Bypass -File `"C:\Scripts\ExportTempReport.ps1`""

$Trigger = New-ScheduledTaskTrigger -Daily -At 3:00am

Register-ScheduledTask -TaskName "ExportTempReport" `
    -Action $Action -Trigger $Trigger
```

Keeping cleanup and reporting separate makes troubleshooting easier.

---

## 🧩 What to consider next
The main choice now is how often you want the Windows 11 cleanup to run—daily, weekly, or only when the system is idle.


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
