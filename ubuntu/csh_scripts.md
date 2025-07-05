<a name="topage"></a>

# csh_scripts.md

```csh
#!/bin/csh

echo "===== System Health Report ====="
echo "Hostname: `hostname`"
echo "Date: `date`"
echo "Uptime: `uptime | sed 's/.*up \\([^,]*\\), .*/\\1/'`"
echo

echo "🧠 Memory Usage:"
free -h
echo

echo "💾 Disk Usage:"
df -h | grep -v tmpfs
echo

echo "📡 Network Interfaces:"
ifconfig | grep -E '^[a-z]|inet '
echo

echo "🌐 Internet Connectivity:"
ping -c 2 8.8.8.8 > /dev/null
if ($status == 0) then
    echo "Internet: Connected"
else
    echo "Internet: Disconnected"
endif
echo

echo "🔥 CPU Load:"
uptime | awk -F'load average:' '{ print "Load Average:" $2 }'
echo

echo "🔍 Top 5 Memory-Hungry Processes:"
ps aux | sort -nk4 | tail -5
echo

echo "🔍 Top 5 CPU-Hungry Processes:"
ps aux | sort -nk3 | tail -5
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
