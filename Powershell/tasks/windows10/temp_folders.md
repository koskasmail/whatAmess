<a name="topage"></a>

# windows 10 - temp folders

A PowerShell program can scan all Windows 10 directories for temporary files and produce a list showing **folder name**, **full path**, and **total size**. The script below is ready to run and uses only built‑in cmdlets.

---

## 🔍 PowerShell script to find temporary files across Windows 10

This script searches common temp locations plus any folder containing “temp” in its name, calculates folder size, and outputs a clean table.

```powershell
# Search all Windows 10 directories for temporary files
$targets = @(
    "$env:TEMP",
    "$env:TMP",
    "C:\Windows\Temp",
    "C:\Users\*\AppData\Local\Temp"
)

# Add any folder named "temp" anywhere on the system
$extra = Get-ChildItem -Path C:\ -Directory -Recurse -ErrorAction SilentlyContinue |
         Where-Object { $_.Name -match "temp" }

$allFolders = $targets + $extra.FullName | Select-Object -Unique

$results = foreach ($folder in $allFolders) {
    if (Test-Path $folder) {
        $files = Get-ChildItem -Path $folder -Recurse -File -ErrorAction SilentlyContinue
        $size  = ($files | Measure-Object -Property Length -Sum).Sum

        [PSCustomObject]@{
            FolderName = Split-Path $folder -Leaf
            Path       = $folder
            SizeMB     = [math]::Round($size / 1MB, 2)
        }
    }
}

$results | Sort-Object SizeMB -Descending | Format-Table -AutoSize
```

---

## 📌 What the script does

### 1. Targets known temp directories
- User temp (`%TEMP%`, `%TMP%`)
- Windows temp (`C:\Windows\Temp`)
- All user profiles’ temp folders

### 2. Finds any folder named “temp” anywhere on the drive
Useful for apps that create their own temp directories.

### 3. Calculates size of all files inside each folder
Uses `Measure-Object` to sum file sizes.

### 4. Outputs a clean table
Example output:

```
FolderName   Path                                   SizeMB
----------   ----                                   ------
Temp         C:\Users\Admin\AppData\Local\Temp      512.33
Temp         C:\Windows\Temp                        233.10
tempfiles    C:\ProgramData\App\tempfiles           12.55
```

---

## 🧠 Optional enhancements you can add
- Delete temp files older than X days  
- Export results to CSV  
- Scan only specific drives  
- Include file count per folder  
- Add progress bar for large drives  

-----

Cleaning temporary files and exporting the results both build naturally on the discovery script you already have. The safest approach is to separate **analysis**, **export**, and **cleanup**, so you always know exactly what will be deleted and can review it before taking action.

---

## 🧹 Cleaning temporary files safely

Cleaning should focus on predictable temp locations and avoid anything outside those paths unless you explicitly approve it. A safe pattern is:

- Identify files.
- Filter by age or size.
- Log what will be deleted.
- Delete only after confirmation.

### Script: clean temp files older than a chosen number of days

```powershell
# Number of days to keep
$days = 7

# Temp locations
$folders = @(
    "$env:TEMP",
    "$env:TMP",
    "C:\Windows\Temp",
    "C:\Users\*\AppData\Local\Temp"
)

# Collect files
$files = foreach ($folder in $folders) {
    Get-ChildItem -Path $folder -Recurse -File -ErrorAction SilentlyContinue |
        Where-Object { $_.LastWriteTime -lt (Get-Date).AddDays(-$days) }
}

# Log preview
$log = "C:\TempCleanupLog.txt"
$files | Select-Object FullName, Length, LastWriteTime |
    Sort-Object Length -Descending |
    Out-File $log

Write-Host "Preview saved to $log"
Write-Host "Files found:" $files.Count

# Confirm deletion
$answer = Read-Host "Delete these files? (y/n)"
if ($answer -eq "y") {
    $files | Remove-Item -Force -ErrorAction SilentlyContinue
    Write-Host "Cleanup complete."
} else {
    Write-Host "Cleanup cancelled."
}
```

### Why this approach is safe
- It logs everything before deletion.
- It deletes only files older than a threshold.
- It avoids removing system-critical files.
- It requires explicit confirmation.

