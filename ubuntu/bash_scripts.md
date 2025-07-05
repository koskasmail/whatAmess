<a name="topage"></a>

# bash_scripts.md


```bash
#!/bin/bash

echo "===== System Health Report ====="
echo "Hostname: $(hostname)"
echo "Date: $(date)"
echo "Uptime: $(uptime -p)"
echo

echo "🧠 Memory Usage:"
free -h
echo

echo "💾 Disk Usage:"
df -h --total | grep -v tmpfs
echo

echo "📡 Network Interfaces:"
ip -brief address
echo

echo "🌐 Internet Connectivity:"
ping -c 2 8.8.8.8 >/dev/null && echo "Internet: Connected" || echo "Internet: Disconnected"
echo

echo "🔥 CPU Load:"
uptime | awk -F'load average:' '{ print "Load Average:" $2 }'
echo

echo "🔍 Top 5 Memory-Hungry Processes:"
ps -eo pid,comm,%mem --sort=-%mem | head -n 6
echo

echo "🔍 Top 5 CPU-Hungry Processes:"
ps -eo pid,comm,%cpu --sort=-%cpu | head -n 6
echo

echo "🛠️ Failed Services:"
systemctl --failed --no-pager
echo "===== End of Report ====="
```

---

##### online cheat sheet
* www


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
