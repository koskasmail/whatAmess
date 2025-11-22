
# android_offline_build_environment.md

#### java
```sh
Trying to build an Android project on a machine without internet,
but Gradle is failing because it can’t download dependencies (like `com.android.tools.build:gradle:8.1.2`).
To make Gradle builds work offline, you need to "pre‑download all required dependencies"
and set up a local repository that Gradle can use. Here’s a step‑by‑step approach:
```

---

## 🛠 Steps to Create an Offline Build Environment

### 1. Identify Required Dependencies
- Look at your `build.gradle` files (both project and module level).
- Note all plugins and libraries (e.g., `com.android.tools.build:gradle`, AndroidX libraries, IBM SDK dependencies, etc.).

### 2. Download Dependencies on a Machine with Internet
- On a computer with internet access, run:
  ```bash
  gradlew build --refresh-dependencies
  ```
  This forces Gradle to download everything fresh into its cache.

- Gradle caches dependencies in:
  - **Windows:** `C:\Users\<YourUser>\.gradle\caches\modules-2\files-2.1`
  - **Linux/macOS:** `~/.gradle/caches/modules-2/files-2.1`

### 3. Copy the Gradle Cache to the Offline Machine
- Copy the `.gradle` folder from the online machine to the offline one.
- Place it in the same user directory (`C:\Users\<YourUser>\.gradle` on Windows).

### 4. Use a Local Maven Repository (Optional but Cleaner)
- Instead of copying caches, you can publish dependencies to a local Maven repo:
  ```bash
  gradlew publishToMavenLocal
  ```
- This puts artifacts in `~/.m2/repository`.
- Then, in your `build.gradle`, add:
  ```gradle
  repositories {
      mavenLocal()
  }
  ```
- This ensures Gradle looks in your local repo first.

### 5. Enable Gradle Offline Mode
- In **Android Studio**:  
  Go to `File > Settings > Build, Execution, Deployment > Gradle` → check **Offline work**.
- Or via command line:
  ```bash
  gradlew assembleDebug --offline
  ```

### 6. Match Java & Gradle Versions
- Your error shows a mismatch: Gradle plugin `8.1.2` requires **Java 11+**, but your environment is using **Java 8**.
- Install **Java 11 or 17** on the offline machine.
- Update `gradle-wrapper.properties` to use a compatible Gradle version (e.g., `gradle-8.4-all.zip`).

---

## ✅ Summary
- Pre‑download dependencies on a machine with internet.
- Copy `.gradle` cache or set up a local Maven repo.
- Use `--offline` mode in Gradle.
- Ensure Java version matches the Android Gradle Plugin requirements.

