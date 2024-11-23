<a name="topage"></a>

# js_mouse_events

## 📜 js mouse events

#### **01**. click
#### **02**. mousedown
#### **03**. mouseup
#### **04**. mousemove
#### **05**. mouseover
#### **06**. mouseout
#### **07**. mouseenter
#### **08**. mouseleave

-----

#### html 

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>mouse events</title>
</head>
<body>
    <div>Button Mouse events</div>
    <div>
        <span><button id="click_event">Click</button></span>
        <span><button id="mousedown_event">mousedown</button></span>
        <span><button id="mouseup_event">mouseup</button></span>
    </div>

    <script src="script.js"></script>
</body>
</html>
```


#### ✏️ 01. click

```
const link_click_event = document.getElementById("click_event");
link_click_event.addEventListener('click',fn_click_event);


function fn_click_event () {
    console.log("'Click' button was clicked");
}
```

##### 🔥 output

```
'Click' button was clicked
```

-----

#### ✏️ 02. mousedown

```
const link_mousedown_event = document.getElementById("mousedown_event");
link_mousedown_event.addEventListener('mousedown',fn_link_mousedown_event);


function fn_link_mousedown_event () {
    console.log("'mousedown' button was clicked");
}
```

##### 🔥 output

```
'mousedown' button was clicked
```

-----

#### ✏️ 03. mouseup

```
const link_mouseup_event = document.getElementById("mouseup_event");
link_mouseup_event.addEventListener('mouseup',fn_link_mouseup_event);


function fn_link_mouseup_event () {
    console.log("'mouseup' button was clicked");
}
```

##### 🔥 output

```
```

-----

#### ✏️ 04. mousemove

```
```

##### 🔥 output

```
```

-----

#### ✏️ 05. mouseover

```
```

##### 🔥 output

```
```

-----

#### ✏️ 06. mouseout

```
```

##### 🔥 output

```
```

-----
#### ✏️ 07. mouseenter

```
```

##### 🔥 output

```
```

-----


#### ✏️ 08. mouseleave

```
```

##### 🔥 output

```
```

-----


<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