---

## 📤 Exporting results (CSV, JSON, or text)

Exporting the folder-size scan is straightforward. You can reuse the `$results` object from the earlier script.

### Export to CSV

```powershell
$results | Export-Csv -Path "C:\TempFolderReport.csv" -NoTypeInformation
```

### Export to JSON

```powershell
$results | ConvertTo-Json | Out-File "C:\TempFolderReport.json"
```

### Export to plain text

```powershell
$results | Format-Table -AutoSize | Out-File "C:\TempFolderReport.txt"
```

### When exports are useful
- Auditing storage usage.
- Tracking growth over time.
- Feeding data into monitoring tools.
- Sending reports to IT or management.

---

## 🧩 Combining scanning, exporting, and cleaning

A complete workflow usually looks like this:

1. **Scan** all temp folders and calculate sizes.
2. **Export** the results for review.
3. **Clean** files older than X days.
4. **Re-scan** to verify the cleanup impact.
5. **Log** everything for traceability.

This gives you a predictable, repeatable automation process.

------

A scheduled task is the last piece that turns your PowerShell cleanup workflow into a reliable, automated maintenance job on Windows 10. The key is deciding whether you want the task to run **as a script**, **as a PowerShell command**, or **with elevated privileges**, because cleanup usually requires admin rights.

---

## 🧭 How scheduled tasks work for PowerShell automation
Windows Task Scheduler can run any PowerShell script on a fixed schedule. A typical cleanup job includes:

- A `.ps1` script stored in a stable location  
- A scheduled task that calls PowerShell with that script  
- A trigger (daily, weekly, at logon, etc.)  
- A principal (user account) with the right permissions  
- Optional logging so you can verify it ran  

This structure keeps automation predictable and easy to maintain.

---

## 🛠️ Step-by-step: create a scheduled task for your cleanup script

### 1. Save your cleanup script
Place your cleanup script somewhere permanent, for example:

```
C:\Scripts\CleanTemp.ps1
```

Make sure the folder exists and is readable by the account that will run the task.

---

### 2. Create the scheduled task using PowerShell

This example runs the cleanup script **daily at 02:00**, with highest privileges, and logs output.

```powershell
$Action = New-ScheduledTaskAction -Execute "powershell.exe" `
    -Argument "-NoProfile -ExecutionPolicy Bypass -File `"C:\Scripts\CleanTemp.ps1`""

$Trigger = New-ScheduledTaskTrigger -Daily -At 2:00am

$Principal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -RunLevel Highest

Register-ScheduledTask -TaskName "CleanTempFiles" `
    -Action $Action -Trigger $Trigger -Principal $Principal `
    -Description "Daily cleanup of temporary files"
```

### Why SYSTEM is useful
- It has full access to all user temp folders  
- It avoids password expiration issues  
- It ensures the task runs even when no one is logged in  

---

## 📋 Verifying the task

Check that the task exists:

```powershell
Get-ScheduledTask -TaskName "CleanTempFiles"
```

Run it manually to test:

```powershell
Start-ScheduledTask -TaskName "CleanTempFiles"
```

Check last run result:

```powershell
Get-ScheduledTaskInfo -TaskName "CleanTempFiles"
```

---

## 🧾 Adding logging to your script

Inside your cleanup script, add:

```powershell
$log = "C:\Scripts\CleanTemp.log"
"[$(Get-Date)] Cleanup started" | Out-File $log -Append
```

This gives you a persistent audit trail.

---

## 🔄 Optional: schedule a second task to export reports

If you want a daily CSV of folder sizes:

```powershell
$Action = New-ScheduledTaskAction -Execute "powershell.exe" `
    -Argument "-NoProfile -ExecutionPolicy Bypass -File `"C:\Scripts\ExportTempReport.ps1`""

$Trigger = New-ScheduledTaskTrigger -Daily -At 3:00am

Register-ScheduledTask -TaskName "ExportTempReport" -Action $Action -Trigger $Trigger
```

This keeps cleanup and reporting separate, which makes troubleshooting easier.

---

## 🧩 What to consider next
The main decision now is how often you want the cleanup to run and whether you prefer a **daily**, **weekly**, or **idle-time** trigger.


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
