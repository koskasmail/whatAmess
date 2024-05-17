
# review

# section 1

* hello-world
```
docker build -t hello-world
```

* docker run and deploy
```
docker run hello-world
```

* task
    * from-end
    * back-end
    * database

# section 1
* what is docker ?
* virtual machin vs containers
* docker architecture 
* installing docker
* development workflow

## what is docker ?
* docker is a platform for building, running, shipping applications.
* docker package everything you need for you software (example: nodejs, mongodb, app)
* set a new machine to run your application
```
$ docker-compose up
```

remove old machine (with all the dep)
```
$ docker-compose down --rmi all deployments
```

## virtual machin vs containers
* virtual machin 

## docker architecture 
* `client` component that talk to `server` component (docker engine) using `RESTful API`.
* `server` component (docker engine) sits on the background and takes care of 
    * building docker containers
    * running docker containers

* docker container is a process run on your computer.
* docker container in host share the Operating System (kernel) of the host.

## installing docker
* docker version
```
sudo docker version
```

#### `output`

##### `Client`

```
Client: Docker Engine - Community
 Cloud integration: v1.0.35+desktop.10
 Version:           26.1.2
 API version:       1.45
 Go version:        go1.21.10
 Git commit:        211e74b
 Built:             Wed May  8 13:59:59 2024
 OS/Arch:           linux/amd64
 Context:           default
```

##### `server`

```
Server: Docker Engine - Community
 Engine:
  Version:          26.1.2
  API version:      1.45 (minimum version 1.24)
  Go version:       go1.21.10
  Git commit:       ef1912d
  Built:            Wed May  8 13:59:59 2024
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.6.31
  GitCommit:        e377cd56a71523140ca6ae87e30244719194a521
 runc:
  Version:          1.1.12
  GitCommit:        v1.1.12-0-g51d5e94
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0

```

### installing docker

#### docker get installation
* https://docs.docker.com/get-docker/

* docker engine


### development workflow

1. install
2. running
3. executing

--------

* TODO
   * check docker desktop for linux.


--------
* document url
    * https://youtu.be/pTFZFxd4hOI?si=tx1dV3CPVRomKG7w

