
# fb_main-menu #2 fix

Here is the **full fixed version**. I simplified it so there are no line-break parser problems with operators like `-ne` / `-eq`.

### `main-menu.ps1`

```powershell
param(
    [string]$JsonFile = (Join-Path $PSScriptRoot "menu.json")
)

# ============================================================
# Windows Forms
# ============================================================

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

[System.Windows.Forms.Application]::EnableVisualStyles()

# ============================================================
# Create JSON if it doesn't exist
# ============================================================

if (-not (Test-Path -LiteralPath $JsonFile)) {
    @() | ConvertTo-Json | Set-Content -LiteralPath $JsonFile -Encoding UTF8
}

# ============================================================
# Read menu.json
# ============================================================

function Read-MenuItems {

    try {

        if (-not (Test-Path -LiteralPath $JsonFile)) {
            return @()
        }

        $content = Get-Content -LiteralPath $JsonFile -Raw

        if ([string]::IsNullOrWhiteSpace($content)) {
            return @()
        }

        $data = $content | ConvertFrom-Json

        if ($null -eq $data) {
            return @()
        }

        return @($data)
    }
    catch {

        [System.Windows.Forms.MessageBox]::Show(
            "Could not read menu.json.`r`n`r`n$($_.Exception.Message)",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Error
        ) | Out-Null

        return @()
    }
}

# ============================================================
# Save menu.json
# ============================================================

function Save-MenuItems {
    param(
        [object[]]$Items
    )

    try {

        $json = @($Items) | ConvertTo-Json -Depth 10

        Set-Content `
            -LiteralPath $JsonFile `
            -Value $json `
            -Encoding UTF8
    }
    catch {

        [System.Windows.Forms.MessageBox]::Show(
            "Could not save menu.json.`r`n`r`n$($_.Exception.Message)",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Error
        ) | Out-Null
    }
}

# ============================================================
# Resolve PS1 path
# ============================================================

function Resolve-ScriptPath {
    param(
        [string]$FileName
    )

    if ([string]::IsNullOrWhiteSpace($FileName)) {
        return $null
    }

    # Absolute path
    if ([System.IO.Path]::IsPathRooted($FileName)) {
        return [System.IO.Path]::GetFullPath($FileName)
    }

    # Relative path
    return [System.IO.Path]::GetFullPath(
        (Join-Path $PSScriptRoot $FileName)
    )
}

# ============================================================
# Refresh ListView
# ============================================================

function Refresh-List {

    $list.Items.Clear()

    $items = @(Read-MenuItems)

    foreach ($item in $items) {

        $name = [string]$item.name
        $description = [string]$item.description
        $fileName = [string]$item.file_name

        $row = New-Object System.Windows.Forms.ListViewItem($name)

        [void]$row.SubItems.Add($description)
        [void]$row.SubItems.Add($fileName)

        # Store JSON object in ListView row
        $row.Tag = $item

        [void]$list.Items.Add($row)
    }
}

# ============================================================
# Create Add/Edit dialog
# ============================================================

