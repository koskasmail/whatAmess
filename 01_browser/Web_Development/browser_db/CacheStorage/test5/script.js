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
    let contacts = match ? await match.json() : [];

    if ('editIndex' in event.data.contact) {
      contacts[event.data.contact.editIndex] = event.data.contact;
    } else {
      contacts.push(event.data.contact);
    }

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

  contacts.forEach((contact, index) => {

    console.log(contact,index);
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${contact.firstname}</td>
      <td>${contact.lastname}</td>
      <td>${contact.phone}</td>
      <td>${contact.address}</td>
      <td>
        <a href="#" class="me-2 text-primary" onclick="reeditContact(${index})">Reedit</a>
        <a href="#" class="text-danger" onclick="deleteContact(${index})">Delete</a>
      </td>
    `;
    tbody.appendChild(row);
  });
}

async function deleteContact(index) {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  let contacts = match ? await match.json() : [];

  contacts.splice(index, 1); // Remove contact
  await cache.put(CONTACT_KEY, new Response(JSON.stringify(contacts)));
  renderTable(contacts);
}

async function reeditContact(index) {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  const contact = contacts[index];

  // Pass contact data via sessionStorage
  sessionStorage.setItem('editContactIndex', index);
  sessionStorage.setItem('editContactData', JSON.stringify(contact));

  window.open('form.html', '_blank', 'width=400,height=500');
}


window.addEventListener('load', loadContacts);
