const CACHE_NAME = 'contact-cache';
const CONTACT_KEY = 'contacts';

const table = new Tabulator("#contactTable", {
  layout: "fitColumns",
  columns: [
    { title: "First Name", field: "firstname" },
    { title: "Last Name", field: "lastname" },
    { title: "Phone", field: "phone" },
    { title: "Address", field: "address" }
  ]
});

document.getElementById('addBtn').addEventListener('click', () => {
  window.open('form.html', '_blank', 'width=400,height=500');
});

document.getElementById('refreshBtn').addEventListener('click', loadContacts);

document.getElementById('cleanBtn').addEventListener('click', async () => {
  const cache = await caches.open(CACHE_NAME);
  await cache.delete(CONTACT_KEY);
  table.clearData();
});

async function loadContacts() {
  const cache = await caches.open(CACHE_NAME);
  const match = await cache.match(CONTACT_KEY);
  const contacts = match ? await match.json() : [];
  table.setData(contacts);
}

window.addEventListener('message', async (event) => {
  if (event.data && event.data.type === 'new-contact') {
    const cache = await caches.open(CACHE_NAME);
    const match = await cache.match(CONTACT_KEY);
    const contacts = match ? await match.json() : [];
    contacts.push(event.data.contact);
    await cache.put(CONTACT_KEY, new Response(JSON.stringify(contacts)));
    loadContacts();
  }
});

window.addEventListener('load', loadContacts);
