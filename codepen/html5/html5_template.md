
# html5_template

#### html
```
<header>header</header>
<nav>nav</nav>
<section>section</section>
<aside>aside</aside>
<footer>footer</footer>
```

#### css
```
body { 
	font-family: segoe-ui_normal,Segoe UI,Segoe,Segoe WP,Helvetica Neue,Helvetica,sans-serif;
	display: grid;
	grid-template-areas: 
    "header header header"
    "nav section aside"
    "footer footer footer";
	grid-template-rows: 80px 1fr 50px;
	grid-template-columns: 15% 1fr 18%;
	grid-gap: 5px;
  height: 100vh;
  margin: 10px;  
}

header {
	background: #707070;
	grid-area: header;
}

nav {
	background: #C9BFBF;
	grid-area: nav; 
}

section {
	background: #ABABAB;
	grid-area: section;    
}

aside {
	background: #C9C9C9;
	grid-area: aside; 
}

footer {
	background: #707070;
	grid-area: footer;
}

header, nav, section, aside, footer {
	padding: 5px;
}
```
