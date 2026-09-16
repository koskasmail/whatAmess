Absolutely. Below is the **full Angular 19.1.7 code** for the dashboard shown in the screenshot.

## 1. Project structure

```text
ricuz-angular19/
├── package.json
├── angular.json
├── tsconfig.json
├── tsconfig.app.json
└── src/
    ├── index.html
    ├── main.ts
    ├── styles.css
    └── app/
        ├── app.config.ts
        ├── app.routes.ts
        ├── app.component.ts
        ├── dashboard.component.ts
        ├── dashboard.component.html
        ├── dashboard.component.css
        ├── dashboard.models.ts
        ├── dashboard.service.ts
        └── icon.component.ts
```

---

# 2. `package.json`

```json
{
  "name": "ricuz-dashboard-angular",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "start": "ng serve",
    "build": "ng build",
    "watch": "ng build --watch --configuration development"
  },
  "dependencies": {
    "@angular/animations": "19.1.7",
    "@angular/common": "19.1.7",
    "@angular/compiler": "19.1.7",
    "@angular/core": "19.1.7",
    "@angular/forms": "19.1.7",
    "@angular/platform-browser": "19.1.7",
    "@angular/platform-browser-dynamic": "19.1.7",
    "@angular/router": "19.1.7",
    "rxjs": "~7.8.1",
    "tslib": "^2.8.1",
    "zone.js": "~0.15.0"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "19.1.7",
    "@angular/cli": "19.1.7",
    "@angular/compiler-cli": "19.1.7",
    "typescript": "~5.7.2"
  }
}
```

---

# 3. `angular.json`

```json
{
  "$schema": "./node_modules/@angular/cli/lib/config/schema.json",
  "version": 1,
  "newProjectRoot": "projects",
  "projects": {
    "ricuz-dashboard": {
      "projectType": "application",
      "schematics": {},
      "root": "",
      "sourceRoot": "src",
      "prefix": "app",

      "architect": {
        "build": {
          "builder": "@angular-devkit/build-angular:application",

          "options": {
            "outputPath": "dist/ricuz-dashboard",
            "index": "src/index.html",
            "browser": "src/main.ts",

            "polyfills": [
              "zone.js"
            ],

            "tsConfig": "tsconfig.app.json",

            "assets": [],

            "styles": [
              "src/styles.css"
            ]
          },

          "configurations": {
            "production": {
              "budgets": [
                {
                  "type": "initial",
                  "maximumWarning": "1mb",
                  "maximumError": "2mb"
                }
              ],
              "outputHashing": "all"
            },

            "development": {
              "optimization": false,
              "extractLicenses": false,
              "sourceMap": true
            }
          },

          "defaultConfiguration": "production"
        },

        "serve": {
          "builder": "@angular-devkit/build-angular:dev-server",

          "configurations": {
            "production": {
              "buildTarget": "ricuz-dashboard:build:production"
            },

            "development": {
              "buildTarget": "ricuz-dashboard:build:development"
            }
          },

          "defaultConfiguration": "development"
        }
      }
    }
  }
}
```

---

# 4. `tsconfig.json`

```json
{
  "compileOnSave": false,

  "compilerOptions": {
    "outDir": "./out-tsc",
    "strict": true,

    "noImplicitOverride": true,
    "noPropertyAccessFromIndexSignature": true,
    "noImplicitReturns": true,
    "noFallthroughCasesInSwitch": true,

    "skipLibCheck": true,
    "esModuleInterop": true,

    "sourceMap": true,
    "declaration": false,

    "experimentalDecorators": true,

    "moduleResolution": "bundler",

    "importHelpers": true,

    "target": "ES2022",
    "module": "ES2022",

    "lib": [
      "ES2022",
      "dom"
    ]
  },

  "angularCompilerOptions": {
    "enableI18nLegacyMessageIdFormat": false,
    "strictInjectionParameters": true,
    "strictInputAccessModifiers": true,
    "strictTemplates": true
  }
}
```

---

# 5. `tsconfig.app.json`

```json
{
  "extends": "./tsconfig.json",

  "compilerOptions": {
    "types": []
  },

  "files": [
    "src/main.ts"
  ],

  "include": [
    "src/**/*.d.ts"
  ]
}
```

---

# 6. `src/index.html`

