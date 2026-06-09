
# build_VSCode_extension_2.md

-----

You can build a minimal VS Code extension that **runs the currently active `.ps1` file in the integrated terminal**.  
Below is a complete, working example — small, clean, and ready to paste into a new extension.

---

## 🚀 **Takeaway**
Create a VS Code command that:
1. Gets the active editor file.
2. Ensures it’s a `.ps1`.
3. Sends a PowerShell execution command to the integrated terminal.

---

## 📁 **Extension Structure**
```
your-extension/
│── package.json
│── extension.js
└── README.md
```

---

## 📦 **package.json**
This registers the command in VS Code.

```json
{
  "name": "run-ps1",
  "displayName": "Run Current PS1",
  "version": "0.0.1",
  "publisher": "yourname",
  "engines": {
    "vscode": "^1.70.0"
  },
  "activationEvents": [
    "onCommand:extension.runPs1"
  ],
  "main": "extension.js",
  "contributes": {
    "commands": [
      {
        "command": "extension.runPs1",
        "title": "Run Current PowerShell File"
      }
    ]
  }
}
```

---

## ⚙️ **extension.js**
This is the logic that executes the file.

```js
const vscode = require("vscode");

function activate(context) {
    let disposable = vscode.commands.registerCommand("extension.runPs1", function () {
        const editor = vscode.window.activeTextEditor;
        if (!editor) {
            vscode.window.showErrorMessage("No active editor");
            return;
        }

        const filePath = editor.document.fileName;

        if (!filePath.toLowerCase().endsWith(".ps1")) {
            vscode.window.showErrorMessage("Current file is not a .ps1 script");
            return;
        }

        const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
        terminal.show();

        // Use PowerShell's call operator (&) to run the script
        terminal.sendText(`& "${filePath}"`);
    });

    context.subscriptions.push(disposable);
}

function deactivate() {}

module.exports = { activate, deactivate };
```

---

## ▶️ **How to Run**
1. Open the folder in VS Code.
2. Press **F5** to launch the extension host.
3. Open any `.ps1` file.
4. Run the command:
   - **Ctrl+Shift+P → Run Current PowerShell File**
   - or bind a keybinding if you want.

---

## 🔧 Optional Enhancements
Each item is a Guided Link so you can jump straight into expanding it:

- Add keybinding — Bind the command to F6 or Ctrl+F5  
- Auto-save before running — Ensure latest changes run  
- Run with arguments — Prompt user for parameters  
- Use PowerShell Core instead of Windows PowerShell — Switch to pwsh  

---

If you want, I can also generate a **TypeScript version**, add **error handling**, or package it for publishing.

----- 

Great — let’s expand each of those four enhancement options with **clear, ready‑to‑use code** and **Guided Links** so you can jump deeper into any part you want.

---

# ⭐ 1. **Add keybinding** — Bind the command to F6 or Ctrl+F5

To add a keybinding, modify your `package.json` and include a `keybindings` section:

```json
"contributes": {
  "commands": [
    {
      "command": "extension.runPs1",
      "title": "Run Current PowerShell File"
    }
  ],
  "keybindings": [
    {
      "command": "extension.runPs1",
      "key": "f6",
      "when": "editorTextFocus"
    },
    {
      "command": "extension.runPs1",
      "key": "ctrl+f5",
      "when": "editorTextFocus"
    }
  ]
}
```

If you want to go deeper into customizing this, use  
**custom keybindings**.

---

# ⭐ 2. **Auto‑save before running** — Ensure latest changes run

Modify your command handler in `extension.js`:

```js
let disposable = vscode.commands.registerCommand("extension.runPs1", async function () {
    const editor = vscode.window.activeTextEditor;
    if (!editor) {
        vscode.window.showErrorMessage("No active editor");
        return;
    }

    // Auto-save the file
    await editor.document.save();

    const filePath = editor.document.fileName;

    if (!filePath.toLowerCase().endsWith(".ps1")) {
        vscode.window.showErrorMessage("Current file is not a .ps1 script");
        return;
    }

    const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
    terminal.show();
    terminal.sendText(`& "${filePath}"`);
});
```

