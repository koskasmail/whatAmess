### Chrome extension for CRUD personal records with extension storage

Below is a complete, minimal Chrome Extension (Manifest V3) that uses `chrome.storage.local` to add, list, update, and delete personal records. The main popup shows all saved records with checkboxes for bulk delete, and each row has an Update button that opens a dedicated edit window. Add/update forms support Save and Cancel.

---

## File structure

- manifest.json
- popup.html
- popup.js
- edit.html
- edit.js
- styles.css

---

## manifest.json

```json
{
  "manifest_version": 3,
  "name": "Personal Records CRUD",
  "version": "1.0.0",
  "description": "Manage personal records using Chrome extension storage",
  "permissions": ["storage"],
  "action": {
    "default_title": "Records",
    "default_popup": "popup.html"
  }
}
```

---

## styles.css

```css
:root {
  --bg: #0f172a;
  --panel: #111827;
  --text: #e5e7eb;
  --muted: #94a3b8;
  --accent: #22c55e;
  --danger: #ef4444;
  --focus: #38bdf8;
  --border: #1f2937;
}

* { box-sizing: border-box; }
body {
  margin: 0;
  font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial;
  color: var(--text);
  background: linear-gradient(180deg, #0b1220, var(--bg));
}
.container {
  width: 360px;
  padding: 12px;
}
h1, h2 {
  margin: 0 0 8px;
  font-size: 16px;
}
.panel {
  background: var(--panel);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 6px 24px rgba(0,0,0,0.35);
}

.controls {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}
button {
  all: unset;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  border: 1px solid var(--border);
  background: #0b1326;
  color: var(--text);
}
button.primary { border-color: #14532d; background: #0b2618; }
button.primary:hover { outline: 2px solid #14532d; }
button.danger { border-color: #7f1d1d; background: #26110f; color: #fecaca; }
button.danger:hover { outline: 2px solid #7f1d1d; }
button.secondary:hover { outline: 2px solid var(--focus); }

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}
.input-grid .full { grid-column: 1 / -1; }
label {
  display: block;
  font-size: 12px;
  color: var(--muted);
  margin-bottom: 4px;
}
input, textarea, select {
  width: 100%;
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: #0b1326;
  color: var(--text);
}
textarea { resize: vertical; min-height: 64px; }
input:focus, textarea:focus, select:focus {
  outline: 2px solid var(--focus);
}

.list {
  display: grid;
  gap: 8px;
}
.row {
  display: grid;
  grid-template-columns: 24px 1fr auto;
  gap: 8px;
  align-items: center;
  padding: 8px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: #0b1326;
}
.row .meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.row .title { font-weight: 600; }
.row .sub { color: var(--muted); font-size: 12px; }

.none {
  color: var(--muted);
  text-align: center;
  padding: 16px;
}
.footer {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
  color: var(--muted);
  font-size: 12px;
}
hr { border: none; border-top: 1px solid var(--border); margin: 10px 0; }
```

---

## popup.html

```html
<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Records</title>
  <link rel="stylesheet" href="styles.css" />
</head>
<body>
  <div class="container">
    <h1>Personal records</h1>

    <div class="panel">
      <div class="controls">
        <button id="addBtn" class="primary">➕ Add record</button>
        <button id="deleteSelectedBtn" class="danger">🗑️ Delete selected</button>
        <button id="refreshBtn" class="secondary">🔄 Refresh</button>
      </div>

      <div id="list" class="list"></div>

      <div class="footer">
        <span id="count">0 records</span>
        <span>Storage: chrome.storage.local</span>
      </div>
    </div>

    <hr />

    <div class="panel">
      <h2>Add new record</h2>
      <form id="addForm">
        <div class="input-grid">
          <div>
            <label>First name</label>
            <input id="firstName" required />
          </div>
          <div>
            <label>Last name</label>
            <input id="lastName" required />
          </div>
          <div>
            <label>Phone #1</label>
            <input id="phone1" />
          </div>
          <div>
            <label>Phone #2</label>
            <input id="phone2" />
          </div>
          <div class="full">
            <label>Department</label>
            <input id="department" />
          </div>
          <div class="full">
            <label>Comment</label>
            <textarea id="comment"></textarea>
          </div>
        </div>
        <div class="controls" style="margin-top:8px;">
          <button type="submit" class="primary">💾 Save</button>
          <button type="button" id="cancelAdd" class="secondary">✖️ Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <script src="popup.js"></script>
</body>
</html>
```

