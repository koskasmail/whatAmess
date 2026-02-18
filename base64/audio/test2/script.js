const DEFAULT_DEV = 2;
const DEFAULT_REST = 2;

let devMinutes = DEFAULT_DEV;
let restMinutes = DEFAULT_REST;

let isDev = true;
let timeLeft = 0;
let timer = null;
let running = false;

const beep = document.getElementById("beepSound");

function loadSettings() {
    const savedDev = localStorage.getItem("devTime");
    const savedRest = localStorage.getItem("restTime");

    devMinutes = savedDev ? parseInt(savedDev) : DEFAULT_DEV;
    restMinutes = savedRest ? parseInt(savedRest) : DEFAULT_REST;

    document.getElementById("devTime").value = devMinutes;
    document.getElementById("restTime").value = restMinutes;

    updateDisplay(0);
}

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

document.getElementById("defaultBtn").onclick = function () {
    localStorage.removeItem("devTime");
    localStorage.removeItem("restTime");
    alert("Reset to default!");
    loadSettings();
};

// START / STOP BUTTON
document.getElementById("startBtn").onclick = function () {
    if (!running) {
        startTimer();
    } else {
        stopTimer();
    }
};

function startTimer() {
    running = true;
    document.getElementById("startBtn").textContent = "Stop";

    clearInterval(timer);
    isDev = true;
    timeLeft = devMinutes * 60;

    runTimer();
}

function stopTimer() {
    running = false;
    document.getElementById("startBtn").textContent = "Start";
    clearInterval(timer);
    document.getElementById("status").textContent = "Stopped";
}

function runTimer() {
    updateDisplay();

    timer = setInterval(() => {
        timeLeft--;

        if (timeLeft <= 0) {
            beep.play(); // 🔔 Play beep sound

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

loadSettings();

