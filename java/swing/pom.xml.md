# pom.xml

#### example #1:

| xxx | name | groupId | artifactId | version | comment |
|  -  | :-: | :-: |  :-: | :-: | :-: |
| 01 | poi | org.apache.poi | poi | 5.4.1 | xxx |
| 02 | pdfbox | org.apache.pdfbox | pdfbox | 3.0.5 | xxx |
| 03 | poi-ooxml | org.apache.poi | poi-ooxml | 5.4.1 | xxx |
| 04 | jsoup | org.jsoup | jsoup | 1.21.1 | xxx |

#### pom.xml

````
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>jaron</groupId>
  <artifactId>jaron</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>viewer</name>
  <description>Jaron Viewer</description>

  <dependencies>
  	<dependency>
  		<groupId>org.apache.poi</groupId>
  		<artifactId>poi</artifactId>
  		<version>5.4.1</version>
  	</dependency>
  	<dependency>
  		<groupId>org.apache.pdfbox</groupId>
  		<artifactId>pdfbox</artifactId>
  		<version>3.0.5</version>
  	</dependency>
  	<dependency>
  		<groupId>org.apache.poi</groupId>
  		<artifactId>poi-ooxml</artifactId>
  		<version>5.4.1</version>
  	</dependency>
  	<dependency>
  		<groupId>org.jsoup</groupId>
  		<artifactId>jsoup</artifactId>
  		<version>1.21.1</version>
  	</dependency>
  </dependencies>
</project>
````
------

#### example #2:

| xxx | name | groupId | artifactId | version | comment |
|  -  | :-: | :-: |  :-: | :-: | :-: |
| 01 | poi | org.apache.poi | poi | 5.4.1 | xxx |
| 02 | pdfbox | org.apache.pdfbox | pdfbox | 3.0.5 | xxx |
| 03 | poi-ooxml | org.apache.poi | poi-ooxml | 5.4.1 | xxx |
| 04 | jsoup | org.jsoup | jsoup | 1.21.1 | xxx |
| 05 | java | jdk | 1.8| xxx |

#### pom.xml

````
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>jaron</groupId>
  <artifactId>jaron</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>viewer</name>
  <description>Jaron Viewer</description>
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  
  <dependencies>
  	<dependency>
  		<groupId>org.apache.poi</groupId>
  		<artifactId>poi</artifactId>
  		<version>5.4.1</version>
  	</dependency>
  	<dependency>
  		<groupId>org.apache.pdfbox</groupId>
  		<artifactId>pdfbox</artifactId>
  		<version>3.0.5</version>
  	</dependency>
  	<dependency>
  		<groupId>org.apache.poi</groupId>
  		<artifactId>poi-ooxml</artifactId>
  		<version>5.4.1</version>
  	</dependency>
  	<dependency>
  		<groupId>org.jsoup</groupId>
  		<artifactId>jsoup</artifactId>
  		<version>1.21.1</version>
  	</dependency>
  </dependencies>
</project>
````

------

#### example #3:

------

#### example #4:

------

#### example #5:

------
