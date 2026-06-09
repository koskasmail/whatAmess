               To build a VS Code extension that executes the currently open .ps1 file inside the integrated terminal, you must create a command that retrieves the active text editor's file path, checks for a .ps1 extension, and passes a formatted execution string to the VS Code terminal API.Here is the complete step-by-step guide to generating, coding, and configuring this extension.1. Initialize the Extension ProjectGenerate a fresh extension template using the official VS Code Yeoman generator.Run the generator command in your terminal:bashnpx yo code
חשוב להשתמש בקוד בזהירות.Select the following prompt choices:Type of extension: New Extension (TypeScript)Extension name: execute-ps1Identifier: execute-ps1Source control: Yes / No (your choice)Package manager: npm2. Configure the Manifest (package.json)Open the generated project folder and replace your package.json configurations. You need to declare the command, map it to a keyboard shortcut, and place an execution button in the editor title toolbar.json{
  "name": "execute-ps1",
  "displayName": "Execute PS1 Terminal",
  "description": "Executes the current .ps1 file in the integrated VS Code terminal",
  "version": "0.0.1",
  "engines": {
    "vscode": "^1.85.0"
  },
  "categories": [
    "Other"
  ],
  "activationEvents": [],
  "main": "./out/extension.js",
  "contributes": {
    "commands": [
      {
        "command": "execute-ps1.runFile",
        "title": "PowerShell: Run Current File in Terminal",
        "icon": "$(play)"
      }
    ],
    "keybindings": [
      {
        "command": "execute-ps1.runFile",
        "key": "ctrl+alt+p",
        "mac": "cmd+alt+p",
        "when": "editorLangId == powershell"
      }
    ],
    "menus": {
      "editor/title": [
        {
          "command": "execute-ps1.runFile",
          "group": "navigation",
          "when": "editorLangId == powershell"
        }
      ]
    }
  },
  "scripts": {
    "vscode:prepublish": "npm run compile",
    "compile": "tsc -p ./",
    "watch": "tsc -watch -p ./"
  },
  "devDependencies": {
    "@types/vscode": "^1.85.0",
    "typescript": "^5.3.3"
  }
}
חשוב להשתמש בקוד בזהירות.3. Write the Extension Logic (src/extension.ts)Open src/extension.ts and replace its entire contents with the following logic. This code locates an existing terminal or creates a new one, sanitizes the file path, wraps it in the PowerShell call operator &, and sends it to the shell.typescriptimport * as vscode from 'vscode';
import * as path from 'path';

export function activate(context: vscode.ExtensionContext) {
    let disposable = vscode.commands.registerCommand('execute-ps1.runFile', () => {
        const activeEditor = vscode.window.activeTextEditor;
        
        // 1. Ensure an editor window is open
        if (!activeEditor) {
            vscode.window.showErrorMessage('No active editor found.');
            return;
        }

        const document = activeEditor.document;
        const filePath = document.uri.fsPath;
        const extension = path.extname(filePath).toLowerCase();

        // 2. Validate that the open file is a PowerShell script
        if (extension !== '.ps1') {
            vscode.window.showWarningMessage('The active file is not a PowerShell (.ps1) script.');
            return;
        }

        // 3. Prompt user to save if there are unsaved changes
        if (document.isDirty) {
            document.save();
        }

        // 4. Retrieve or spawn a terminal
        let terminal = vscode.window.activeTerminal;
        if (!terminal) {
            terminal = vscode.window.createTerminal('PowerShell Execution');
        }

        // Show the terminal panel to the user
        terminal.show(true);

        // 5. Construct the PowerShell command safely wrapping spaces in quotes
        // Example: & "C:\path to file\script.ps1"
        const targetCommand = `& "${filePath}"`;

        // 6. Send execution command to the terminal window
        terminal.sendText(targetCommand);
    });

    context.subscriptions.push(disposable);
}

export function deactivate() {}
חשוב להשתמש בקוד בזהירות.4. Test the Extension LocallyPress F5 inside your VS Code window containing this extension code.A new Extension Development Host window will pop up.In this new window, open any folder containing a .ps1 script.Open the .ps1 file. You will see a Play Icon \((\blacktriangleright)\) on the top-right toolbar.Click the play button, or use the keybinding Ctrl + Alt + P (Cmd + Alt + P on Mac).The active integrated terminal will accept the file string and execute the script immediately.