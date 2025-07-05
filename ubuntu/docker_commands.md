<a name="topage"></a>

# docker_commands.md


### 🧰 **General Docker Basics**
* `docker version` – Show Docker version
* `docker info` – System-wide info
* `docker help` – List all commands
* `docker system df` – Disk usage
* `docker system prune` – Remove unused data
* `docker events` – Real-time event stream
* `docker stats` – Live container resource usage

---

### 📦 **Image Management**
* `docker pull <image>` – Download image
* `docker build -t <name> .` – Build from Dockerfile
* `docker images` – List images
* `docker rmi <image>` – Remove image
* `docker tag <src> <target>` – Tag image
* `docker save <image>` – Save to tar
* `docker load <file>` – Load from tar
* `docker inspect <image>` – View metadata
* `docker history <image>` – Show image layers
* `docker image prune` – Clean unused images

---

### 🧪 **Container Lifecycle**
* `docker run <image>` – Start container
* `docker run -d <image>` – Detached mode
* `docker run -it <image>` – Interactive shell
* `docker ps` – List running containers
* `docker ps -a` – All containers
* `docker stop <container>` – Stop container
* `docker start <container>` – Start container
* `docker restart <container>` – Restart container
* `docker kill <container>` – Force stop
* `docker rm <container>` – Remove container

---

### 🧠 **Container Interaction**
* `docker exec -it <container> bash` – Shell access
* `docker attach <container>` – Attach to running container
* `docker logs <container>` – View logs
* `docker top <container>` – Show processes
* `docker inspect <container>` – Detailed info
* `docker cp <src> <dest>` – Copy files
* `docker rename <old> <new>` – Rename container
* `docker update <container>` – Update config
* `docker wait <container>` – Wait for stop
* `docker pause/unpause <container>` – Suspend/resume

---

### 🧱 **Volumes**
* `docker volume create <name>` – Create volume
* `docker volume ls` – List volumes
* `docker volume inspect <name>` – Volume info
* `docker volume rm <name>` – Remove volume
* `docker volume prune` – Clean unused volumes
* `docker run -v <volume>:<path> <image>` – Mount volume
* `docker run --mount src=<vol>,dst=<path> <image>` – Mount with options

---

### 🌐 **Networking**
* `docker network ls` – List networks
* `docker network create <name>` – Create network
* `docker network inspect <name>` – Network info
* `docker network rm <name>` – Remove network
* `docker network connect <net> <container>` – Attach container
* `docker network disconnect <net> <container>` – Detach container
* `docker run --network <net> <image>` – Use network
* `docker run -p <host>:<container> <image>` – Port mapping
* `docker run --link <container>:alias <image>` – Link containers

---

### 🛠️ **Docker Compose**
* `docker-compose up` – Start services
* `docker-compose down` – Stop services
* `docker-compose build` – Build images
* `docker-compose logs` – View logs
* `docker-compose ps` – List containers
* `docker-compose exec <service> bash` – Shell access
* `docker-compose restart` – Restart services
* `docker-compose pull` – Pull images
* `docker-compose stop/start` – Control services
* `docker-compose config` – Validate config

---

### 🧭 **Advanced & Swarm**
* `docker swarm init` – Start swarm
* `docker swarm join` – Join swarm
* `docker node ls` – List nodes
* `docker service create` – Create service
* `docker service ls` – List services
* `docker service ps` – Service tasks
* `docker service update` – Update service
* `docker stack deploy` – Deploy stack
* `docker stack rm` – Remove stack
* `docker stack services` – Stack services

---

### 🧪 **Builder & Context**
* `docker buildx ls` – List builders
* `docker buildx create` – Create builder
* `docker buildx build` – Build with buildx
* `docker context ls` – List contexts
* `docker context create` – Create context
* `docker context use <name>` – Switch context
* `docker manifest inspect <image>` – View manifest
* `docker manifest push <image>` – Push manifest
* `docker checkpoint create` – Save container state
* `docker checkpoint rm` – Remove checkpoint

---


##### online cheat sheet
* [Docker CLI cheat sheet from Docker Docs](https://docs.docker.com/get-started/docker_cheatsheet.pdf)
* [100 Docker commands guide from FOSS TechNix](https://www.fosstechnix.com/docker-basic-commands/)

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
