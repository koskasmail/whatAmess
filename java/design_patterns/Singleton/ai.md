<a name="topage"></a>

# **Singleton pattern**

* The Java **Singleton pattern** used when you need to ensure that **only one instance** of a class exists throughout the application. 

## examples: 

---

### 🔧 1. **Database Connection Manager**

Ensures only one connection manager exists to avoid resource leaks.

```java
public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;

    private DatabaseConnectionManager() {
        // Initialize connection pool or settings
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public void connect() {
        System.out.println("Connecting to database...");
    }
}
```

**Usage**:
```java
DatabaseConnectionManager db = DatabaseConnectionManager.getInstance();
db.connect();
```

---

### 📝 2. **Logger Utility**

Centralizes logging across the application.

```java
public class Logger {
    private static Logger instance;

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
```

**Usage**:
```java
Logger logger = Logger.getInstance();
logger.log("Application started.");
```

---

### ⚙️ 3. **Configuration Manager**

Loads and provides access to app settings.

```java
public class ConfigManager {
    private static ConfigManager instance;
    private Properties config = new Properties();

    private ConfigManager() {
        // Load config from file
        config.setProperty("app.name", "MyApp");
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getSetting(String key) {
        return config.getProperty(key);
    }
}
```

**Usage**:
```java
String appName = ConfigManager.getInstance().getSetting("app.name");
System.out.println(appName);
```

---

These examples show how Singleton helps with **resource management**, **centralized control**, and **consistent access**. Want to explore thread-safe versions or how to break a Singleton using reflection or serialization?

----

# java singleton `thread-safe`

When it comes to making a **Singleton thread-safe in Java**, especially in multi-threaded environments, there are several approaches—each with its own trade-offs. Here are the most popular and effective ones:

---

### 🧵 1. **Synchronized Method (Simple but Slower)**

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

- ✅ Easy to implement
- ❌ Slower due to synchronization on every call

---

### ⚡ 2. **Double-Checked Locking (Efficient and Lazy)**

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

- ✅ Fast after initialization
- ✅ Thread-safe with `volatile`
- ⚠️ Slightly more complex

---

### 🧙 3. **Bill Pugh Singleton (Elegant and Lazy)**

```java
public class Singleton {
    private Singleton() {}

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

- ✅ Lazy initialization
- ✅ Thread-safe without synchronization
- ✅ Clean and recommended for most cases

---

### 🧬 4. **Enum Singleton (Robust and Safe)**

```java
public enum Singleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Doing something...");
    }
}
```

- ✅ Thread-safe by JVM design
- ✅ Serialization and reflection-safe
- ❌ Less flexible if you need lazy loading or subclassing

---

Each of these patterns has its place depending on your needs. If you're building a high-performance app, **Double-Checked Locking** or **Bill Pugh** is ideal. For simplicity and safety, **Enum Singleton** is unbeatable.

Want to see how these behave under concurrent stress tests or explore breaking them with reflection?

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
