
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.ibm.ecm.extension.PluginLogger;
import com.ibm.json.java.JSONObject;

public class Test0031 {

	private static final String POST_URL = "http://ofekbytesRequestAsync?ExportRequestID=2";
	
    public Test0031() {
    	try {
    		submitRequest();		
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR !!!");
		}
  }
    
      
	private static int submitRequest( ) throws IOException {
		
		URL obj = new URL(POST_URL);
		
		HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
		httpURLConnection.setRequestMethod("GET");

		// For POST only - START
		httpURLConnection.setDoOutput(true);
		OutputStream os = httpURLConnection.getOutputStream();
		
		os.flush();
		os.close();

		int responseCode = 0;
		responseCode = httpURLConnection.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		return responseCode;	
	}
}	
