Add-Type -AssemblyName PresentationFramework, WindowsBase, PresentationCore

# Define JSON storage file paths
$script:jsonFilePath  = Join-Path -Path $PSScriptRoot -ChildPath "paths3.json"
$script:emojiFilePath = Join-Path -Path $PSScriptRoot -ChildPath "emojies.json"

# Ensure paths.json exists
if (-not (Test-Path $jsonFilePath)) {
    @{ } | ConvertTo-Json | Set-Content -Path $jsonFilePath -Encoding UTF8
}

# Create default emojis.json if it doesn't exist
if (-not (Test-Path $script:emojiFilePath)) {
    $defaultEmojis = @(
        @{ Label = "Local";     Emoji = [char]::ConvertFromUtf32(0x1F4BB) }, # 💻
        @{ Label = "Network";   Emoji = [char]::ConvertFromUtf32(0x1F310) }, # 🌐
        @{ Label = "Logs";      Emoji = [char]::ConvertFromUtf32(0x1F4DC) }, # 📜
        @{ Label = "Config";    Emoji = [char]::ConvertFromUtf32(0x1F527) }, # 🔧
        @{ Label = "Setup";     Emoji = [char]::ConvertFromUtf32(0x1F4E6) }, # 📦
        @{ Label = "Services";  Emoji = [char]::ConvertFromUtf32(0x1F50C) }, # 🔌
        @{ Label = "Downloads"; Emoji = [char]::ConvertFromUtf32(0x1F4E5) }, # 📥
        @{ Label = "Music";     Emoji = [char]::ConvertFromUtf32(0x1F3B1) }, # 🎵
        @{ Label = "Documents"; Emoji = [char]::ConvertFromUtf32(0x1F4C4) }, # 📄
        @{ Label = "Videos";    Emoji = [char]::ConvertFromUtf32(0x1F3AC) }, # 🎬
        @{ Label = "Pictures";  Emoji = [char]::ConvertFromUtf32(0x1F5BC) }, # 🖼️
        @{ Label = "Desktop";   Emoji = [char]::ConvertFromUtf32(0x1F5A5) }  # 🖥️
    )
    $defaultEmojis | ConvertTo-Json -Depth 3 | Set-Content -Path $script:emojiFilePath -Encoding UTF8
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
        Title="Path Manager" Height="560" Width="820" WindowStartupLocation="CenterScreen">
    <Grid Margin="10">
        <Grid.RowDefinitions>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="*"/>
            <RowDefinition Height="Auto"/>
        </Grid.RowDefinitions>

        <!-- Form Inputs -->
        <GroupBox Header=" Record Details " Grid.Row="0" Margin="0,0,0,10" Padding="10">
            <Grid>
                <Grid.ColumnDefinitions>
                    <ColumnDefinition Width="90"/>
                    <ColumnDefinition Width="*"/>
                    <ColumnDefinition Width="140"/>
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
                <TextBox Name="txtName" Grid.Row="0" Grid.Column="1" Grid.ColumnSpan="3" VerticalAlignment="Center" Height="25"/>

                <!-- Description Input -->
                <Label Content="Description:" Grid.Row="2" Grid.Column="0"/>
                <TextBox Name="txtDescription" Grid.Row="2" Grid.Column="1" Grid.ColumnSpan="3" VerticalAlignment="Center" Height="25"/>

                <!-- Path & Icon Input -->
                <Label Content="Folder Path:" Grid.Row="4" Grid.Column="0"/>
                <TextBox Name="txtPath" Grid.Row="4" Grid.Column="1" VerticalAlignment="Center" Height="25" Margin="0,0,5,0"/>
                
                <ComboBox Name="cmbType" Grid.Row="4" Grid.Column="2" Height="25" VerticalAlignment="Center" Margin="0,0,5,0" SelectedIndex="0" FontFamily="Segoe UI Emoji" FontSize="13"/>

                <Button Name="btnBrowse" Content="Browse..." Grid.Row="4" Grid.Column="3" Height="25"/>

                
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
        <ListView Name="lvRecords" Grid.Row="2" Margin="0,0,0,8">
            <ListView.View>
                <GridView>
                    <GridViewColumn Header="Type" Width="55">
                        <GridViewColumn.CellTemplate>
                            <DataTemplate>
                                <TextBlock Text="{Binding Icon}" FontFamily="Segoe UI Emoji" FontSize="14" HorizontalAlignment="Center"/>
                            </DataTemplate>
                        </GridViewColumn.CellTemplate>
                    </GridViewColumn>
                    <GridViewColumn Header="Name" Width="165" DisplayMemberBinding="{Binding Name}"/>
                    <GridViewColumn Header="Description" Width="200" DisplayMemberBinding="{Binding Description}"/>
                    <GridViewColumn Header="Folder Path" Width="330" DisplayMemberBinding="{Binding Path}"/>
                </GridView>
            </ListView.View>
        </ListView>

        <!-- Search / Filter Field -->
        <Grid Grid.Row="3">
            <Grid.ColumnDefinitions>
                <ColumnDefinition Width="65"/>
                <ColumnDefinition Width="*"/>
                <ColumnDefinition Width="70"/>
            </Grid.ColumnDefinitions>
            <Label Content="Filter:" Grid.Column="0" VerticalAlignment="Center" FontWeight="Bold"/>
            <TextBox Name="txtFilter" Grid.Column="1" Height="25" VerticalContentAlignment="Center" Margin="0,0,5,0"/>
            <Button Name="btnClearFilter" Content="Clear" Grid.Column="2" Height="25"/>
        </Grid>

        <!-- Notification Overlay Toast -->
        <Border Name="toastBorder" Grid.RowSpan="4" VerticalAlignment="Bottom" HorizontalAlignment="Center" 
                Margin="0,0,0,45" Background="#333333" CornerRadius="5" Padding="15,8" Opacity="0" IsHitTestVisible="False">
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
$cmbType        = $window.FindName("cmbType")
$btnBrowse      = $window.FindName("btnBrowse")
$txtFilter      = $window.FindName("txtFilter")
$btnClearFilter = $window.FindName("btnClearFilter")
$lvRecords      = $window.FindName("lvRecords")
$btnOpen        = $window.FindName("btnOpen")
$btnSave        = $window.FindName("btnSave")
$btnDelete      = $window.FindName("btnDelete")
$btnClear       = $window.FindName("btnClear")
$btnExit        = $window.FindName("btnExit")
$toastBorder    = $window.FindName("toastBorder")

# Load Categories dynamically from emojis.json
function Load-EmojiConfig {
    $cmbType.Items.Clear()
    if (Test-Path $script:emojiFilePath) {
        $jsonContent = Get-Content -Path $script:emojiFilePath -Raw -Encoding UTF8
        if ($jsonContent) {
            $emojiData = $jsonContent | ConvertFrom-Json
            foreach ($item in $emojiData) {
                $cmbType.Items.Add("$($item.Emoji) $($item.Label)") | Out-Null
            }
        }
    }
    if ($cmbType.Items.Count -gt 0) {
        $cmbType.SelectedIndex = 0
    }
}

Load-EmojiConfig

# Master in-memory data store
$script:masterRecords = [System.Collections.Generic.List[PSObject]]::new()

# Collection bound to the ListView
$script:records = [System.Collections.ObjectModel.ObservableCollection[PSObject]]::new()
$lvRecords.ItemsSource = $script:records

# Helper: Filter and Refresh ListView
function Refresh-FilteredRecords {
    $filter = $txtFilter.Text.Trim()
    $script:records.Clear()

    $filtered = $script:masterRecords | Where-Object {
        -not $filter -or 
        ($_.Icon -like "*$filter*") -or
        ($_.Name -like "*$filter*") -or 
        ($_.Description -like "*$filter*") -or 
        ($_.Path -like "*$filter*")
    }

    if ($script:sortAscending) {
        $filtered = $filtered | Sort-Object -Property $script:sortColumn
    } else {
        $filtered = $filtered | Sort-Object -Property $script:sortColumn -Descending
    }

    foreach ($item in $filtered) {
        $script:records.Add($item)
    }
}

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

# Event: Live Search / Filter
$txtFilter.Add_TextChanged({
    Refresh-FilteredRecords
})

# Event: Clear Search Filter
$btnClearFilter.Add_Click({
    $txtFilter.Text = ""
})

# Event: Column Header Click Handler
$window.AddHandler([System.Windows.Controls.GridViewColumnHeader]::ClickEvent, [System.Windows.RoutedEventHandler]{
    param($sender, $e)
    
    $header = $e.OriginalSource -as [System.Windows.Controls.GridViewColumnHeader]
    if ($header -and $header.Column -and $header.Column.Header) {
        $columnTitle = $header.Column.Header.ToString()

        $property = switch ($columnTitle) {
            "Type"        { "Icon" }
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
            Refresh-FilteredRecords
        }
    }
})

# Load JSON Data into Master List
function Load-JsonData {
    $script:masterRecords.Clear()
    if (Test-Path $script:jsonFilePath) {
        $content = Get-Content -Path $script:jsonFilePath -Raw -Encoding UTF8
        if ($content) {
            $data = $content | ConvertFrom-Json
            if ($data) {
                foreach ($item in $data) {
                    $defaultIcon = if ($cmbType.Items.Count -gt 0) { $cmbType.Items[0].ToString().Split(' ')[0] } else { "💻" }
                    $script:masterRecords.Add([PSCustomObject]@{
                        Icon        = if ($item.Icon) { $item.Icon } else { $defaultIcon }
                        Name        = $item.Name
                        Description = if ($item.Description) { $item.Description } else { "" }
                        Path        = $item.Path
                    })
                }
            }
        }
    }
    Refresh-FilteredRecords
}

# Save Master Records back to JSON
function Save-JsonData {
    $array = @()
    foreach ($item in $script:masterRecords) {
        $array += [PSCustomObject]@{
            Icon        = $item.Icon
            Name        = $item.Name
            Description = $item.Description
            Path        = $item.Path
        }
    }
    $array | ConvertTo-Json -Depth 3 | Set-Content -Path $script:jsonFilePath -Encoding UTF8
}

# Helper: Clear Text Fields
function Clear-Fields {
    $txtName.Text = ""
    $txtDescription.Text = ""
    $txtPath.Text = ""
    if ($cmbType.Items.Count -gt 0) { $cmbType.SelectedIndex = 0 }
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
        
        $selectedIcon = $lvRecords.SelectedItem.Icon
        for ($i = 0; $i -lt $cmbType.Items.Count; $i++) {
            if ($cmbType.Items[$i].ToString().StartsWith($selectedIcon)) {
                $cmbType.SelectedIndex = $i
                break
            }
        }
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
    $selectedType = $cmbType.SelectedItem.ToString()
    $icon        = $selectedType.Split(' ')[0]

    if (-not $name -or -not $path) {
        [System.Windows.MessageBox]::Show("Please enter both a Name and a Folder Path.", "Validation Error")
        return
    }

    $existing = $script:masterRecords | Where-Object { $_.Name -eq $name }
    if ($existing) {
        $existing.Icon        = $icon
        $existing.Description = $description
        $existing.Path        = $path
    } else {
        $script:masterRecords.Add([PSCustomObject]@{
            Icon        = $icon
            Name        = $name
            Description = $description
            Path        = $path
        })
    }

    Save-JsonData
    Refresh-FilteredRecords
    Clear-Fields
})

# Event: Delete Record
$btnDelete.Add_Click({
    $selected = $lvRecords.SelectedItem
    if (-not $selected) {
        [System.Windows.MessageBox]::Show("Please select a record from the list to delete.", "Select Record")
        return
    }

    $script:masterRecords.Remove($selected) | Out-Null
    Save-JsonData
    Refresh-FilteredRecords
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

# Event: Window Closing
$window.Add_Closed({
    Show-SplashScreen -line1 "See you soon" -line2 "Jaron..." -displaySeconds 3
})

# Load data on start and display main window
Load-JsonData
$window.ShowDialog() | Out-Null