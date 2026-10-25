
# copyFile

##### Take #1

Below is a complete Windows PowerShell (`.ps1`) program with a Windows-style graphical interface titled Copy File.

It lets you select a local or network source file, choose a destination, copy without confirmation prompts, and display success or error messages.

Administrator privileges: the script automatically requests elevation when needed. Windows may display a UAC permission prompt when elevation is required; a standard Windows security prompt cannot be bypassed by a normal PowerShell script. Once elevated, the copy operation runs without additional confirmation dialogs.

## Features

- Browse for a local file or a file on a network share (`\\server\share\file.ext`).
- Browse for a destination folder and specify the destination filename.
- Copy button to start copying immediately.
- Submit button to perform the copy.
- Cancel button to cancel a copy in progress.
- Exit button to close the application.
- Status bar showing progress, current status, and errors.
- Success and failure messages when the operation finishes.
- Overwrite an existing destination file without an additional confirmation prompt.

## Complete PowerShell program — `Copy-File.ps1`

Save the following code as `Copy-File.ps1` and run it using Windows PowerShell.

```

# Copy-File.ps1
# Windows PowerShell 5.1+
# GUI for copying local or network files with administrator elevation.

# ------------------------------------------------------------
# 1. Request administrator privileges when necessary
# ------------------------------------------------------------
$currentIdentity = [Security.Principal.WindowsIdentity]::GetCurrent()
$principal = New-Object Security.Principal.WindowsPrincipal($currentIdentity)
$isAdmin = $principal.IsInRole(
    [Security.Principal.WindowsBuiltInRole]::Administrator
)

if (-not $isAdmin) {
    try {
        $arguments = "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`""
        Start-Process -FilePath "powershell.exe" `
            -ArgumentList $arguments `
            -Verb RunAs
    }
    catch {
        [System.Windows.Forms.MessageBox]::Show(
            "Administrator privileges were not granted.`n$($_.Exception.Message)",
            "Copy File - Error",
            "OK",
            "Error"
        ) | Out-Null
    }
    exit
}

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

[System.Windows.Forms.Application]::EnableVisualStyles()

# ------------------------------------------------------------
# 2. Main window
# ------------------------------------------------------------
$form = New-Object System.Windows.Forms.Form
$form.Text = "Copy File"
$form.Size = New-Object System.Drawing.Size(760, 510)
$form.MinimumSize = New-Object System.Drawing.Size(760, 510)
$form.StartPosition = "CenterScreen"
$form.BackColor = [System.Drawing.Color]::FromArgb(245, 247, 250)
$form.Font = New-Object System.Drawing.Font("Segoe UI", 10)
$form.FormBorderStyle = "FixedDialog"
$form.MaximizeBox = $false

# Header
$header = New-Object System.Windows.Forms.Panel
$header.Location = New-Object System.Drawing.Point(0, 0)
$header.Size = New-Object System.Drawing.Size(760, 76)
$header.BackColor = [System.Drawing.Color]::FromArgb(35, 70, 115)
$form.Controls.Add($header)

$title = New-Object System.Windows.Forms.Label
$title.Text = "COPY FILE"
$title.Location = New-Object System.Drawing.Point(24, 12)
$title.Size = New-Object System.Drawing.Size(400, 32)
$title.ForeColor = [System.Drawing.Color]::White
$title.Font = New-Object System.Drawing.Font(
    "Segoe UI", 19, [System.Drawing.FontStyle]::Bold
)
$header.Controls.Add($title)

$subtitle = New-Object System.Windows.Forms.Label
$subtitle.Text = "Copy local files and network files"
$subtitle.Location = New-Object System.Drawing.Point(27, 45)
$subtitle.Size = New-Object System.Drawing.Size(500, 22)
$subtitle.ForeColor = [System.Drawing.Color]::WhiteSmoke
$header.Controls.Add($subtitle)

# ------------------------------------------------------------
# Helper: create labels
# ------------------------------------------------------------
function New-Label {
    param(
        [string]$Text,
        [int]$X,
        [int]$Y,
        [int]$Width = 160
    )

    $control = New-Object System.Windows.Forms.Label
    $control.Text = $Text
    $control.Location = New-Object System.Drawing.Point($X, $Y)
    $control.Size = New-Object System.Drawing.Size($Width, 25)
    $control.ForeColor = [System.Drawing.Color]::FromArgb(45, 55, 70)
    return $control
}

