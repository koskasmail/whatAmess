<a name="topage"></a>

# tips_01_two_ways

## 📜 content of element
#### **01.** Array loop
#### **02.** conditional statement
#### **03.** objects
#### **04.** Array manipulation
#### **05.** Null / Undefined
#### **06.** default function parameters
#### **07.** Asynchronous code

-----

#### ✏️ 01. Array loops
```
const MyArray = [];
MyArray.push(10);
MyArray.push(20,30);
MyArray.push(40,50, 60);

console.log("///----[for() loop]----///");
for (var i=0; i < MyArray.length; i++) {
    console.log(MyArray[i]);
}

console.log("///----[forEach() loop]----///");
MyArray.forEach(item => console.log(item));
```

-----

#### ✏️ 02. conditional statement

```
var personal = {
    name: "jaron",
    age: 51
};

console.log("///----[if..else statement]----///");
if ( personal.age > 21 ) {
    console.log("young");
}
else {
    console.log("old");
}

console.log("///----[Ternary Operator - shorthand statement]----///");
console.log(personal.age > 21 ? "young":"old");
```

-----

#### ✏️ 03. objects

```
var personal = {
    name: "jaron",
    age: 51
};

///----[user array "personal" to call the inner variables "name" and "age"]----///
console.log(personal.name);
console.log(personal.age);

///----[copy array "personal" to a variables "name" and "age"]----///
const { name, age} = personal;
console.log(name, age);
```

-----

#### ✏️ 04. Array manipulation

```
var arrNumbers = [2,4,6];
var arrCalcResult = [];
var x = 2;

///----[Array]----///
for (var index=0; index < arrNumbers.length; index++) {
    arrCalcResult.push(arrNumbers[index] * x);
}

console.error(arrNumbers); 
console.error(arrCalcResult); 


///----[map - creates a new array]----///
const arrCalc = arrNumbers.map(number => number * x) 
console.error(arrNumbers); 
console.error(arrCalc); 
```

-----

#### ✏️ 05. Null / Undefined

```
var user;
if (user !== null && user !== undefined && user.name !== undefined) {
    console.log(user.name);
}

var user2;
var result = user2?.name;
console.log(result);
```

-----

#### ✏️ 06. default function parameters

```
function fnGreet(message, name) {
    if (!name) { 
        name = 'guest'
    }
    return message + " " + name;
}

console.log(fnGreet("Hello"));
console.log(fnGreet("Hello","jaron"));

const fnGreet2 = (message, name="guest") => `${message}, ${name}`;
console.log(fnGreet2("Hi",));
console.log(fnGreet2("Hi","jaron"));
```

-----

#### ✏️ 07. Asynchronous code

```
var data = "test";

console.log(data);

function getData(callBack) {
    setTimeout(() => {
        callBack("Data");
    }, 1000);
}

getData(function(data) {
    console.log(data);
});


var data2 = "test2";
const getData2 = () => new  Promise(resolve => setTimeout(() => resolve("data"),1000));
getData2().then(data2 => console.log(data2));
```

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
