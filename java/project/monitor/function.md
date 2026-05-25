#### menu
* [x] 01. MonitorMain
* [x] 02. General
* [x] 03. Ip4
* [x] 04. HostName
* [x] 05. NetworkInfo
* [x] 06. NetworkBasic
* [ ] 07. NetworkInformation
* [ ] ccc

#### 01. class: MonitorMain

```java
package monitor;

import java.net.SocketException;

public class MonitorMain {

	public MonitorMain() {
		System.out.println("MonitorMain....");
		General g = new General();		
		HostName hn = new HostName();
		
		/* ip address (ip4) */
		Ip4 ip4 = new Ip4(); 
		
		/* network Information -  information */
		NetworkInfo ni = new NetworkInfo();		
		
		/* network Information - basic information */
		try {
			NetworkBasic nw = new NetworkBasic();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-=-=-=-=-=-");
		WindowsIpConfiguration wic = new WindowsIpConfiguration();
		
		System.out.println("-=-=-=-=-=-");
		Hd hd = new Hd();

		System.out.println("-=-=-=-=-=-");
		NetworkInformation nwi = new NetworkInformation();
	}

	public static void main(String[] args) {
		System.out.println("Main....");
		MonitorMain mm = new MonitorMain();	
	}
}
```

----

#### 02. class: General

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

----

#### 03. class: Ip4

```
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

----

#### 04. class: HostName

```
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

----

#### 05. class: NetworkInfo

```
package monitor;

import java.net.*;
import java.util.*;

public class NetworkInfo {

	public NetworkInfo() {
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (interfaces.hasMoreElements()) {
			NetworkInterface ni = interfaces.nextElement();

			System.out.println("Interface: " + ni.getName());
			System.out.println("Display Name: " + ni.getDisplayName());
			try {
				System.out.println("MAC: " + Arrays.toString(ni.getHardwareAddress()));
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				System.out.println("Address: " + addr.getHostAddress());
			}

			System.out.println("--------------------------------");
		}
	}
}

```

----

#### 06. NetworkBasic

```
package monitor;

import java.net.*;
import java.util.*;

public class NetworkBasic {

	public NetworkBasic() throws SocketException {
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (interfaces.hasMoreElements()) {
			NetworkInterface ni = interfaces.nextElement();

			// Skip loopback, virtual, or down interfaces if needed
			if (!ni.isUp() || ni.isLoopback()) {
				continue;
			}

			byte[] mac = ni.getHardwareAddress();

			// Skip printing MAC if null or empty
			if (mac == null || mac.length == 0) {
				continue;
			}

			System.out.println("Interface: " + ni.getName());
			System.out.println("Display Name: " + ni.getDisplayName());

			// Format MAC address properly (AA-BB-CC-DD-EE-FF)
			StringBuilder macStr = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				macStr.append(String.format("%02X", mac[i]));
				if (i < mac.length - 1)
					macStr.append("-");
			}
			System.out.println("MAC: " + macStr);

			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				System.out.println("Address: " + addr.getHostAddress());
			}

			System.out.println("--------------------------------");
		}
	}
}
```

----

#### 07. class: NetworkInformation

```
package monitor;

import java.net.*;
import java.util.*;
import java.io.*;

public class NetworkInformation {

   	public NetworkInformation() throws Exception {
        showHostInfo();
        showNetworkInterfaces();
        /////showDNSInfo();
        /////showSystemInfo();
    }

    // ---------------- HOST INFO ----------------
    private static void showHostInfo() throws Exception {
        System.out.println("=== HOST INFORMATION ===");
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("Host Name: " + localHost.getHostName());
        System.out.println("Host Address: " + localHost.getHostAddress());
        System.out.println();
    }

    // ---------------- NETWORK INTERFACES ----------------
    private static void showNetworkInterfaces() throws Exception {
        System.out.println("=== NETWORK INTERFACES ===");

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        while (nets.hasMoreElements()) {
            NetworkInterface net = nets.nextElement();

            System.out.println("Interface: " + net.getName());
            System.out.println("Display Name: " + net.getDisplayName());
            System.out.println("Up: " + net.isUp());
            System.out.println("Loopback: " + net.isLoopback());
            System.out.println("Virtual: " + net.isVirtual());
            System.out.println("MTU: " + net.getMTU());

            // MAC Address
            byte[] mac = net.getHardwareAddress();
            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (byte b : mac) sb.append(String.format("%02X-", b));
                System.out.println("MAC Address: " + sb.substring(0, sb.length() - 1));
            }

            // IP Addresses
            Enumeration<InetAddress> addrs = net.getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress addr = addrs.nextElement();
                if (addr instanceof Inet4Address)
                    System.out.println("IPv4: " + addr.getHostAddress());
                else if (addr instanceof Inet6Address)
                    System.out.println("IPv6: " + addr.getHostAddress());
            }

            System.out.println();
        }
    }

    // ---------------- DNS SERVERS ----------------
    private static void showDNSInfo() throws Exception {
        System.out.println("=== DNS SERVERS ===");

        // Windows
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            runCommand("ipconfig /all");
        }
        // Linux / Mac
        else {
            runCommand("cat /etc/resolv.conf");
        }

        System.out.println();
    }

    // ---------------- SYSTEM INFO ----------------
    private static void showSystemInfo() {
        System.out.println("=== SYSTEM INFORMATION ===");
        Properties props = System.getProperties();
        props.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println();
    }

    // ---------------- COMMAND EXECUTOR ----------------
    private static void runCommand(String cmd) throws Exception {
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("dns") || line.toLowerCase().contains("wins"))
                System.out.println(line.trim());
        }
    }
}

```

----
