
# docker-links

### links
* https://docs.docker.com/guides/walkthroughs/what-is-a-container/
* https://docs.docker.com/desktop/install/linux-install/

### mariadb
* https://hub.docker.com/_/mariadb

### create a file call
* docker-compose.yml
* insert data in file

````
# Use root/example as user/password credentials
version: '3.1'

services:

  db:
    image: mariadb
    restart: always
    environment:
      MARIADB_ROOT_PASSWORD: example

  adminer:
    image: adminer
    restart: always
    ports:
      - 8080:8080
````

### exacute the command 
```
docker stack deploy -c stack.yml mariadb
```

##### or this command
```
docker-compose -f stack.yml
```

### run this url
* http://swarm-ip:8080
* http://localhost:8080
* http://host-ip:8080

