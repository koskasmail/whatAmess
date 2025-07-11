<a name="topage"></a>

# PowerShell

🧠 What PowerShell Is

* PowerShell a command-line shell and scripting language created by Microsoft that lets you
   * automate tasks
   * manage systems
   * interact with Windows
   * available also on Linux and macOS

* It’s built on the .NET framework and uses C# under the hood.
* It’s object-oriented, meaning it works with structured data (objects), not just plain text like older shells.
* It’s both a scripting language and a command-line interface, so you can write full programs or run quick commands.

🛠️ `install` PowerShell
* linux
   * [powershell | 7.5 | linux| ubuntu |](https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.5)
   * [powershell | 7.5 | linux| debian |](https://learn.microsoft.com/en-us/powershell/scripting/install/install-debian?view=powershell-7.5)

🛠️ How to Edit PowerShell Code
* `edit` PowerShell scripts in several ways:
   * `text editor` (Notepad)
   * `PowerShell ISE` (Integrated Scripting Environment): Comes with Windows and has syntax highlighting and debugging tools.
   * Visual Studio Code: With the `PowerShell` extension, you get `IntelliSense`, `debugging`, and more.
* `save PowerShell scripts Code
   * `Sava AS`
      * file name + `.ps1` extension at the end.
         * `doSomthing.ps1`
  
🧪 How to Execute PowerShell Code

* There are a few ways to run PowerShell scripts:
   * From the command line: Open `PowerShell` and type .\scriptname.ps1 to run a script.
      * $ PowerShell .\scriptname.ps1
   * Right-click the .ps1 file and choose “Run with PowerShell.”
   * In Visual Studio Code: Press `F5` to run the whole script or `F8` to run selected lines.

🧱 How to Compile PowerShell Code
PowerShell is interpreted, not compiled — meaning it runs line-by-line without needing to be built into an executable. But if you want to package a script into an .exe file, you can use tools like:

PS2EXE: Converts .ps1 scripts into standalone executables.

Merge-Script from PowerShell Pro Tools: Embeds your script into a .NET app.

If you’re just getting started, I can walk you through writing your first script or help you set up a PowerShell environment. Want to try a simple example?



----

Installing PowerShell is pretty straightforward, and you’ve got a few options depending on your operating system and preferences. Here's how to do it:

---

### 💻 For Windows (PowerShell 7+)

**Recommended Method: Using Winget (Windows Package Manager)**
1. Open **PowerShell as Administrator**.
2. Run this command:
   ```
   winget install Microsoft.PowerShell
   ```
3. Wait for the installation to complete — that’s it!

**Alternative Methods:**
- **Microsoft Store**: Search for “PowerShell” and click **Get** to install.
- **MSI Installer**:
  - Go to the [official PowerShell GitHub releases page](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell?view=powershell-7.5).
  - Download the `.msi` file for your system.
  - Run the installer and follow the prompts.

---

### 🐧 For Linux

PowerShell supports most major distros. Here's a quick example for Ubuntu:

```bash
# Download Microsoft repository GPG keys
wget -q https://packages.microsoft.com/config/ubuntu/20.04/packages-microsoft-prod.deb

# Register the repository
sudo dpkg -i packages-microsoft-prod.deb

# Update and install
sudo apt-get update
sudo apt-get install -y powershell
```

Then launch it with:
```bash
pwsh
```

---

### 🍎 For macOS

Using **Homebrew**:
```bash
brew install --cask powershell
```
Then start it with:
```bash
pwsh
```

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


------


<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>