---

## popup.js

```javascript
// Storage helpers
async function getAll() {
  const { records } = await chrome.storage.local.get({ records: [] });
  return records;
}
async function saveAll(records) {
  await chrome.storage.local.set({ records });
}
function genId() {
  return crypto.randomUUID ? crypto.randomUUID() : `id_${Date.now()}_${Math.random().toString(16).slice(2)}`;
}

// UI elements
const listEl = document.getElementById('list');
const countEl = document.getElementById('count');
const addBtn = document.getElementById('addBtn');
const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
const refreshBtn = document.getElementById('refreshBtn');

const addForm = document.getElementById('addForm');
const firstNameEl = document.getElementById('firstName');
const lastNameEl = document.getElementById('lastName');
const phone1El = document.getElementById('phone1');
const phone2El = document.getElementById('phone2');
const departmentEl = document.getElementById('department');
const commentEl = document.getElementById('comment');
const cancelAddBtn = document.getElementById('cancelAdd');

// Render list
async function renderList() {
  const records = await getAll();
  listEl.innerHTML = '';
  if (!records.length) {
    listEl.innerHTML = `<div class="none">No records saved yet</div>`;
  } else {
    for (const r of records) {
      const row = document.createElement('div');
      row.className = 'row';
      row.innerHTML = `
        <input type="checkbox" class="select" data-id="${r.id}">
        <div class="meta">
          <div class="title">${escapeHtml(r.firstName)} ${escapeHtml(r.lastName)}</div>
          <div class="sub">
            Dept: ${escapeHtml(r.department || '')}
            • P1: ${escapeHtml(r.phone1 || '')}
            • P2: ${escapeHtml(r.phone2 || '')}
          </div>
          <div class="sub">Comment: ${escapeHtml(r.comment || '')}</div>
        </div>
        <div>
          <button class="secondary updateBtn" data-id="${r.id}">✏️ Update</button>
        </div>
      `;
      listEl.appendChild(row);
    }
  }
  countEl.textContent = `${records.length} record${records.length === 1 ? '' : 's'}`;
}

function escapeHtml(s) {
  return String(s)
    .replaceAll('&','&amp;')
    .replaceAll('<','&lt;')
    .replaceAll('>','&gt;')
    .replaceAll('"','&quot;')
    .replaceAll("'",'&#39;');
}

// Add record
addForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const newRecord = {
    id: genId(),
    firstName: firstNameEl.value.trim(),
    lastName: lastNameEl.value.trim(),
    phone1: phone1El.value.trim(),
    phone2: phone2El.value.trim(),
    department: departmentEl.value.trim(),
    comment: commentEl.value.trim()
  };
  if (!newRecord.firstName || !newRecord.lastName) {
    alert('First name and Last name are required.');
    return;
  }
  const records = await getAll();
  records.push(newRecord);
  await saveAll(records);
  addForm.reset();
  await renderList();
});

// Cancel add
cancelAddBtn.addEventListener('click', () => {
  addForm.reset();
});

// Open add section quickly
addBtn.addEventListener('click', () => {
  firstNameEl.focus();
});

// Delete selected
deleteSelectedBtn.addEventListener('click', async () => {
  const checkboxes = Array.from(document.querySelectorAll('.select'));
  const selectedIds = checkboxes.filter(cb => cb.checked).map(cb => cb.dataset.id);
  if (!selectedIds.length) {
    alert('Select at least one record to delete.');
    return;
  }
  const confirmDel = confirm(`Delete ${selectedIds.length} selected record(s)?`);
  if (!confirmDel) return;
  const records = await getAll();
  const filtered = records.filter(r => !selectedIds.includes(r.id));
  await saveAll(filtered);
  await renderList();
});

// Update record (open edit page)
listEl.addEventListener('click', (e) => {
  const btn = e.target.closest('.updateBtn');
  if (!btn) return;
  const id = btn.dataset.id;
  const url = chrome.runtime.getURL(`edit.html?id=${encodeURIComponent(id)}`);
  window.open(url, '_blank', 'width=480,height=640');
});

// Refresh
refreshBtn.addEventListener('click', renderList);

// Live updates if storage changes
chrome.storage.onChanged.addListener((changes, areaName) => {
  if (areaName === 'local' && changes.records) {
    renderList();
  }
});

// Initialize
renderList();
```

