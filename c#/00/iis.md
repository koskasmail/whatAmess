
# iis

Running IIS inside a Docker container **is absolutely possible**, as long as you use **Windows containers** (IIS cannot run on Linux containers). You can pull Microsoft’s official IIS images, customize them with your site, and run them like any other container.

---

## 🧩 What IIS-in-Docker Actually Means
IIS runs on top of Windows Server, so Microsoft provides **Windows Server Core + IIS** base images. These images let you:

- Host ASP.NET (classic), ASP.NET MVC, WebForms, or static sites  
- Package legacy apps without rewriting  
- Standardize deployments across environments  
- Run multiple isolated IIS instances on one machine  

Sources:   [oneuptime.com](https://oneuptime.com/blog/post/2026-02-08-how-to-run-iis-in-a-windows-docker-container/view)  [Docker Hub](https://hub.docker.com/r/microsoft/windows-servercore-iis)

---

## 🚀 Quick Manual: How to Run IIS in Docker

### 1. Enable Windows Containers
Docker Desktop must be switched to **Windows Containers mode**.  
Source:   [deepwiki.com](https://deepwiki.com/microsoft/iis-docker/2-getting-started)

### 2. Pull an IIS Base Image
Choose the Windows Server version that matches your host OS:

```powershell
docker pull mcr.microsoft.com/windows/servercore/iis:windowsservercore-ltsc2022
```

Other supported tags include 2016, 2019, 2025.  
Source:   [Docker Hub](https://hub.docker.com/r/microsoft/windows-servercore-iis)

### 3. Run a Basic IIS Container
```powershell
docker run -d -p 8080:80 --name myiis mcr.microsoft.com/windows/servercore/iis
```

Open:  
`http://localhost:8080`  
You should see the default IIS welcome page.

### 4. Add Your Website (Simple Example)
Create a `Dockerfile`:

```dockerfile
FROM mcr.microsoft.com/windows/servercore/iis
RUN powershell -NoProfile -Command \
    Remove-Item -Recurse C:\inetpub\wwwroot\* ; \
    New-Item -Path C:\inetpub\wwwroot\index.html -ItemType File -Value "<h1>Hello from IIS in Docker!</h1>"
```

Build and run:

```powershell
docker build -t mysite .
docker run -d -p 8080:80 mysite
```

### 5. Deploying ASP.NET Apps
Copy your published app into the container:

```dockerfile
COPY ./publish/ /inetpub/wwwroot/
```

### 6. Remote Management & Troubleshooting
Microsoft provides guidance on remote IIS management inside containers, including tips for iterative debugging.  
Source:   [Microsoft Community](https://techcommunity.microsoft.com/blog/itopstalkblog/5-tips-for-iis-on-containers-bonus-%E2%80%93-iis-remote-management/3692044)

---

## 📚 Recommended Documentation & Resources

### Official Microsoft Repos
- **microsoft/iis-docker GitHub repo** — Dockerfiles, samples, and best practices  
    [Github](https://github.com/microsoft/iis-docker)

### Tutorials & Guides
- **How to Run IIS in a Windows Docker Container** — step-by-step deployment guide  
    [oneuptime.com](https://oneuptime.com/blog/post/2026-02-08-how-to-run-iis-in-a-windows-docker-container/view)
- **Getting Started with IIS Docker Images** — prerequisites, networking, verification  
    [deepwiki.com](https://deepwiki.com/microsoft/iis-docker/2-getting-started)

### Base Images
- **Windows Server Core IIS images** on Microsoft Container Registry  
    [Docker Hub](https://hub.docker.com/r/microsoft/windows-servercore-iis)

### more links
* [windows-servercore-iis?utm_source=copilot](https://hub.docker.com/r/microsoft/windows-servercore-iis?utm_source=copilot.com)
* [utm_source=copilot](https://oneuptime.com/blog/post/2026-02-08-how-to-run-iis-in-a-windows-docker-container/view?utm_source=copilot.com)
* [docker-windows-install](https://docs.docker.com/desktop/setup/install/windows-install/)

---