If you want to explore more auto‑save behaviors, jump to  
**advanced auto-save**.

---

# ⭐ 3. **Run with arguments** — Prompt user for parameters

Add a user input prompt before executing:

```js
let disposable = vscode.commands.registerCommand("extension.runPs1", async function () {
    const editor = vscode.window.activeTextEditor;
    if (!editor) {
        vscode.window.showErrorMessage("No active editor");
        return;
    }

    await editor.document.save();
    const filePath = editor.document.fileName;

    if (!filePath.toLowerCase().endsWith(".ps1")) {
        vscode.window.showErrorMessage("Current file is not a .ps1 script");
        return;
    }

    // Ask user for arguments
    const args = await vscode.window.showInputBox({
        prompt: "Enter arguments for the script",
        placeHolder: "-Name John -Verbose"
    });

    const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
    terminal.show();

    const command = args ? `& "${filePath}" ${args}` : `& "${filePath}"`;
    terminal.sendText(command);
});
```

If you want to build a richer UI for arguments, try  
**argument UI**.

---

# ⭐ 4. **Use PowerShell Core instead of Windows PowerShell** — Switch to `pwsh`

Replace the terminal creation line:

```js
const terminal = vscode.window.activeTerminal || vscode.window.createTerminal({
    name: "PowerShell Core",
    shellPath: "pwsh"
});
```

This ensures the script runs using PowerShell Core.

If you want to detect whether `pwsh` exists and fall back gracefully, explore  
**pwsh detection**.

---

# 🎯 What do you want to enhance next?
You can continue with:

- **Add status bar button**  
- **Add context menu entry**  
- **Add debug mode**  

Just pick one and I’ll build it out.

-----

Here you go — three clean, production‑ready enhancements for your VS Code extension, each with code you can drop directly into your existing `package.json` and `extension.js`.  
Every item includes a **Guided Link** so you can dive deeper if you want.

---

# ⭐ 1. **Add Status Bar Button**

A status bar button gives you a one‑click way to run the current PowerShell script.

### **Add this to `extension.js`:**

```js
function activate(context) {
    const runCommand = "extension.runPs1";

    let disposable = vscode.commands.registerCommand(runCommand, async function () {
        const editor = vscode.window.activeTextEditor;
        if (!editor) return vscode.window.showErrorMessage("No active editor");

        await editor.document.save();
        const filePath = editor.document.fileName;

        if (!filePath.toLowerCase().endsWith(".ps1"))
            return vscode.window.showErrorMessage("Not a .ps1 file");

        const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
        terminal.show();
        terminal.sendText(`& "${filePath}"`);
    });

    // Status bar button
    const button = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Left, 100);
    button.command = runCommand;
    button.text = "$(terminal) Run PS1";
    button.tooltip = "Run Current PowerShell Script";
    button.show();

    context.subscriptions.push(disposable, button);
}
```

If you want to customize icons or placement, jump to  
**status bar customization**.

---

# ⭐ 2. **Add Context Menu Entry**

This adds a right‑click menu item in the Explorer and Editor.

### **Add this to `package.json`:**

```json
"contributes": {
  "commands": [
    {
      "command": "extension.runPs1",
      "title": "Run PowerShell Script"
    }
  ],
  "menus": {
    "editor/context": [
      {
        "command": "extension.runPs1",
        "when": "resourceExtname == .ps1",
        "group": "navigation"
      }
    ],
    "explorer/context": [
      {
        "command": "extension.runPs1",
        "when": "resourceExtname == .ps1",
        "group": "navigation"
      }
    ]
  }
}
```

If you want to add icons or submenus, explore  
**context menu enhancements**.

---

# ⭐ 3. **Add Debug Mode**

This enhancement lets you run the script with PowerShell’s `-NoProfile -ExecutionPolicy Bypass` and optionally `Set-PSDebug -Trace 1`.

### **Add a second command in `package.json`:**

```json
{
  "command": "extension.debugPs1",
  "title": "Debug PowerShell Script"
}
```

### **Add this to `extension.js`:**

