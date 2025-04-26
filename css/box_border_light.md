
# box_border_light.md

#### html

```
<svg width="0" height="0" aria-hidden="true">
  <filter id="border-glow" color-interpolation-filters="sRGB" x="-50%" y="-50%" width="200%" height="200%">
    <feComponentTransfer result="box">
      <feFuncA type="table" tableValues="0 0 1"></feFuncA>
    </feComponentTransfer>
    <feComponentTransfer in="SourceGraphic" result="border">
      <feFuncA type="table" tableValues="0 1 0"></feFuncA>
    </feComponentTransfer>
    <feBlend in="box" result="full"></feBlend>
    <feMorphology in="border" operator="dilate" radius="12"></feMorphology>
    <feComposite in2="full" operator="in"></feComposite>
    <feGaussianBlur stdDeviation="12"></feGaussianBlur>
    <feBlend in="full"></feBlend>
  </filter>
  <filter id="grainy-glow" color-interpolation-filters="sRGB" x="-50%" y="-50%" width="200%" height="200%">
    <feComponentTransfer result="box">
      <feFuncA type="table" tableValues="0 0 1"></feFuncA>
    </feComponentTransfer>
    <feComponentTransfer in="SourceGraphic" result="border">
      <feFuncA type="table" tableValues="0 1 0"></feFuncA>
    </feComponentTransfer>
    <feBlend in="box" result="full"></feBlend>
    <feMorphology in="border" operator="dilate" radius="12"></feMorphology>
    <feComposite in2="full" operator="in"></feComposite>
    <feGaussianBlur stdDeviation="12" result="blur"></feGaussianBlur>
    <feTurbulence type="fractalNoise" baseFrequency="7.13"></feTurbulence>
    <feDisplacementMap in="blur" scale="8" yChannelSelector="R"></feDisplacementMap>
    <feBlend in="full"></feBlend>
  </filter>
  <filter id="smooth-glow" color-interpolation-filters="sRGB" x="-50%" y="-50%" width="200%" height="200%">
    <feComponentTransfer>
      <feFuncA type="table" tableValues="0 0 1"></feFuncA>
    </feComponentTransfer>
    <feGaussianBlur stdDeviation="2"></feGaussianBlur>
    <feComponentTransfer>
      <feFuncA type="table" tableValues="-2 3"></feFuncA>
    </feComponentTransfer>
    <feComposite in="SourceGraphic" operator="in" result="box"></feComposite>
    <feComponentTransfer in="SourceGraphic" result="border">
      <feFuncA type="table" tableValues="0 1 0"></feFuncA>
    </feComponentTransfer>
    <feComponentTransfer>
      <feFuncA type="linear" slope="9999"></feFuncA>
    </feComponentTransfer>
    <feGaussianBlur stdDeviation="3"></feGaussianBlur>
    <feComponentTransfer result="round">
      <feFuncA type="table" tableValues="-2 5"></feFuncA>
    </feComponentTransfer>
    <feBlend in="box" result="opaque"></feBlend>
    <feMorphology in="border" operator="dilate" radius="12" result="exp"></feMorphology>
    <feComposite in2="opaque" operator="in"></feComposite>
    <feGaussianBlur stdDeviation="12" result="blur"></feGaussianBlur>
    <feTurbulence type="fractalNoise" baseFrequency="7.13"></feTurbulence>
    <feDisplacementMap in="blur" scale="8" yChannelSelector="R" result="glow"></feDisplacementMap>
    <feComposite in="exp" in2="round" operator="in"></feComposite>
    <feComponentTransfer>
      <feFuncA type="gamma" exponent="1.25"></feFuncA>
    </feComponentTransfer>
    <feBlend in2="glow"></feBlend>
    <feBlend in="box"></feBlend>
  </filter>
</svg>
<header>
  <h1>animated border gradient on scroll into view</h1><em>scroll down to see effects</em>
</header>
<main>
  <div class="card animatable hidden css-pseudo">
    <h2>Option #1</h2>
    <p>Uses pure CSS and needs a pseudo-element. This requires inheriting the backround layer responsible for the border gradient onto the pseudo element that then gets blurred.</p>
  </div>
  <div class="card animatable hidden svg-filter">
    <h2>Option #2</h2>
    <p>Needs no pseudo-elements, but requires an SVG filter. The basic idea is the same as<a href="https://codepen.io/thebabydino/pen/xbKjQMv" target="_blank">here</a>
      - everything within the padding limit is fully opaque, while the border has an alpha in the [0,.5] interval, so the filter can extract the border and the padding-box separately and apply effeccts on just one of them.
    </p>
  </div>
  <div class="card animatable hidden svg-grainy">
    <h2>Option #3</h2>
    <p>Similar to the previous one, only also adding a grain effect to the border glow. This involves using a displacement map generated via a feTurbulence primitive.</p>
  </div>
  <div class="card animatable hidden svg-smooth">
    <h2>Option #4</h2>
    <p>Similar to the previous ones, only also rounding the card box corners and the rotating card border ends.</p>
  </div>
</main>
<footer></footer>
```