```html
<!doctype html>

<html
  lang="he"
  dir="rtl">

<head>

  <meta charset="utf-8">

  <title>
    ריקוז - מערכת מסמכים
  </title>

  <base href="/">

  <meta
    name="viewport"
    content="width=device-width, initial-scale=1">

</head>

<body>

  <app-root></app-root>

</body>

</html>
```

---

# 7. `src/main.ts`

```typescript
import { bootstrapApplication } from '@angular/platform-browser';

import { AppComponent } from './app/app.component';

import { appConfig } from './app/app.config';


bootstrapApplication(
  AppComponent,
  appConfig
)
.catch(
  error => console.error(error)
);
```

---

# 8. `src/styles.css`

```css
html,
body {

  margin: 0;

  min-height: 100%;

  font-family:
    Arial,
    "Segoe UI",
    sans-serif;

  background: #eaf2fc;

}

body {

  direction: rtl;

}

button,
input,
select,
textarea {

  font-family: inherit;

}

* {

  box-sizing: border-box;

}
```

---

# 9. `src/app/app.component.ts`

```typescript
import {
  Component
} from '@angular/core';

import {
  RouterOutlet
} from '@angular/router';


@Component({

  selector: 'app-root',

  standalone: true,

  imports: [
    RouterOutlet
  ],

  template: `
    
    <router-outlet></router-outlet>

  `

})
export class AppComponent {

}
```

---

# 10. `src/app/app.config.ts`

```typescript
import {
  ApplicationConfig
} from '@angular/core';

import {
  provideRouter
} from '@angular/router';

import {
  routes
} from './app.routes';


export const appConfig: ApplicationConfig = {

  providers: [

    provideRouter(routes)

  ]

};
```

---

# 11. `src/app/app.routes.ts`

```typescript
import {
  Routes
} from '@angular/router';

import {
  DashboardComponent
} from './dashboard.component';


export const routes: Routes = [

  {
    path: '',
    component: DashboardComponent
  },

  {
    path: '**',
    redirectTo: ''
  }

];
```

---

# 12. `src/app/dashboard.models.ts`

```typescript
export type ServiceId =
  | 'folder'
  | 'document'
  | 'committee'
  | 'branch'
  | 'inspection'
  | 'recent'
  | 'reading-list'
  | 'home';


export interface DashboardService {

  id: ServiceId;

  label: string;

  description: string;

  icon: string;

}


export interface RecentSearch {

  title: string;

  subtitle: string;

  count: number;

}


export interface ReadingItem {

  title: string;

  subtitle: string;

  documents: number;

}
```

---

# 13. `src/app/dashboard.service.ts`

This is the service that controls the dashboard actions.

```typescript
import {
  Injectable
} from '@angular/core';

import {
  DashboardService,
  ReadingItem,
  RecentSearch,
  ServiceId
} from './dashboard.models';


@Injectable({
  providedIn: 'root'
})
export class DashboardServiceApi {


  /*
   * Main search services
   */

  readonly services: DashboardService[] = [

    {
      id: 'folder',
      label: 'חוזה/תיק',
      description: 'איתור תיק או חוזה',
      icon: 'folder'
    },

    {
      id: 'document',
      label: 'מסמך',
      description: 'חיפוש מסמכים',
      icon: 'document'
    },

    {
      id: 'committee',
      label: 'ועדה',
      description: 'חיפוש ועדה',
      icon: 'users'
    },

    {
      id: 'branch',
      label: 'ח.פ',
      description: 'חיפוש לפי ח.פ',
      icon: 'building'
    },

    {
      id: 'inspection',
      label: 'ביקורת פנים',
      description: 'ביקורות וממצאים',
      icon: 'clipboard'
    }

  ];


  /*
   * Recent searches
   */

  readonly recentSearches: RecentSearch[] = [

    {
      title: 'קבצים מתוך חוזה',
      subtitle:
        'חוזה שמתחיל ב-332 · חוזה שמסתיים ב-332',
      count: 5
    },

    {
      title: 'קבצים מתוך חוזה',
      subtitle:
        'חוזה שמתחיל ב-332 · חוזה שמסתיים ב-332',
      count: 5
    },

    {
      title: 'קבצים מתוך חוזה',
      subtitle:
        'חוזה שמתחיל ב-332 · חוזה שמסתיים ב-332',
      count: 5
    },

    {
      title: 'קבצים מתוך חוזה',
      subtitle:
        'חוזה שמתחיל ב-332 · חוזה שמסתיים ב-332',
      count: 5
    }

  ];


  /*
   * Reading list
   */

  readonly readingItems: ReadingItem[] = [

    {
      title: 'מסמכים מתוך ועדה 231231824',
      subtitle: 'תוצאות החיפוש עבור ועדה זו',
      documents: 5
    },

    {
      title: 'מסמכים מתוך ועדה 231231824',
      subtitle: 'תוצאות החיפוש עבור ועדה זו',
      documents: 5
    },

    {
      title: 'מסמכים מתוך ועדה 231231824',
      subtitle: 'תוצאות החיפוש עבור ועדה זו',
      documents: 5
    },

    {
      title: 'מסמכים מתוך ועדה 231231824',
      subtitle: 'תוצאות החיפוש עבור ועדה זו',
      documents: 5
    }

  ];


  /*
   * Button action
   */

  openService(
    id: ServiceId
  ): string {

    const messages:
      Record<ServiceId, string> = {

        home:
          'דף הבית',

        folder:
          'נפתח שירות חיפוש חוזה/תיק',

        document:
          'נפתח שירות חיפוש מסמך',

        committee:
          'נפתח שירות חיפוש ועדה',

        branch:
          'נפתח שירות חיפוש ח.פ',

        inspection:
          'נפתח שירות ביקורת פנים',

        recent:
          'נפתחו החיפושים האחרונים',

        'reading-list':
          'נפתחה רשימת קריאה'

      };


    return messages[id];

  }


  /*
   * Delete item from reading list
   */

  deleteReadingItem(
    index: number
  ): void {

    this.readingItems.splice(
      index,
      1
    );

  }

}
```

