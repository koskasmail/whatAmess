
#### status service

```
Write-Host "# ================================"
Write-Host "#   SERVICE STATUS BANNER"
Write-Host "# ================================"
Write-Host "# "


# Detailed status for services containing "IBMWAS90Service"
Get-Service | Where-Object { $_.Name -like "*IBMWAS90Service*" } | ForEach-Object {
    Write-Host "Service: $($_.Name)"
    Write-Host "Display Name: $($_.DisplayName)"
    Write-Host "Status: $($_.Status)"
    Write-Host "-----------------------------"
}

# Pause at the end
Read-Host "Press ENTER to exit"
```

-----

#### stop service

```
Write-Host "# ================================"
Write-Host "#   STOP SERVICES "
Write-Host "# ================================"
Write-Host ""

###---[services stop]----###
Get-Service | Where-Object { $_.Name -like "*IBMWAS90Service*" } | ForEach-Object {
    Write-Host "Stopping service: $($_.Name)"
    Stop-Service -Name $_.Name -Force
}

Write-Host "# ================================"
Write-Host "#   SERVICE STATUS BANNER"
Write-Host "# ================================"
Write-Host ""

###---[service status]----###
Get-Service | Where-Object { $_.Name -like "*IBMWAS90Service*" } | ForEach-Object {
    Write-Host "Service: $($_.Name)"
    Write-Host "Display Name: $($_.DisplayName)"
    Write-Host "Status: $($_.Status)"
    Write-Host "-----------------------------"
}

# Pause at the end
Read-Host "Press ENTER to exit"
```

-----

#### start service

```
Write-Host "# ================================"
Write-Host "#   START SERVICES "
Write-Host "# ================================"
Write-Host ""

###---[services start]----###
Get-Service | Where-Object { $_.Name -like "*IBMWAS90Service*" } | ForEach-Object {
    Write-Host "Starting service: $($_.Name)"
    Start-Service -Name $_.Name
}

Write-Host "# ================================"
Write-Host "#   SERVICE STATUS BANNER"
Write-Host "# ================================"
Write-Host ""

###---[service status]----###
Get-Service | Where-Object { $_.Name -like "*IBMWAS90Service*" } | ForEach-Object {
    Write-Host "Service: $($_.Name)"
    Write-Host "Display Name: $($_.DisplayName)"
    Write-Host "Status: $($_.Status)"
    Write-Host "-----------------------------"
}

# Pause at the end
Read-Host "Press ENTER to exit"
```








