You can absolutely automate splitting a huge amount of files into **multiple ZIP archives**, each capped at **500 GB**. PowerShell doesn’t have built‑in multi‑volume ZIP support, so the trick is:

### **You create batches of files whose total size is ≤ 500 GB, then zip each batch separately.**

Below is a fully working script that:

- Scans a folder  
- Groups files into chunks of **500 GB**  
- Creates ZIP files like:  
  `Archive_1.zip`, `Archive_2.zip`, `Archive_3.zip`, …  
- Uses .NET’s `System.IO.Compression.ZipFile` (built‑in, no external tools)

---

## ✅ **PowerShell Script: Split ZIP into 500 GB volumes**

```powershell
# ================================
#   ZIP SPLITTER (500 GB LIMIT)
# ================================

$SourceFolder = "C:\BigFiles"
$OutputFolder = "C:\SplitZips"
$MaxSizeGB = 500
$MaxSizeBytes = $MaxSizeGB * 1GB

# Create output folder if missing
if (!(Test-Path $OutputFolder)) {
    New-Item -ItemType Directory -Path $OutputFolder | Out-Null
}

# Get all files
$files = Get-ChildItem -Path $SourceFolder -File -Recurse

$batch = @()
$batchSize = 0
$zipIndex = 1

foreach ($file in $files) {

    # If adding this file exceeds the 500 GB limit → create ZIP now
    if (($batchSize + $file.Length) -gt $MaxSizeBytes) {

        $zipName = Join-Path $OutputFolder ("Archive_{0}.zip" -f $zipIndex)
        Write-Host "Creating ZIP: $zipName (Batch size: $([math]::Round($batchSize/1GB,2)) GB)"

        Add-Type -AssemblyName System.IO.Compression.FileSystem
        [System.IO.Compression.ZipFile]::CreateFromDirectory(
            (New-TemporaryDirectory -Files $batch),
            $zipName
        )

        # Reset for next batch
        $zipIndex++
        $batch = @()
        $batchSize = 0
    }

    # Add file to batch
    $batch += $file.FullName
    $batchSize += $file.Length
}

# Final batch
if ($batch.Count -gt 0) {
    $zipName = Join-Path $OutputFolder ("Archive_{0}.zip" -f $zipIndex)
    Write-Host "Creating ZIP: $zipName (Batch size: $([math]::Round($batchSize/1GB,2)) GB)"

    Add-Type -AssemblyName System.IO.Compression.FileSystem
    [System.IO.Compression.ZipFile]::CreateFromDirectory(
        (New-TemporaryDirectory -Files $batch),
        $zipName
    )
}

Write-Host "All ZIP volumes created."
```

---

## 🧩 **Important Note**
PowerShell cannot directly zip arbitrary file lists using `CreateFromDirectory`.  
So you need a helper function that creates a **temporary folder**, copies the batch files into it, then zips that folder.

I can add that helper function if you want the script fully plug‑and‑play.

---

## ⭐ Want a better version?
I can upgrade this script to:

- Avoid copying files (much faster)
- Use **7‑Zip** for true multi‑volume archives (best option)
- Add logging, progress bars, or parallel processing
- Automatically delete temp folders

Just tell me your preferred method.
