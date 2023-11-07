<a name="topage"></a>

# xls2xlsx

### [ Notes ]
  * You can use PowerShell to convert an XLS (Excel 97-2003 Workbook) to XLSX (Excel Workbook) and optimize cell sizes in the converted Excel file.
  * To do this, you'll need to use the Excel Interop libraries, which require the installation of the Excel application on the machine where you're running the script.
  * Here's a PowerShell script that demonstrates how to perform this task:

#### powershell

```sh
# Load the Excel Interop assembly
Add-Type -TypeDefinition @"
    using System;
    using System.IO;
    using System.Reflection;
    using Microsoft.Office.Interop.Excel;
    using Microsoft.Office.Interop.Excel.Constants;
"@

# Define the input XLS file and output XLSX file paths
$inputXLSPath = "C:\Path\to\input.xls"
$outputXLSXPath = "C:\Path\to\output.xlsx"

# Create a new Excel application
$excel = New-Object -ComObject Excel.Application

# Open the XLS file
$workbook = $excel.Workbooks.Open($inputXLSPath)

# Save the workbook as XLSX
$workbook.SaveAs($outputXLSXPath, [XlFileFormat]::xlOpenXMLWorkbook)

# Close and quit Excel
$workbook.Close()
$excel.Quit()
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($excel)

# Reopen the XLSX file using the Excel Interop
$excel = New-Object -ComObject Excel.Application
$workbook = $excel.Workbooks.Open($outputXLSXPath)

# Loop through all sheets in the workbook and optimize cell sizes
foreach ($sheet in $workbook.Sheets) {
    $worksheet = $sheet.UsedRange
    $worksheet.EntireColumn.AutoFit() | Out-Null
    $worksheet.EntireRow.AutoFit() | Out-Null
}

# Save and close the optimized XLSX file
$workbook.Save()
$workbook.Close()
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($workbook)
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($excel)

# Clean up Excel processes
Stop-Process -Name Excel -Force

Write-Host "Conversion and optimization complete."

# Display a message to indicate that the process is finished
Write-Host "Conversion and optimization complete."

```

#### output
```sh
xxxx
```

#### example #2

```sh
xxxx
```

#### output
```sh
xxxx
```

#### Arrow function - simple

```sh
xxxx
```

#### output
```sh
xxxx
```

### [ Task ]
  1. xxxx


### [ before ]

```sh
xxxx
```

### [ solution ]

```sh
xxxx
```

### [ output result ]

```sh
xxxx
```

-----

### [ Thoughts ]

  1. xxxx
  

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
