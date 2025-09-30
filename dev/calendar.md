
# calendar

#### json

#### events_2.json
```
[
  { "date": "25/09/2025", "name": "Alice", "title": "Team Meeting" },
  { "date": "28/09/2025", "name": "Bob", "title": "Project Deadline" }
]
```

#### events_3.json
```
[
  { "date": "25/09/2025", "name": "Alice", "title": "Team Meeting", "background-color": "#FFD700" },
  { "date": "28/09/2025", "name": "Bob", "title": "Project Deadline", "background-color": "#90EE90" },
  { "date": "28/04/2025", "name": "kessler yaron", "title": "birthday", "background-color": "lightblue" }
]
```

#### events_4.json
```
[
  { "date": "25/09/2025", "name": "Alice", "title": "Team Meeting", "background-color": "#FFD700" },
  { "date": "28/09/2025", "name": "Bob", "title": "Project Deadline", "background-color": "#90EE90" },
  { "date": "28/04", "name": "kessler yaron", "title": "birthday", "background-color": "lightblue" }
]
```

---- 
#### index.html
```
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>JavaScript Calendar</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="calendar">
    <div class="header">
      <button onclick="changeMonth(-1)">◀</button>
      <h2 id="monthYear"></h2>
      <button onclick="changeMonth(1)">▶</button>
    </div>
    <table>
      <thead>
        <tr>
          <th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th>
          <th>Thu</th><th>Fri</th><th>Sat</th>
        </tr>
      </thead>
      <tbody id="calendarBody"></tbody>
    </table>
  </div>
  <script src="script_4.js" defer></script>
</body>
</html>
```

---- 

#### style.css

```
body {
    font-family: sans-serif;
    display: flex;
    justify-content: center;
    margin-top: 50px;
  }
  .calendar {
    width: 350px;
    text-align: center;
  }
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th, td {
    padding: 10px;
    border: 1px solid #ccc;
  }
```

#### style.css

##### script_0.js
```
let currentDate = new Date();

function renderCalendar(date) {
  const monthYear = document.getElementById("monthYear");
  const calendarBody = document.getElementById("calendarBody");
  calendarBody.innerHTML = "";

  const year = date.getFullYear();
  const month = date.getMonth();
  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${year}`;

  let row = document.createElement("tr");
  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let day = 1; day <= daysInMonth; day++) {
    if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }
    const cell = document.createElement("td");
    cell.textContent = day;
    row.appendChild(cell);
  }
  calendarBody.appendChild(row);
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar(currentDate);
}

renderCalendar(currentDate);
```

##### script_1.js
```
let currentDate = new Date();

function renderCalendar(date) {
  const monthYear = document.getElementById("monthYear");
  const calendarBody = document.getElementById("calendarBody");
  calendarBody.innerHTML = "";

  const year = date.getFullYear();
  const month = date.getMonth();
  const today = new Date();
  const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${year}`;

  let row = document.createElement("tr");
  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let day = 1; day <= daysInMonth; day++) {
    if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }
    const cell = document.createElement("td");
    cell.textContent = day;

    // ✅ Highlight today's date
    if (isCurrentMonth && day === today.getDate()) {
      cell.style.backgroundColor = "yellow";
    }

    row.appendChild(cell);
  }
  calendarBody.appendChild(row);
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar(currentDate);
}

renderCalendar(currentDate);
```


##### script_2.js
```
let currentDate = new Date();
let events = [];

async function loadEvents() {
  const response = await fetch('events_2.json');
  events = await response.json();
  renderCalendar(currentDate);
}

function renderCalendar(date) {
  const monthYear = document.getElementById("monthYear");
  const calendarBody = document.getElementById("calendarBody");
  calendarBody.innerHTML = "";

  const year = date.getFullYear();
  const month = date.getMonth();
  const today = new Date();
  const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${year}`;

  let row = document.createElement("tr");
  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let day = 1; day <= daysInMonth; day++) {
    if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }

    const cell = document.createElement("td");
    cell.textContent = day;

    // Highlight today's date
    if (isCurrentMonth && day === today.getDate()) {
      cell.style.backgroundColor = "yellow";
    }

    // Check for events
    const dateStr = `${String(day).padStart(2, '0')}/${String(month + 1).padStart(2, '0')}/${year}`;
    const event = events.find(e => e.date === dateStr);
    if (event) {
      const eventDiv = document.createElement("div");
      eventDiv.style.fontSize = "0.8em";
      eventDiv.style.marginTop = "5px";
      eventDiv.textContent = `${event.name}: ${event.title}`;
      cell.appendChild(eventDiv);
    }

    row.appendChild(cell);
  }
  calendarBody.appendChild(row);
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar(currentDate);
}

