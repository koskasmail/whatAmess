

#### delete_temp_files.md

```
$Temps = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "CN|CPE"
$Folder1 = $Temps.FullName + "\Config\Temp"
$Folder2 = $Temps.FullName + "\Temp"
$Folder3 = $Temps.FullName + "\WSTemp"

gci $Folder1 | Remove-Item -Recurse -Force
gci $Folder2 | Remove-Item -Recurse -Force
gci $Folder3 | Remove-Item -Recurse -Force
```

-----

#### clear temp files & logs

```
$Node = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "CN|CPE"
$NodeLogs = $Node.FullName + "\Logs\"
$Node1 = $Node.FullName + "\Config\Temp"
$Node2 = $Node.FullName + "\Temp"
$Node3 = $Node.FullName + "\WSTemp"
$Node4 = (gci $NodeLogs -Attributes Directory | where name -Match "CN|CPE").FullName

gci $Node1 | Remove-Item -Recurse -Force
gci $Node2 | Remove-Item -Recurse -Force
gci $Node3 | Remove-Item -Recurse -Force
gci $Node4 | Remove-Item -Recurse -Force

$MGR = gci C:\IBM\WebSphere\AppServer\profiles | where name -Match "MGR"
If ($MGR){
	$MGRLogs = $MGR.FullName + "\Logs\"
	$MGR1 = $MGR.FullName + "\Config\Temp"
	$MGR2 = $MGR.FullName + "\Temp"
	$MGR3 = $MGR.FullName + "\WSTemp"
	$MGR4 = (gci $MGRLogs -Attributes Directory | where name -Match "MGR").FullName

	gci $MGR1 | Remove-Item -Recurse -Force
	gci $MGR2 | Remove-Item -Recurse -Force
	gci $MGR3 | Remove-Item -Recurse -Force
	gci $MGR4 | Remove-Item -Recurse -Force
}
```

----
