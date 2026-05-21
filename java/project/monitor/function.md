

#### class: General

* generalInformation()
    * get information
        * java
        * os
        * user
* printArr - print array.

``` java
import java.util.ArrayList;

public class General {

	///----[onload]----///
	public General() {
		ArrayList<String> result = new ArrayList<>();
		result.clear();
		
		System.out.println("General()");
		result = gen();
		
		printArr(result);
	}
	
	private void printArr(ArrayList<String> result) {
		System.out.println("printArr() >>> ");
		
		int max = result.size();
		
		for (int x=0; x<max; x++) {
			System.out.println(result.get(x));
		}
		
	}

	public ArrayList<String> generalInformation() {
		
		ArrayList<String> gl = new ArrayList<>();
		gl.clear();
		
		gl.add(System.getProperty("java.version"));
		gl.add(System.getProperty("java.home"));
		gl.add(System.getProperty("java.vendor"));
		gl.add(System.getProperty("java.vendor.url"));
		gl.add(System.getProperty("os.arch"));
		gl.add(System.getProperty("os.name"));
		gl.add(System.getProperty("os.version"));
		gl.add(System.getProperty("user.name"));
		gl.add(System.getProperty("user.dir"));
		gl.add("-----");
		
		return gl;
	}	
}
```

```
	public static void getDiskFree(PluginLogger logger) {
        File file = new File("/");
        long freeSpace = file.getFreeSpace();
        long totalSpace = file.getTotalSpace();
        double freeSpaceGB = (double) freeSpace / (1024 * 1024 * 1024); // Convert to GB
        double totalSpaceGB = (double) totalSpace / (1024 * 1024 * 1024); // Convert to GB
        double freeSpacePercentage = (freeSpaceGB / totalSpaceGB) * 100;
        
        JSONObject o1 = new JSONObject();
        o1.put("freeSpaceGB", freeSpaceGB);
        
        JSONObject o2 = new JSONObject();
        o2.put("totalSpaceGB", totalSpaceGB);
        
        JSONObject o3 = new JSONObject();
        o3.put("freeSpacePercentage", freeSpacePercentage);
        
        returnValue.add(o1);
        returnValue.add(o2);
        returnValue.add(o3);
        
        System.err.println("Server Hard Drive Free Space: " + freeSpaceGB + " GB");
        System.err.println("Server Hard Drive Total Space: " + totalSpaceGB + " GB");
        System.err.println("Server Hard Drive Free Space Percentage: " + freeSpacePercentage + "%");
	}
```

#### class: MonitorMain
```java
package monitor;

public class MonitorMain {

	public MonitorMain() {
		System.out.println("MonitorMain....");
		General g = new General();		
		HostName hn = new HostName();
		Ip4 ip4 = new Ip4();
		
	}

	public static void main(String[] args) {
		System.out.println("Main....");
		MonitorMain mm = new MonitorMain();	
	}
}

```

#### class: HostName
```java
package monitor;

import java.net.InetAddress;

public class HostName {

	///----[stHostName]----///
	private String stHostName = "";

	///----[getHostName]----///
	public String getHostName() {
		return stHostName;
	}

	///----[setHostName]----///
	public void setHostName(String hostName) {
		this.stHostName = hostName;
	}

	///----[hostNameDetect - detect host name ]----///
	private void hostNameDetect() {
		try {
			setHostName(InetAddress.getLocalHost().getHostName());
		} catch (Exception e) {
			setHostName("x");
		}
	}

	///----[class Constructor - onload]----///
	public HostName() {
		hostNameDetect();
		System.out.println("Server Host Name: " + getHostName());
	}
	
}
```

#### class: Ip4

```java
package monitor;

import java.net.InetAddress;

public class Ip4 {

	private String ip4Address = "";

	public String getIp4Address() {
		return ip4Address;
	}

	public void setIp4Address(String ip4Address) {
		this.ip4Address = ip4Address;
	}

	private void ip4Detect() {

	}

	public Ip4() {
		try {
			setIp4Address(InetAddress.getLocalHost().getHostAddress());
			System.err.println("Server IP Address: " + getIp4Address());
//			System.out.println(" " + InetAddress.get)
		} catch (Exception e) {
			System.err.println("Server IP Address: xxx.xxx.xxx");
			e.printStackTrace();
		}
	}
}
```

