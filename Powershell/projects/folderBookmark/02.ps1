Add-Type -AssemblyName PresentationFramework, WindowsBase, PresentationCore

# Define JSON storage file path
$script:jsonFilePath = Join-Path -Path $PSScriptRoot -ChildPath "paths.json"
if (-not (Test-Path $jsonFilePath)) {
    @{ } | ConvertTo-Json | Set-Content -Path $jsonFilePath
}

# Track active sort column and direction ($true = Ascending, $false = Descending)
$script:sortColumn = "Name"
$script:sortAscending = $true

# Helper: Show Custom Splash Screen Window
function Show-SplashScreen {
    param(
        [string]$line1,
        [string]$line2,
        [int]$displaySeconds = 3
    )

    [xml]$splashXaml = @"
<Window xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        WindowStyle="None" AllowsTransparency="True" Background="Transparent"
        Width="450" Height="220" WindowStartupLocation="CenterScreen" ShowInTaskbar="False" Topmost="True">
    <Border Background="#1E1E1E" CornerRadius="12" BorderBrush="#007ACC" BorderThickness="2">
        <Border.Effect>
            <DropShadowEffect BlurRadius="20" ShadowDepth="0" Opacity="0.6" Color="#007ACC"/>
        </Border.Effect>
        <Grid VerticalAlignment="Center" HorizontalAlignment="Center">
            <StackPanel HorizontalAlignment="Center">
                <TextBlock Text="$line1" Foreground="#007ACC" FontSize="26" FontWeight="Bold" HorizontalAlignment="Center" Margin="0,0,0,10"/>
                <TextBlock Text="$line2" Foreground="White" FontSize="20" FontWeight="SemiBold" HorizontalAlignment="Center"/>
            </StackPanel>
        </Grid>
    </Border>
</Window>
"@

    $splashReader = (New-Object System.Xml.XmlNodeReader $splashXaml)
    $splashWindow = [Windows.Markup.XamlReader]::Load($splashReader)
    
    # Show non-modal window, process events, pause, then close
    $splashWindow.Show()
    [System.Windows.Threading.Dispatcher]::CurrentDispatcher.Invoke([Action]{}, [System.Windows.Threading.DispatcherPriority]::Background)
    Start-Sleep -Seconds $displaySeconds
    $splashWindow.Close()
}

# 1. SHOW WELCOME SPLASH SCREEN
Show-SplashScreen -line1 "Jaron AnyWhere" -line2 "Path Manager" -displaySeconds 3