---

# 14. `src/app/icon.component.ts`

Instead of installing Font Awesome or another library, this uses SVG icons.

```typescript
import {
  ChangeDetectionStrategy,
  Component,
  input
} from '@angular/core';


@Component({

  selector: 'app-icon',

  standalone: true,

  template: `

    @switch (name()) {

      @case ('folder') {

        <svg viewBox="0 0 24 24">

          <path
            d="M3 6.5A1.5 1.5 0 0 1
               4.5 5H10l2 2h7.5
               A1.5 1.5 0 0 1
               21 8.5v9
               A1.5 1.5 0 0 1
               19.5 19h-15
               A1.5 1.5 0 0 1
               3 17.5v-11Z" />

          <path d="M3 9h18" />

        </svg>

      }


      @case ('document') {

        <svg viewBox="0 0 24 24">

          <path d="M6 3h8l4 4v14H6z" />

          <path d="M14 3v5h5" />

          <path d="M9 13h6" />

          <path d="M9 16h6" />

        </svg>

      }


      @case ('users') {

        <svg viewBox="0 0 24 24">

          <circle
            cx="9"
            cy="8"
            r="3" />

          <circle
            cx="17"
            cy="9"
            r="2.5" />

          <path
            d="M3.5 19c.6-3.2
               2.5-5 5.5-5
               s4.9 1.8 5.5 5" />

          <path
            d="M14 14.5
               c2.8-.7
               5.2 1
               6 3.8" />

        </svg>

      }


      @case ('building') {

        <svg viewBox="0 0 24 24">

          <path
            d="M4 21V6l8-3
               8 3v15" />

          <path d="M8 10h2" />

          <path d="M14 10h2" />

          <path d="M8 14h2" />

          <path d="M14 14h2" />

          <path
            d="M10 21v-3h4v3" />

        </svg>

      }


      @case ('clipboard') {

        <svg viewBox="0 0 24 24">

          <path
            d="M8 5h8" />

          <path
            d="M9 3h6v4H9z" />

          <path
            d="M6 5H4v16h16V5h-2" />

          <path
            d="m8 13
               2 2
               5-5" />

        </svg>

      }


      @case ('search') {

        <svg viewBox="0 0 24 24">

          <circle
            cx="10.5"
            cy="10.5"
            r="6.5" />

          <path
            d="m16 16 5 5" />

        </svg>

      }


      @case ('home') {

        <svg viewBox="0 0 24 24">

          <path
            d="m3 10
               9-7
               9 7v10H5V10" />

          <path
            d="M9 20v-6h6v6" />

        </svg>

      }


      @case ('phone') {

        <svg viewBox="0 0 24 24">

          <path
            d="M6.5 3.5
               9 3l2 5
               -2 1.5
               c1.2 2.4
               3.1 4.3
               5.5 5.5
               L16 13l5 2
               -.5 2.5
               c-.3 1.4
               -1.6 2.5
               -3.1 2.5
               C10.5 20
               4 13.5
               4 6.6
               4 5.1
               5.1 3.8
               6.5 3.5Z" />

        </svg>

      }


      @case ('logout') {

        <svg viewBox="0 0 24 24">

          <path d="M10 4H5v16h5" />

          <path
            d="m14 8
               4 4
               -4 4" />

          <path
            d="M9 12h9" />

        </svg>

      }


      @case ('trash') {

        <svg viewBox="0 0 24 24">

          <path d="M5 7h14" />

          <path
            d="M9 7V4h6v3" />

          <path
            d="M7 7l1 13h8l1-13" />

          <path
            d="M10 10v7" />

          <path
            d="M14 10v7" />

        </svg>

      }


      @case ('arrow') {

        <svg viewBox="0 0 24 24">

          <path
            d="m14 6
               -6 6
               6 6" />

        </svg>

      }


      @case ('info') {

        <svg viewBox="0 0 24 24">

          <circle
            cx="12"
            cy="12"
            r="9" />

          <path d="M12 10v6" />

          <path d="M12 7h.01" />

        </svg>

      }


      @case ('list') {

        <svg viewBox="0 0 24 24">

          <path d="M8 6h12" />

          <path d="M8 12h12" />

          <path d="M8 18h12" />

          <path d="M4 6h.01" />

          <path d="M4 12h.01" />

          <path d="M4 18h.01" />

        </svg>

      }

    }

  `,

  styles: [`

    :host {

      display: inline-flex;

      width: 22px;

      height: 22px;

    }


    svg {

      width: 100%;

      height: 100%;

      fill: none;

      stroke: currentColor;

      stroke-width: 1.65;

      stroke-linecap: round;

      stroke-linejoin: round;

    }

  `],

  changeDetection:
    ChangeDetectionStrategy.OnPush

})
export class IconComponent {

  readonly name =
    input.required<string>();

}
```

