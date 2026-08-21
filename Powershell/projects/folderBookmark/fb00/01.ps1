Add-Type -AssemblyName PresentationFramework, WindowsBase, PresentationCore

# Define JSON storage file path
$script:jsonFilePath = Join-Path -Path $PSScriptRoot -ChildPath "paths.json"
if (-not (Test-Path $jsonFilePath)) {
    @{ } | ConvertTo-Json | Set-Content -Path $jsonFilePath
}

# XAML Markup for WPF GUI
[xml]$xaml = @"
<Window xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="Path Manager" Height="450" Width="650" WindowStartupLocation="CenterScreen">
    <Grid Margin="10">
        <Grid.RowDefinitions>
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
                    <ColumnDefinition Width="80"/>
                </Grid.ColumnDefinitions>
                <Grid.RowDefinitions>
                    <RowDefinition Height="Auto"/>
                    <RowDefinition Height="5"/>
                    <RowDefinition Height="Auto"/>
                </Grid.RowDefinitions>

                <Label Content="Name:" Grid.Row="0" Grid.Column="0"/>
                <TextBox Name="txtName" Grid.Row="0" Grid.Column="1" Grid.ColumnSpan="2" VerticalAlignment="Center" Height="25"/>

                <Label Content="Folder Path:" Grid.Row="2" Grid.Column="0"/>
                <TextBox Name="txtPath" Grid.Row="2" Grid.Column="1" VerticalAlignment="Center" Height="25" Margin="0,0,5,0"/>
                <Button Name="btnBrowse" Content="Browse..." Grid.Row="2" Grid.Column="2" Height="25"/>
            </Grid>
        </GroupBox>

        <!-- Data ListView -->
        <ListView Name="lvRecords" Grid.Row="1" Margin="0,0,0,10">
            <ListView.View>
                <GridView>
                    <GridViewColumn Header="Name" Width="200" DisplayMemberBinding="{Binding Name}"/>
                    <GridViewColumn Header="Folder Path" Width="380" DisplayMemberBinding="{Binding Path}"/>
                </GridView>
            </ListView.View>
        </ListView>

        <!-- Action Buttons -->
        <StackPanel Grid.Row="2" Orientation="Horizontal" HorizontalAlignment="Right">
            <Button Name="btnOpen" Content="Open Folder" Width="100" Height="30" Margin="5,0"/>
            <Button Name="btnSave" Content="Save / Update" Width="100" Height="30" Margin="5,0"/>
            <Button Name="btnDelete" Content="Delete" Width="80" Height="30" Margin="5,0"/>
            <Button Name="btnClear" Content="Clear Inputs" Width="90" Height="30" Margin="5,0"/>
        </StackPanel>
    </Grid>
</Window>
"@

# Read XAML
$reader = (New-Object System.Xml.XmlNodeReader $xaml)
$window = [Windows.Markup.XamlReader]::Load($reader)

# Map UI Controls
$txtName   = $window.FindName("txtName")
$txtPath   = $window.FindName("txtPath")
$btnBrowse = $window.FindName("btnBrowse")
$lvRecords = $window.FindName("lvRecords")
$btnOpen   = $window.FindName("btnOpen")
$btnSave   = $window.FindName("btnSave")
$btnDelete = $window.FindName("btnDelete")
$btnClear  = $window.FindName("btnClear")

# Memory store for records (Fixed dot notation for generic class)
$script:records = [System.Collections.ObjectModel.ObservableCollection[PSObject]]::new()
$lvRecords.ItemsSource = $script:records

# Load JSON Data
function Load-JsonData {
    $script:records.Clear()
    if (Test-Path $script:jsonFilePath) {
        $content = Get-Content -Path $script:jsonFilePath -Raw
        if ($content) {
            $data = $content | ConvertFrom-Json
            if ($data -is [PSCustomObject]) {
                $data.psobject.Properties | ForEach-Object {
                    $script:records.Add([PSCustomObject]@{ Name = $_.Name; Path = $_.Value })
                }
            }
        }
    }
}

# Save Records back to JSON
function Save-JsonData {
    $hashtable = [ordered]@{}
    foreach ($item in $script:records) {
        $hashtable[$item.Name] = $item.Path
    }
    $hashtable | ConvertTo-Json -Depth 2 | Set-Content -Path $script:jsonFilePath
}

# Helper: Clear Text Fields
function Clear-Fields {
    $txtName.Text = ""
    $txtPath.Text = ""
    $lvRecords.UnselectAll()
}

# Event: Select Item in ListView to Edit
$lvRecords.Add_SelectionChanged({
    if ($lvRecords.SelectedItem) {
        $txtName.Text = $lvRecords.SelectedItem.Name
        $txtPath.Text = $lvRecords.SelectedItem.Path
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
    $name = $txtName.Text.Trim()
    $path = $txtPath.Text.Trim()

    if (-not $name -or -not $path) {
        [System.Windows.MessageBox]::Show("Please enter both a Name and a Folder Path.", "Validation Error")
        return
    }

    # Check if entry already exists (Edit Mode)
    $existing = $script:records | Where-Object { $_.Name -eq $name }
    if ($existing) {
        $existing.Path = $path
        $lvRecords.Items.Refresh()
    } else {
        # New Entry
        $script:records.Add([PSCustomObject]@{ Name = $name; Path = $path })
    }

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
    $path = $txtPath.Text.Trim()
    if ($path -and (Test-Path $path)) {
        Start-Process "explorer.exe" -ArgumentList "`"$path`""
    } else {
        [System.Windows.MessageBox]::Show("The path does not exist or is empty.", "Path Error")
    }
})

# Event: Clear Input Fields
$btnClear.Add_Click({ Clear-Fields })

# Load data on start and display window
Load-JsonData
$window.ShowDialog() | Out-Null
