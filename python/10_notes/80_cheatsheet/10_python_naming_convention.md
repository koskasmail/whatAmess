<a name="topage"></a>

# 10_python_naming_convention

#### 📘 Quick Java Summary Table


| Convention | Few Words | Used For | Example |
|-----------|-----------|----------|---------|
| PascalCase | Capital Each Word | Classes | `UserHandler` | 
| UPPERCASE | ALL CAPS / all UPERCASE | Constants | `MAX_USERS` | 
| lowercase | all lower case package | package | `bank`, `credit card` |
| snake_case | lower_case_words | variables, functions, Methods, Modules | `user_id` | 
| members | private | _name |
| magic | methods | __name__() | 

-----

#### 📘 a complete list of the most common naming conventions:

| Item                                      | Convention                                  | Example                                |
| ----------------------------------------- | ------------------------------------------- | -------------------------------------- |
| **Variables**                             | `lowercase_with_underscores` (snake_case)   | `user_name`, `total_price`             |
| **Functions**                             | `lowercase_with_underscores` (snake_case)   | `calculate_total()`, `get_age()`       |
| **Methods**                               | `lowercase_with_underscores`                | `save_file()`, `print_info()`          |
| **Classes**                               | `CapWords` (PascalCase)                     | `Student`, `BankAccount`               |
| **Exceptions**                            | `CapWords` ending with `Error` (usually)    | `FileNotFoundError`, `MyCustomError`   |
| **Modules (files)**                       | `lowercase` or `lowercase_with_underscores` | `math.py`, `student_records.py`        |
| **Packages (folders)**                    | `lowercase`                                 | `utils`, `models`                      |
| **Constants**                             | `UPPERCASE_WITH_UNDERSCORES`                | `PI`, `MAX_SIZE`, `DEFAULT_PORT`       |
| **Private variables/functions**           | Prefix with one underscore `_`              | `_password`, `_helper()`               |
| **Name mangling (private class members)** | Prefix with two underscores `__`            | `__balance`                            |
| **Magic (dunder) methods**                | Double underscores before and after         | `__init__()`, `__str__()`, `__len__()` |
| **Type variables (typing)**               | `CapWords` (often short)                    | `T`, `UserType`                        |
| **Generic constants (enum members)**      | `UPPERCASE_WITH_UNDERSCORES`                | `Color.RED`, `Status.ACTIVE`           |

-----

#### Quick examples

```python
# Constant
MAX_SPEED = 120

# Variable
student_name = "Alice"

# Function
def calculate_area(radius):
    return 3.14 * radius ** 2

# Class
class BankAccount:
    pass

# Private method
def _helper():
    pass

# Magic method
class Person:
    def __init__(self, name):
        self.name = name
```

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