---

# 15. `src/app/dashboard.component.ts`

```typescript
import {
  ChangeDetectionStrategy,
  Component,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  DashboardServiceApi
} from './dashboard.service';

import {
  ServiceId
} from './dashboard.models';

import {
  IconComponent
} from './icon.component';


@Component({

  selector: 'app-dashboard',

  standalone: true,

  imports: [
    CommonModule,
    IconComponent
  ],

  templateUrl:
    './dashboard.component.html',

  styleUrl:
    './dashboard.component.css',

  changeDetection:
    ChangeDetectionStrategy.OnPush

})
export class DashboardComponent {


  readonly api =
    inject(DashboardServiceApi);


  toast = '';


  run(
    id: ServiceId
  ): void {

    this.toast =
      this.api.openService(id);


    window.setTimeout(
      () => this.toast = '',
      2200
    );

  }


  deleteReading(
    index: number
  ): void {

    this.api.deleteReadingItem(index);

  }

}
```

---

# 16. `src/app/dashboard.component.html`

This is the main page.

```html
<div class="shell">


  <!-- ================================= -->
  <!-- TOP BAR -->
  <!-- ================================= -->

  <header class="topbar">


    <div class="brand">

      רמיזדוקס

    </div>


    <div class="top-actions">


      <button
        type="button"
        (click)="run('home')">

        <app-icon
          name="logout">
        </app-icon>

        יציאה

      </button>


      <button
        type="button"
        (click)="toast='מרכז העזרה'">

        <app-icon
          name="phone">
        </app-icon>

        עזרה

      </button>


    </div>


    <div class="partner-logo">

      <span class="partner-mark">

        ריקוז

      </span>

      <small>

        מערכת ניהול מסמכים

      </small>

    </div>


  </header>



  <!-- ================================= -->
  <!-- SIDE BAR -->
  <!-- ================================= -->

  <aside class="sidebar">


    <!-- USER -->

    <div class="profile">


      <div class="avatar">

        י

      </div>


      <div>

        <small>

          כינוי טוב

        </small>

        <strong>

          טל מחוזי

        </strong>

      </div>


      <app-icon
        name="list">
      </app-icon>


    </div>



    <!-- HOME -->

    <button
      class="nav-item active"
      type="button"
      (click)="run('home')">


      <app-icon
        name="home">
      </app-icon>


      <span>

        בית

      </span>


    </button>



    <div class="nav-label">

      חיפוש לפי:

    </div>



    <!-- FOLDER -->

    <button
      class="nav-item"
      type="button"
      (click)="run('folder')">


      <app-icon
        name="folder">
      </app-icon>


      חוזה/תיק


    </button>



    <!-- DOCUMENT -->

    <button
      class="nav-item"
      type="button"
      (click)="run('document')">


      <app-icon
        name="document">
      </app-icon>


      מסמך


    </button>



    <!-- COMMITTEE -->

    <button
      class="nav-item"
      type="button"
      (click)="run('committee')">


      <app-icon
        name="users">
      </app-icon>


      ועדה


    </button>



    <!-- COMPANY -->

    <button
      class="nav-item"
      type="button"
      (click)="run('branch')">


      <app-icon
        name="building">
      </app-icon>


      ח.פ


    </button>



    <!-- INSPECTION -->

    <button
      class="nav-item"
      type="button"
      (click)="run('inspection')">


      <app-icon
        name="clipboard">
      </app-icon>


      ביקורת פנים


    </button>


  </aside>



  <!-- ================================= -->
  <!-- MAIN CONTENT -->
  <!-- ================================= -->

  <main class="content">



    <!-- ================================= -->
    <!-- STATISTICS -->
    <!-- ================================= -->

    <section class="stats">


      <article class="stat-card">


        <span>

          צפייה בתיקי מסמכים

        </span>


        <b>

          103

        </b>


        <small>

          בחודש

        </small>


      </article>



      <article class="stat-card">


        <span>

          שעות במערכת

        </span>


        <b>

          32

        </b>


        <small>

          בחודש

        </small>


      </article>



      <article class="stat-card">


        <span>

          כניסות למערכת

        </span>


        <b>

          120

        </b>


        <small>

          בחודש

        </small>


      </article>


    </section>



    <!-- ================================= -->
    <!-- SEARCH SERVICES -->
    <!-- ================================= -->

    <section class="search-panel">


      <h1>

        מה תחפש היום?

      </h1>



      <div class="service-grid">


        @for (
          service of api.services;
          track service.id
        ) {


          <button
            class="service-card"
            type="button"
            (click)="run(service.id)">


            <app-icon
              [name]="service.icon">
            </app-icon>


            <strong>

              {{ service.label }}

            </strong>


            <span>

              {{ service.description }}

            </span>


          </button>


        }


      </div>


    </section>



    <!-- ================================= -->
    <!-- BOTTOM -->
    <!-- ================================= -->

    <section class="bottom-grid">



      <!-- ================================= -->
      <!-- RECENT SEARCHES -->
      <!-- ================================= -->

      <article class="panel recent">


        <div class="panel-title">


          <h2>

            חיפושים אחרונים

          </h2>


          <app-icon
            name="search">
          </app-icon>


        </div>



        <div class="recent-list">


          @for (
            item of api.recentSearches;
            track $index
          ) {


            <button
              class="recent-row"
              type="button"
              (click)="run('recent')">


              <app-icon
                name="folder">
              </app-icon>


              <div>


                <strong>

                  {{ item.title }}

                </strong>


                <p>

                  {{ item.subtitle }}

                </p>


                <small>

                  לפני
                  {{ $index + 1 }}
                  שעות
                  ·
                  נמצאו
                  {{ item.count }}
                  תוצאות

                </small>


              </div>


            </button>


          }


        </div>


      </article>



      <!-- ================================= -->
      <!-- READING LIST -->
      <!-- ================================= -->

      <article class="panel reading">


        <div class="reading-heading">


          <div>


            <h2>

              רשימת קריאה

              <span>

                ({{ api.readingItems.length }})

              </span>

            </h2>


            <a
              href="javascript:void(0)"
              (click)="run('reading-list')">

              ניקוי רשימת קריאה

            </a>


          </div>


        </div>



        <!-- INFO -->

        <div class="notice">


          <app-icon
            name="info">
          </app-icon>


          <span>

            ניתן לשמור מסמך ברשימת הקריאה.
            המסמך עצמו לא יימחק מהמערכת.

          </span>


        </div>



        <!-- ITEMS -->

        <div class="reading-list">


          @for (
            item of api.readingItems;
            track $index
          ) {


            <div class="reading-card">


              <!-- DELETE -->

              <button
                class="delete"
                title="מחיקה"
                type="button"
                (click)="deleteReading($index)">


                <app-icon
                  name="trash">
                </app-icon>


              </button>



              <!-- CONTENT -->

              <div class="reading-main">


                <strong>

                  {{ item.title }}

                </strong>


                <p>

                  {{ item.subtitle }}

                </p>


                <small>

                  לפני
                  {{ $index + 5 }}
                  ימים

                  ·

                  {{ item.documents }}
                  מסמכים

                </small>


              </div>



              <!-- VIEW -->

              <button
                class="view-button"
                type="button"
                (click)="run('reading-list')">


                צפייה במסמכים


                <app-icon
                  name="arrow">
                </app-icon>


              </button>


            </div>


          }


        </div>


      </article>


    </section>


  </main>



  <!-- ================================= -->
  <!-- TOAST -->
  <!-- ================================= -->

  @if (toast) {


    <div
      class="toast"
      role="status">


      {{ toast }}


    </div>


  }


</div>
```

