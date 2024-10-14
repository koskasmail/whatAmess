If you're looking to manage browser cache or handle caching in a web application using Java, you typically do this on the server side. Here are some ways you can control caching with Java, especially when using Java-based web frameworks like Spring or servlets:

### 1. Using Servlet Filters
You can create a filter to set caching headers for your responses.

```java
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CacheControlFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
```

### 2. Setting Cache Headers in Spring Controllers
In a Spring Boot application, you can use the `@RequestMapping` annotation to set cache headers directly in your controllers.

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/my-resource")
    public ResponseEntity<String> getResource() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        return ResponseEntity.ok().headers(headers).body("Your resource data");
    }
}
```

### 3. Using Java Web Frameworks (e.g., JSF, Spring MVC)
Frameworks like JSF and Spring MVC have built-in support for managing caching through annotations or configuration.

#### Spring MVC Example
```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    @RequestMapping("/data")
    @ResponseBody
    public String getData(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        return "Fresh data";
    }
}
```

### 4. Managing Static Resources
For static resources (like CSS and JS files), you can configure your web server (like Apache or Nginx) to set appropriate caching headers.

### 5. Cleaning Server Cache
If you're using caching libraries like Ehcache or Guava, you can clear the cache programmatically:

```java
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

Cache<String, Object> cache = Caffeine.newBuilder().build();
// Clear the entire cache
cache.invalidateAll();
```

### Summary
In Java web applications, you manage cache primarily through HTTP headers, filters, and response configurations. These strategies ensure that clients receive the latest content without relying on stale cache entries.
