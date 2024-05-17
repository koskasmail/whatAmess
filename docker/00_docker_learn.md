
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

#### application into an image
* Take application and make a change  and dockerize it (make a small change so it can be run by docker) by adding a docker file to it. 
* Dockerfile is a plain text file that include instruction that docker uses to package up this application into an image.
* The image contain everything our application needs to run.
    * OS, runtime environment(node/python),application files, third party libraries, environment variables, etc.
* docker package the application into an image.

#### running container locally on development machine
* Once we have the image, we tell docker to start a container using that image.
* a container is a process with his own file system that provided by the image.
* an application get loaded inside a container or process.
* running it in local machine.
* instead of loading the application directly, we will tell docker to run this application inside a container (isolated environment).
* Once we have the image we can push it to docker registry (docker hub)
* docker hub is like `github` to `git` (docker image that anyone can use).
* when the application image is on docker hub then we can put it on any machine running docker.
* this machine has the same image we have on our development machine (specific version of our application with everything it needs).
* Starting the application the way we start it on our development machine by telling docker to 'start a container` using this image.
* All the instrauction for building an image of an application are 
* All the instruction for building an image of an application are written in a `docker file`. 
* using a `docker file` we can package up your application into an image and run it virtually anywhere

### evelopment workflow  
* make folder called "hello-docker" and enter it.
```
mkdir hello-docker
cd hello-docker
```
* create a new file call "app.js"
* enter a command inside the "app.js" file
* app.js
```
console.log("Hello Docker !");
```
* we want to dockerize this application/file ("app.js")
* using docker we want to build, run, ship this application.

# instructions to deploying this program 
   * start with an os
   * install node
   * copy App files
   * run node `app.js`

# instructions to deploying this program  in docker
* running these instructions inside a docker file and package up our application.
* add `Dockerfile` in the same folder as `app.js`
* install docker inside vscode. (docker microsoft)

* Dockerfile
```
# base image 
FROM  node:alpine
# copy all the files from the current directory into the /app directory
COPY . /app  
WORKDIR /app
# execute command
CMD node app.js
```

* build image with tag from current directory `.`
```
sudo docker build -t hello-docker .
```

* show image in docker
```
sudo docker image ls
```
   * output
   ```
   REPOSITORY     TAG       IMAGE ID       CREATED         SIZE
   hello-docker   latest    84afe6ebea2f   3 minutes ago   148MB
   mariadb        latest    465bc4da7f09   2 months ago    405MB
   hello-world    latest    d2c94e258dcb   12 months ago   13.3kB
   ```
* run this program
```
sudo docker run hello-docker
```

* output
```

```

#### commands
```
mkdir hello-docker
cd hello-docker
```

 "app.js"
 ```
 console.log("Hello Docker !");
 ```

 "Dockerfile"
 ```
 FROM  node:alpine
 COPY . /app  
 WORKDIR /app
 CMD node app.js
 ```

* deploy docker
```
sudo docker build -t hello-docker .
```

* show images
```
sudo docker image ls
```

* run images
```
sudo docker run hello-docker
```


--------

* TODO
   * check docker desktop for linux.


--------
* document url
    * https://youtu.be/pTFZFxd4hOI?si=tx1dV3CPVRomKG7w