# Source
$form.Controls.Add((New-Label "Source file:" 28 100 150))

$sourceBox = New-Object System.Windows.Forms.TextBox
$sourceBox.Location = New-Object System.Drawing.Point(28, 128)
$sourceBox.Size = New-Object System.Drawing.Size(560, 29)
$sourceBox.Anchor = "Top, Left, Right"
$sourceBox.BorderStyle = "FixedSingle"
$form.Controls.Add($sourceBox)

$copyButton = New-Object System.Windows.Forms.Button
$copyButton.Text = "Copy..."
$copyButton.Location = New-Object System.Drawing.Point(600, 126)
$copyButton.Size = New-Object System.Drawing.Size(110, 32)
$copyButton.BackColor = [System.Drawing.Color]::FromArgb(225, 235, 247)
$copyButton.FlatStyle = "Flat"
$form.Controls.Add($copyButton)

$sourceHint = New-Object System.Windows.Forms.Label
$sourceHint.Text = "Local file or network path, e.g. \\server\share\file.zip"
$sourceHint.Location = New-Object System.Drawing.Point(29, 159)
$sourceHint.Size = New-Object System.Drawing.Size(650, 22)
$sourceHint.ForeColor = [System.Drawing.Color]::Gray
$sourceHint.Font = New-Object System.Drawing.Font("Segoe UI", 8.5)
$form.Controls.Add($sourceHint)

# Destination
$form.Controls.Add((New-Label "Destination file:" 28 194 180))

$destinationBox = New-Object System.Windows.Forms.TextBox
$destinationBox.Location = New-Object System.Drawing.Point(28, 222)
$destinationBox.Size = New-Object System.Drawing.Size(560, 29)
$destinationBox.Anchor = "Top, Left, Right"
$destinationBox.BorderStyle = "FixedSingle"
$form.Controls.Add($destinationBox)

$destinationButton = New-Object System.Windows.Forms.Button
$destinationButton.Text = "Browse..."
$destinationButton.Location = New-Object System.Drawing.Point(600, 220)
$destinationButton.Size = New-Object System.Drawing.Size(110, 32)
$destinationButton.BackColor = [System.Drawing.Color]::FromArgb(225, 235, 247)
$destinationButton.FlatStyle = "Flat"
$form.Controls.Add($destinationButton)

$destinationHint = New-Object System.Windows.Forms.Label
$destinationHint.Text = "Choose a destination folder; the source filename is retained."
$destinationHint.Location = New-Object System.Drawing.Point(29, 253)
$destinationHint.Size = New-Object System.Drawing.Size(650, 22)
$destinationHint.ForeColor = [System.Drawing.Color]::Gray
$destinationHint.Font = New-Object System.Drawing.Font("Segoe UI", 8.5)
$form.Controls.Add($destinationHint)

# ------------------------------------------------------------
# 3. Status panel
# ------------------------------------------------------------
$statusPanel = New-Object System.Windows.Forms.GroupBox
$statusPanel.Text = "Copy status"
$statusPanel.Location = New-Object System.Drawing.Point(28, 286)
$statusPanel.Size = New-Object System.Drawing.Size(682, 105)
$statusPanel.ForeColor = [System.Drawing.Color]::FromArgb(45, 55, 70)
$form.Controls.Add($statusPanel)

$statusLabel = New-Object System.Windows.Forms.Label
$statusLabel.Text = "Ready - select a source and destination."
$statusLabel.Location = New-Object System.Drawing.Point(15, 25)
$statusLabel.Size = New-Object System.Drawing.Size(640, 24)
$statusPanel.Controls.Add($statusLabel)

$progress = New-Object System.Windows.Forms.ProgressBar
$progress.Location = New-Object System.Drawing.Point(15, 53)
$progress.Size = New-Object System.Drawing.Size(650, 20)
$progress.Minimum = 0
$progress.Maximum = 100
$progress.Style = "Continuous"
$statusPanel.Controls.Add($progress)

$detailLabel = New-Object System.Windows.Forms.Label
$detailLabel.Text = "No operation running."
$detailLabel.Location = New-Object System.Drawing.Point(15, 77)
$detailLabel.Size = New-Object System.Drawing.Size(650, 20)
$detailLabel.ForeColor = [System.Drawing.Color]::Gray
$detailLabel.Font = New-Object System.Drawing.Font("Segoe UI", 8.5)
$statusPanel.Controls.Add($detailLabel)

