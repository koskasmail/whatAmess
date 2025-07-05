<a name="topage"></a>

# bash_or_csh.md


To find out whether you're using **Bash** or **C shell (csh)** in Ubuntu (or any Linux system), here are a few quick ways to check:

---

### 🧪 **Check Your Current Shell**
Run this command in your terminal:
```bash
echo $0
```
- If it returns something like `bash`, you're using **Bash**.
- If it returns `csh` or `tcsh`, you're using **C shell**.

---

### 🔍 **Check the Default Shell for Your User**
```bash
echo $SHELL
```


---

### 🧠 **Use the `ps` Command**
```bash
ps -p $$
```
This shows the process name of your current shell. Look at the `CMD` column in the output.

---

### 🧭 **Check Installed Shells**
```bash
cat /etc/shells
```
This lists all valid login shells installed on your system.

---

If you want to switch between shells, you can type `bash` or `csh` in the terminal to launch that shell. To make one your default, use:
```bash
chsh -s /bin/bash
```
or
```bash
chsh -s /bin/csh
```


---

##### online cheat sheet
* www

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