function Show-EditDialog {
    param(
        [object]$ExistingItem,
        [string]$Title
    )

    # --------------------------------------------------------
    # Dialog
    # --------------------------------------------------------

    $dialog = New-Object System.Windows.Forms.Form

    $dialog.Text = $Title
    $dialog.StartPosition = "CenterParent"
    $dialog.FormBorderStyle = "FixedDialog"

    $dialog.MinimizeBox = $false
    $dialog.MaximizeBox = $false
    $dialog.ShowInTaskbar = $false

    $dialog.ClientSize = New-Object System.Drawing.Size(520, 260)

    # --------------------------------------------------------
    # Font
    # --------------------------------------------------------

    $font = New-Object System.Drawing.Font(
        "Segoe UI",
        9
    )

    # --------------------------------------------------------
    # Name label
    # --------------------------------------------------------

    $nameLabel = New-Object System.Windows.Forms.Label

    $nameLabel.Text = "Name:"
    $nameLabel.Location = New-Object System.Drawing.Point(15, 20)
    $nameLabel.Size = New-Object System.Drawing.Size(100, 25)
    $nameLabel.Font = $font

    $dialog.Controls.Add($nameLabel)

    # --------------------------------------------------------
    # Name textbox
    # --------------------------------------------------------

    $nameBox = New-Object System.Windows.Forms.TextBox

    $nameBox.Location = New-Object System.Drawing.Point(120, 17)
    $nameBox.Size = New-Object System.Drawing.Size(370, 25)
    $nameBox.Font = $font

    if ($null -ne $ExistingItem) {
        $nameBox.Text = [string]$ExistingItem.name
    }

    $dialog.Controls.Add($nameBox)

    # --------------------------------------------------------
    # Description label
    # --------------------------------------------------------

    $descriptionLabel = New-Object System.Windows.Forms.Label

    $descriptionLabel.Text = "Description:"
    $descriptionLabel.Location = New-Object System.Drawing.Point(15, 70)
    $descriptionLabel.Size = New-Object System.Drawing.Size(100, 25)
    $descriptionLabel.Font = $font

    $dialog.Controls.Add($descriptionLabel)

    # --------------------------------------------------------
    # Description textbox
    # --------------------------------------------------------

    $descriptionBox = New-Object System.Windows.Forms.TextBox

    $descriptionBox.Location = New-Object System.Drawing.Point(120, 67)
    $descriptionBox.Size = New-Object System.Drawing.Size(370, 25)
    $descriptionBox.Font = $font

    if ($null -ne $ExistingItem) {
        $descriptionBox.Text = [string]$ExistingItem.description
    }

    $dialog.Controls.Add($descriptionBox)

    # --------------------------------------------------------
    # File label
    # --------------------------------------------------------

    $fileLabel = New-Object System.Windows.Forms.Label

    $fileLabel.Text = "PS1 file:"
    $fileLabel.Location = New-Object System.Drawing.Point(15, 120)
    $fileLabel.Size = New-Object System.Drawing.Size(100, 25)
    $fileLabel.Font = $font

    $dialog.Controls.Add($fileLabel)

    # --------------------------------------------------------
    # File textbox
    # --------------------------------------------------------

    $fileBox = New-Object System.Windows.Forms.TextBox

    $fileBox.Location = New-Object System.Drawing.Point(120, 117)
    $fileBox.Size = New-Object System.Drawing.Size(370, 25)
    $fileBox.Font = $font

    if ($null -ne $ExistingItem) {
        $fileBox.Text = [string]$ExistingItem.file_name
    }

    $dialog.Controls.Add($fileBox)

    # --------------------------------------------------------
    # Hint
    # --------------------------------------------------------

    $hint = New-Object System.Windows.Forms.Label

    $hint.Text = "Example: test.ps1 or scripts\test.ps1"
    $hint.Location = New-Object System.Drawing.Point(120, 147)
    $hint.Size = New-Object System.Drawing.Size(370, 20)
    $hint.ForeColor = [System.Drawing.Color]::Gray

    $dialog.Controls.Add($hint)

    # --------------------------------------------------------
    # OK button
    # --------------------------------------------------------

    $okButton = New-Object System.Windows.Forms.Button

    $okButton.Text = "OK"
    $okButton.Location = New-Object System.Drawing.Point(330, 205)
    $okButton.Size = New-Object System.Drawing.Size(75, 30)

    $dialog.Controls.Add($okButton)

    # --------------------------------------------------------
    # Cancel button
    # --------------------------------------------------------

    $cancelButton = New-Object System.Windows.Forms.Button

    $cancelButton.Text = "Cancel"
    $cancelButton.Location = New-Object System.Drawing.Point(415, 205)
    $cancelButton.Size = New-Object System.Drawing.Size(75, 30)

    $dialog.Controls.Add($cancelButton)

    # --------------------------------------------------------
    # OK event
    # --------------------------------------------------------

    $okButton.Add_Click({

        if ([string]::IsNullOrWhiteSpace($nameBox.Text)) {

            [System.Windows.Forms.MessageBox]::Show(
                "Name is required.",
                "main-menu",
                [System.Windows.Forms.MessageBoxButtons]::OK,
                [System.Windows.Forms.MessageBoxIcon]::Warning
            ) | Out-Null

            return
        }

        if ([string]::IsNullOrWhiteSpace($fileBox.Text)) {

            [System.Windows.Forms.MessageBox]::Show(
                "PS1 file is required.",
                "main-menu",
                [System.Windows.Forms.MessageBoxButtons]::OK,
                [System.Windows.Forms.MessageBoxIcon]::Warning
            ) | Out-Null

            return
        }

        $dialog.Tag = [PSCustomObject]@{
            name = $nameBox.Text.Trim()
            description = $descriptionBox.Text.Trim()
            file_name = $fileBox.Text.Trim()
        }

        $dialog.DialogResult = [System.Windows.Forms.DialogResult]::OK
        $dialog.Close()
    })

    # --------------------------------------------------------
    # Cancel event
    # --------------------------------------------------------

    $cancelButton.Add_Click({

        $dialog.DialogResult =
            [System.Windows.Forms.DialogResult]::Cancel

        $dialog.Close()
    })

    # --------------------------------------------------------
    # Show
    # --------------------------------------------------------

    $result = $dialog.ShowDialog($window)

    if ($result -eq [System.Windows.Forms.DialogResult]::OK) {
        $returnValue = $dialog.Tag
    }
    else {
        $returnValue = $null
    }

    $dialog.Dispose()

    return $returnValue
}

