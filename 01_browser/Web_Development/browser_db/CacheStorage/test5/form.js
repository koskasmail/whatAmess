// document.getElementById('contactForm').addEventListener('submit', (e) => {
//   e.preventDefault();
//   const contact = {
//     id: Date.now(),
//     firstname: document.getElementById('firstname').value,
//     lastname: document.getElementById('lastname').value,
//     phone: document.getElementById('phone').value,
//     address: document.getElementById('address').value
//   };
//   window.opener.postMessage({ type: 'new-contact', contact }, '*');
//   window.close();
// });

document.getElementById('clearBtn').addEventListener('click', () => {
  document.getElementById('contactForm').reset();
});

document.getElementById('cancelBtn').addEventListener('click', () => {
  window.close();
});

document.getElementById('contactForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const contact = {
    id: Date.now(),
    firstname: document.getElementById('firstname').value,
    lastname: document.getElementById('lastname').value,
    phone: document.getElementById('phone').value,
    address: document.getElementById('address').value
  };

  const index = sessionStorage.getItem('editContactIndex');
  if (index !== null) {
    contact.editIndex = parseInt(index);
  }

  window.opener.postMessage({ type: 'new-contact', contact }, '*');
  sessionStorage.clear();
  window.close();
});