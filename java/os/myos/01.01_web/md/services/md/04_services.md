# 04_services

Yes. Assuming this is for your **Spring Boot Java project with H2**, you can add two REST services:

1. **Get all people** → returns `firstName`, `lastName`, `phone`, `cellphone`
2. **Search people** → accepts either first name or last name and returns matching records.

A clean structure would be:

```text
src/main/java/com/jaron/sod/
├── controller/
│   └── PersonController.java
├── service/
│   └── PersonService.java
├── repository/
│   └── PersonRepository.java
├── entity/
│   └── Person.java
└── dto/
    └── PersonResponse.java
```

### 1. Entity

```java
package com.jaron.sod.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phone;
    private String cellphone;

    public Person() {
    }

    public Person(String firstName, String lastName,
                  String phone, String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.cellphone = cellphone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
```

### 2. Repository

```java
package com.jaron.sod.repository;

import com.jaron.sod.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstNameContainingIgnoreCase(String firstName);

    List<Person> findByLastNameContainingIgnoreCase(String lastName);
}
```

### 3. Response DTO

This makes sure the API returns only the four fields you requested.

```java
package com.jaron.sod.dto;

public class PersonResponse {

    private String firstName;
    private String lastName;
    private String phone;
    private String cellphone;

    public PersonResponse(String firstName,
                          String lastName,
                          String phone,
                          String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.cellphone = cellphone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCellphone() {
        return cellphone;
    }
}
```

### 4. Service

```java
package com.jaron.sod.service;

import com.jaron.sod.dto.PersonResponse;
import com.jaron.sod.entity.Person;
import com.jaron.sod.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    // Service 1 - get all records
    public List<PersonResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Service 2 - search by first name OR last name
    public List<PersonResponse> search(String name) {

        List<Person> firstNameResults =
                repository.findByFirstNameContainingIgnoreCase(name);

        List<Person> lastNameResults =
                repository.findByLastNameContainingIgnoreCase(name);

        return java.util.stream.Stream.concat(
                    firstNameResults.stream(),
                    lastNameResults.stream()
                )
                .distinct()
                .map(this::toResponse)
                .toList();
    }

    private PersonResponse toResponse(Person person) {

        return new PersonResponse(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                person.getCellphone()
        );
    }
}
```

### 5. Controller

```java
package com.jaron.sod.controller;

import com.jaron.sod.dto.PersonResponse;
import com.jaron.sod.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    // GET all people
    @GetMapping
    public List<PersonResponse> getAll() {
        return service.getAll();
    }

    // Search first name OR last name
    //
    // Example:
    // /api/person/search?name=John
    //
    @GetMapping("/search")
    public List<PersonResponse> search(
            @RequestParam String name) {

        return service.search(name);
    }
}
```

### 6. H2 configuration

For example, in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/sod
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

And your Maven dependencies need H2 + JPA:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### API result

Calling:

```text
GET /api/person
```

returns:

```json
[
  {
    "firstName": "John",
    "lastName": "Smith",
    "phone": "03-1234567",
    "cellphone": "050-1234567"
  },
  {
    "firstName": "David",
    "lastName": "Cohen",
    "phone": "03-7654321",
    "cellphone": "052-7654321"
  }
]
```

And:

```text
GET /api/person/search?name=Smith
```

returns records where **first name OR last name** contains `Smith`:

```json
[
  {
    "firstName": "John",
    "lastName": "Smith",
    "phone": "03-1234567",
    "cellphone": "050-1234567"
  }
]
```

The search is **case-insensitive** and also supports partial names, so `smi` will match `Smith`.

If you show me your **current Spring Boot/H2 code**, I can integrate these two services directly into your existing project rather than making a separate structure.
