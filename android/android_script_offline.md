
# android_script_offline.md

# script_offline

Perfect — you’ve pasted the standard `gradlew.bat` script. By itself, this script just launches Gradle. To make it work **offline**, you need to ensure that:

1. The **Gradle distribution** (`gradle-<version>-all.zip`) is already downloaded and placed in your project’s `gradle/wrapper/` directory.
2. The **Android Gradle Plugin (AGP)** and all other dependencies are pre‑cached or available in a local Maven repository.
3. The script is told to run Gradle in **offline mode** so it never tries to fetch from the internet.

Here’s how you can adapt your `.bat` file for offline use:

---

## 🔧 Modified `gradlew-offline.bat`

```bat
@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows (Offline Mode)
@rem
@rem ##########################################################################

if "%OS%"=="Windows_NT" setlocal

set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
goto fail

:init
set CMD_LINE_ARGS=%*

@rem Setup the command line
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Force Gradle to run in offline mode
set GRADLE_OPTS=%GRADLE_OPTS% --offline

@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% ^
  "-Dorg.gradle.appname=%APP_BASE_NAME%" ^
  -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

:end
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
if not "" == "%GRADLE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
```

---

## 📌 What This Does
- Adds `--offline` to `GRADLE_OPTS` so Gradle never tries to fetch dependencies online.
- Still uses the wrapper JAR (`gradle-wrapper.jar`) in your project.
- Relies on you having **all dependencies pre‑downloaded** in:
  - `%USERPROFILE%\.gradle\caches\` (Windows)
  - or a local Maven repo (`~/.m2/repository`).

---

## ✅ Checklist Before Running Offline
1. On a computer with internet:
   - Run `gradlew build --refresh-dependencies`.
   - Copy the `.gradle` cache folder to the offline machine.
2. Ensure `gradle-wrapper.properties` points to a **local Gradle distribution**:
   ```properties
   distributionUrl=file:///C:/path/to/gradle-8.4-all.zip
   ```
   (instead of `https://services.gradle.org/...`).
3. Verify Java version: Android Gradle Plugin 8.1.2 requires **Java 11+**.

---

👉 ....

---
