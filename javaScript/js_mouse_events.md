<a name="topage"></a>

# js_mouse_events

## 📜 js mouse events

#### **00.** html
#### **01.** click
#### **02.** mousedown
#### **03.** mouseup
#### **04.** mousemove
#### **05.** mouseover
#### **06.** mouseout
#### **07.** mouseenter
#### **08.** mouseleave

-----

#### ✏️ 00. html 

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
        <span><button id="mousemove_event">mousemove</button></span>
        <span><button id="mouseover_event">mouseover</button></span>
        <span><button id="mouseout_event">mouseout</button></span>
        <span><button id="mouseenter_event">mouseenter</button></span>
        <span><button id="mouseleave_event">mouseleave</button></span>
    </div>
</body>
</html>
```


#### ✏️ 01. click

```Js
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

```Js
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

```Js
const link_mouseup_event = document.getElementById("mouseup_event");
link_mouseup_event.addEventListener('mouseup',fn_link_mouseup_event);

function fn_link_mouseup_event () {
    console.log("'mouseup' button was clicked");
}
```

##### 🔥 output

```
'mouseup' button was clicked
```

-----

#### ✏️ 04. mousemove

```Js
const link_mousemove_event = document.getElementById("mousemove_event");
link_mousemove_event.addEventListener('mousemove',fn_link_mousemove_event);

function fn_link_mousemove_event () {
    console.log("'mousemove' button was clicked");
}
```

##### 🔥 output

```
'mousemove' button was clicked
```

-----

#### ✏️ 05. mouseover

```Js
const link_mouseover_event = document.getElementById("mouseover_event");
link_mouseover_event.addEventListener('mouseover',fn_link_mouseover_event);

function fn_link_mouseover_event () {
    console.log("'mouseover' button event");
}

```

##### 🔥 output

```
'mouseover' button event
```

-----

#### ✏️ 06. mouseout

```
// mouseout
const link_mouseout_event = document.getElementById("mouseout_event");
link_mouseout_event.addEventListener('mouseout',fn_link_mouseout_event);

function fn_link_mouseout_event () {
    console.log("'mouseout' button event");
}
```

##### 🔥 output

```
'mouseout' button event
```

-----
#### ✏️ 07. mouseenter

```
// mouseenter
const link_mouseenter_event = document.getElementById("mouseenter_event");
link_mouseenter_event.addEventListener('mouseenter',fn_link_mouseenter_event);

function fn_link_mouseenter_event () {
    console.log("'mouseenter' button event");
}
```

##### 🔥 output

```
'mouseenter' button event
```

-----

#### ✏️ 08. mouseleave

```
// mouseleave
const link_mouseleave_event = document.getElementById("mouseleave_event");
link_mouseleave_event.addEventListener('mouseleave',fn_link_mouseleave_event);

function fn_link_mouseleave_event () {
    console.log("'mouseleave' button event");
}
```

##### 🔥 output

```
'mouseleave' button event
```

-----


<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
