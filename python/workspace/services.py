from fastapi import FastAPI
from fastapi.responses import JSONResponse
from datetime import datetime, date

app = FastAPI()

# Example holiday list (add your own)
HOLIDAYS = {
    "01-01": "New Year's Day",
    "12-25": "Christmas Day",
    "04-23": "Independence Day (Example)"
}

def get_greeting(now):
    hour = now.hour

    # Time‑based greeting
    if 5 <= hour < 12:
        greeting = "Good morning"
    elif 12 <= hour < 17:
        greeting = "Good afternoon"
    elif 17 <= hour < 22:
        greeting = "Good evening"
    else:
        greeting = "Good night"

    # Day‑based greeting
    weekday = now.strftime("%A")

    # Holiday check
    today_key = now.strftime("%m-%d")
    holiday = HOLIDAYS.get(today_key)

    if holiday:
        greeting += f" and happy {holiday}"

    return greeting, weekday, holiday


@app.get("/greet")
def greet():
    now = datetime.now()

    greeting, weekday, holiday = get_greeting(now)

    response = {
        "date": now.strftime("%Y-%m-%d"),
        "time": now.strftime("%H:%M:%S"),
        "weekday": weekday,
        "holiday": holiday if holiday else None,
        "greeting": greeting
    }

    return JSONResponse(content=response)


# Run with: uvicorn filename:app --host 0.0.0.0 --port 8080