# ============================================================
# Add item
# ============================================================

function Add-MenuItem {

    $newItem = Show-EditDialog `
        -ExistingItem $null `
        -Title "Add menu item"

    if ($null -eq $newItem) {
        return
    }

    $items = @(Read-MenuItems)

    $items += $newItem

    Save-MenuItems $items

    Refresh-List
}

# ============================================================
# Edit item
# ============================================================

function Edit-SelectedItem {

    if ($list.SelectedItems.Count -eq 0) {

        [System.Windows.Forms.MessageBox]::Show(
            "Select an item first.",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Information
        ) | Out-Null

        return
    }

    $selected = $list.SelectedItems[0].Tag

    $edited = Show-EditDialog `
        -ExistingItem $selected `
        -Title "Edit menu item"

    if ($null -eq $edited) {
        return
    }

    $items = @(Read-MenuItems)

    for ($i = 0; $i -lt $items.Count; $i++) {

        if (
            $items[$i].name -eq $selected.name -and
            $items[$i].file_name -eq $selected.file_name
        ) {

            $items[$i] = $edited

            break
        }
    }

    Save-MenuItems $items

    Refresh-List
}

# ============================================================
# Delete item
# ============================================================

function Delete-SelectedItem {

    if ($list.SelectedItems.Count -eq 0) {

        [System.Windows.Forms.MessageBox]::Show(
            "Select an item first.",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Information
        ) | Out-Null

        return
    }

    $selected = $list.SelectedItems[0].Tag

    $answer = [System.Windows.Forms.MessageBox]::Show(
        "Delete '$($selected.name)'?",
        "main-menu",
        [System.Windows.Forms.MessageBoxButtons]::YesNo,
        [System.Windows.Forms.MessageBoxIcon]::Question
    )

    if ($answer -ne [System.Windows.Forms.DialogResult]::Yes) {
        return
    }

    $items = @(Read-MenuItems)

    $newItems = @()

    foreach ($item in $items) {

        if (
            $item.name -eq $selected.name -and
            $item.file_name -eq $selected.file_name
        ) {
            continue
        }

        $newItems += $item
    }

    Save-MenuItems $newItems

    Refresh-List
}

# ============================================================
# Run selected PS1
# ============================================================

