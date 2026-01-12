
# fastapi


### Table_of_Content

#### subject
* [online](#online)
* [offline](#offline)

----

### online

The error `ModuleNotFoundError: No module named 'fastapi'` simply means that the FastAPI library is not installed in the Python environment you are currently using.

Since you are on **Ubuntu**, the best way to fix this is to install it using `pip`. However, it is highly recommended to do this within a **virtual environment** to avoid messing up your system-wide Python installation.

---

### Step 1: Install Python and Pip (If missing)

First, ensure your system package list is updated and you have `pip` and the virtual environment tool installed:

```bash
sudo apt update
sudo apt install python3-pip python3-venv

```

### Step 2: Set Up a Virtual Environment (Recommended)

This creates an isolated "bubble" for your project so its libraries don't conflict with other projects.

1. **Navigate** to your project folder:
```bash
cd /home/yaron/git/python/services/

```


2. **Create** the environment:
```bash
python3 -m venv venv

```


3. **Activate** it:
```bash
source venv/bin/activate

```


*(Your terminal prompt should now show `(venv)` at the beginning.)*

### Step 3: Install FastAPI

Now that you are inside your environment, install FastAPI. It is usually installed with the `[standard]` flag to include a high-performance server called **Uvicorn**.

```bash
pip install "fastapi[standard]"

```

---

### How to Run Your Script

Once installed, you can run your FastAPI application. If your file `01.py` contains an app instance like `app = FastAPI()`, you typically run it using the FastAPI CLI:

```bash
fastapi dev 01.py

```

Or via Uvicorn directly:

```bash
uvicorn 01:app --reload

```

### Quick Fix (No Virtual Environment)

If you want to skip the virtual environment and install it directly to your user account (not recommended for long-term projects), just run:

```bash
python3 -m pip install fastapi

```

**Would you like me to provide a basic "Hello World" code snippet for your `01.py` file to test if the installation worked?**

* [Top](#Table_of_Content)

------

### offline

To install FastAPI on an offline computer (Windows or Ubuntu), you cannot just download the `fastapi` file alone. FastAPI has several "dependencies" (other libraries it needs to run) that must also be transferred.

The most reliable way is to use a second computer that **has internet** to download all the necessary files, then move them via USB to the offline machine.

---

### Step 1: On the Computer WITH Internet

You need to download the `.whl` (Wheel) files for FastAPI and all its requirements.

1. Open your terminal (Ubuntu) or Command Prompt (Windows).
2. Create a folder to hold the files:
```bash
mkdir fastapi_offline && cd fastapi_offline

```


3. **Download** the packages (do not install them):
```bash
pip download fastapi uvicorn

```


*Note: If you want the full standard installation, use `pip download "fastapi[standard]"`.*
4. **Copy** this entire `fastapi_offline` folder to a USB drive.

---

### Step 2: On the OFFLINE Computer

Move the folder from the USB to your offline machine (e.g., to your home directory or `C:\`).

1. Open your terminal/command prompt and navigate into that folder:
```bash
cd path/to/fastapi_offline

```


2. **Install** using the local files only:
```bash
pip install --no-index --find-links . fastapi uvicorn

```


* `--no-index`: Tells pip not to look at the online PyPI repository.
* `--find-links .`: Tells pip to look for the installation files in the current directory (`.`).



---

### Important Considerations

* **Python Versions:** The online and offline computers should ideally have the same version of Python (e.g., both 3.10 or both 3.12). If the online computer is Windows and the offline one is Ubuntu, use this command on the online computer to force it to download Linux-compatible files:
```bash
pip download --platform manylinux2014_x86_64 --only-binary=:all: fastapi uvicorn

```


* **Documentation:** FastAPI's interactive docs (Swagger UI) normally load from a CDN (internet). If you are strictly offline, the `/docs` page may appear blank. You can solve this by installing the `fastapi-offline` package (repeat the steps above for that package) and changing your code from `from fastapi import FastAPI` to `from fastapi_offline import FastAPIOffline as FastAPI`.

**Would you like me to show you how to check your Python version on both machines to make sure they match?**

* [Top](#Table_of_Content)


----

<p align="right">(<a href="#fastapi">back to top</a>)</p>
<br/>
<br/>
