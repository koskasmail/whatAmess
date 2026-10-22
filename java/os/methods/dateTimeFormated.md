
# date Time Formmated

#### formated 

##### code
```
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public static String getCurrentDateTime() {
    return LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss.SSS")
    );
}
```

##### Example output:
```
20261022T10:55:23.456
```
