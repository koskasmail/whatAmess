const CACHE_NAME = 'contact-cache';
const CONTACT_KEY = 'contacts';

document.getElementById('addBtn').addEventListener('click', () => {
  window.open('form.html', '_blank', 'width=400,height=500');
});

document.getElementById('refreshBtn').addEventListener('click', loadContacts);

document.getElementById('cleanBtn').addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  await cache.delete(CONTACT_KEY);
  renderTable([]);
});

window.addEventListener('message', async (event) => {
  if (event.data?.type === 'new-contact') {
    const cache = await caches.open(CACHE_NAME);
    const match = await cache.match(CONTACT_KEY);
    const contacts = match ? await match.json() : [];
    contacts.push(event.data.contact);
    await cache.put(CONTACT_KEY, new Response(JSON.stringify(contacts)));
    renderTable(contacts);
  }
});

async function loadContacts() {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  renderTable(contacts);
}

function renderTable(contacts) {
  const tbody = document.getElementById('contactTableBody');
  tbody.innerHTML = '';
  contacts.forEach(contact => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${contact.firstname}</td>
      <td>${contact.lastname}</td>
      <td>${contact.phone}</td>
      <td>${contact.address}</td>
    `;
    tbody.appendChild(row);
  });
}

window.addEventListener('load', loadContacts);