```	
		public static void getIpAddress(PluginLogger logger) {
	        try {
	            String ipAddress = InetAddress.getLocalHost().getHostAddress();
	            System.err.println("Server IP Address: " + ipAddress);
	            JSONObject sip = new JSONObject();
	            sip.put("ipAddress", ipAddress);
	            returnValue.add(sip);
	        } catch (Exception e) {
	       	 	System.err.println("Server IP Address: xxx.xxx.xxx");
	            JSONObject esip = new JSONObject();
		        esip.put("ipAddress", "xxx.xxx.xxx");
		        returnValue.add(esip);
	            e.printStackTrace();
	        }
		}
```
		
```		
		public static void memStatus (PluginLogger logger) {
			
	        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	        // Get the total physical memory size in bytes
	        long totalMemoryBytes = osBean.getTotalPhysicalMemorySize();
	        long totalMemoryMB = totalMemoryBytes / (1024 * 1024);

	        // Get the free physical memory size in bytes
	        long freeMemoryBytes = osBean.getFreePhysicalMemorySize();
	        long freeMemoryMB = freeMemoryBytes / (1024 * 1024);

	        // Calculate the used physical memory size in bytes
	        long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;
	        long usedMemoryMB = usedMemoryBytes / (1024 * 1024);

	        // Calculate the memory usage percentage
	        double memoryUsagePercentage = (usedMemoryBytes / (double) totalMemoryBytes) * 100;

	        JSONObject o21 = new JSONObject();
	        o21.put("totalMemoryMB", totalMemoryMB);
	        
	        JSONObject o22 = new JSONObject();
	        o22.put("freeMemoryMB", freeMemoryMB);
	        
	        JSONObject o23 = new JSONObject();
	        o23.put("usedMemoryMB", usedMemoryMB);
	        
	        JSONObject o24 = new JSONObject();
	        o24.put("memoryUsagePercentage", memoryUsagePercentage);
	        
	        
	        returnValue.add(o21);
	        returnValue.add(o22);
	        returnValue.add(o23);
	        returnValue.add(o24);
	        
//	        // Print the memory status
//	        System.out.println("Total Memory: " + totalMemoryMB + " MB");
//	        System.out.println("Free Memory: " + freeMemoryMB + " MB");
//	        System.out.println("Used Memory: " + usedMemoryMB + " MB");
//	        System.out.println("Memory Usage: " + memoryUsagePercentage + "%");
	    }
```		

```    
		public static void General(PluginLogger logger) {
			
			String javaVersion = System.getProperty("java.version");
			String javaHome = System.getProperty("java.home");
			String javaVendor = System.getProperty("java.vendor");
			String javaVendorUrl = System.getProperty("java.vendor.url");
			String osArch = System.getProperty("os.arch");
			String osName = System.getProperty("os.name");
			String osVersion = System.getProperty("os.version");
			String userName = System.getProperty("user.name");
			String userHome = System.getProperty("user.home");
			String userDir = System.getProperty("user.dir");
			
			
	        JSONObject o11 = new JSONObject();
	        o11.put("javaVersion", javaVersion);

	        JSONObject o12 = new JSONObject();
	        o12.put("javaHome", javaHome);

	        JSONObject o13 = new JSONObject();
	        o13.put("javaVendor", javaVendor);

	        JSONObject o14 = new JSONObject();
	        o14.put("javaVendorUrl", javaVendorUrl);
	        
	        JSONObject o15 = new JSONObject();
	        o15.put("osArch", osArch);
	        
	        JSONObject o16 = new JSONObject();
	        o16.put("osName", osName);
	        
	        JSONObject o17 = new JSONObject();
	        o17.put("osVersion", osVersion);
	        	   
	        JSONObject o18 = new JSONObject();
	        o18.put("userName", userName);
	        
	        JSONObject o19 = new JSONObject();
	        o19.put("userHome", userHome);
	        
	        JSONObject o20 = new JSONObject();
	        o20.put("userDir", userDir);	        
	        
	        returnValue.add(o11);
	        returnValue.add(o12);
	        returnValue.add(o13);
	        returnValue.add(o14);
	        returnValue.add(o15);
	        returnValue.add(o16);
	        returnValue.add(o17);
	        returnValue.add(o18);
	        returnValue.add(o19);
	        returnValue.add(o20);
		}
```