loadEvents();
```


##### script_3.js
```
let currentDate = new Date();
let events = [];

async function loadEvents() {
  const response = await fetch('events_3.json');
  events = await response.json();
  renderCalendar(currentDate);
}

function renderCalendar(date) {
  const monthYear = document.getElementById("monthYear");
  const calendarBody = document.getElementById("calendarBody");
  calendarBody.innerHTML = "";

  const year = date.getFullYear();
  const month = date.getMonth();
  const today = new Date();
  const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${year}`;

  let row = document.createElement("tr");
  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let day = 1; day <= daysInMonth; day++) {
    if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }

    const cell = document.createElement("td");
    cell.textContent = day;

    // Highlight today's date
    if (isCurrentMonth && day === today.getDate()) {
      cell.style.backgroundColor = "yellow";
    }

    // Match events
    const dateStr = `${String(day).padStart(2, '0')}/${String(month + 1).padStart(2, '0')}/${year}`;
    const event = events.find(e => e.date === dateStr);
    if (event) {
      cell.style.backgroundColor = event["background-color"];
      const eventDiv = document.createElement("div");
      eventDiv.style.fontSize = "0.8em";
      eventDiv.style.marginTop = "5px";
      eventDiv.textContent = `${event.name}: ${event.title}`;
      cell.appendChild(eventDiv);
    }

    row.appendChild(cell);
  }
  calendarBody.appendChild(row);
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar(currentDate);
}

loadEvents();
```


##### script_4.js
```
let currentDate = new Date();
let events = [];

async function loadEvents() {
  const response = await fetch('events_4.json');
  events = await response.json();
  renderCalendar(currentDate);
}

function renderCalendar(date) {
  const monthYear = document.getElementById("monthYear");
  const calendarBody = document.getElementById("calendarBody");
  calendarBody.innerHTML = "";

  const year = date.getFullYear();
  const month = date.getMonth();
  const today = new Date();
  const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${year}`;

  let row = document.createElement("tr");
  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let day = 1; day <= daysInMonth; day++) {
    if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }

    const cell = document.createElement("td");
    cell.textContent = day;

    // Highlight today's date
    if (isCurrentMonth && day === today.getDate()) {
      cell.style.backgroundColor = "yellow";
    }

    // Format current calendar date
    const fullDateStr = `${String(day).padStart(2, '0')}/${String(month + 1).padStart(2, '0')}/${year}`;
    const recurringDateStr = `${String(day).padStart(2, '0')}/${String(month + 1).padStart(2, '0')}`;

    // Match events
    const matchingEvents = events.filter(e => {
      return e.date === fullDateStr || e.date === recurringDateStr;
    });

    matchingEvents.forEach(event => {
      cell.style.backgroundColor = event["background-color"];
      const eventDiv = document.createElement("div");
      eventDiv.style.fontSize = "0.8em";
      eventDiv.style.marginTop = "5px";
      eventDiv.textContent = `${event.name}: ${event.title}`;
      cell.appendChild(eventDiv);
    });

    row.appendChild(cell);
  }
  calendarBody.appendChild(row);
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar(currentDate);
}

loadEvents();
```


<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