-----

#### css

```
@property --ang {
	syntax: "<angle>";
	initial-value: 0deg;
	inherits: true;
}

.card {
	--ani-grad: conic-gradient(
			from var(--ang),
			rgb(from #ffd23f r g b / var(--alpha, 0.5)),
			#0000 40%
		)
		border-box;
	border: solid 6px #0000;
	background: linear-gradient(#312244 0 0) padding-box, var(--ani-grad);
	animation: ang 3s linear infinite;
}

@keyframes ang {
	0% {
		--ang: 1turn;
	}
}

.css-pseudo {
	--alpha: 1;
	position: relative;

	&::before {
		--full-cov: conic-gradient(red 0 0);
		position: absolute;
		inset: -6px;
		border: inherit;
		box-shadow: 0 0 1.5em rgb(0 0 0 / 0.01);
		background: var(--ani-grad);
		filter: blur(0.5em);
		mask: var(--full-cov) no-clip subtract, var(--full-cov) padding-box;
		pointer-events: none;
		content: "";
	}
}

.svg-filter {
	filter: url(#border-glow);
}
.svg-grainy {
	filter: url(#grainy-glow);
}

.svg-smooth {
	filter: url(#smooth-glow);
}

.animatable {
	transition: 1s;
}

.hidden {
	opacity: 0;
	filter: blur(2px);
	translate: -100%;
}

/* layout & prettyfying, not really relevant for demo */
* {
	margin: 0;
}

html,
body,
header,
footer,
main,
div {
	display: grid;
}

body {
	background: #3e1f47;
	color: #fde4cf;
	font: clamp(0.75em, 2.75vw, 1.5em) genos, sans-serif;
}

header,
main,
footer {
	place-items: center;
	min-height: 100vh;
	min-height: 100dvh;
}

header {
	text-align: center;
}

h1 {
	width: Min(100%, 12em);
	color: #0000;
	-webkit-text-stroke: #e9ff70 0.02em;
	font-size: 3.5em;
	line-height: 1;
	text-wrap: balance;
	filter: drop-shadow(1px 1px 2px #000);
}

main {
	grid-gap: 4em;
	padding: 2em;
}

.card {
	box-sizing: border-box;
	place-content: center;
	width: Min(100%, 21em);
	padding: 0.5em;
}

h2 {
	font-family: tangerine;
}

a {
	color: gold;
}
```

-----

#### js

```
const observer = new IntersectionObserver(entries =>{
  entries.forEach(entry => {
      if(entry.isIntersecting)
        entry.target.classList.remove('hidden')
      else entry.target.classList.add('hidden')
  });
});
const hiddenElements = document.querySelectorAll('.animatable')
hiddenElements.forEach((el) => observer.observe(el));
```

-----

#### html

```
```

-----
