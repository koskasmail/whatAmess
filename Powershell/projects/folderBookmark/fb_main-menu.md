
# fb main-menu



Sure. The main Windows-native GUI code is below. It uses **Windows Forms**, so there is no WPF/XAML involved.

### `main-menu.ps1`

```powershell
param(
    [string]$JsonFile = (Join-Path $PSScriptRoot "menu.json")
)

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

[System.Windows.Forms.Application]::EnableVisualStyles()

# ------------------------------------------------------------
# JSON
# ------------------------------------------------------------

if (-not (Test-Path -LiteralPath $JsonFile)) {
    "[]" | Set-Content -LiteralPath $JsonFile -Encoding UTF8
}

function Read-MenuItems {
    try {
        $raw = Get-Content -LiteralPath $JsonFile -Raw -ErrorAction Stop

        if ([string]::IsNullOrWhiteSpace($raw)) {
            return @()
        }

        $data = $raw | ConvertFrom-Json

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

function Save-MenuItems($items) {
    @($items) |
        ConvertTo-Json -Depth 5 |
        Set-Content -LiteralPath $JsonFile -Encoding UTF8
}

# ------------------------------------------------------------
# Resolve PS1 path
# ------------------------------------------------------------

function Resolve-ScriptPath([string]$fileName) {

    if ([string]::IsNullOrWhiteSpace($fileName)) {
        return $null
    }

    # Absolute path
    if ([System.IO.Path]::IsPathRooted($fileName)) {
        return [System.IO.Path]::GetFullPath($fileName)
    }

    # Relative path
    return [System.IO.Path]::GetFullPath(
        (Join-Path $PSScriptRoot $fileName)
    )
}

# ------------------------------------------------------------
# Refresh ListView
# ------------------------------------------------------------

function Refresh-List {

    $list.Items.Clear()

    foreach ($item in @(Read-MenuItems)) {

        $row = New-Object System.Windows.Forms.ListViewItem(
            $item.name
        )

        [void]$row.SubItems.Add(
            $item.description
        )

        [void]$row.SubItems.Add(
            $item.file_name
        )

        # Keep original JSON object attached to row
        $row.Tag = $item

        [void]$list.Items.Add($row)
    }
}

# ------------------------------------------------------------
# Add / Edit dialog
# ------------------------------------------------------------

function Edit-MenuItem($existing, [string]$title) {

    $dialog = New-Object System.Windows.Forms.Form

    $dialog.Text = $title
    $dialog.StartPosition = "CenterParent"
    $dialog.FormBorderStyle = "FixedDialog"
    $dialog.MinimizeBox = $false
    $dialog.MaximizeBox = $false
    $dialog.ShowInTaskbar = $false

    $dialog.ClientSize =
        New-Object System.Drawing.Size(500,245)

    $font =
        New-Object System.Drawing.Font(
            "Segoe UI",
            9
        )

    # --------------------------------------------------------
    # Name
    # --------------------------------------------------------

    $nameLabel =
        New-Object System.Windows.Forms.Label

    $nameLabel.Text = "Name:"
    $nameLabel.Location =
        New-Object System.Drawing.Point(15,18)
    $nameLabel.Size =
        New-Object System.Drawing.Size(100,25)
    $nameLabel.Font = $font

    $dialog.Controls.Add($nameLabel)

    $nameBox =
        New-Object System.Windows.Forms.TextBox

    $nameBox.Location =
        New-Object System.Drawing.Point(120,15)

    $nameBox.Size =
        New-Object System.Drawing.Size(355,25)

    $nameBox.Font = $font

    if ($existing) {
        $nameBox.Text = $existing.name
    }

    $dialog.Controls.Add($nameBox)

    # --------------------------------------------------------
    # Description
    # --------------------------------------------------------

    $descLabel =
        New-Object System.Windows.Forms.Label

    $descLabel.Text = "Description:"
    $descLabel.Location =
        New-Object System.Drawing.Point(15,73)

    $descLabel.Size =
        New-Object System.Drawing.Size(100,25)

    $descLabel.Font = $font

    $dialog.Controls.Add($descLabel)

    $descBox =
        New-Object System.Windows.Forms.TextBox

    $descBox.Location =
        New-Object System.Drawing.Point(120,70)

    $descBox.Size =
        New-Object System.Drawing.Size(355,25)

    $descBox.Font = $font

    if ($existing) {
        $descBox.Text = $existing.description
    }

    $dialog.Controls.Add($descBox)

    # --------------------------------------------------------
    # PS1 filename
    # --------------------------------------------------------

    $fileLabel =
        New-Object System.Windows.Forms.Label

    $fileLabel.Text = "PS1 file:"
    $fileLabel.Location =
        New-Object System.Drawing.Point(15,128)

    $fileLabel.Size =
        New-Object System.Drawing.Size(100,25)

    $fileLabel.Font = $font

    $dialog.Controls.Add($fileLabel)

    $fileBox =
        New-Object System.Windows.Forms.TextBox

    $fileBox.Location =
        New-Object System.Drawing.Point(120,125)

    $fileBox.Size =
        New-Object System.Drawing.Size(355,25)

    $fileBox.Font = $font

    if ($existing) {
        $fileBox.Text = $existing.file_name
    }

    $dialog.Controls.Add($fileBox)

    # --------------------------------------------------------
    # Hint
    # --------------------------------------------------------

    $hint =
        New-Object System.Windows.Forms.Label

    $hint.Text =
        "Relative paths are relative to the main-menu folder."

    $hint.Location =
        New-Object System.Drawing.Point(120,153)

    $hint.Size =
        New-Object System.Drawing.Size(355,20)

    $hint.ForeColor =
        [System.Drawing.Color]::Gray

    $hint.Font =
        New-Object System.Drawing.Font(
            "Segoe UI",
            8
        )

    $dialog.Controls.Add($hint)

    # --------------------------------------------------------
    # OK
    # --------------------------------------------------------

    $ok =
        New-Object System.Windows.Forms.Button

    $ok.Text = "OK"

    $ok.DialogResult =
        [System.Windows.Forms.DialogResult]::OK

    $ok.Location =
        New-Object System.Drawing.Point(320,190)

    $ok.Size =
        New-Object System.Drawing.Size(75,28)

    $dialog.Controls.Add($ok)

    # --------------------------------------------------------
    # Cancel
    # --------------------------------------------------------

    $cancel =
        New-Object System.Windows.Forms.Button

    $cancel.Text = "Cancel"

    $cancel.DialogResult =
        [System.Windows.Forms.DialogResult]::Cancel

    $cancel.Location =
        New-Object System.Drawing.Point(400,190)

    $cancel.Size =
        New-Object System.Drawing.Size(75,28)

    $dialog.Controls.Add($cancel)

    $dialog.AcceptButton = $ok
    $dialog.CancelButton = $cancel

    # --------------------------------------------------------
    # Show dialog
    # --------------------------------------------------------

    if (
        $dialog.ShowDialog($window) -
        ne [System.Windows.Forms.DialogResult]::OK
    ) {
        $dialog.Dispose()
        return $null
    }

    # --------------------------------------------------------
    # Validate
    # --------------------------------------------------------

    if (
        [string]::IsNullOrWhiteSpace($nameBox.Text) -or
        [string]::IsNullOrWhiteSpace($fileBox.Text)
    ) {

        [System.Windows.Forms.MessageBox]::Show(
            "Name and PS1 file are required.",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Warning
        ) | Out-Null

        $dialog.Dispose()

        return $null
    }

    # --------------------------------------------------------
    # Return object
    # --------------------------------------------------------

    $result = [pscustomobject]@{
        name        = $nameBox.Text.Trim()
        description = $descBox.Text.Trim()
        file_name   = $fileBox.Text.Trim()
    }

    $dialog.Dispose()

    return $result
}

# ------------------------------------------------------------
# Execute selected PS1
# ------------------------------------------------------------

function Run-Selected {

    if ($list.SelectedItems.Count -eq 0) {
        return
    }

    $item =
        $list.SelectedItems[0].Tag

    $scriptPath =
        Resolve-ScriptPath $item.file_name

    # --------------------------------------------------------
    # Check file
    # --------------------------------------------------------

    if (
        -not $scriptPath -or
        -not (Test-Path -LiteralPath $scriptPath -PathType Leaf)
    ) {

        [System.Windows.Forms.MessageBox]::Show(
            "PS1 file not found:`r`n$scriptPath",
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

        $psi =
            New-Object System.Diagnostics.ProcessStartInfo

        $psi.FileName =
            Join-Path `
                $env:SystemRoot `
                "System32\WindowsPowerShell\v1.0\powershell.exe"

        $psi.Arguments =
            '-NoProfile -ExecutionPolicy Bypass -File "' +
            $scriptPath +
            '"'

        $psi.WorkingDirectory =
            Split-Path -Parent $scriptPath

        $psi.UseShellExecute = $true

        # ----------------------------------------------------
        # Start child PS1
        # ----------------------------------------------------

        $process =
            [System.Diagnostics.Process]::Start($psi)

        # Wait until child PS1 closes
        $process.WaitForExit()

        $process.Dispose()
    }
    catch {

        [System.Windows.Forms.MessageBox]::Show(
            "Could not run the selected script.`r`n`r`n$($_.Exception.Message)",
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

$window =
    New-Object System.Windows.Forms.Form

$window.Text = "main-menu"

$window.StartPosition = "CenterScreen"

$window.ClientSize =
    New-Object System.Drawing.Size(900,520)

$window.MinimumSize =
    New-Object System.Drawing.Size(700,400)

$window.Font =
    New-Object System.Drawing.Font(
        "Segoe UI",
        9
    )

# ============================================================
# LIST
# ============================================================

$list =
    New-Object System.Windows.Forms.ListView

$list.View =
    [System.Windows.Forms.View]::Details

$list.FullRowSelect = $true
$list.GridLines = $true
$list.HideSelection = $false

$list.Anchor =
    "Top,Bottom,Left,Right"

$list.Location =
    New-Object System.Drawing.Point(12,45)

$list.Size =
    New-Object System.Drawing.Size(876,405)

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
    340
)

$window.Controls.Add($list)

# ============================================================
# ADD BUTTON
# ============================================================

$add =
    New-Object System.Windows.Forms.Button

$add.Text = "Add"

$add.Location =
    New-Object System.Drawing.Point(12,12)

$add.Size =
    New-Object System.Drawing.Size(75,28)

$window.Controls.Add($add)

# ============================================================
# EDIT BUTTON
# ============================================================

$edit =
    New-Object System.Windows.Forms.Button

$edit.Text = "Edit"

$edit.Location =
    New-Object System.Drawing.Point(93,12)

$edit.Size =
    New-Object System.Drawing.Size(75,28)

$window.Controls.Add($edit)

# ============================================================
# DELETE BUTTON
# ============================================================

$delete =
    New-Object System.Windows.Forms.Button

$delete.Text = "Delete"

$delete.Location =
    New-Object System.Drawing.Point(174,12)

$delete.Size =
    New-Object System.Drawing.Size(75,28)

$window.Controls.Add($delete)

# ============================================================
# RELOAD BUTTON
# ============================================================

$reload =
    New-Object System.Windows.Forms.Button

$reload.Text = "Reload"

$reload.Location =
    New-Object System.Drawing.Point(255,12)

$reload.Size =
    New-Object System.Drawing.Size(75,28)

$window.Controls.Add($reload)

# ============================================================
# LOAD BUTTON
# ============================================================

$load =
    New-Object System.Windows.Forms.Button

$load.Text = "Load"

$load.Anchor =
    "Bottom,Right"

$load.Location =
    New-Object System.Drawing.Point(813,458)

$load.Size =
    New-Object System.Drawing.Size(75,30)

$window.Controls.Add($load)

# ============================================================
# EVENTS
# ============================================================

$window.Add_Shown({
    Refresh-List
})

# ADD
$add.Add_Click({

    $newItem =
        Edit-MenuItem `
            $null `
            "Add menu item"

    if ($newItem) {

        $items =
            @(Read-MenuItems)

        $items += $newItem

        Save-MenuItems $items

        Refresh-List
    }
})

# EDIT
$edit.Add_Click({

    if ($list.SelectedItems.Count -eq 0) {
        return
    }

    $selected =
        $list.SelectedItems[0].Tag

    $edited =
        Edit-MenuItem `
            $selected `
            "Edit menu item"

    if ($edited) {

        $items =
            @(Read-MenuItems)

        for (
            $i = 0;
            $i -lt $items.Count;
            $i++
        ) {

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
})

# DELETE
$delete.Add_Click({

    if ($list.SelectedItems.Count -eq 0) {
        return
    }

    $selected =
        $list.SelectedItems[0].Tag

    $answer =
        [System.Windows.Forms.MessageBox]::Show(
            "Delete '$($selected.name)'?",
            "main-menu",
            [System.Windows.Forms.MessageBoxButtons]::YesNo,
            [System.Windows.Forms.MessageBoxIcon]::Question
        )

    if (
        $answer -eq
        [System.Windows.Forms.DialogResult]::Yes
    ) {

        $items =
            @(Read-MenuItems) |
            Where-Object {
                -not (
                    $_.name -eq $selected.name -and
                    $_.file_name -eq $selected.file_name
                )
            }

        Save-MenuItems @($items)

        Refresh-List
    }
})

# RELOAD
$reload.Add_Click({
    Refresh-List
})

# LOAD
$load.Add_Click({
    Run-Selected
})

# DOUBLE CLICK
$list.Add_DoubleClick({
    Run-Selected
})

# ENTER
$window.Add_KeyDown({

    if (
        $_.KeyCode -
        eq [System.Windows.Forms.Keys]::Enter
    ) {
        Run-Selected
    }
})

# ============================================================
# START
# ============================================================

[void][System.Windows.Forms.Application]::Run($window)
```

### `menu.json`

```json
[
    {
        "name": "Test",
        "description": "Test menu",
        "file_name": "test.ps1"
    },
    {
        "name": "Backup",
        "description": "Run backup script",
        "file_name": "scripts\\backup.ps1"
    },
    {
        "name": "Settings",
        "description": "Open settings",
        "file_name": "settings.ps1"
    }
]
```

The important part for your requested behavior is `Run-Selected`: it calls `$window.Hide()`, starts the selected `.ps1`, waits with `WaitForExit()`, and then calls `$window.Show()` when the child script closes.

I can also make the next version **even more Windows-native**, with a menu-style layout such as **large icon buttons / shortcuts**, similar to an old Windows Control Panel.