# XAML Markup for Main WPF GUI
[xml]$xaml = @"
<Window xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="Path Manager" Height="520" Width="820" WindowStartupLocation="CenterScreen">
    <Grid Margin="10">
        <Grid.RowDefinitions>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="*"/>
        </Grid.RowDefinitions>

        <!-- Form Inputs -->
        <GroupBox Header=" Record Details " Grid.Row="0" Margin="0,0,0,10" Padding="10">
            <Grid>
                <Grid.ColumnDefinitions>
                    <ColumnDefinition Width="90"/>
                    <ColumnDefinition Width="*"/>
                    <ColumnDefinition Width="80"/>
                </Grid.ColumnDefinitions>
                <Grid.RowDefinitions>
                    <RowDefinition Height="Auto"/>
                    <RowDefinition Height="5"/>
                    <RowDefinition Height="Auto"/>
                    <RowDefinition Height="5"/>
                    <RowDefinition Height="Auto"/>
                </Grid.RowDefinitions>

                <!-- Name Input -->
                <Label Content="Name:" Grid.Row="0" Grid.Column="0"/>
                <TextBox Name="txtName" Grid.Row="0" Grid.Column="1" Grid.ColumnSpan="2" VerticalAlignment="Center" Height="25"/>

                <!-- Description Input -->
                <Label Content="Description:" Grid.Row="2" Grid.Column="0"/>
                <TextBox Name="txtDescription" Grid.Row="2" Grid.Column="1" Grid.ColumnSpan="2" VerticalAlignment="Center" Height="25"/>

                <!-- Path Input -->
                <Label Content="Folder Path:" Grid.Row="4" Grid.Column="0"/>
                <TextBox Name="txtPath" Grid.Row="4" Grid.Column="1" VerticalAlignment="Center" Height="25" Margin="0,0,5,0"/>
                <Button Name="btnBrowse" Content="Browse..." Grid.Row="4" Grid.Column="2" Height="25"/>
            </Grid>
        </GroupBox>

        <!-- Action Buttons -->
        <StackPanel Grid.Row="1" Orientation="Horizontal" HorizontalAlignment="Left" Margin="0,0,0,10">
            <Button Name="btnOpen" Content="Open Folder" Width="100" Height="30" Margin="0,0,5,0"/>
            <Button Name="btnSave" Content="Save / Update" Width="100" Height="30" Margin="5,0"/>
            <Button Name="btnDelete" Content="Delete" Width="80" Height="30" Margin="5,0"/>
            <Button Name="btnClear" Content="Clear Inputs" Width="90" Height="30" Margin="5,0"/>
            <Button Name="btnExit" Content="Exit" Width="80" Height="30" Margin="5,0"/>
        </StackPanel>

        <!-- Data ListView -->
        <ListView Name="lvRecords" Grid.Row="2">
            <ListView.View>
                <GridView>
                    <GridViewColumn Header="Name" Width="180" DisplayMemberBinding="{Binding Name}"/>
                    <GridViewColumn Header="Description" Width="220" DisplayMemberBinding="{Binding Description}"/>
                    <GridViewColumn Header="Folder Path" Width="370" DisplayMemberBinding="{Binding Path}"/>
                </GridView>
            </ListView.View>
        </ListView>

        <!-- Notification Overlay Toast -->
        <Border Name="toastBorder" Grid.RowSpan="3" VerticalAlignment="Bottom" HorizontalAlignment="Center" 
                Margin="0,0,0,20" Background="#333333" CornerRadius="5" Padding="15,8" Opacity="0" IsHitTestVisible="False">
            <TextBlock Name="txtToast" Text="Open folder - please wait..." Foreground="White" FontWeight="Bold" FontSize="13"/>
        </Border>
    </Grid>
</Window>
"@

# Read XAML
$reader = (New-Object System.Xml.XmlNodeReader $xaml)
$window = [Windows.Markup.XamlReader]::Load($reader)

# Map UI Controls
$txtName        = $window.FindName("txtName")
$txtDescription = $window.FindName("txtDescription")
$txtPath        = $window.FindName("txtPath")
$btnBrowse      = $window.FindName("btnBrowse")
$lvRecords      = $window.FindName("lvRecords")
$btnOpen        = $window.FindName("btnOpen")
$btnSave        = $window.FindName("btnSave")
$btnDelete      = $window.FindName("btnDelete")
$btnClear       = $window.FindName("btnClear")
$btnExit        = $window.FindName("btnExit")
$toastBorder    = $window.FindName("toastBorder")

# Memory store for records
$script:records = [System.Collections.ObjectModel.ObservableCollection[PSObject]]::new()
$lvRecords.ItemsSource = $script:records

# Helper: Show fading toast notification
function Show-Toast {
    param([string]$message)
    
    $animation = New-Object System.Windows.Media.Animation.DoubleAnimation
    $animation.From = 1.0
    $animation.To = 0.0
    $animation.Duration = [System.TimeSpan]::FromSeconds(2)
    
    $toastBorder.Opacity = 1.0
    $toastBorder.BeginAnimation([System.Windows.UIElement]::OpacityProperty, $animation)
}

# Helper: Sort Collection In-Place
function Sort-Records {
    if ($script:records.Count -le 1) { return }

    $sorted = if ($script:sortAscending) {
        $script:records | Sort-Object -Property $script:sortColumn
    } else {
        $script:records | Sort-Object -Property $script:sortColumn -Descending
    }

    $script:records.Clear()
    foreach ($item in $sorted) {
        $script:records.Add($item)
    }
}

# Event: Column Header Click Handler
$window.AddHandler([System.Windows.Controls.GridViewColumnHeader]::ClickEvent, [System.Windows.RoutedEventHandler]{
    param($sender, $e)
    
    $header = $e.OriginalSource -as [System.Windows.Controls.GridViewColumnHeader]
    if ($header -and $header.Column -and $header.Column.Header) {
        $columnTitle = $header.Column.Header.ToString()

        $property = switch ($columnTitle) {
            "Name"        { "Name" }
            "Description" { "Description" }
            "Folder Path" { "Path" }
            default       { $null }
        }

        if ($property) {
            if ($script:sortColumn -eq $property) {
                $script:sortAscending = -not $script:sortAscending
            } else {
                $script:sortColumn = $property
                $script:sortAscending = $true
            }
            Sort-Records
        }
    }
})

