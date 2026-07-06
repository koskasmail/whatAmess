

#### delete_temp_files.md

```
$Temps = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "CN|CPE"
$Folder1 = $Temps.FullName + "\Config\Temp"
$Folder2 = $Temps.FullName + "\Temp"
$Folder3 = $Temps.FullName + "\WSTemp"

gci $Folder1 | Remove-Item -Recurse -Force
gci $Folder2 | Remove-Item -Recurse -Force
gci $Folder3 | Remove-Item -Recurse -Force
```

-----

#### clear temp files & logs

```
$Node = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "CN|CPE"
$NodeLogs = $Node.FullName + "\Logs\"
$Node1 = $Node.FullName + "\Config\Temp"
$Node2 = $Node.FullName + "\Temp"
$Node3 = $Node.FullName + "\WSTemp"
$Node4 = (gci $NodeLogs -Attributes Directory | where name -Match "CN|CPE").FullName

gci $Node1 | Remove-Item -Recurse -Force
gci $Node2 | Remove-Item -Recurse -Force
gci $Node3 | Remove-Item -Recurse -Force
gci $Node4 | Remove-Item -Recurse -Force

$MGR = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "MGR"
If ($MGR){
	$MGRLogs = $MGR.FullName + "\Logs\"
	$MGR1 = $MGR.FullName + "\Config\Temp"
	$MGR2 = $MGR.FullName + "\Temp"
	$MGR3 = $MGR.FullName + "\WSTemp"
	$MGR4 = (gci $MGRLogs -Attributes Directory | where name -Match "MGR").FullName

	gci $MGR1 | Remove-Item -Recurse -Force
	gci $MGR2 | Remove-Item -Recurse -Force
	gci $MGR3 | Remove-Item -Recurse -Force
	gci $MGR4 | Remove-Item -Recurse -Force
}
```

----

#### delete temp files #2

```
Write-Host "# ================================"
Write-Host "#   DELETE TEMP FILES "
Write-Host "# ================================"
Write-Host ""


Write-Host "Cleaning Temp folders in CN/CPE profiles..." -ForegroundColor Yellow
Write-Host "-------------------------------------------------------------"

# Get all profiles matching CN or CPE
$Temps = Get-ChildItem "C:\IBM\WebSphere\AppServer\profiles" | Where-Object { $_.Name -match "CN|CPE" }

foreach ($profile in $Temps) {

    Write-Host "Processing profile: $($profile.Name)" -ForegroundColor Cyan

    $Folder1 = Join-Path $profile.FullName "Config\Temp"
    $Folder2 = Join-Path $profile.FullName "Temp"
    $Folder3 = Join-Path $profile.FullName "WSTemp"

    $folders = @($Folder1, $Folder2, $Folder3)

    foreach ($folder in $folders) {
        if (Test-Path $folder) {
            Write-Host "Deleting contents of: $folder"
            Get-ChildItem $folder -ErrorAction SilentlyContinue | Remove-Item -Recurse -Force -ErrorAction SilentlyContinue
        } else {
            Write-Host "Folder not found: $folder" -ForegroundColor DarkYellow
        }
    }

    Write-Host "-------------------------------------------------------------"
}

Read-Host "Cleanup complete. Press ENTER to exit."
```

-----