function Run-SelectedItem {

    if ($list.SelectedItems.Count -eq 0) {

        [System.Windows.Forms.MessageBox]::Show(
            "Select an item first.",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Information
        ) | Out-Null

        return
    }

    $item = $list.SelectedItems[0].Tag

    $scriptPath = Resolve-ScriptPath $item.file_name

    # --------------------------------------------------------
    # Check file
    # --------------------------------------------------------

    if (
        [string]::IsNullOrWhiteSpace($scriptPath) -or
        -not (Test-Path -LiteralPath $scriptPath -PathType Leaf)
    ) {

        [System.Windows.Forms.MessageBox]::Show(
            "PS1 file was not found:`r`n`r`n$scriptPath",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Warning
        ) | Out-Null

        return
    }

    # --------------------------------------------------------
    # Hide main menu
    # --------------------------------------------------------

    $window.Hide()

    try {

        # ----------------------------------------------------
        # Start PowerShell child process
        # ----------------------------------------------------

        $process = Start-Process `
            -FilePath "powershell.exe" `
            -ArgumentList @(
                "-NoProfile"
                "-ExecutionPolicy"
                "Bypass"
                "-File"
                $scriptPath
            ) `
            -WorkingDirectory (Split-Path -Parent $scriptPath) `
            -PassThru

        # ----------------------------------------------------
        # Wait until PS1 closes
        # ----------------------------------------------------

        $process.WaitForExit()

        $process.Dispose()
    }
    catch {

        [System.Windows.Forms.MessageBox]::Show(
            "Could not start the PS1 file.`r`n`r`n$($_.Exception.Message)",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Error
        ) | Out-Null
    }
    finally {

        # ----------------------------------------------------
        # Return to main menu
        # ----------------------------------------------------

        Refresh-List

        $window.Show()
        $window.Activate()
    }
}

# ============================================================
# MAIN WINDOW
# ============================================================

$window = New-Object System.Windows.Forms.Form

$window.Text = "main-menu"

$window.StartPosition = "CenterScreen"

$window.ClientSize =
    New-Object System.Drawing.Size(900, 520)

$window.MinimumSize =
    New-Object System.Drawing.Size(700, 400)

$window.Font =
    New-Object System.Drawing.Font(
        "Segoe UI",
        9
    )

# ============================================================
# LIST VIEW
# ============================================================

$list = New-Object System.Windows.Forms.ListView

$list.View =
    [System.Windows.Forms.View]::Details

$list.FullRowSelect = $true
$list.GridLines = $true
$list.HideSelection = $false

$list.Anchor =
    [System.Windows.Forms.AnchorStyles]::Top `
    -bor [System.Windows.Forms.AnchorStyles]::Bottom `
    -bor [System.Windows.Forms.AnchorStyles]::Left `
    -bor [System.Windows.Forms.AnchorStyles]::Right

$list.Location =
    New-Object System.Drawing.Point(12, 48)

$list.Size =
    New-Object System.Drawing.Size(876, 395)

[void]$list.Columns.Add(
    "Name",
    180
)

[void]$list.Columns.Add(
    "Description",
    320
)

[void]$list.Columns.Add(
    "PS1 file",
    350
)

$window.Controls.Add($list)

# ============================================================
# ADD BUTTON
# ============================================================

$addButton = New-Object System.Windows.Forms.Button

$addButton.Text = "Add"

$addButton.Location =
    New-Object System.Drawing.Point(12, 12)

$addButton.Size =
    New-Object System.Drawing.Size(75, 28)

$window.Controls.Add($addButton)

# ============================================================
# EDIT BUTTON
# ============================================================

$editButton = New-Object System.Windows.Forms.Button

$editButton.Text = "Edit"

$editButton.Location =
    New-Object System.Drawing.Point(93, 12)

$editButton.Size =
    New-Object System.Drawing.Size(75, 28)

$window.Controls.Add($editButton)

# ============================================================
# DELETE BUTTON
# ============================================================

$deleteButton = New-Object System.Windows.Forms.Button

$deleteButton.Text = "Delete"

$deleteButton.Location =
    New-Object System.Drawing.Point(174, 12)

$deleteButton.Size =
    New-Object System.Drawing.Size(75, 28)

$window.Controls.Add($deleteButton)

# ============================================================
# RELOAD BUTTON
# ============================================================

$reloadButton = New-Object System.Windows.Forms.Button

$reloadButton.Text = "Reload"

$reloadButton.Location =
    New-Object System.Drawing.Point(255, 12)

$reloadButton.Size =
    New-Object System.Drawing.Size(75, 28)

$window.Controls.Add($reloadButton)

# ============================================================
# LOAD BUTTON
# ============================================================

$loadButton = New-Object System.Windows.Forms.Button

$loadButton.Text = "Load"

$loadButton.Anchor =
    [System.Windows.Forms.AnchorStyles]::Bottom `
    -bor [System.Windows.Forms.AnchorStyles]::Right

