<a name="topage"></a>

# mariadb installation

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
60c15bb5bb03: Pull complete 
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

* rtfm: thanks for
   * https://mariadb.com/kb/en/installing-and-using-mariadb-via-docker/

 ### Creating a Container - 
 * XXXXXXXXXX TODO: fix the command - XXXXXXXXXXXXXXXXXXXX
 
 ```
 docker run --name mariadbtest -e MYSQL_ROOT_PASSWORD=mypass -p 3306:3306 -d docker.io/library/mariadb:10.3
 ```


#### output
```
xxxxxxxxxxxxxxxxxx
```


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
