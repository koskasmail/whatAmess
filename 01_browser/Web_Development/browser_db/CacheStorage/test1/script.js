const CACHE_NAME = 'contact-cache';
const CONTACT_KEY = 'contacts';

document.getElementById('addBtn').addEventListener('click', async () => {
  const name = prompt('Enter contact name:');
  if (!name) return;

  const contact = { name, id: Date.now() };
  const cache = await caches.open(CACHE_NAME);
  const existing = await cache.match(CONTACT_KEY);
  let contacts = existing ? await existing.json() : [];

  contacts.push(contact);
  const response = new Response(JSON.stringify(contacts));
  await cache.put(CONTACT_KEY, response);
  displayContacts(contacts);
});

document.getElementById('refreshBtn').addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  displayContacts(contacts);
});

document.getElementById('cleanBtn').addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  await cache.delete(CONTACT_KEY);
  displayContacts([]);
});

function displayContacts(contacts) {
  const list = document.getElementById('contactList');
  list.innerHTML = '';
  contacts.forEach(contact => {
    const li = document.createElement('li');
    li.textContent = contact.name;
    list.appendChild(li);
  });
}

// Load contacts on page load
window.addEventListener('load', async () => {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  displayContacts(contacts);
});
