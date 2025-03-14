<a name="topage"></a>

## 01_warehouse_inventory

#### create a frontend (html,css,js etc)  and backend (java) to create an warehouse inventory

----

Sure! I can help you create a basic frontend and backend for a warehouse inventory system. Here, we'll have the following structure:

1. **Frontend (HTML, CSS, JavaScript)** - To display inventory data and allow user interaction.
2. **Backend (Java)** - A simple Java Spring Boot API to handle inventory data (CRUD operations).
3. **Database** - A simple database to store inventory data (we can use an in-memory database like H2 for simplicity, or a MySQL database if you'd like to persist data).

### 1. Frontend (HTML, CSS, JavaScript)
The frontend will allow users to view the inventory, add new items, update, and delete items.

#### HTML (index.html)
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Inventory</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>Warehouse Inventory</h1>
        <button id="addItemBtn">Add Item</button>
        <table id="inventoryTable">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <!-- Inventory data will be injected here by JavaScript -->
            </tbody>
        </table>
    </div>

    <!-- Add Item Modal -->
    <div id="addItemModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" id="closeModal">&times;</span>
            <h2>Add Item</h2>
            <form id="addItemForm">
                <input type="text" id="itemName" placeholder="Item Name" required />
                <input type="number" id="itemQuantity" placeholder="Quantity" required />
                <input type="number" id="itemPrice" placeholder="Price" required />
                <button type="submit">Add Item</button>
            </form>
        </div>
    </div>

    <script src="scripts.js"></script>
</body>
</html>
```

#### CSS (styles.css)
```css
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
}

.container {
    width: 80%;
    margin: 20px auto;
    padding: 20px;
    background-color: white;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    margin-bottom: 20px;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 15px;
    border: none;
    cursor: pointer;
    margin-bottom: 20px;
}

button:hover {
    background-color: #45a049;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

tr:hover {
    background-color: #f1f1f1;
}

button.delete-btn {
    background-color: #f44336;
}

button.delete-btn:hover {
    background-color: #e53935;
}

/* Modal styles */
.modal {
    display: none;
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
    background-color: #fff;
    padding: 20px;
    margin: 15% auto;
    width: 300px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.close-btn {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close-btn:hover,
.close-btn:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}
```

#### JavaScript (scripts.js)
```javascript
document.addEventListener('DOMContentLoaded', () => {
    const addItemBtn = document.getElementById('addItemBtn');
    const addItemModal = document.getElementById('addItemModal');
    const closeModalBtn = document.getElementById('closeModal');
    const addItemForm = document.getElementById('addItemForm');
    const inventoryTable = document.getElementById('inventoryTable').getElementsByTagName('tbody')[0];

    // Show modal
    addItemBtn.addEventListener('click', () => {
        addItemModal.style.display = 'block';
    });

    // Close modal
    closeModalBtn.addEventListener('click', () => {
        addItemModal.style.display = 'none';
    });

    // Submit new item
    addItemForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const name = document.getElementById('itemName').value;
        const quantity = document.getElementById('itemQuantity').value;
        const price = document.getElementById('itemPrice').value;

        const item = {
            name,
            quantity,
            price
        };

        fetch('http://localhost:8080/api/inventory', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        })
        .then(response => response.json())
        .then(data => {
            addItemModal.style.display = 'none';
            fetchInventory();
        });
    });

    // Fetch inventory items
    function fetchInventory() {
        fetch('http://localhost:8080/api/inventory')
        .then(response => response.json())
        .then(data => {
            inventoryTable.innerHTML = '';
            data.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.quantity}</td>
                    <td>${item.price}</td>
                    <td><button class="delete-btn" onclick="deleteItem(${item.id})">Delete</button></td>
                `;
                inventoryTable.appendChild(row);
            });
        });
    }

    // Delete item
    window.deleteItem = function(id) {
        fetch(`http://localhost:8080/api/inventory/${id}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            fetchInventory();
        });
    };

    // Initialize
    fetchInventory();
});
```

### 2. Backend (Java Spring Boot)

To create the backend, we'll use Spring Boot to create a REST API to manage inventory items.

#### Create Spring Boot Project
You can create a Spring Boot project using [Spring Initializr](https://start.spring.io/). Choose the following dependencies:
- Spring Web
- Spring Data JPA
- H2 Database (or MySQL)

#### Java Code

##### Main Application Class (WarehouseInventoryApplication.java)
```java
package com.warehouse.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WarehouseInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseInventoryApplication.class, args);
    }
}
```

##### Inventory Model (Inventory.java)
```java
package com.warehouse.inventory.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Inventory {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int quantity;
    private double price;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
```

##### Repository Interface (InventoryRepository.java)
```java
package com.warehouse.inventory.repository;

import com.warehouse.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
```

##### Controller (InventoryController.java)
```java
package com.warehouse.inventory.controller;

import com.warehouse.inventory.model.Inventory;
import com.warehouse.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    // Get all inventory items
    @GetMapping
    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    // Add new inventory item
    @PostMapping
    public Inventory addItem(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Delete an inventory item
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }
}
```

#### Application Properties (application.properties)
```properties
# In-memory database configuration for simplicity
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### 3. Running the Application
- Run the backend (Spring Boot application).
- Open the `index.html` in the frontend folder.

The frontend will interact with the backend using RESTful API calls to add, display, and delete inventory items.

This should give you a basic warehouse inventory system. You can customize the frontend and backend based on your needs (e.g., using a real database, adding more features, or improving UI).


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
