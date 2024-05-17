
# Hello Docker


### mkdir
```
mkdir hello-docker
cd hello-docker
```

### create file "app.js"
 ```
 console.log("Hello Docker !");
 ```

### create file "Dockerfile"
 ```
 FROM  node:alpine
 COPY . /app  
 WORKDIR /app
 CMD node app.js
 ```

### deploy docker
```
sudo docker build -t hello-docker .
```

### show images
```
sudo docker image ls
```

### run images
```
sudo docker run hello-docker
```
