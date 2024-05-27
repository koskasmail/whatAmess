<a name="topage"></a>

# docker_container

# A
# B
# C
# D
   * dictionary etc.
# E
# F
# G
# H
## help (containers)
   * docker --help

 ```
 sudo docker --help
 ```
  
   * output
      
```
Usage:  docker [OPTIONS] COMMAND

A self-sufficient runtime for containers

Common Commands:
  run         Create and run a new container from an image
  exec        Execute a command in a running container
  ps          List containers
  ...
  https://docs.docker.com/go/guides/
  ```

   * docker command --help
```
sudo docker stats --help
```

   * output
      
```
Usage:  docker stats [OPTIONS] [CONTAINER...]

Display a live stream of container(s) resource usage statistics

Aliases:
  docker container stats, docker stats

Options:
  -a, --all             Show all containers (default shows just running)
      --format string   Format output using a custom template:
                        'table':            Print output in table format with column headers (default)
                        'table TEMPLATE':   Print output in table format using the given Go template
                        'json':             Print in JSON format
                        'TEMPLATE':         Print output using the given Go template.
                        Refer to https://docs.docker.com/go/formatting/ for more information about formatting output with templates
      --no-stream       Disable streaming stats and only pull the first result
      --no-trunc        Do not truncate output
```

----
      
# I
## Inspect an existing container
   * Inspect an existing container:
docker inspect <container_id>

----

# J
# K
# L
## list containers
   * list currently running containers:

 ```
 docker ps
 ```

   * `-a` list all docker containers (status: stop/start):
```
docker ps -a
```

   * `--all`list all docker containers (status: stop/start):
```
docker ps --all
```

----

# M
# N
# O
# P
# Q
# R
## run
   * Creates a docker container from docker image.
   * command:
       ```
       docker start <container_name/container-id>
       ```
   * example:
       * start 1 container-id (5395481d7201)
       ```
       sudo docker start 5395481d7201
       ```

 * edit it
```
    * Docker Logo from Docker Inc & @bloglaurelGeneral Usage Build Images
      Debug
    * Volumes
      Start a container in background
    * $> docker run -d jenkins
      Start an interactive container
      $> docker run -it ubuntu bash
      Export port from a container
      $> docker run -p 80:80 -d nginx
      Start a named container
      $> docker run --name m
   ```    
       
# S
## `start` an existing container:
   * command:
       ```
       docker start <container_name/container-id>
       ```
   * example:
       * start 1 container-id (5395481d7201)
       ```
       sudo docker start 5395481d7201
       ```
## `stop` an existing container:     
   * command:
       ```
       docker stop <container_name/container-id>
       ```
   * example:
       * stop 3 container-id (5395481d7201 42bc125f19c3 b60e702047e4)
       ```
       sudo docker stop 5395481d7201 42bc125f19c3 b60e702047e4
       ```
## `stat` - docker container stats
   * command:
       ```
       docker container stats
       ```
   * output
       ```
      CONTAINER ID   NAME           CPU %     MEM USAGE / LIMIT     MEM %     NET I/O       BLOCK I/O         PIDS
      5395481d7201   some-mariadb   0.02%     76.86MiB / 15.33GiB   0.49%     8.09kB / 0B   4.81MB / 32.8kB   11
       ``` 
   * `ctrl+c` - exit the `stats` window

# T 
# U
# V
# W
# X
# Y
# Z

### links - edit it
* https://docs.docker.com/get-started/docker_cheatsheet.pdf
* https://dockerlux.github.io/pdf/cheat-sheet-v2.pdf
* https://phoenixnap.com/kb/docker-commands-cheat-sheet
* https://phoenixnap.com/kb/wp-content/uploads/2022/12/running-a-container-cheat-sheet.png
* https://students.mimuw.edu.pl/~zbyszek/bezp/docker/4855175-docker-cheatsheet-r4v2.pdf
* https://www.studocu.com/row/document/universite-des-sciences-et-de-la-technologie-houari-boumediene/reseaux-et-systemes-informatiques/docker-commands-cheat-sheet-pdf/68044248
* https://www.hostinger.com/tutorials/docker-cheat-sheet
* https://hub.docker.com/_/mariadb
* https://mkyong.com/
* http://www.dev.ucm.minfin.gov.ao/cs/groups/public/documents/document/zmlu/mdy3/~edisp/minfin067977.pdf
* https://www.docker.com/
* https://mkyong.com/docker/how-to-run-an-init-script-for-docker-mariadb/
* https://mkyong.com/docker/how-to-run-an-init-script-for-docker-postgres/
----


<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
