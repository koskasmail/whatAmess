<a name="topage"></a>

# basic.md

### 1: unity

```
### install unity
sudo apt-get update -y; sudo apt-get upgrade -y
sudo sh -c 'echo "deb https://hub.unity3d.com/linux/repos/deb stable main" > /etc/apt/sources.list.d/unityhub.list'
wget -qO - https://hub.unity3d.com/linux/keys/public | sudo tee /etc/apt/trusted.gpg.d/myrepo.asc
sudo apt-get update -y; sudo apt-get upgrade -y
http://security.ubuntu.com/ubuntu/pool/main/o/openssl/libssl1.1_1.1.1l-1ubuntu1.6_amd64.deb && sudo dpkg -i libssl1.1_1.1.1l-1ubuntu1.6_amd64.deb
sudo apt install unityhub=3.1.0
sudo apt upgrade unityhub
```


----

### 2: Docker

```
# Add Docker's official GPG key:
sudo apt-get update -y ; apt-get upgrade -y
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
 "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
 $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
 sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

```

----

### 3: brave-browser

```
sudo apt-get update -y ; apt-get upgrade -y

sudo apt install curl -y

sudo curl -fsSLo /usr/share/keyrings/brave-browser-archive-keyring.gpg https://brave-browser-apt-release.s3.brave.com/brave-browser-archive-keyring.gpg

echo "deb [signed-by=/usr/share/keyrings/brave-browser-archive-keyring.gpg] https://brave-browser-apt-release.s3.brave.com/ stable main"|sudo tee /etc/apt/sources.list.d/brave-browser-release.list

sudo apt update -y

sudo apt install brave-browser -y
```

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