---

# 17. `src/app/dashboard.component.css`

```css
:host {

  display: block;

  min-height: 100vh;

}


/* ========================================= */
/* MAIN */
/* ========================================= */

.shell {

  min-height: 100vh;

  background: #eaf2fc;

  color: #123d67;

  direction: rtl;

}



/* ========================================= */
/* HEADER */
/* ========================================= */

.topbar {

  height: 42px;

  background: #ffffff;

  border-bottom:
    1px solid #d8e0ea;

  display: flex;

  align-items: center;

  position: relative;

  padding: 0 16px;

  z-index: 2;

}



.brand {

  position: absolute;

  right: 16px;

  font-size: 20px;

  font-weight: 800;

  color: #103b66;

}



.top-actions {

  display: flex;

  align-items: center;

  gap: 22px;

  margin-right: 115px;

}



.top-actions button {

  border: 0;

  background: transparent;

  color: #153e66;

  display: flex;

  align-items: center;

  gap: 5px;

  cursor: pointer;

  font: inherit;

  font-size: 13px;

}



.partner-logo {

  position: absolute;

  left: 18px;

  top: 5px;

  display: flex;

  flex-direction: column;

  line-height: 1;

  color: #193f63;

}



.partner-mark {

  font-size: 14px;

  font-weight: 800;

}



.partner-logo small {

  margin-top: 3px;

  font-size: 6px;

  color: #708398;

}



/* ========================================= */
/* SIDEBAR */
/* ========================================= */

.sidebar {

  position: fixed;

  top: 42px;

  right: 0;

  width: 145px;

  height: calc(100vh - 42px);

  background: #ffffff;

  border-left:
    1px solid #d6e0eb;

  z-index: 3;

}



.profile {

  height: 51px;

  border-bottom:
    1px solid #dce4ed;

  display: flex;

  align-items: center;

  gap: 7px;

  padding: 5px 10px;

}



.profile > app-icon {

  margin-right: auto;

  color: #0c75bb;

}



.avatar {

  width: 27px;

  height: 27px;

  border:
    1px solid #c4d3e1;

  border-radius: 50%;

  display: grid;

  place-items: center;

  color: #1a527d;

  font-weight: 700;

}



.profile small,
.profile strong {

  display: block;

}



.profile small {

  font-size: 8px;

  color: #8292a2;

}



.profile strong {

  font-size: 11px;

  color: #244967;

}



/* ========================================= */
/* NAVIGATION */
/* ========================================= */

.nav-item {

  width: 100%;

  min-height: 32px;

  border: 0;

  border-bottom:
    1px solid #edf1f5;

  background: #ffffff;

  color: #294d6e;

  padding: 0 11px;

  display: flex;

  align-items: center;

  gap: 9px;

  cursor: pointer;

  font: inherit;

  font-size: 10px;

  text-align: right;

}



.nav-item app-icon {

  color: #0a7bc2;

  width: 16px;

  height: 16px;

}



.nav-item:hover {

  background: #f4f8fc;

}



.nav-item.active {

  background: #eaf3fd;

  border-right:
    2px solid #0087d7;

  color: #0b4b7b;

  font-weight: 700;

}



.nav-label {

  padding:
    14px 12px 6px;

  color: #8d9aa8;

  font-size: 9px;

}



/* ========================================= */
/* CONTENT */
/* ========================================= */

.content {

  width: calc(100% - 145px);

  margin-right: 145px;

  padding:
    14px 30px 24px;

}



/* ========================================= */
/* STATISTICS */
/* ========================================= */

.stats {

  width: min(738px, 100%);

  margin:
    0 auto 14px;

  display: grid;

  grid-template-columns:
    repeat(3, 1fr);

  gap: 14px;

}



.stat-card {

  height: 80px;

  background: #ffffff;

  border-radius: 9px;

  padding:
    13px 18px;

  position: relative;

}



.stat-card span,
.stat-card small {

  display: block;

  font-size: 9px;

}



.stat-card span {

  color: #5b7590;

  margin-bottom: 3px;

}



.stat-card b {

  display: block;

  font-size: 30px;

  font-weight: 400;

  line-height: 1;

  color: #123f69;

}



.stat-card small {

  position: absolute;

  bottom: 12px;

  right: 18px;

  color: #8192a3;

}



/* ========================================= */
/* SEARCH PANEL */
/* ========================================= */

.search-panel {

  width: min(738px, 100%);

  margin:
    0 auto 14px;

  background: #ffffff;

  border-radius: 10px;

  padding:
    13px 15px 14px;

}



.search-panel h1 {

  margin:
    0 0 12px;

  font-size: 15px;

  text-align: right;

  color: #173f65;

}



/* ========================================= */
/* SERVICES */
/* ========================================= */

.service-grid {

  display: grid;

  grid-template-columns:
    repeat(5, 1fr);

  gap: 12px;

}



.service-card {

  height: 76px;

  border: 0;

  border-radius: 5px;

  background: #f0f5fb;

  color: #0a5789;

  cursor: pointer;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  gap: 5px;

  font: inherit;

  transition:
    transform .15s,
    background .15s;

}



.service-card:hover {

  transform: translateY(-2px);

  background: #e7f1fc;

}



.service-card app-icon {

  width: 21px;

  height: 21px;

}



.service-card strong {

  font-size: 11px;

}



/* ========================================= */
/* BOTTOM */
/* ========================================= */

.bottom-grid {

  width: min(738px, 100%);

  margin: 0 auto;

  display: grid;

  grid-template-columns:
    1fr 1.2fr;

  gap: 14px;

  direction: ltr;

}



.panel {

  background: #ffffff;

  border-radius: 8px;

  min-height: 328px;

  overflow: hidden;

  direction: rtl;

}



/* ========================================= */
/* RECENT SEARCHES */
/* ========================================= */

.panel-title {

  height: 49px;

  padding:
    12px 16px;

  display: flex;

  align-items: center;

  justify-content: space-between;

}



.panel-title h2,
.reading-heading h2 {

  margin: 0;

  color: #173f65;

  font-size: 14px;

}



.panel-title app-icon {

  color: #0a78bd;

}



.recent-list {

  padding:
    0 15px 8px;

}



.recent-row {

  width: 100%;

  min-height: 68px;

  padding: 8px 0;

  border: 0;

  border-bottom:
    1px solid #edf1f5;

  background: transparent;

  color: #173f65;

  cursor: pointer;

  display: flex;

  align-items: flex-start;

  gap: 8px;

  text-align: right;

  font: inherit;

}



.recent-row app-icon {

  width: 17px;

  height: 17px;

  color: #0a87d0;

  margin-top: 2px;

}



.recent-row strong {

  display: block;

  font-size: 10px;

  margin-bottom: 3px;

}



.recent-row p,
.recent-row small {

  display: block;

  margin: 0;

  font-size: 8px;

}



.recent-row p {

  color: #4b6682;

}



.recent-row small {

  color: #93a2b1;

  margin-top: 3px;

}



/* ========================================= */
/* READING LIST */
/* ========================================= */

.reading-heading {

  height: 49px;

  padding:
    12px 16px;

}



.reading-heading > div {

  width: 100%;

  display: flex;

  align-items: center;

  justify-content: space-between;

}



.reading-heading a {

  color: #0784cd;

  text-decoration: none;

  font-size: 9px;

  cursor: pointer;

}



/* ========================================= */
/* NOTICE */
/* ========================================= */

.notice {

  margin:
    0 15px 9px;

  min-height: 35px;

  border:
    1px solid #b8c9db;

  background: #f2f6fa;

  border-radius: 5px;

  display: flex;

  align-items: center;

  gap: 6px;

  padding:
    5px 8px;

  color: #46627e;

  font-size: 8px;

}



.notice app-icon {

  width: 13px;

  height: 13px;

}



/* ========================================= */
/* READING CARDS */
/* ========================================= */

.reading-list {

  padding:
    0 15px 12px;

}



.reading-card {

  min-height: 96px;

  border:
    1px solid #cbd9e6;

  border-radius: 5px;

  margin-bottom: 9px;

  padding: 11px 12px;

  position: relative;

  display: flex;

  flex-direction: column;

}



.delete {

  position: absolute;

  top: 10px;

  left: 10px;

  border: 0;

  background: transparent;

  color: #6683a0;

  cursor: pointer;

  padding: 0;

}



.delete app-icon {

  width: 15px;

  height: 15px;

}



.reading-main {

  padding-left: 24px;

}



.reading-main strong {

  font-size: 10px;

  display: block;

  margin-bottom: 4px;

}



.reading-main p,
.reading-main small {

  display: block;

  margin: 0;

  font-size: 8px;

}



.reading-main p {

  color: #4d6681;

}



.reading-main small {

  color: #8fa0b0;

  margin-top: 4px;

}



/* ========================================= */
/* VIEW BUTTON */
/* ========================================= */

.view-button {

  margin-top: 9px;

  height: 29px;

  border:
    1px solid #1595dc;

  border-radius: 5px;

  background: #ffffff;

  color: #0786cc;

  font: inherit;

  font-size: 8px;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 5px;

}



.view-button:hover {

  background: #f0f8ff;

}



.view-button app-icon {

  width: 12px;

  height: 12px;

}



/* ========================================= */
/* TOAST */
/* ========================================= */

.toast {

  position: fixed;

  bottom: 22px;

  left: 50%;

  transform:
    translateX(-50%);

  background: #123f69;

  color: #ffffff;

  border-radius: 7px;

  padding:
    10px 18px;

  font-size: 12px;

  box-shadow:
    0 8px 25px
    rgba(20, 60, 95, .2);

  z-index: 20;

}



/* ========================================= */
/* TABLET */
/* ========================================= */

@media (max-width: 850px) {


  .sidebar {

    width: 110px;

  }


  .content {

    width:
      calc(100% - 110px);

    margin-right: 110px;

    padding: 12px;

  }


  .service-grid {

    grid-template-columns:
      repeat(3, 1fr);

  }


  .bottom-grid {

    grid-template-columns: 1fr;

  }

}



/* ========================================= */
/* MOBILE */
/* ========================================= */

@media (max-width: 600px) {


  .sidebar {

    width: 0;

    overflow: hidden;

    border: 0;

  }


  .content {

    width: 100%;

    margin-right: 0;

    padding: 10px;

  }


  .brand {

    right: 10px;

  }


  .top-actions {

    margin-right: 100px;

  }


  .stats {

    grid-template-columns: 1fr;

  }


  .stat-card {

    height: 68px;

  }


  .service-grid {

    grid-template-columns:
      repeat(2, 1fr);

  }

}
```