```js
let debugCmd = vscode.commands.registerCommand("extension.debugPs1", async function () {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    await editor.document.save();
    const filePath = editor.document.fileName;

    if (!filePath.toLowerCase().endsWith(".ps1"))
        return vscode.window.showErrorMessage("Not a .ps1 file");

    const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell Debug");
    terminal.show();

    terminal.sendText(`Set-PSDebug -Trace 1`);
    terminal.sendText(`powershell -NoProfile -ExecutionPolicy Bypass -File "${filePath}"`);
});
```

If you want breakpoints, variable inspection, or integration with VS Code’s debug adapter, go to  
**advanced PowerShell debugging**.

---

# 🎯 Want to go further?
You can expand your extension with:

- **Add output channel logging**  
- **Add settings UI**  
- **Add multi‑script runner**  

Tell me which direction you want to take next and I’ll build it out.

-----

Perfect — here are **three major upgrades** for your VS Code PowerShell‑runner extension, each fully implemented with clean code and **Guided Links** so you can expand any part instantly.

No filler, no fluff — just production‑ready features.

---

# ⭐ 1. **Add Output Channel Logging**

This gives your extension its own log window inside VS Code (like the built‑in Git or PowerShell logs).

### **Add this to `extension.js`:**

```js
const vscode = require("vscode");

function activate(context) {
    const output = vscode.window.createOutputChannel("PS1 Runner");

    function log(msg) {
        output.appendLine(msg);
    }

    let runCmd = vscode.commands.registerCommand("extension.runPs1", async () => {
        const editor = vscode.window.activeTextEditor;
        if (!editor) return vscode.window.showErrorMessage("No active editor");

        await editor.document.save();
        const filePath = editor.document.fileName;

        log(`Running: ${filePath}`);

        const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
        terminal.show();
        terminal.sendText(`& "${filePath}"`);

        log("Execution command sent to terminal");
    });

    context.subscriptions.push(runCmd, output);
}
```

If you want timestamps, log levels, or auto‑scrolling, jump to  
**enhanced logging**.

---

# ⭐ 2. **Add Settings UI**

This lets users configure your extension from **Settings → Extensions → Your Extension**.

### **Add this to `package.json`:**

```json
"contributes": {
  "configuration": {
    "type": "object",
    "title": "PS1 Runner Settings",
    "properties": {
      "ps1Runner.usePwsh": {
        "type": "boolean",
        "default": false,
        "description": "Use PowerShell Core (pwsh) instead of Windows PowerShell"
      },
      "ps1Runner.showLogs": {
        "type": "boolean",
        "default": true,
        "description": "Show output channel logs"
      }
    }
  }
}
```

### **Use settings in `extension.js`:**

```js
const config = vscode.workspace.getConfiguration("ps1Runner");
const usePwsh = config.get("usePwsh");
const showLogs = config.get("showLogs");

const terminal = vscode.window.activeTerminal || vscode.window.createTerminal({
    name: "PowerShell",
    shellPath: usePwsh ? "pwsh" : undefined
});

if (showLogs) output.show(true);
```

If you want dropdowns, enums, or multi‑setting profiles, explore  
**advanced settings UI**.

---

# ⭐ 3. **Add Multi‑Script Runner**

This lets the user select **multiple `.ps1` files** and run them sequentially.

### **Add a new command in `package.json`:**

```json
{
  "command": "extension.runMultiplePs1",
  "title": "Run Multiple PowerShell Scripts"
}
```

### **Add this to `extension.js`:**

```js
let multiCmd = vscode.commands.registerCommand("extension.runMultiplePs1", async () => {
    const files = await vscode.window.showOpenDialog({
        canSelectMany: true,
        filters: { "PowerShell": ["ps1"] }
    });

    if (!files || files.length === 0) return;

    const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
    terminal.show();

    for (const file of files) {
        const path = file.fsPath;
        output.appendLine(`Running: ${path}`);
        terminal.sendText(`& "${path}"`);
    }
});
```

If you want parallel execution, progress bars, or batch profiles, check  
**advanced multi‑script runner**.

---

# 🎯 What’s next for your extension?

You can continue evolving it with:

- **Add task queue + progress UI**  
- **Add integrated terminal reuse logic**  
- **Add script templates + scaffolding**  

