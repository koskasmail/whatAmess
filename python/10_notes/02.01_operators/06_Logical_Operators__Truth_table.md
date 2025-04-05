<a name="topage"></a>

07_Logical_Operators__Truth_table


#### `and` Logical_Operators

| a | b | a and b | Note | 
| :-: | :-: | :-: | :-: | 
| False | False | False |  | 
| False | True | False |  | 
| True | False | False |  | 
| True | True | True |  | 

#### `or` Logical_Operators

| a | b | a or b | Note | 
| :-: | :-: | :-: | :-: | 
| False | False | False |  | 
| False | True | True |  | 
| True | False | True |  | 
| True | True | True |  | 


#### `not` Logical_Operators

| a | not a | Note | 
| :-: | :-: | :-: |
| False | True |  |
| True | False |  |

----


#### example
```
b1 = 2
b2 = 5

b3 = (b1 * b2) > (b1 + b2)
print(f"b3 = {b3}")
```

#### example

```
number = 4;
result = number > 0 and number % 2 == 0
print(f"number = {number} and the result is ==> {result}");
```


#### example
```
number = 4;
result = number > 0 and number % 2 == 0
print(f"number = {number} and the result is ==> {result}");
```

#### example

```
age = 20;
has_license = "true"
has_insurance = "true"
result = age >= 18 and has_license and has_insurance 
print(result);
```

#### example
```
b1 = True;
b2 = True;
b3 = False;

b4 = b1 and b2 and (not b3)
print(f"b4 = {b4}")
```



----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
