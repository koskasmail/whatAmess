<a name="topage"></a>

# 02_Installing_Microsoft_SQL_Server

## SQL Server installation
* few words



# step 1: Import the public repository GPG keys:

#### Import the public repository GPG keys:
```
curl https://packages.microsoft.com/keys/microsoft.asc | sudo tee /etc/apt/trusted.gpg.d/microsoft.asc
```

#### Register the SQL Server Ubuntu repository:
```
sudo add-apt-repository "$(wget -qO- https://packages.microsoft.com/config/ubuntu/20.04/mssql-server-2022.list)"
```

#### Run the following commands to install SQL Server:
```
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install -y mssql-server
```



----
<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
