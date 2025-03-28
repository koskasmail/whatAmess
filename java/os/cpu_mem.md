To determine CPU usage, free CPU, and CPU percentage in Java 8, you can use the `com.sun.management.OperatingSystemMXBean` class. Here's an example of how you might implement this:

```java
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class CpuUsageExample {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // CPU usage as a percentage of the available CPU
        double cpuLoad = osBean.getSystemCpuLoad() * 100;

        // Free physical memory in bytes
        long freeMemory = osBean.getFreePhysicalMemorySize();

        // Total physical memory in bytes
        long totalMemory = osBean.getTotalPhysicalMemorySize();

        // Used physical memory in bytes
        long usedMemory = totalMemory - freeMemory;

        System.out.printf("CPU Load: %.2f%%\n", cpuLoad);
        System.out.println("Free Memory: " + freeMemory + " bytes");
        System.out.println("Used Memory: " + usedMemory + " bytes");
    }
}
```

### Key Notes:
1. **System CPU Load**: `getSystemCpuLoad()` gives the system-wide CPU usage as a fraction between 0 and 1. Multiply it by 100 to get the percentage.
2. **Memory**: The `getFreePhysicalMemorySize()` and `getTotalPhysicalMemorySize()` methods return values in bytes, allowing you to calculate memory usage easily.

Make sure to include the `jvm.options` to allow access to the internal MXBean if you encounter any access limitations. Let me know if you'd like further help with this!