# Load JSON Data
function Load-JsonData {
    $script:records.Clear()
    if (Test-Path $script:jsonFilePath) {
        $content = Get-Content -Path $script:jsonFilePath -Raw
        if ($content) {
            $data = $content | ConvertFrom-Json
            if ($data) {
                foreach ($item in $data) {
                    $script:records.Add([PSCustomObject]@{
                        Name        = $item.Name
                        Description = if ($item.Description) { $item.Description } else { "" }
                        Path        = $item.Path
                    })
                }
            }
        }
    }
    Sort-Records
}

# Save Records back to JSON
function Save-JsonData {
    $array = @()
    foreach ($item in $script:records) {
        $array += [PSCustomObject]@{
            Name        = $item.Name
            Description = $item.Description
            Path        = $item.Path
        }
    }
    $array | ConvertTo-Json -Depth 3 | Set-Content -Path $script:jsonFilePath
}

# Helper: Clear Text Fields
function Clear-Fields {
    $txtName.Text = ""
    $txtDescription.Text = ""
    $txtPath.Text = ""
    $lvRecords.UnselectAll()
}

# Helper: Open Folder Logic
function Open-SelectedFolder {
    $path = $txtPath.Text.Trim()
    if ($path -and (Test-Path $path)) {
        Show-Toast
        Start-Process "explorer.exe" -ArgumentList "`"$path`""
    } else {
        [System.Windows.MessageBox]::Show("The path does not exist or is empty.", "Path Error")
    }
}

# Event: Select Item in ListView to Edit
$lvRecords.Add_SelectionChanged({
    if ($lvRecords.SelectedItem) {
        $txtName.Text        = $lvRecords.SelectedItem.Name
        $txtDescription.Text = $lvRecords.SelectedItem.Description
        $txtPath.Text        = $lvRecords.SelectedItem.Path
    }
})

# Event: Double Click Row in ListView to Open Folder
$lvRecords.Add_MouseDoubleClick({
    if ($lvRecords.SelectedItem) {
        Open-SelectedFolder
    }
})

# Event: Browse Folder Button
$btnBrowse.Add_Click({
    Add-Type -AssemblyName System.Windows.Forms
    $dialog = New-Object System.Windows.Forms.FolderBrowserDialog
    if ($dialog.ShowDialog() -eq [System.Windows.Forms.DialogResult]::OK) {
        $txtPath.Text = $dialog.SelectedPath
    }
})

# Event: Save / Edit Record
$btnSave.Add_Click({
    $name        = $txtName.Text.Trim()
    $description = $txtDescription.Text.Trim()
    $path        = $txtPath.Text.Trim()

    if (-not $name -or -not $path) {
        [System.Windows.MessageBox]::Show("Please enter both a Name and a Folder Path.", "Validation Error")
        return
    }

    # Check if entry already exists (Edit Mode)
    $existing = $script:records | Where-Object { $_.Name -eq $name }
    if ($existing) {
        $existing.Description = $description
        $existing.Path        = $path
        $lvRecords.Items.Refresh()
    } else {
        # New Entry
        $script:records.Add([PSCustomObject]@{
            Name        = $name
            Description = $description
            Path        = $path
        })
    }

    Sort-Records
    Save-JsonData
    Clear-Fields
})

# Event: Delete Record
$btnDelete.Add_Click({
    $selected = $lvRecords.SelectedItem
    if (-not $selected) {
        [System.Windows.MessageBox]::Show("Please select a record from the list to delete.", "Select Record")
        return
    }

    $script:records.Remove($selected) | Out-Null
    Save-JsonData
    Clear-Fields
})

# Event: Open Folder Button
$btnOpen.Add_Click({
    Open-SelectedFolder
})

# Event: Clear Input Fields
$btnClear.Add_Click({ Clear-Fields })

# Event: Exit Button
$btnExit.Add_Click({
    $window.Close()
})

# Event: Window Closing (Triggers on Exit button or 'X' button)
$window.Add_Closed({
    Show-SplashScreen -line1 "See you soon" -line2 "Jaron..." -displaySeconds 3
})

# Load data on start and display main window
Load-JsonData
$window.ShowDialog() | Out-Null
