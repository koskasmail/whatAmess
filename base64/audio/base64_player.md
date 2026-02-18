

# base64_player.md

-----


#### base 64 audi
Below is a complete example that:

✅ Loads a **JSON file from a URL** when you press **Load JSON**
✅ JSON contains:

* `name`
* `type` (mp3 or wav)
* `base64` audio data
  ✅ Keeps the **same GUI style** (Play / Pause / Stop)
  ✅ Supports multiple audio items (MP3 + WAV example included)

---

# 📄 Example JSON File (audio.json)

Save this as `audio.json` on your server:

```json
[
  {
    "name": "Sample MP3",
    "type": "mp3",
    "base64": "SUQzAwAAAAAAI1RTU0UAAAAPAAADTGF2ZjU2LjE2LjEwMAAAAAAAAAAAAAAA//tQxAADBzQABAAADYAAAACAAADSAAAAAEAA..."
  },
  {
    "name": "Sample WAV",
    "type": "wav",
    "base64": "UklGRiQAAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQAAAAA="
  }
]
```

⚠ Note: The Base64 above is shortened for example purposes. Replace with full valid Base64 audio data.

---

# 🎵 HTML + CSS + JS Program

Save as `index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>JSON Base64 Audio Player</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: linear-gradient(to right, #667eea, #764ba2);
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .player {
        background: white;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.3);
        text-align: center;
        width: 400px;
    }

    input, select {
        width: 100%;
        padding: 8px;
        margin-bottom: 10px;
        border-radius: 8px;
        border: 1px solid #ccc;
    }

    button {
        padding: 8px 15px;
        margin: 5px;
        font-size: 14px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: 0.3s;
    }

    .load { background-color: #007bff; color: white; }
    .play { background-color: #28a745; color: white; }
    .pause { background-color: #ffc107; color: white; }
    .stop { background-color: #dc3545; color: white; }

    button:hover {
        opacity: 0.85;
    }

    .status {
        margin-top: 8px;
        font-size: 13px;
        color: #555;
    }
</style>
</head>
<body>

<div class="player">
    <h2>JSON Base64 Audio Player</h2>

    <input type="text" id="jsonUrl" placeholder="Enter JSON file URL">
    <button class="load" onclick="loadJSON()">Load JSON</button>

    <select id="audioList"></select>

    <div>
        <button class="play" onclick="playAudio()">Play</button>
        <button class="pause" onclick="pauseAudio()">Pause</button>
        <button class="stop" onclick="stopAudio()">Stop</button>
    </div>

    <div class="status" id="status">No audio loaded</div>
</div>

<audio id="audio"></audio>

<script>
    const audio = document.getElementById("audio");
    const statusText = document.getElementById("status");
    const audioList = document.getElementById("audioList");

    let audioData = [];

    async function loadJSON() {
        const url = document.getElementById("jsonUrl").value.trim();

        if (!url) {
            alert("Please enter JSON URL.");
            return;
        }

        try {
            const response = await fetch(url);
            audioData = await response.json();

            audioList.innerHTML = "";

            audioData.forEach((item, index) => {
                const option = document.createElement("option");
                option.value = index;
                option.textContent = item.name + " (" + item.type + ")";
                audioList.appendChild(option);
            });

            statusText.textContent = "JSON loaded successfully.";
        } catch (error) {
            statusText.textContent = "Error loading JSON file.";
        }
    }

    audioList.addEventListener("change", function() {
        const selected = audioData[this.value];
        if (!selected) return;

        const mimeType = selected.type === "mp3" ? "audio/mpeg" : "audio/wav";
        audio.src = "data:" + mimeType + ";base64," + selected.base64;
        audio.load();

        statusText.textContent = "Loaded: " + selected.name;
    });

    function playAudio() {
        audio.play();
    }

    function pauseAudio() {
        audio.pause();
    }

    function stopAudio() {
        audio.pause();
        audio.currentTime = 0;
    }
</script>

</body>
</html>
```

---

# 🚀 How It Works

1. Enter the URL of `audio.json`
2. Press **Load JSON**
3. Select MP3 or WAV from dropdown
4. Press Play

---

# ⚠ Important

If testing locally:

* You must run from a local server (not double-click file)
* Example:

```bash
python -m http.server 8000
```

Then open:

```
http://localhost:8000
```

---

If you'd like, I can upgrade this to:

* 🎵 Playlist autoplay
* 📊 Show audio duration
* 🎚 Volume slider
* 📂 Load JSON from local file
* 🧠 Convert audio file → Base64 inside browser

Just tell me 🚀

-----
