<a name="topage"></a>

# mariadb.md

1. installing MariaDB
2. creating a `Northwind` style sample database

---

## 🛠️ Part 1: Install MariaDB on Ubuntu/Debian
Here’s a Bash snippet to install and secure MariaDB:

```bash
#!/bin/bash

echo "🔧 Installing MariaDB..."
sudo apt update
sudo apt install -y mariadb-server mariadb-client

echo "🔒 Securing MariaDB..."
sudo mysql_secure_installation

echo "✅ MariaDB installed and secured."
```

This will:
- Install the server and client
- Run the interactive security script to set root password, remove test DB, etc.

---

## 🧪 Part 2: Create a Northwind-Like Sample Database

Here’s a script that:
- Creates a database called `northwind`
- Adds sample tables (`customers`, `products`, `orders`)
- Inserts a few rows of mock data

```bash
#!/bin/bash

DB_NAME="northwind"
DB_USER="northadmin"
DB_PASS="northpass"

echo "🚀 Creating database and user..."
sudo mariadb -u root <<EOF
CREATE DATABASE IF NOT EXISTS $DB_NAME;
CREATE USER IF NOT EXISTS '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASS';
GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'localhost';
FLUSH PRIVILEGES;
EOF

echo "📦 Creating tables and inserting sample data..."
sudo mariadb -u $DB_USER -p$DB_PASS $DB_NAME <<EOF
CREATE TABLE customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100),
  country VARCHAR(50)
);

CREATE TABLE products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  price DECIMAL(10,2),
  stock INT
);

CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT,
  product_id INT,
  quantity INT,
  order_date DATE,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO customers (name, email, country) VALUES
('Alice', 'alice@example.com', 'USA'),
('Bob', 'bob@example.com', 'UK');

INSERT INTO products (name, price, stock) VALUES
('Laptop', 999.99, 10),
('Mouse', 19.99, 100);

INSERT INTO orders (customer_id, product_id, quantity, order_date) VALUES
(1, 1, 1, CURDATE()),
(2, 2, 2, CURDATE());
EOF

echo "✅ Northwind-style database created!"
```

---

## 🧭 Full Northwind Schema

If you want the full Microsoft-style Northwind schema with dozens of tables and relationships, check out:
- [Northwind for MySQL on GitHub](https://github.com/epikur-io/northwind)
- [Northwind Extended SQL Scripts](https://github.com/mrin9/northwind)

#### download `.sql` file and import into `mariadb`:
```bash
sudo mariadb -u root < northwind.sql
```

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
