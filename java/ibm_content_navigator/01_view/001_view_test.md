


```
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.integration.ApplicationException;
import com.filenet.api.integration.DataAccess;
import com.filenet.api.property.Properties;

public class CustomViewerServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Get the document ID from the request
            String documentId = request.getParameter("documentId");

            // Get the ObjectStore instance
            ObjectStore objectStore = DataAccess.getObjectStore(request);

            // Retrieve the document
            Document document = Factory.Document.fetchInstance(objectStore, documentId, null);

            // Get the document properties
            Properties properties = document.getProperties();

            // Get the desired property value (e.g., file name)
            String fileName = properties.getStringValue("FileName");

            // Generate the URL for the custom viewer
            String viewerUrl = "http://your-viewer-server/viewer?fileName=" + fileName;

            // Redirect to the custom viewer
            response.sendRedirect(viewerUrl);

        } catch (ApplicationException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }
}
```
