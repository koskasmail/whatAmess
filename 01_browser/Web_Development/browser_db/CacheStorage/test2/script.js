const CACHE_NAME = 'contact-cache';
const CONTACT_KEY = 'contacts';

const popup = document.getElementById('popupForm');
const addBtn = document.getElementById('addBtn');
const refreshBtn = document.getElementById('refreshBtn');
const cleanBtn = document.getElementById('cleanBtn');
const saveBtn = document.getElementById('saveBtn');
const clearFormBtn = document.getElementById('clearFormBtn');
const cancelBtn = document.getElementById('cancelBtn');

addBtn.addEventListener('click', () => {
  popup.classList.remove('hidden');
});

cancelBtn.addEventListener('click', () => {
  popup.classList.add('hidden');
});

clearFormBtn.addEventListener('click', () => {
  document.getElementById('firstname').value = '';
  document.getElementById('lastname').value = '';
  document.getElementById('phone').value = '';
  document.getElementById('address').value = '';
});

saveBtn.addEventListener('click', async () => {
  const contact = {
    id: Date.now(),
    firstname: document.getElementById('firstname').value,
    lastname: document.getElementById('lastname').value,
    phone: document.getElementById('phone').value,
    address: document.getElementById('address').value
  };

  const cache = await caches.open(CACHE_NAME);
  const existing = await cache.match(CONTACT_KEY);
  let contacts = existing ? await existing.json() : [];

  contacts.push(contact);
  await cache.put(CONTACT_KEY, new Response(JSON.stringify(contacts)));

  popup.classList.add('hidden');
  displayContacts(contacts);
});

refreshBtn.addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  displayContacts(contacts);
});

cleanBtn.addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  await cache.delete(CONTACT_KEY);
  displayContacts([]);
});

function displayContacts(contacts) {
  const list = document.getElementById('contactList');
  list.innerHTML = '';
  contacts.forEach(contact => {
    const li = document.createElement('li');
    li.textContent = `${contact.firstname} ${contact.lastname} - ${contact.phone} - ${contact.address}`;
    list.appendChild(li);
  });
}

window.addEventListener('load', async () => {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  displayContacts(contacts);
});
