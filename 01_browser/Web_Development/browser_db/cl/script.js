    async function loadChecklist() {
      const response = await fetch('checklist.json');
      const checklistJSON = await response.json();
      const container = document.getElementById('pdf-content');

      Object.entries(checklistJSON).forEach(([section, items]) => {
        const sectionDiv = document.createElement('div');
        sectionDiv.className = 'section';

        const title = document.createElement('h2');
        title.textContent = section;
        sectionDiv.appendChild(title);

        const ul = document.createElement('ul');

        items.forEach((item, index) => {
          const id = `${section}-${index}`;
          const li = document.createElement('li');
          const checkbox = document.createElement('input');
          checkbox.type = 'checkbox';
          checkbox.id = id;

          if (localStorage.getItem(id) === 'true') {
            checkbox.checked = true;
          }

          checkbox.addEventListener('change', () => {
            localStorage.setItem(id, checkbox.checked);
          });

          const label = document.createElement('label');
          label.htmlFor = id;
          label.textContent = ` ${item}`;

          li.appendChild(checkbox);
          li.appendChild(label);
          ul.appendChild(li);
        });

        sectionDiv.appendChild(ul);
        container.appendChild(sectionDiv);
      });
    }

    function saveChecklist() {
      const checkboxes = document.querySelectorAll('input[type="checkbox"]');
      checkboxes.forEach(cb => {
        localStorage.setItem(cb.id, cb.checked);
      });
      alert("✅ נשמר בהצלחה!");
    }

    function downloadPDF() {
      const element = document.getElementById('pdf-content');
      const opt = {
        margin:       0.5,
        filename:     'פרוטוקול_בדיקת_דירה.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { scale: 2 },
        jsPDF:        { unit: 'in', format: 'a4', orientation: 'portrait' }
      };

      html2pdf().set(opt).from(element).save();
    }

    loadChecklist();
