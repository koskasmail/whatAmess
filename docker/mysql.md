
# mysql

install docker mysql
```
$ docker run --detach --name some-mariadb --env MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=1  mariadb:latest
```

#### demo

```
It seems like you're running a Docker container using the mariadb:latest image with the name some-mariadb. This command is instructing Docker to:

docker run: This command is used to create and start a new Docker container.
--detach or -d: This flag tells Docker to run the container in detached mode, meaning it runs in the background.
--name some-mariadb: This sets the name of the container to some-mariadb.
--env MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=1: This sets an environment variable within the container to allow an empty root password for the MariaDB instance.
mariadb:latest: This specifies the Docker image to use for creating the container. In this case, it's the latest version of MariaDB available on Docker Hub.
This command will spin up a MariaDB container named some-mariadb, running in the background with an empty root password allowed.
```

### install #1
```
$ docker run --detach --name some-mariadb --env MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=1  mariadb:latest
```

### instasll #1 output
```
sudo docker run --detach --name todo --env MARIADB_ROOT_PASSWORD=1  mariadb:latest
Unable to find image 'mariadb:latest' locally
latest: Pulling from library/mariadb
a8b1c5f80c2d: Pull complete 
d452ffbe7f24: Pull complete 
4ba312e23b80: Pull complete 
5cff2f71f4cf: Pull complete 
7b3561610bd5: Pull complete 
2be8c7da712f: Pull complete 
880bb1834a6d: Pull complete 
59853578cb11: Pull complete 
Digest: sha256:f0a6faee9d0e55492f238d1d11ff13d77616ea12d8c38bedf090da2ee05532be
Status: Downloaded newer image for mariadb:latest
1adeb16fb7c3bc88311f5411555d711478133a5b38a014856e1faf1f949db15c
```