# Bottom status bar
$statusBar = New-Object System.Windows.Forms.StatusStrip
$statusBar.SizingGrip = $false
$statusBar.BackColor = [System.Drawing.Color]::FromArgb(230, 234, 240)

$statusBarText = New-Object System.Windows.Forms.ToolStripStatusLabel
$statusBarText.Text = "Ready"
[void]$statusBar.Items.Add($statusBarText)
$form.Controls.Add($statusBar)

# ------------------------------------------------------------
# 4. Background copy worker
#    Copies in chunks so the UI remains responsive and Cancel
#    can interrupt an active copy.
# ------------------------------------------------------------
$worker = New-Object System.ComponentModel.BackgroundWorker
$worker.WorkerReportsProgress = $true
$worker.WorkerSupportsCancellation = $true

$worker.add_DoWork({
    param($sender, $e)

    $src = $e.Argument.Source
    $dst = $e.Argument.Destination
    $inputStream = $null
    $outputStream = $null

    try {
        if (-not [System.IO.File]::Exists($src)) {
            throw "Source file does not exist or is inaccessible: $src"
        }

        $destinationDirectory = [System.IO.Path]::GetDirectoryName($dst)

        if ([string]::IsNullOrWhiteSpace($destinationDirectory)) {
            throw "The destination path is invalid."
        }

        if (-not [System.IO.Directory]::Exists($destinationDirectory)) {
            throw "Destination directory does not exist: $destinationDirectory"
        }

        if ([string]::Equals(
            [System.IO.Path]::GetFullPath($src),
            [System.IO.Path]::GetFullPath($dst),
            [System.StringComparison]::OrdinalIgnoreCase
        )) {
            throw "Source and destination refer to the same file."
        }

        $inputStream = [System.IO.File]::Open(
            $src,
            [System.IO.FileMode]::Open,
            [System.IO.FileAccess]::Read,
            [System.IO.FileShare]::Read
        )

        # Create or overwrite the destination without confirmation.
        $outputStream = [System.IO.File]::Open(
            $dst,
            [System.IO.FileMode]::Create,
            [System.IO.FileAccess]::Write,
            [System.IO.FileShare]::None
        )

        $total = $inputStream.Length
        $buffer = New-Object byte[] (1024 * 1024)
        [long]$copied = 0
        $lastPercent = -1

        while (($read = $inputStream.Read($buffer, 0, $buffer.Length)) -gt 0) {
            if ($sender.CancellationPending) {
                $e.Cancel = $true
                break
            }

            $outputStream.Write($buffer, 0, $read)
            $copied += $read

            if ($total -gt 0) {
                $percent = [int](($copied * 100.0) / $total)
            }
            else {
                $percent = 100
            }

            if ($percent -ne $lastPercent) {
                $sender.ReportProgress(
                    $percent,
                    ("Copied {0:N2} MB of {1:N2} MB" -f
                        ($copied / 1MB), ($total / 1MB))
                )
                $lastPercent = $percent
            }
        }

        if ($e.Cancel) {
            $outputStream.SetLength(0)
        }
        else {
            $outputStream.Flush()
            $e.Result = @{
                Source = $src
                Destination = $dst
                Bytes = $copied
            }
        }
    }
    catch {
        $e.Result = $null
        $e.Cancel = $false
        $e.Data = $_.Exception.Message
    }
    finally {
        if ($outputStream) { $outputStream.Dispose() }
        if ($inputStream) { $inputStream.Dispose() }
    }
})

$worker.add_ProgressChanged({
    param($sender, $e)

    $progress.Value = [Math]::Max(
        0, [Math]::Min(100, $e.ProgressPercentage)
    )

    $statusLabel.Text = "Copying... $($e.ProgressPercentage)%"
    $detailLabel.Text = [string]$e.UserState
    $statusBarText.Text = "Copying"
})

# ------------------------------------------------------------
# 5. Common UI helpers
# ------------------------------------------------------------
$setBusy = {
    param([bool]$busy)

    $copyButton.Enabled = -not $busy
    $destinationButton.Enabled = -not $busy
    $sourceBox.Enabled = -not $busy
    $destinationBox.Enabled = -not $busy
    $submitButton.Enabled = -not $busy
    $cancelButton.Enabled = $busy
}