---

# Run

From the project directory:

```bash
npm install
```

Then:

```bash
npm start
```

Open:

```text
http://localhost:4200
```

### The important part for adding your real backend

Right now the buttons call:

```typescript
run(id: ServiceId)
```

which calls:

```typescript
this.api.openService(id)
```

Later, the `DashboardServiceApi` can be changed to Angular `HttpClient`, for example:

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DashboardServiceApi {

  private http = inject(HttpClient);

  searchDocuments(value: string) {

    return this.http.get(
      `/api/documents/search`,
      {
        params: {
          value
        }
      }
    );

  }

}
```

Then the **חוזה/תיק**, **מסמך**, **ועדה**, **ח.פ**, **ביקורת פנים**, **רשימת קריאה**, and statistics can each be connected to your Java/Spring backend.

The complete project is also here:

[Download the Angular 19.1.7 project ZIP](sandbox:/mnt/data/ricuz-angular19-dashboard.zip)

----

#### share link
* https://chatgpt.com/s/t_6aaaabf44e088191aafeddde1479e0ed
* https://chatgpt.com/backend-api/estuary/content?id=file_0000000007988206be29340ce1e719a0&fn=ricuz-angular19-dashboard.zip&cd=attachment&ts=497102&p=fs&cid=1&sig=8d299e946ae3d51998bf0faf5e19cc8f34a2dffb1c436fd1a993d6839c070787&v=0

