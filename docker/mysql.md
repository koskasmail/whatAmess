
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





