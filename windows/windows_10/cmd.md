
# Windows 10

## 


#### check GPU

```
C:\Users\yaron>wmic path win32_VideoController get name
```

#### output

```
Name
NVIDIA GeForce 920MX
Intel(R) HD Graphics 620
```

-----

#### memory status

##### memory: Total + available

```
systeminfo | findstr /C:"Total Physical Memory" /C:"Available Physical Memory"
```

##### memory: Total + available

```
wmic OS get TotalVisibleMemorySize,FreePhysicalMemory /Value
```

##### memory: Total + available

```
powershell "Get-CimInstance Win32_OperatingSystem | Select-Object TotalVisibleMemorySize,FreePhysicalMemory"
```

##### System Information 

```
systeminfo
```

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
