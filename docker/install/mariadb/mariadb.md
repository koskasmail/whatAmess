<a name="topage"></a>

# mariadb

### download mariadb

````
sudo docker search mariadb
````

#### output
```
.
.
NAME                                         DESCRIPTION                                     STARS    OFFICIAL
mariadb                                      MariaDB Server is a high performing open sou…   5783      [OK]
phpmyadmin                                   phpMyAdmin - A web interface for MySQL and M…   1001      [OK]
mariadb/maxscale                             MariaDB MaxScale - The world's most advanced…   35        
mariadb/server                               This repository/image is deprecated. Please …   56        
mariadb/columnstore                          MariaDB ColumnStore Engine for Analytics        4         
bitnami/mariadb                              Bitnami container image for MariaDB             192       
mariadb/xpand-single                         Single node MariaDB Xpand docker image, for …   3         
circleci/mariadb                             CircleCI images for MariaDB                     4         
bitnami/mariadb-galera                       Bitnami container image for MariaDB Galera      25        
mariadb/mariadb-operator-enterprise                                                          0         
mariadb/maxscale-prometheus-exporter-ubi                                                     0         
mariadb/mariadb-operator-enterprise-bundle                                                   0         
cimg/mariadb                                                                                 0         
mariadb/mariadb-prometheus-exporter-ubi                                                      0         
bitnamicharts/mariadb                                                                        0         
rapidfort/mariadb                            RapidFort optimized, hardened image for Mari…   23        
mariadb/developers                           Docker demo images to support sample code an…   0         
elestio/mariadb                              Mariadb, verified and packaged by Elestio       0         
linuxserver/mariadb                          A Mariadb container, brought to you by Linux…   391       
rapidfort/mariadb-ib                         RapidFort optimized, hardened image for Mari…   8         
rapidfort/mariadb-official                   RapidFort optimized, hardened image for Mari…   11        
vmware/mariadb-photon                                                                        0         
chainguard/mariadb                           MariaDB  is one of the most popular open sou…   0         
bitnamicharts/mariadb-galera                                                                 0         
wodby/mariadb                                Alpine-based MariaDB container image with or…   8  
```

### check/download the latest version on `mariadb` site
https://mariadb.org/download/?t=mariadb&p=mariadb&r=11.4.2&os=Linux&cpu=x86_64&pkg=tar_gz&i=systemd&mirror=archive

#### download mariadb latest version (07/2024)
````
sudo docker pull mariadb:10.4
````

#### output
```
11.4.2: Pulling from library/mariadb
9c704ecd0c69: Pull complete 
f8f7f3c9a741: Pull complete 
97c034108521: Pull complete 
50366979c20a: Pull complete 
0221331af5c0: Pull complete 
e3c4d1c9d9cb: Pull complete 
eef7a8467f98: Pull complete 
60c15bb5bb03: Pull complete $ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   18 minutes ago   Up 10 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest
$ sudo docker stop mariadbtest
mariadbtest
$ sudo docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
$ sudo docker ps -A
unknown shorthand flag: 'A' in -A
See 'docker ps --help'.
$ sudo docker ps a
"docker ps" accepts no arguments.
See 'docker ps --help'.

Usage:  docker ps [OPTIONS]

List containers
$ sudo docker ps all
\"docker ps" accepts no arguments.
See 'docker ps --help'.

Usage:  docker ps [OPTIONS]

List containers
$ sudo docker ps -all
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS                      PORTS     NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Exited (0) 20 seconds ago             mariadbtest
$ sudo docker start mariadbtest
mariadbtest
$ sudo docker ps all
"docker ps" accepts no arguments.
See 'docker ps --help'.

Usage:  docker ps [OPTIONS]

List containers
$ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS         PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Up 7 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest
$ sudo docker ps -all
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Up 13 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest

```

### show a list of installed images

````
sudo docker images
````

#### output
```
REPOSITORY     TAG       IMAGE ID       CREATED         SIZE
mariadb        11.4.2    4486d64c9c3b   3 weeks ago     406MB
hello-docker   latest    2d4bd7156ea6   6 weeks ago     148MB
<none>         <none>    84afe6ebea2f   6 weeks ago     148MB
mariadb        latest    465bc4da7f09   4 months ago    405MB
hello-world    latest    d2c94e258dcb   14 months ago   13.3kB
```

### Creating a Container 
```
sudo docker run --name mariadbtest -e MYSQL_ROOT_PASSWORD=mypass -p 3306:3306 -d docker.io/library/mariadb:11.4.2
 ```

#### output
```
bfefabc6473de3b9693df29de550f72c9815de8c8e6ec3ab6f57a20dfa9c0ca8
```

### show all stoped/running containers 
```
sudo docker ps -all
```

### show all running containers
```
sudo docker ps
```

#### output
```
CONTAINER ID   IMAGE            COMMAND                  CREATED         STATUS         PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   4 minutes ago   Up 4 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest
```

----

### restart mariadb containers
```
sudo docker restart mariadbtest
```

#### example
```
$ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED         STATUS         PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   8 minutes ago   Up 8 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest

$ sudo docker restart mariadbtest
mariadbtest

$ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED         STATUS         PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   8 minutes ago   Up 4 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest
```

---- 

### stop and start mariadb containers

#### stop
```
sudo docker stop mariadbtest
```

#### start
```
sudo docker start mariadbtest
```

#### example

```
$ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   18 minutes ago   Up 10 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest

$ sudo docker stop mariadbtest
mariadbtest

$ sudo docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES

$ sudo docker ps -all
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS                      PORTS     NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Exited (0) 20 seconds ago             mariadbtest

$ sudo docker start mariadbtest
mariadbtest

$ sudo docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS         PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Up 7 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest

$ sudo docker ps -all
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                       NAMES
bfefabc6473d   mariadb:11.4.2   "docker-entrypoint.s…"   19 minutes ago   Up 13 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp   mariadbtest
```

#### stop service after 10 second
```
sud docker stop --time=10 mariadbtest
```


### stop service container with `kill` parameters
```
sudo docker kill mariadbtest
```

-----

### log docker container
```
docker logs mariadbtest
```

-----
* TODO
* TODO:xxxx continue here xxx
* split this file.

### access the container via Bash
```
sudo docker exec -it mariadbtest bash
```

-----

### Connecting to MariaDB from Outside the Container

* TODO: learn more about it.

#### IP address that has been assigned to the container:

```
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mariadbtest
```

#### output
```
172.17.0.2
```

### enter the database:

#### install mariadb-client in local machine
```
sudo apt-get install mariadb-client
```

#### enter mariadb container using this command and then the passowrd (mypass)

```
$ sudo mariadb -h 172.17.0.2 -u root -p <enter>
```

#### example
```
$ sudo mariadb -h 172.17.0.2 -u root -p <enter>
Enter password: 
```

#### output

```
$ sudo mariadb -h 172.17.0.2 -u root -p
Enter password: 
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 9
Server version: 11.4.2-MariaDB-ubu2404 mariadb.org binary distribution

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> 
```

-----

### remove container
```
sudo docker rm mariadbtest
```

### remove container and destroy the value
```
sudo docker rm -v mariadbtest
```

-----



* rtfm: thanks for
   * https://mariadb.com/kb/en/installing-and-using-mariadb-via-docker/

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