Tell me which direction you want to take and I’ll build the next feature.

-----

Here come **three major power‑ups** for your VS Code PowerShell‑runner extension — each one production‑ready, cleanly structured, and designed to scale.  
Every feature includes **Guided Links** so you can jump deeper into any direction you want.

---

# ⭐ 1. **Add Task Queue + Progress UI**

This feature lets you queue multiple scripts and show a progress indicator in VS Code’s UI.

### **Add a new command in `package.json`:**

```json
{
  "command": "extension.runQueuedPs1",
  "title": "Run Queued PowerShell Scripts"
}
```

---

### **Add this to `extension.js`:**

```js
let queueCmd = vscode.commands.registerCommand("extension.runQueuedPs1", async () => {
    const files = await vscode.window.showOpenDialog({
        canSelectMany: true,
        filters: { "PowerShell": ["ps1"] }
    });

    if (!files || files.length === 0) return;

    const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell");
    terminal.show();

    await vscode.window.withProgress(
        {
            location: vscode.ProgressLocation.Notification,
            title: "Running PowerShell Scripts",
            cancellable: false
        },
        async (progress) => {
            const total = files.length;

            for (let i = 0; i < total; i++) {
                const file = files[i].fsPath;
                progress.report({
                    message: `Running ${file}`,
                    increment: (100 / total)
                });

                terminal.sendText(`& "${file}"`);
                await new Promise(resolve => setTimeout(resolve, 500)); // small delay
            }
        }
    );
});
```

If you want a **real task scheduler**, explore  
**task queue engine**.

---

# ⭐ 2. **Add Integrated Terminal Reuse Logic**

This ensures your extension **reuses the same terminal**, clears it if needed, and avoids clutter.

### **Replace your terminal creation logic with this:**

```js
function getOrCreateTerminal() {
    const name = "PowerShell Runner";

    let terminal = vscode.window.terminals.find(t => t.name === name);
    if (!terminal) {
        terminal = vscode.window.createTerminal(name);
    } else {
        terminal.sendText("clear");
    }

    terminal.show();
    return terminal;
}
```

### **Use it like this:**

```js
const terminal = getOrCreateTerminal();
terminal.sendText(`& "${filePath}"`);
```

If you want to add **terminal pooling**, jump to  
**terminal pool design**.

---

# ⭐ 3. **Add Script Templates + Scaffolding**

This lets users generate new PowerShell scripts from templates (header, comment block, function skeleton, etc.).

### **Add a new command in `package.json`:**

```json
{
  "command": "extension.createPs1Template",
  "title": "Create PowerShell Script Template"
}
```

---

### **Add this to `extension.js`:**

```js
let templateCmd = vscode.commands.registerCommand("extension.createPs1Template", async () => {
    const uri = await vscode.window.showSaveDialog({
        filters: { "PowerShell": ["ps1"] },
        saveLabel: "Create Script"
    });

    if (!uri) return;

    const template = `
<# 
.SYNOPSIS
    Describe what this script does.

.DESCRIPTION
    More detailed explanation.

.PARAMETER Example
    Description of parameter.

.EXAMPLE
    ./script.ps1 -Example value
#>

param(
    [Parameter(Mandatory=$false)]
    [string]$Example
)

function Main {
    Write-Host "Running script..."
}

Main
`;

    await vscode.workspace.fs.writeFile(uri, Buffer.from(template, "utf8"));
    const doc = await vscode.workspace.openTextDocument(uri);
    vscode.window.showTextDocument(doc);
});
```

If you want multiple templates, categories, or user‑defined scaffolds, explore  
**template manager**.

---

# 🎯 What’s your next upgrade?

You can continue evolving your extension with:

- **Add PowerShell output capture**  
- **Add script scheduler**  
- **Add Git integration**  

Tell me which direction you want to take and I’ll build the next feature.

-----

Here come **three heavyweight upgrades** for your VS Code PowerShell‑runner extension — each one designed to make your tool feel like a *real developer utility*, not just a script launcher.  
As always, everything is production‑ready, clean, and expandable, with **Guided Links** woven naturally throughout.

