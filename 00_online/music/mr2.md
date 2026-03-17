You’ve got a solid UI already—the main issues are:

1. ❌ You’re not actually playing the selected station (audio is hardcoded)
2. ❌ Stop doesn’t fully reset playback
3. ❌ No JSON-based station data
4. ❌ Buttons + screen don’t show real station info

Let’s fix all of that cleanly.

---

# ✅ 1. Create a JSON file (stations.json)

Put this next to your HTML:

```json
[
  {
    "name": "Rock FM",
    "type": "Music",
    "station": "CH 1",
    "url": "https://www2.cs.uic.edu/~i101/SoundFiles/StarWars60.wav"
  },
  {
    "name": "News Radio",
    "type": "News",
    "station": "CH 2",
    "url": "https://www2.cs.uic.edu/~i101/SoundFiles/CantinaBand60.wav"
  }
]
```

---

# ✅ 2. Replace your JS with THIS (fixed version)

This version:

* Loads JSON
* Creates buttons dynamically
* Plays the correct station
* Stops properly
* Displays full info on screen

```html
<script>
  const buttonsContainer = document.getElementById("buttonsContainer");
  const apiFrame = document.getElementById("apiFrame");
  const playToggle = document.getElementById("playToggle");
  const powerLed = document.getElementById("powerLed");
  const statusText = document.getElementById("statusText");
  const volumeSlider = document.getElementById("volume");
  const radioAudio = document.getElementById("radioAudio");
  const screenHeader = document.getElementById("screenHeader");
  const dialIndicator = document.getElementById("dialIndicator");

  let stations = [];
  let isPlaying = false;
  let currentStationIndex = null;

  // ✅ LOAD JSON
  fetch("stations.json")
    .then(res => res.json())
    .then(data => {
      stations = data;
      createButtons();
    })
    .catch(err => console.error("Error loading JSON:", err));

  function createButtons() {
    stations.forEach((station, index) => {
      const wrapper = document.createElement("div");

      const label = document.createElement("div");
      label.className = "btn-label";
      label.textContent = station.station;

      const btn = document.createElement("button");
      btn.className = "station-btn";
      btn.textContent = station.name; // ✅ button name from JSON
      btn.dataset.index = index;

      btn.addEventListener("click", () => {
        setActiveStation(index);

        if (isPlaying) {
          loadStation(index);
          playCurrentStation();
        } else {
          screenHeader.textContent = "Press Play";
        }
      });

      wrapper.appendChild(label);
      wrapper.appendChild(btn);
      buttonsContainer.appendChild(wrapper);
    });
  }

  function setActiveStation(index) {
    currentStationIndex = index;

    document.querySelectorAll(".station-btn")
      .forEach(b => b.classList.remove("active"));

    document.querySelector(`.station-btn[data-index="${index}"]`)
      ?.classList.add("active");

    // Move dial
    const percent = (index / (stations.length - 1)) * 100;
    dialIndicator.style.left = 5 + percent * 0.9 + "%";

    showStationInfo(index);
  }

  function showStationInfo(index) {
    const s = stations[index];

    // ✅ Show ALL station info
    screenHeader.innerHTML = `
      ${s.name} | ${s.type} | ${s.station}
    `;
  }

  function loadStation(index) {
    const s = stations[index];

    // show URL (optional)
    apiFrame.src = s.url;
  }

  function playCurrentStation() {
    const s = stations[currentStationIndex];

    // ✅ IMPORTANT: change audio source dynamically
    radioAudio.src = s.url;

    radioAudio.volume = volumeSlider.value / 100;

    radioAudio.play()
      .catch(() => console.warn("Autoplay blocked"));
  }

  playToggle.addEventListener("click", () => {
    isPlaying = !isPlaying;

    if (isPlaying) {
      playToggle.textContent = "Stop";
      playToggle.classList.remove("stopped");
      powerLed.classList.add("on");
      statusText.textContent = "Playing";

      if (currentStationIndex === null) {
        setActiveStation(0);
      }

      loadStation(currentStationIndex);
      playCurrentStation();

    } else {
      playToggle.textContent = "Play";
      playToggle.classList.add("stopped");
      powerLed.classList.remove("on");
      statusText.textContent = "Stopped";

      // ✅ FULL STOP FIX
      radioAudio.pause();
      radioAudio.currentTime = 0;
      radioAudio.src = "";
    }
  });

  volumeSlider.addEventListener("input", () => {
    radioAudio.volume = volumeSlider.value / 100;
  });
</script>
```

---

# 🔥 What was broken (and now fixed)

### ❌ Problem: Audio never changed

You had:

```html
<source src="..." />
```

➡️ Always plays same file

✅ FIX:

```js
radioAudio.src = station.url;
```

---

### ❌ Problem: Stop didn’t really stop

You only did:

```js
radioAudio.pause();
```

✅ FIX:

```js
radioAudio.pause();
radioAudio.currentTime = 0;
radioAudio.src = "";
```

---

### ❌ Problem: No real station data

You used static array of URLs

✅ FIX:

* JSON file
* Dynamic UI
* Real station info shown

---

### ❌ Problem: Screen didn’t show details

Now shows:

```
Rock FM | Music | CH 1
```

---

# 🚀 Optional improvements (if you want next)

I can help you add:

* 🎵 Real online radio streams (not WAV files)
* 🎚️ Smooth volume knob animation
* 📻 FM frequency display (like 88.5 MHz)
* 💾 Save last station (localStorage)
* 🔊 Equalizer bars animation

Just tell me 👍