---

## edit.html

```html
<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Edit record</title>
  <link rel="stylesheet" href="styles.css" />
</head>
<body>
  <div class="container" style="width:480px;">
    <h1>Edit personal record</h1>

    <div class="panel">
      <form id="editForm">
        <div class="input-grid">
          <div>
            <label>First name</label>
            <input id="firstName" required />
          </div>
          <div>
            <label>Last name</label>
            <input id="lastName" required />
          </div>
          <div>
            <label>Phone #1</label>
            <input id="phone1" />
          </div>
          <div>
            <label>Phone #2</label>
            <input id="phone2" />
          </div>
          <div class="full">
            <label>Department</label>
            <input id="department" />
          </div>
          <div class="full">
            <label>Comment</label>
            <textarea id="comment"></textarea>
          </div>
        </div>
        <div class="controls" style="margin-top:8px;">
          <button type="submit" class="primary">💾 Save</button>
          <button type="button" id="cancelBtn" class="secondary">✖️ Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <script src="edit.js"></script>
</body>
</html>
```

---

## edit.js

```javascript
async function getAll() {
  const { records } = await chrome.storage.local.get({ records: [] });
  return records;
}
async function saveAll(records) {
  await chrome.storage.local.set({ records });
}

function getParam(key) {
  const url = new URL(location.href);
  return url.searchParams.get(key);
}

const firstNameEl = document.getElementById('firstName');
const lastNameEl = document.getElementById('lastName');
const phone1El = document.getElementById('phone1');
const phone2El = document.getElementById('phone2');
const departmentEl = document.getElementById('department');
const commentEl = document.getElementById('comment');
const editForm = document.getElementById('editForm');
const cancelBtn = document.getElementById('cancelBtn');

let recordId = null;

async function loadRecord() {
  recordId = getParam('id');
  if (!recordId) {
    alert('Missing record id.');
    window.close();
    return;
  }
  const records = await getAll();
  const r = records.find(x => x.id === recordId);
  if (!r) {
    alert('Record not found.');
    window.close();
    return;
  }
  firstNameEl.value = r.firstName || '';
  lastNameEl.value = r.lastName || '';
  phone1El.value = r.phone1 || '';
  phone2El.value = r.phone2 || '';
  departmentEl.value = r.department || '';
  commentEl.value = r.comment || '';
}

editForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const records = await getAll();
  const idx = records.findIndex(x => x.id === recordId);
  if (idx === -1) {
    alert('Record not found.');
    window.close();
    return;
  }
  records[idx] = {
    ...records[idx],
    firstName: firstNameEl.value.trim(),
    lastName: lastNameEl.value.trim(),
    phone1: phone1El.value.trim(),
    phone2: phone2El.value.trim(),
    department: departmentEl.value.trim(),
    comment: commentEl.value.trim()
  };
  await saveAll(records);
  window.close(); // close edit window after save
});

cancelBtn.addEventListener('click', () => window.close());

loadRecord();
```

---

### How to install and use

- **Load the extension:**
  1. Open Chrome → More tools → Extensions.
  2. Enable Developer mode.
  3. Click “Load unpacked” and select the folder containing these files.

- **Use the popup:**
  - **Add record:** Fill the form and click Save. Cancel clears the form.
  - **Show records:** The list shows all saved records.
  - **Delete selected:** Tick checkboxes and click Delete selected.
  - **Update record:** Click ✏️ Update to open the edit window; Save or Cancel there.

---

### Notes

- **Storage:** Records are kept under the key `records` in `chrome.storage.local` (persisted per browser profile).
- **IDs:** Each record gets a unique ID via `crypto.randomUUID()` (fallback provided).
- **Sanitization:** Display uses safe HTML escaping to prevent injection in the list.
