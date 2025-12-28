Here’s a clean, structured Angular example that gives you exactly what you described:  

- A **navbar** with two buttons:  
  1. **File Explorer** – browse folders/files, navigate into folders, open files  
  2. **System Info** – show browser + memory info  
- A **file explorer component** that:  
  - Lists folders/files  
  - Allows navigation into folders  
  - Opens files and displays their content  
  - Includes **Page Up / Page Down** for long files  
- A **system info component** that shows:  
  - Browser name  
  - Version  
  - Platform  
  - Memory usage (if supported by the browser)  

Below is a full working example you can paste into a new Angular project.

---

# ✅ **1. app.module.ts**
```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { FileExplorerComponent } from './file-explorer/file-explorer.component';
import { SystemInfoComponent } from './system-info/system-info.component';

@NgModule({
  declarations: [
    AppComponent,
    FileExplorerComponent,
    SystemInfoComponent
  ],
  imports: [BrowserModule],
  bootstrap: [AppComponent]
})
export class AppModule {}
```

---

# ✅ **2. app.component.html**
```html
<nav style="padding: 10px; background: #1976d2; color: white;">
  <button (click)="view='files'">File Explorer</button>
  <button (click)="view='system'">System Info</button>
</nav>

<div style="padding: 20px;">
  <app-file-explorer *ngIf="view==='files'"></app-file-explorer>
  <app-system-info *ngIf="view==='system'"></app-system-info>
</div>
```

---

# ✅ **3. app.component.ts**
```ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  view = 'files';
}
```

---

# ✅ **4. file-explorer.component.ts**
This uses a **mock file system**. You can replace it with real API calls later.

```ts
import { Component } from '@angular/core';

interface FileNode {
  name: string;
  type: 'file' | 'folder';
  content?: string;
  children?: FileNode[];
}

@Component({
  selector: 'app-file-explorer',
  templateUrl: './file-explorer.component.html'
})
export class FileExplorerComponent {
  fileSystem: FileNode = {
    name: 'root',
    type: 'folder',
    children: [
      { name: 'notes.txt', type: 'file', content: 'This is a sample text file.\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10' },
      {
        name: 'projects',
        type: 'folder',
        children: [
          { name: 'readme.md', type: 'file', content: '# Project Readme\nSome content here...' }
        ]
      }
    ]
  };

  currentPath: FileNode[] = [this.fileSystem];
  selectedFileContent = '';
  page = 0;
  linesPerPage = 5;

  get currentFolder(): FileNode {
    return this.currentPath[this.currentPath.length - 1];
  }

  open(node: FileNode) {
    if (node.type === 'folder') {
      this.currentPath.push(node);
      this.selectedFileContent = '';
    } else {
      this.selectedFileContent = node.content || '';
      this.page = 0;
    }
  }

  goBack() {
    if (this.currentPath.length > 1) {
      this.currentPath.pop();
      this.selectedFileContent = '';
    }
  }

  get paginatedContent() {
    const lines = this.selectedFileContent.split('\n');
    const start = this.page * this.linesPerPage;
    return lines.slice(start, start + this.linesPerPage).join('\n');
  }

  pageUp() {
    if (this.page > 0) this.page--;
  }

  pageDown() {
    const lines = this.selectedFileContent.split('\n');
    if ((this.page + 1) * this.linesPerPage < lines.length) this.page++;
  }
}
```

---

# ✅ **5. file-explorer.component.html**
```html
<h2>File Explorer</h2>

<button (click)="goBack()" [disabled]="currentPath.length === 1">⬅ Back</button>

<div *ngIf="!selectedFileContent">
  <h3>Folder: {{ currentFolder.name }}</h3>
  <ul>
    <li *ngFor="let item of currentFolder.children" (click)="open(item)" style="cursor:pointer;">
      📁 {{ item.name }} <span *ngIf="item.type==='file'">📄</span>
    </li>
  </ul>
</div>

<div *ngIf="selectedFileContent">
  <h3>File Viewer</h3>

  <pre style="background:#eee; padding:10px; height:200px; overflow:auto;">
{{ paginatedContent }}
  </pre>

  <button (click)="pageUp()">Page Up</button>
  <button (click)="pageDown()">Page Down</button>
</div>
```

---

# ✅ **6. system-info.component.ts**
```ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-system-info',
  templateUrl: './system-info.component.html'
})
export class SystemInfoComponent {
  browserInfo = navigator.userAgent;
  platform = navigator.platform;
  memory = (navigator as any).deviceMemory || 'Not supported';
  cores = navigator.hardwareConcurrency;
}
```

---

# ✅ **7. system-info.component.html**
```html
<h2>System Information</h2>

<ul>
  <li><strong>Browser:</strong> {{ browserInfo }}</li>
  <li><strong>Platform:</strong> {{ platform }}</li>
  <li><strong>Memory (GB):</strong> {{ memory }}</li>
  <li><strong>CPU Cores:</strong> {{ cores }}</li>
</ul>
```

---

# 🎉 **You're Done**
This gives you:

### ✔ Angular navbar  
### ✔ File explorer with folder navigation  
### ✔ File viewer with page up/down  
### ✔ System info viewer  

If you want, I can also add:

- File upload support  
- Real filesystem access (Electron / backend API)  
- Material UI styling  
- Routing instead of `*ngIf` views  

Just tell me what direction you want to take next.
