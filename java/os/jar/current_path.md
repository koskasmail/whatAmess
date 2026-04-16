

# current_path

#### NOTE:
* current executing program path.
* current executing jar path.

-----

In Java, there are **two slightly different things** you’re trying to get:

1. The **current program location while running in IDE / class files**
2. The **JAR file location when running a packaged JAR**

Here’s how to handle both properly.

---

## ✅ 1. Get current running path (works in both IDE and JAR)

```java
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String path = Paths.get("").toAbsolutePath().toString();
        System.out.println("Current working directory: " + path);
    }
}
```

✔ This gives:

* The directory where you launched the program from
* Works in IDE and JAR
  ❗ But NOT necessarily where the `.jar` file is located

---

## ✅ 2. Get the actual JAR file location (important part)

```java
import java.io.File;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        File jarFile = new File(
            Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
        );

        System.out.println("JAR location: " + jarFile.getAbsolutePath());
    }
}
```

---

## 🧠 What this returns

### ▶ When running from IDE:

```
.../out/production/your-project/
```

### ▶ When running as JAR:

```
.../your-app.jar
```

---

## ✅ 3. Get ONLY the folder containing the JAR

```java
File jarFile = new File(
    Main.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .toURI()
);

String jarDir = jarFile.getParentFile().getAbsolutePath();
System.out.println("JAR directory: " + jarDir);
```

---

## ⚠️ Important Notes

* `user.dir` → where program is launched from
* `CodeSource` → where the actual compiled code / JAR lives
* They are often **different**

---

## 💡 Pro tip (safe combined method)

```java
public static String getAppPath() {
    try {
        File file = new File(
            Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
        );
        return file.getParent();
    } catch (Exception e) {
        return System.getProperty("user.dir");
    }
}
```

-----

# single executable jar

##### single executable jar process on a computer

Short answer: you can’t reliably detect “another instance in memory” just by inspecting the JVM. The **correct, cross-platform way** is to use a **lock** (file lock or port lock). That way, only one instance of your JAR can run at a time.

Here’s the clean solution.

---

# ✅ Recommended: File lock (works everywhere)

Create a lock file next to your JAR and try to lock it.
If locking fails → another instance is already running → exit.

```java
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SingleInstanceApp {

    private static FileLock lock;

    public static void main(String[] args) {
        try {
            File jarFile = new File(
                SingleInstanceApp.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );

            File lockFile = new File(jarFile.getParent(), "app.lock");

            RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
            FileChannel channel = raf.getChannel();

            lock = channel.tryLock();

            if (lock == null) {
                System.out.println("Another instance is already running. Exiting...");
                channel.close();
                raf.close();
                return; // exit
            }

            // Optional: delete lock file on exit
            lockFile.deleteOnExit();

            System.out.println("Application started.");

            // ---- your app logic here ----

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

# 🧠 Why this works

* OS guarantees only **one process can hold the lock**
* Works even if:

  * user renames the JAR
  * runs from different folders
* No need to scan processes (which is unreliable and OS-specific)

---

# ⚠️ Important notes

* If app crashes → lock is released automatically by OS
* Don’t use just a “.lock file exists” check ❌ (not safe)
* Always use `tryLock()` ✔

---

# 🚀 Alternative (less recommended): Port locking

You can bind a port:

```java
import java.net.ServerSocket;

ServerSocket socket;

try {
    socket = new ServerSocket(9999);
} catch (Exception e) {
    System.out.println("Already running.");
    return;
}
```

✔ Simple
❌ Can conflict with other apps
❌ Needs fixed port

---

# 🔥 Best practice for your case

Since your app already uses config (`fmc.json`), do this:

* Put lock file in same folder as:

  * JAR
  * OR `reportfile` path

Example:

```java
File lockFile = new File(config.get("path"), "fmc.lock");
```

---

If you want next step, I can:

* Integrate this into your existing **file monitor program**
* Add **PID tracking + graceful shutdown**
* Or make it **system tray app with “already running → focus existing window”**


-----

-----