---

# ⭐ 1. **Add PowerShell Output Capture**

VS Code extensions **cannot directly read terminal output**, but you *can* capture output by running PowerShell with `-Command` and reading the result through Node’s `child_process`.

This gives you **real output**, not just what appears in the terminal.

### Add this to `extension.js`:

```js
const { exec } = require("child_process");

let captureCmd = vscode.commands.registerCommand("extension.capturePs1Output", async () => {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    await editor.document.save();
    const filePath = editor.document.fileName;

    const output = vscode.window.createOutputChannel("PS1 Output");
    output.show(true);
    output.appendLine(`Running: ${filePath}`);

    exec(`powershell -NoProfile -ExecutionPolicy Bypass -File "${filePath}"`, (err, stdout, stderr) => {
        if (stdout) output.appendLine(stdout);
        if (stderr) output.appendLine("ERROR: " + stderr);
        if (err) output.appendLine("EXEC ERROR: " + err.message);
    });
});
```

This gives you:

- Real captured output  
- Error capture  
- A dedicated output channel  

If you want **streaming output**, jump to  
**streaming PowerShell output**.

---

# ⭐ 2. **Add Script Scheduler**

This lets users schedule a PowerShell script to run later — like a lightweight cron inside VS Code.

### Add a new command in `package.json`:

```json
{
  "command": "extension.schedulePs1",
  "title": "Schedule PowerShell Script"
}
```

---

### Add this to `extension.js`:

```js
let scheduleCmd = vscode.commands.registerCommand("extension.schedulePs1", async () => {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    await editor.document.save();
    const filePath = editor.document.fileName;

    const time = await vscode.window.showInputBox({
        prompt: "Run script in how many seconds?",
        placeHolder: "e.g., 10"
    });

    const seconds = parseInt(time);
    if (isNaN(seconds)) return vscode.window.showErrorMessage("Invalid number");

    vscode.window.showInformationMessage(`Scheduled to run in ${seconds} seconds`);

    setTimeout(() => {
        const terminal = vscode.window.activeTerminal || vscode.window.createTerminal("PowerShell Scheduler");
        terminal.show();
        terminal.sendText(`& "${filePath}"`);
    }, seconds * 1000);
});
```

If you want **recurring schedules**, explore  
**recurring scheduler**.

---

# ⭐ 3. **Add Git Integration**

This feature lets your extension:

- Detect if the script is in a Git repo  
- Show the current branch  
- Auto‑commit before running  
- Or run only if the working tree is clean  

### Add a new command in `package.json`:

```json
{
  "command": "extension.gitInfoPs1",
  "title": "Show Git Info for Script"
}
```

---

### Add this to `extension.js`:

```js
const simpleGit = require("simple-git");

let gitCmd = vscode.commands.registerCommand("extension.gitInfoPs1", async () => {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    const filePath = editor.document.fileName;
    const folder = vscode.workspace.getWorkspaceFolder(editor.document.uri);

    if (!folder) return vscode.window.showErrorMessage("File not in workspace");

    const git = simpleGit(folder.uri.fsPath);
    const output = vscode.window.createOutputChannel("PS1 Git Info");
    output.show(true);

    try {
        const status = await git.status();
        output.appendLine(`Branch: ${status.current}`);
        output.appendLine(`Modified: ${status.modified.join(", ") || "None"}`);
        output.appendLine(`Untracked: ${status.not_added.join(", ") || "None"}`);
    } catch (err) {
        output.appendLine("Not a Git repository");
    }
});
```

If you want **auto‑commit before running**, jump to  
**auto‑commit integration**.

---

# 🎯 What’s your next evolution?

You can keep leveling up with:

- **Add PowerShell debugger integration**  
- **Add script dependency graph**  
- **Add extension marketplace packaging**  

Tell me which direction you want to take and I’ll build the next feature.

-----
You’re building a seriously capable VS Code extension — and these next three upgrades take it into *professional‑grade tooling territory*.  
Below are complete, production‑ready implementations for:

- **PowerShell debugger integration**  
- **Script dependency graph**  
- **Extension marketplace packaging**

