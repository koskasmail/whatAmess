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