$loadButton.Location =
    New-Object System.Drawing.Point(813, 458)

$loadButton.Size =
    New-Object System.Drawing.Size(75, 30)

$window.Controls.Add($loadButton)

# ============================================================
# BUTTON EVENTS
# ============================================================

$addButton.Add_Click({
    Add-MenuItem
})

$editButton.Add_Click({
    Edit-SelectedItem
})

$deleteButton.Add_Click({
    Delete-SelectedItem
})

$reloadButton.Add_Click({
    Refresh-List
})

$loadButton.Add_Click({
    Run-SelectedItem
})

# ============================================================
# Double click
# ============================================================

$list.Add_DoubleClick({
    Run-SelectedItem
})

# ============================================================
# Keyboard
# ============================================================

$window.Add_KeyDown({

    if ($_.KeyCode -eq [System.Windows.Forms.Keys]::Enter) {
        Run-SelectedItem
    }

})

# ============================================================
# Initial load
# ============================================================

Refresh-List

# ============================================================
# Start GUI
# ============================================================

[void][System.Windows.Forms.Application]::Run($window)
```

### `menu.json`

Put this in the **same directory** as `main-menu.ps1`:

```json
[
    {
        "name": "Test",
        "description": "Test menu",
        "file_name": "test.ps1"
    },
    {
        "name": "AC",
        "description": "Run AC PowerShell program",
        "file_name": "C:\\Users\\TYARONK\\workspace_all\\01_workspace_vs\\powershell\\03_projects\\path\\AC.ps1"
    },
    {
        "name": "111",
        "description": "Run 111 PowerShell program",
        "file_name": "C:\\Users\\TYARONK\\workspace_all\\01_workspace_vs\\powershell\\03_projects\\path\\111.ps1"
    }
]
```

### `test.ps1`

Here's a simple child program for testing the hide/return behavior:

```powershell
Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

[System.Windows.Forms.Application]::EnableVisualStyles()

$form = New-Object System.Windows.Forms.Form

$form.Text = "Test"
$form.StartPosition = "CenterScreen"

$form.ClientSize =
    New-Object System.Drawing.Size(450, 250)

$form.Font =
    New-Object System.Drawing.Font(
        "Segoe UI",
        10
    )

$title = New-Object System.Windows.Forms.Label

$title.Text = "Test PowerShell Program"

$title.Font =
    New-Object System.Drawing.Font(
        "Segoe UI",
        18,
        [System.Drawing.FontStyle]::Bold
    )

$title.AutoSize = $true

$title.Location =
    New-Object System.Drawing.Point(30, 30)

$form.Controls.Add($title)

$message = New-Object System.Windows.Forms.Label

$message.Text =
    "main-menu is currently hidden.`r`n`r`nClose this window to return to main-menu."

$message.AutoSize = $true

$message.Location =
    New-Object System.Drawing.Point(32, 90)

$form.Controls.Add($message)

[void][System.Windows.Forms.Application]::Run($form)
```

### Directory structure

```text
main_menu\
│
├── main-menu.ps1
├── menu.json
└── test.ps1
```

Then run:

```powershell
powershell.exe -ExecutionPolicy Bypass -File ".\main-menu.ps1"
```

The important behavior is:

```text
             main-menu
                 │
                 │ Double click / Load
                 ▼
        ┌─────────────────┐
        │ Hide main-menu  │
        └────────┬────────┘
                 │
                 ▼
           Start test.ps1
                 │
                 │
          ┌──────▼──────┐
          │ Test window │
          └──────┬──────┘
                 │
             User closes
                 │
                 ▼
        ┌─────────────────┐
        │ Show main-menu  │
        └─────────────────┘
```

This version specifically fixes the parser errors you posted: **every `-eq`, `-ne`, `-bor`, etc. is kept syntactically intact**, rather than putting the operator itself on a separate line.
