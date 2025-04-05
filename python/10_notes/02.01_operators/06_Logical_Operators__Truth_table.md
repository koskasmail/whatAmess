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

#### example

```
number = 15

result1 = not (number >= 1 and number <= 10)
result2 = (not number >= 1) or (not number <= 10)

print(result1)  # True
print(result2)  # True
```

#### example

```
b1 = True;
b2 = True;
b3 = False;

b4 = b1 and b2 and (not b3)
print(f"b4 = {b4}") # b4 = true
```

#### example

```
# These two expressions are equivalent:
result1 = not (is_student or is_employed) # result1 = not(false or False) = True
print(result1)  # True

result2 = (not is_student) and (not is_employed) # result2 =  (not false) and (not false) = true
print(result2)  # True
```

#### example

```
has_license = True;
has_space = True;
has_experience = False;

can_sell_regular_pet = (has_license == True or has_experience == True) and (has_space == True);
can_sell_exotic_pet = (has_license == True and has_experience == True and has_space == True);
cannot_sell_any_pet = (has_license == False and has_experience == False and has_space == False);

print("Can sell regular pet:", can_sell_regular_pet)
print("Can sell exotic pet:", can_sell_exotic_pet)
print("Cannot sell any pet:", cannot_sell_any_pet)
```

#### example

```
```


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