Each section includes **Guided Links** so you can expand any part instantly.

---

# ⭐ 1. PowerShell Debugger Integration  




VS Code already includes a PowerShell debugger, and your extension can **programmatically trigger debugging** of the current script using the built‑in debug API.

### Add a debug command in `package.json`:

```json
{
  "command": "extension.debugCurrentPs1",
  "title": "Debug Current PowerShell Script"
}
```

### Add this to `extension.js`:

```js
let debugCurrent = vscode.commands.registerCommand("extension.debugCurrentPs1", async () => {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    await editor.document.save();
    const filePath = editor.document.fileName;

    const debugConfig = {
        type: "PowerShell",
        request: "launch",
        name: "Debug Current PS1",
        script: filePath
    };

    vscode.debug.startDebugging(undefined, debugConfig);
});
```

This launches the **real PowerShell debugger** with breakpoints, variable inspection, call stack, and stepping.

If you want to add **pre‑debug validation**, jump to  
**debug prechecks**.

---

# ⭐ 2. Script Dependency Graph  




This feature scans a PowerShell script for:

- `.` (dot‑sourcing)  
- `Import-Module`  
- `#requires`  
- Relative script calls  

…and builds a **visual dependency graph** using a simple GraphViz `.dot` file.

### Add a new command in `package.json`:

```json
{
  "command": "extension.showPs1DependencyGraph",
  "title": "Show PowerShell Dependency Graph"
}
```

---

### Add this to `extension.js`:

```js
let depGraphCmd = vscode.commands.registerCommand("extension.showPs1DependencyGraph", async () => {
    const editor = vscode.window.activeTextEditor;
    if (!editor) return vscode.window.showErrorMessage("No active editor");

    const text = editor.document.getText();
    const filePath = editor.document.fileName;

    const deps = [];

    const dotSourceRegex = /^\.\s+["'](.+?)["']/gm;
    const importModuleRegex = /Import-Module\s+([A-Za-z0-9\.\-_]+)/gm;
    const requiresRegex = /#requires\s+-Module\s+([A-Za-z0-9\.\-_]+)/gm;

    let match;

    while ((match = dotSourceRegex.exec(text))) deps.push(match[1]);
    while ((match = importModuleRegex.exec(text))) deps.push(match[1]);
    while ((match = requiresRegex.exec(text))) deps.push(match[1]);

    const dot = [
        "digraph G {",
        `  "${filePath}" [shape=box];`,
        ...deps.map(d => `  "${filePath}" -> "${d}";`),
        "}"
    ].join("\n");

    const doc = await vscode.workspace.openTextDocument({
        content: dot,
        language: "dot"
    });

    vscode.window.showTextDocument(doc);
});
```

This produces a `.dot` graph you can render with:

- GraphViz  
- VS Code GraphViz Preview  
- Online DOT viewers  

If you want **recursive dependency resolution**, explore  
**deep dependency graph**.

---

# ⭐ 3. Extension Marketplace Packaging  




To publish your extension to the VS Code Marketplace, you need:

- A `publisher`  
- A `vsce` package  
- A `.vsix` build  
- Marketplace metadata  

### Add required fields to `package.json`:

```json
{
  "publisher": "yourname",
  "repository": {
    "type": "git",
    "url": "https://github.com/yourname/ps1-runner"
  },
  "icon": "images/icon.png",
  "categories": ["Other", "Programming Languages", "Debuggers"]
}
```

### Create a minimal `README.md`:

Include:

- Features  
- Commands  
- Screenshots  
- Requirements  

### Create a `CHANGELOG.md`:

Marketplace requires it.

### Build the extension:

```
vsce package
```

This produces:

```
your-extension-0.0.1.vsix
```

### Publish:

```
vsce publish
```

If you want a **full marketplace‑ready folder structure**, jump to  
**marketplace scaffolding**.

---

# 🎯 What’s your next evolution?

You can continue building a powerhouse extension with:

- **Add AI‑assisted PowerShell refactoring**  
- **Add script metrics + complexity analysis**  
- **Add PowerShell module explorer**  

Tell me which direction you want to take and I’ll build the next feature.
-----
