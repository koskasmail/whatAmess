Add-Type -AssemblyName PresentationFramework, WindowsBase, PresentationCore

# Define JSON storage file path
$script:emojiFilePath = Join-Path -Path $PSScriptRoot -ChildPath "emojis.json"

# In-memory collections
$script:masterEmojis = [System.Collections.Generic.List[PSObject]]::new()
$script:records = [System.Collections.ObjectModel.ObservableCollection[PSObject]]::new()

# XAML Markup for GUI
[xml]$xaml = @"
<Window xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="Emoji List Manager" Height="480" Width="560" WindowStartupLocation="CenterScreen">
    <Grid Margin="10">
        <Grid.RowDefinitions>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="*"/>
        </Grid.RowDefinitions>

        <!-- Form Inputs -->
        <GroupBox Header=" Emoji Entry Details " Grid.Row="0" Margin="0,0,0,10" Padding="10">
            <Grid>
                <Grid.ColumnDefinitions>
                    <ColumnDefinition Width="60"/>
                    <ColumnDefinition Width="*"/>
                    <ColumnDefinition Width="60"/>
                    <ColumnDefinition Width="80"/>
                </Grid.ColumnDefinitions>

                <Label Content="Label:" Grid.Column="0" VerticalAlignment="Center"/>
                <TextBox Name="txtLabel" Grid.Column="1" Height="25" VerticalAlignment="Center" Margin="0,0,10,0"/>

                <Label Content="Emoji:" Grid.Column="2" VerticalAlignment="Center"/>
                <TextBox Name="txtEmoji" Grid.Column="3" Height="25" VerticalAlignment="Center" FontFamily="Segoe UI Emoji" FontSize="14" HorizontalContentAlignment="Center"/>
            </Grid>
        </GroupBox>

        <!-- Action Buttons -->
        <StackPanel Grid.Row="1" Orientation="Horizontal" HorizontalAlignment="Left" Margin="0,0,0,10">
            <Button Name="btnSave" Content="Save / Add" Width="90" Height="30" Margin="0,0,5,0"/>
            <Button Name="btnDelete" Content="Delete" Width="75" Height="30" Margin="5,0"/>
            <Button Name="btnClear" Content="Clear Inputs" Width="85" Height="30" Margin="5,0"/>
            <Button Name="btnLoad" Content="Load File" Width="75" Height="30" Margin="5,0"/>
            <Button Name="btnExit" Content="Exit" Width="75" Height="30" Margin="5,0"/>
        </StackPanel>

        <!-- Data ListView -->
        <ListView Name="lvEmojis" Grid.Row="2">
            <ListView.View>
                <GridView>
                    <GridViewColumn Header="Emoji" Width="80">
                        <GridViewColumn.CellTemplate>
                            <DataTemplate>
                                <TextBlock Text="{Binding Emoji}" FontFamily="Segoe UI Emoji" FontSize="16" HorizontalAlignment="Center"/>
                            </DataTemplate>
                        </GridViewColumn.CellTemplate>
                    </GridViewColumn>
                    <GridViewColumn Header="Label" Width="410" DisplayMemberBinding="{Binding Label}"/>
                </GridView>
            </ListView.View>
        </ListView>
    </Grid>
</Window>
"@

# Read XAML
$reader = (New-Object System.Xml.XmlNodeReader $xaml)
$window = [Windows.Markup.XamlReader]::Load($reader)

# Map UI Controls
$txtLabel = $window.FindName("txtLabel")
$txtEmoji = $window.FindName("txtEmoji")
$btnSave  = $window.FindName("btnSave")
$btnDelete= $window.FindName("btnDelete")
$btnClear = $window.FindName("btnClear")
$btnLoad  = $window.FindName("btnLoad")
$btnExit  = $window.FindName("btnExit")
$lvEmojis = $window.FindName("lvEmojis")

# Bind ListView
$lvEmojis.ItemsSource = $script:records

# Helper: Refresh ListView UI
function Refresh-ListView {
    $script:records.Clear()
    foreach ($item in $script:masterEmojis) {
        $script:records.Add($item)
    }
}

# Load Emojis from JSON
function Load-EmojiData {
    $script:masterEmojis.Clear()
    if (Test-Path $script:emojiFilePath) {
        $content = Get-Content -Path $script:emojiFilePath -Raw -Encoding UTF8
        if ($content) {
            $data = $content | ConvertFrom-Json
            if ($data) {
                foreach ($item in $data) {
                    $script:masterEmojis.Add([PSCustomObject]@{
                        Label = $item.Label
                        Emoji = $item.Emoji
                    })
                }
            }
        }
    }
    Refresh-ListView
}

# Save Master Emojis to JSON
function Save-EmojiData {
    $array = @()
    foreach ($item in $script:masterEmojis) {
        $array += [PSCustomObject]@{
            Label = $item.Label
            Emoji = $item.Emoji
        }
    }
    $array | ConvertTo-Json -Depth 3 | Set-Content -Path $script:emojiFilePath -Encoding UTF8
}

# Helper: Clear Input Fields
function Clear-Fields {
    $txtLabel.Text = ""
    $txtEmoji.Text = ""
    $lvEmojis.UnselectAll()
}

# Event: Select Row to Edit
$lvEmojis.Add_SelectionChanged({
    if ($lvEmojis.SelectedItem) {
        $txtLabel.Text = $lvEmojis.SelectedItem.Label
        $txtEmoji.Text = $lvEmojis.SelectedItem.Emoji
    }
})

# Event: Save / Add / Update Entry
$btnSave.Add_Click({
    $label = $txtLabel.Text.Trim()
    $emoji = $txtEmoji.Text.Trim()

    if (-not $label -or -not $emoji) {
        [System.Windows.MessageBox]::Show("Please enter both a Label and an Emoji.", "Validation Error")
        return
    }

    # Edit Mode: Update existing entry if Label matches
    $existing = $script:masterEmojis | Where-Object { $_.Label -eq $label }
    if ($existing) {
        $existing.Emoji = $emoji
    } else {
        # Add Mode
        $script:masterEmojis.Add([PSCustomObject]@{
            Label = $label
            Emoji = $emoji
        })
    }

    Save-EmojiData
    Refresh-ListView
    Clear-Fields
    [System.Windows.MessageBox]::Show("Emoji configuration saved successfully!", "Success")
})

# Event: Delete Selected Entry
$btnDelete.Add_Click({
    $selected = $lvEmojis.SelectedItem
    if (-not $selected) {
        [System.Windows.MessageBox]::Show("Please select an item from the list to delete.", "Select Item")
        return
    }

    $script:masterEmojis.Remove($selected) | Out-Null
    Save-EmojiData
    Refresh-ListView
    Clear-Fields
})

# Event: Clear Input Fields
$btnClear.Add_Click({ Clear-Fields })

# Event: Load Button (Reload from JSON file)
$btnLoad.Add_Click({
    Load-EmojiData
    Clear-Fields
    [System.Windows.MessageBox]::Show("Emojis reloaded from JSON file.", "Loaded")
})

# Event: Exit Button
$btnExit.Add_Click({
    $window.Close()
})

# Initialize and Show Window
Load-EmojiData
$window.ShowDialog() | Out-Null