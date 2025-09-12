    const fileInput = document.getElementById('fileInput');
    const output = document.getElementById('output');

    fileInput.addEventListener('change', async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const ext = file.name.split('.').pop().toLowerCase();

      if (ext === 'docx') {
        const reader = new FileReader();
        reader.onload = function(e) {
          mammoth.convertToHtml({ arrayBuffer: e.target.result }, {
            styleMap: [
              "b => strong",
              "i => em",
              "u => u",
              "r => span.normal-font",
              "table => table"
            ]
          }).then(result => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(result.value, 'text/html');
            const paragraphs = doc.body.querySelectorAll('p');

            paragraphs.forEach(p => {
              const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
              const dir = rtlChars.test(p.textContent) ? 'rtl' : 'ltr';
              p.setAttribute('dir', dir);
              p.classList.add(dir);
            });

            output.innerHTML = `<h2>DOCX Content:</h2>${doc.body.innerHTML}`;
          });
        };
        reader.readAsArrayBuffer(file);
      }

      else if (ext === 'xlsx' || ext === 'xls') {
        const reader = new FileReader();
        reader.onload = function(e) {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          let html = `<h2>Excel Content:</h2>`;
          workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            const sheetText = XLSX.utils.sheet_to_csv(sheet);
            const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
            const dir = rtlChars.test(sheetText) ? 'rtl' : 'ltr';
            html += `<h3>${sheetName}</h3>`;
            html += `<div dir="${dir}" class="${dir}">${XLSX.utils.sheet_to_html(sheet)}</div>`;
          });
          output.innerHTML = html;
        };
        reader.readAsBinaryString(file);
      }

      else {
        output.innerHTML = `<p>Unsupported file type: ${ext}</p>`;
      }
    });