$finishCopy = {
    param($success, $message, $caption)

    & $setBusy $false

    if ($success) {
        $progress.Value = 100
        $statusLabel.Text = "Completed successfully."
        $detailLabel.Text = $message
        $statusBarText.Text = "Completed"
        [System.Windows.Forms.MessageBox]::Show(
            $form, $message, $caption,
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Information
        ) | Out-Null
    }
    else {
        $statusLabel.Text = $caption
        $detailLabel.Text = $message
        $statusBarText.Text = $caption

        [System.Windows.Forms.MessageBox]::Show(
            $form, $message, $caption,
            [System.Windows.Forms.MessageBoxButtons]::OK,
            [System.Windows.Forms.MessageBoxIcon]::Error
        ) | Out-Null
    }
}

# ------------------------------------------------------------
# 6. Select source file (local or network)
# ------------------------------------------------------------
$copyButton.Add_Click({
    $dialog = New-Object System.Windows.Forms.OpenFileDialog
    $dialog.Title = "Select source file"
    $dialog.CheckFileExists = $true
    $dialog.Multiselect = $false
    $dialog.Filter = "All files (*.*)|*.*"

    if ($dialog.ShowDialog($form) -eq [System.Windows.Forms.DialogResult]::OK) {
        $sourceBox.Text = $dialog.FileName

        # Default destination to the same filename in the user's Desktop.
        if ([string]::IsNullOrWhiteSpace($destinationBox.Text)) {
            $destinationBox.Text = Join-Path `
                ([Environment]::GetFolderPath("Desktop")) `
                ([System.IO.Path]::GetFileName($dialog.FileName))
        }

        $statusLabel.Text = "Source file selected."
        $detailLabel.Text = $dialog.FileName
        $statusBarText.Text = "Ready"
    }

    $dialog.Dispose()
})

# ------------------------------------------------------------
# 7. Select destination folder
# ------------------------------------------------------------
$destinationButton.Add_Click({
    $dialog = New-Object System.Windows.Forms.FolderBrowserDialog
    $dialog.Description = "Select destination folder"
    $dialog.ShowNewFolderButton = $true

    if ($dialog.ShowDialog($form) -eq [System.Windows.Forms.DialogResult]::OK) {
        $fileName = [System.IO.Path]::GetFileName($sourceBox.Text)

        if ([string]::IsNullOrWhiteSpace($fileName)) {
            $fileName = "copied-file"
        }

        $destinationBox.Text = Join-Path $dialog.SelectedPath $fileName
        $statusLabel.Text = "Destination selected."
        $detailLabel.Text = $destinationBox.Text
        $statusBarText.Text = "Ready"
    }

    $dialog.Dispose()
})

# ------------------------------------------------------------
# 8. Submit: execute the copy
# ------------------------------------------------------------
$submitButton = New-Object System.Windows.Forms.Button
$submitButton.Text = "Submit"
$submitButton.Location = New-Object System.Drawing.Point(270, 405)
$submitButton.Size = New-Object System.Drawing.Size(115, 35)
$submitButton.BackColor = [System.Drawing.Color]::FromArgb(35, 125, 75)
$submitButton.ForeColor = [System.Drawing.Color]::White
$submitButton.FlatStyle = "Flat"
$submitButton.Font = New-Object System.Drawing.Font(
    "Segoe UI", 10, [System.Drawing.FontStyle]::Bold
)
$form.Controls.Add($submitButton)

$cancelButton = New-Object System.Windows.Forms.Button
$cancelButton.Text = "Cancel"
$cancelButton.Location = New-Object System.Drawing.Point(395, 405)
$cancelButton.Size = New-Object System.Drawing.Size(90, 35)
$cancelButton.Enabled = $false
$cancelButton.FlatStyle = "Flat"
$form.Controls.Add($cancelButton)

$exitButton = New-Object System.Windows.Forms.Button
$exitButton.Text = "Exit"
$exitButton.Location = New-Object System.Drawing.Point(610, 405)
$exitButton.Size = New-Object System.Drawing.Size(100, 35)
$exitButton.BackColor = [System.Drawing.Color]::FromArgb(220, 75, 75)
$exitButton.ForeColor = [System.Drawing.Color]::White
$exitButton.FlatStyle = "Flat"
$form.Controls.Add($exitButton)

