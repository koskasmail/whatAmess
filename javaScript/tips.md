<a name="topage"></a>

# tips

```
https://www.tiktok.com/@cuteamna_67/photo/7409388508269300999?_d=secCgYIASAHKAESPgo8k6hjS0cpgdaEKGTIjTVWLBH4ykuGQruM6%2FiexJMFmmzipvAfgY1dx4eiBVtxChBKNmq3NNG%2BW4z06gH2GgA%3D&_r=1&aweme_type=150&checksum=30359482a0c7504139f7759ad0af3fe3a79a8f6a6e75305b71765b08c3587919&link_reflow_popup_iteration_sharer=%7B%22click_empty_to_play%22%3A0%2C%22dynamic_cover%22%3A1%2C%22follow_to_play_duration%22%3A-1.0%2C%22profile_clickable%22%3A1%7D&pic_cnt=8&preview_pb=0&sec_user_id=MS4wLjABAAAAFTAzt3hAZrL9UKI2hsK4vH5l52TvbabCGSMwYOtkK6v6SeUrIwnwbzh9sWhERWAu&share_app_id=1233&share_item_id=7409388508269300999&share_link_id=ac3236d5-cab4-4bc5-aa5a-3376eba3f37b&sharer_language=en&social_share_type=14&source=h5_m&timestamp=1725219144&u_code=d9f5bk7a34305m&ug_btm=b2001&ug_photo_idx=2&ugbiz_name=UNKNOWN&user_id=6764225008220619781&utm_campaign=client_share&utm_medium=android&utm_source=whatsapp
```

#### tip #1 Array loops:

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

#### tip #2: conditional statement

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

#### tip #3: objects

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

#### tip #4: Array manipulation

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

#### tip #5: Null / Undefined

```
var user;
if (user !== null && user !== undefined && user.name !== undefined) {
    console.log(user.name);
}

var user2;
var result = user2?.name;
console.log(result);
```

#### tip #6:
```

```

#### tip #7:
```
```


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
