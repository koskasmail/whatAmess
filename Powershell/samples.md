<a name="topage"></a>

# samples.md

----

### welcome


```powershell
# Clear the screen
Clear-Host

# Print current date and time
Write-Host "🗓️ Date: $(Get-Date -Format 'dddd, MMMM dd, yyyy')" -ForegroundColor Cyan
Write-Host "🕒 Time: $(Get-Date -Format 'hh:mm:ss tt')" -ForegroundColor Cyan
Write-Host ""

# Welcome message
Write-Host "👋 Welcome to PowerShell!" -ForegroundColor Green
Write-Host "Hope you're having a productive session." -ForegroundColor Green
Write-Host ""

# Exit the script
Write-Host "👋 Exiting now. Have a great day!" -ForegroundColor Yellow
Start-Sleep -Seconds 2
exit
```

### 🔍 How to Run It
1. Save the code into a file named something like `WelcomeScript.ps1`.
2. Right-click the file and choose **Run with PowerShell**, or open PowerShell and type:
   ```
   .\WelcomeScript.ps1
   ```

----

### welcome with color & banner

Awesome — let’s jazz it up with some retro terminal vibes! Here's your PowerShell script with a splash of ASCII art and color to give it a warm, geek-chic welcome:

```powershell
# Clear the screen
Clear-Host

# Show ASCII banner
$banner = @'
 __        __   _                            _          _ 
 \ \      / /__| | ___ ___  _ __ ___   ___  | |_ ___   | |
  \ \ /\ / / _ \ |/ __/ _ \| '_ ` _ \ / _ \ | __/ _ \  | |
   \ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | |_|
    \_/\_/ \___|_|\___\___/|_| |_| |_|\___|  \__\___/  (_)

'@
Write-Host $banner -ForegroundColor Magenta

# Print current date and time
Write-Host "🗓️ Date: $(Get-Date -Format 'dddd, MMMM dd, yyyy')" -ForegroundColor Cyan
Write-Host "🕒 Time: $(Get-Date -Format 'hh:mm:ss tt')" -ForegroundColor Cyan
Write-Host ""

# Welcome message
Write-Host "👋 Welcome to PowerShell!" -ForegroundColor Green
Write-Host "Hope you're having a productive session. Enjoy your scripting journey!" -ForegroundColor Green
Write-Host ""

# Exit message
Write-Host "👋 Exiting now. Have a great day!" -ForegroundColor Yellow
Start-Sleep -Seconds 2
exit
```

You can paste this into a `.ps1` file (like `FancyWelcome.ps1`) and run it as before.

Want to make it interactive next — like ask the user for their name and greet them personally? I can help with that too 😄


---

### cleans up Windows temporary files and browser cache (Chrome, Edge, Firefox)


### 🧹 PowerShell Cleanup Script

```powershell
# Clear Windows Temp folders
$tempFolders = @(
    "$env:TEMP\*",
    "$env:SystemRoot\Temp\*",
    "$env:SystemDrive\Users\*\AppData\Local\Temp\*",
    "$env:SystemDrive\Windows\Prefetch\*"
)
$tempFolders | ForEach-Object {
    Remove-Item $_ -Force -Recurse -ErrorAction SilentlyContinue
}

# Stop browser processes
Get-Process chrome, msedge, firefox -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue

# Clear Chrome cache
$chromePaths = @(
    "$env:LOCALAPPDATA\Google\Chrome\User Data\Default\Cache",
    "$env:LOCALAPPDATA\Google\Chrome\User Data\Default\Cache2\entries"
)
$chromePaths | ForEach-Object {
    Remove-Item "$_\*" -Force -Recurse -ErrorAction SilentlyContinue
}

# Clear Edge cache
$edgePaths = @(
    "$env:LOCALAPPDATA\Microsoft\Edge\User Data\Default\Cache",
    "$env:LOCALAPPDATA\Microsoft\Edge\User Data\Default\Cache2\entries"
)
$edgePaths | ForEach-Object {
    Remove-Item "$_\*" -Force -Recurse -ErrorAction SilentlyContinue
}

# Clear Firefox cache
$firefoxProfileRoot = "$env:APPDATA\Mozilla\Firefox\Profiles"
if (Test-Path $firefoxProfileRoot) {
    Get-ChildItem $firefoxProfileRoot | ForEach-Object {
        $profilePath = $_.FullName
        $firefoxCachePaths = @(
            "$profilePath\cache2\entries",
            "$profilePath\cache",
            "$profilePath\offlineCache"
        )
        $firefoxCachePaths | ForEach-Object {
            Remove-Item "$_\*" -Force -Recurse -ErrorAction SilentlyContinue
        }
    }
}

Write-Host "✅ Cleanup complete!" -ForegroundColor Green
```

---

### 🚀 How to Use It
1. Open **Notepad** and paste the code above.
2. Save it as `CleanTempAndCache.ps1`.
3. Right-click the file and choose **Run with PowerShell** (or run it manually from PowerShell).



----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