$submitButton.Add_Click({
    $src = $sourceBox.Text.Trim()
    $dst = $destinationBox.Text.Trim()

    if ([string]::IsNullOrWhiteSpace($src) -or
        [string]::IsNullOrWhiteSpace($dst)) {
        [System.Windows.Forms.MessageBox]::Show(
            $form,
            "Please select a source file and enter a destination file path.",
            "Missing information",
            "OK",
            "Warning"
        ) | Out-Null
        return
    }

    if ($worker.IsBusy) { return }

    $progress.Value = 0
    $statusLabel.Text = "Starting copy..."
    $detailLabel.Text = "Please wait."
    $statusBarText.Text = "Starting"

    & $setBusy $true

    try {
        $worker.RunWorkerAsync(@{
            Source = $src
            Destination = $dst
        })
    }
    catch {
        & $finishCopy $false $_.Exception.Message "Copy failed"
    }
})

# ------------------------------------------------------------
# 9. Finish / error / cancellation handling
# ------------------------------------------------------------
$worker.add_RunWorkerCompleted({
    param($sender, $e)

    if ($e.Cancelled) {
        & $finishCopy $false "The copy was cancelled." "Copy cancelled"
        return
    }

    if ($e.Error) {
        & $finishCopy $false $e.Error.Exception.Message "Copy failed"
        return
    }

    if ($e.Result -and $e.Result.Destination) {
        $result = $e.Result
        $message = "File copied successfully.`r`n`r`nSource: $($result.Source)`r`nDestination: $($result.Destination)`r`nSize: $([Math]::Round($result.Bytes / 1MB, 2)) MB"
        & $finishCopy $true $message "Copy successful"
    }
    else {
        $errorMessage = "Copy failed."
        if ($e.Error) { $errorMessage = $e.Error.Exception.Message }
        & $finishCopy $false $errorMessage "Copy failed"
    }
})

$cancelButton.Add_Click({
    if ($worker.IsBusy) {
        $worker.CancelAsync()
        $cancelButton.Enabled = $false
        $statusLabel.Text = "Cancelling..."
        $detailLabel.Text = "Waiting for the current file operation to stop."
        $statusBarText.Text = "Cancelling"
    }
})

# Exit while a copy is running is treated as a cancellation request.
$exitButton.Add_Click({
    if ($worker.IsBusy) {
        $worker.CancelAsync()
        $statusLabel.Text = "Cancelling before exit..."
        $statusBarText.Text = "Cancelling"
        $form.Close()
    }
    else {
        $form.Close()
    }
})

$form.Add_FormClosing({
    param($sender, $e)

    if ($worker.IsBusy) {
        $worker.CancelAsync()
        # Keep the window open until the copy worker finishes,
        # preventing disposal while the worker is still using the UI.
        $e.Cancel = $true
        $statusLabel.Text = "Cancelling..."
        $statusBarText.Text = "Cancelling"
    }
})

# Handle errors captured inside the worker.
# The worker stores normal file errors in e.Data.
$worker.add_RunWorkerCompleted({
    param($sender, $e)

    if (-not $e.Cancelled -and -not $e.Error -and
        -not ($e.Result -and $e.Result.Destination)) {
        if ($e.Data) {
            & $finishCopy $false ([string]$e.Data) "Copy failed"
        }
    }
})

# ------------------------------------------------------------
# 10. Show window
# ------------------------------------------------------------
[void]$form.ShowDialog()
$form.Dispose()

```

## How to run it

1. Save the code as `Copy-File.ps1`.
2. Open PowerShell in the folder containing the file.
3. Run:

```
powershell.exe -NoProfile -ExecutionPolicy Bypass -File ".\Copy-File.ps1"
```

4. Approve the Windows administrator prompt if it appears.
5. Click Copy... to select the source file.
6. Click Browse... to select a destination folder, or edit the full destination filename.
7. Click Submit to start the copy.

## Important notes

- Network files: Use a UNC path such as `\\SERVER\Share\document.pdf`. Administrator elevation does not automatically grant network-share permissions.
- Overwrite: An existing destination file is overwritten without a confirmation prompt.
- Errors: Missing files, access-denied errors, and other copy failures are reported.
- Cancellation: Cancel attempts to stop the copy and remove the partial destination file. If you cancel, the partial file is not guaranteed to be recoverable.
- Security: The UAC prompt is a Windows security feature and cannot safely be suppressed by this script.

------

##### Take #2

------

