You want to **replace the Base64 audio inside your JSON** with a **real filename + path**, load that JSON into memory, and **play the first audio entry when the countdown reaches 0**.

Let’s wire this into your Tomato Clock cleanly and without breaking your existing logic.

Below is a **minimal, clean, fully working integration**.

---

# ✅ 1. Add JSON Audio Array (with filename + path)

Put this **inside your `<script>`**, before any functions:

```js
// Audio list stored as JSON (filename + path)
const audioList = [
    {
        name: "Beep Sound",
        type: "wav",
        path: "media/beep.wav"   // <-- your file path here
    },
    {
        name: "Alert Sound",
        type: "mp3",
        path: "media/alert.mp3"
    },
    {
        name: "Rest Sound",
        type: "wav",
        path: "media/rest.wav"
    }
];
```

You can change the paths to:

```
C:/media/beep.wav
```

…but browsers **cannot** load Windows paths directly.  
You must place the file in your project folder, like:

```
/media/beep.wav
```

---

# ✅ 2. Load the FIRST audio entry into memory

Add this under the JSON:

```js
// Load first audio entry into memory
let currentAudio = new Audio(audioList[0].path);
```

---

# ✅ 3. Play sound when countdown reaches 0

Modify your timer logic:

Find this block:

```js
if (timeLeft <= 0) {
    isDev = !isDev;
    timeLeft = (isDev ? devMinutes : restMinutes) * 60;
}
```

Replace it with:

```js
if (timeLeft <= 0) {

    // Play sound from JSON
    currentAudio.currentTime = 0;
    currentAudio.play();

    // Switch mode
    isDev = !isDev;
    timeLeft = (isDev ? devMinutes : restMinutes) * 60;
}
```

---

# ✅ 4. Full Updated Script Section (clean + ready to paste)

Here is the **complete updated script**, with your JSON audio list, audio loading, and sound playback:

```html
<script>
    // Audio list stored as JSON (filename + path)
    const audioList = [
        {
            name: "Beep Sound",
            type: "wav",
            path: "media/beep.wav"
        },
        {
            name: "Alert Sound",
            type: "mp3",
            path: "media/alert.mp3"
        },
        {
            name: "Rest Sound",
            type: "wav",
            path: "media/rest.wav"
        }
    ];

    // Load first audio entry into memory
    let currentAudio = new Audio(audioList[0].path);

    // Default values
    const DEFAULT_DEV = 2;
    const DEFAULT_REST = 2;

    let devMinutes = DEFAULT_DEV;
    let restMinutes = DEFAULT_REST;

    let isDev = true;
    let timeLeft = 0;
    let timer = null;

    // Load saved settings
    function loadSettings() {
        const savedDev = localStorage.getItem("devTime");
        const savedRest = localStorage.getItem("restTime");

        devMinutes = savedDev ? parseInt(savedDev) : DEFAULT_DEV;
        restMinutes = savedRest ? parseInt(savedRest) : DEFAULT_REST;

        document.getElementById("devTime").value = devMinutes;
        document.getElementById("restTime").value = restMinutes;

        updateDisplay(0);
    }

    // Save settings
    document.getElementById("saveBtn").onclick = function () {
        const dev = parseInt(document.getElementById("devTime").value);
        const rest = parseInt(document.getElementById("restTime").value);

        if (dev > 0 && rest > 0) {
            localStorage.setItem("devTime", dev);
            localStorage.setItem("restTime", rest);
            alert("Saved!");
            loadSettings();
        }
    };

    // Reset to default
    document.getElementById("defaultBtn").onclick = function () {
        localStorage.removeItem("devTime");
        localStorage.removeItem("restTime");
        alert("Reset to default!");
        loadSettings();
    };

    // Start button
    document.getElementById("startBtn").onclick = function () {
        clearInterval(timer);
        isDev = true;
        timeLeft = devMinutes * 60;
        runTimer();
    };

    // Timer logic
    function runTimer() {
        updateDisplay();

        timer = setInterval(() => {
            timeLeft--;

            if (timeLeft <= 0) {

                // Play sound from JSON
                currentAudio.currentTime = 0;
                currentAudio.play();

                // Switch mode
                isDev = !isDev;
                timeLeft = (isDev ? devMinutes : restMinutes) * 60;
            }

            updateDisplay();
        }, 1000);
    }

    function updateDisplay() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;

        document.getElementById("countdown").textContent =
            `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

        document.getElementById("status").textContent =
            isDev ? "Development Time" : "Rest Time";
    }

    // Start
    loadSettings();
</script>
```

---

# 🎉 What You Now Have

### ✔ JSON audio list with:
- name  
- type  
- path  

### ✔ First audio entry loaded into memory  
### ✔ Sound plays automatically when countdown hits 0  
### ✔ No Base64 needed  
### ✔ Clean integration with your existing Tomato Clock  

---

If you want next:

- Let user choose which sound from the JSON  
- Add a sound test button  
- Add volume control  
- Add multiple sounds for dev/rest modes  

Just tell me and I’ll extend it.
