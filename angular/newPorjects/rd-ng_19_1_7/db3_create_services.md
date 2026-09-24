# db3_create_services.md

Yes. Let's change the Angular application so **every button calls an HTTPS REST service**, displays the returned value, and the parameters increase for each service.

For now we'll use example HTTPS URLs. Later you can replace them with your real Java/Spring URLs.

The flow will be:

```text
Angular Dashboard
       │
       │ HTTPS GET
       ▼
Service 1 → 1 parameter
       │
       ▼
Service 2 → 2 parameters
       │
       ▼
Service 3 → 3 parameters
       │
       ▼
Service 4 → 4 parameters
       │
       ▼
Service 5 → 5 parameters
       │
       ▼
Hidden parameters → HTTPS POST
       │
       ▼
       OK / NOT OK
```

---

# 1. Enable Angular HttpClient

Open:

```text
src/app/app.config.ts
```

Change it to:

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {

  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]

};
```

The important addition is:

```typescript
import { provideHttpClient } from '@angular/common/http';
```

and:

```typescript
provideHttpClient()
```

---

# 2. Replace `dashboard.service.ts`

Open:

```text
src/app/dashboard.service.ts
```

Replace the whole file with:

```typescript
import { Injectable, inject } from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

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


  private readonly http =
    inject(HttpClient);


  /*
   * Change this later to your real HTTPS server.
   *
   * Example:
   *
   * https://myserver.company.com/api
   */
  private readonly baseUrl =
    'https://localhost:8443/api';


  /*
   * ==========================================
   * EXISTING DASHBOARD DATA
   * ==========================================
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
    }

  ];


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
    }

  ];


  /*
   * ==========================================
   * SERVICE 1
   * חוזה/תיק
   *
   * ONE PARAMETER
   * ==========================================
   */

  searchFolder(
    folderId: string
  ): Observable<string> {

    const params =
      new HttpParams()
        .set('folderId', folderId);


    return this.http.get<string>(
      `${this.baseUrl}/folder`,
      {
        params
      }
    );

  }


  /*
   * ==========================================
   * SERVICE 2
   * מסמך
   *
   * TWO PARAMETERS
   * ==========================================
   */

  searchDocument(
    documentId: string,
    documentType: string
  ): Observable<string> {

    const params =
      new HttpParams()
        .set('documentId', documentId)
        .set('documentType', documentType);


    return this.http.get<string>(
      `${this.baseUrl}/document`,
      {
        params
      }
    );

  }


  /*
   * ==========================================
   * SERVICE 3
   * ועדה
   *
   * THREE PARAMETERS
   * ==========================================
   */

  searchCommittee(
    committeeId: string,
    year: string,
    status: string
  ): Observable<string> {

    const params =
      new HttpParams()
        .set('committeeId', committeeId)
        .set('year', year)
        .set('status', status);


    return this.http.get<string>(
      `${this.baseUrl}/committee`,
      {
        params
      }
    );

  }


  /*
   * ==========================================
   * SERVICE 4
   * ח.פ
   *
   * FOUR PARAMETERS
   * ==========================================
   */

  searchCompany(
    companyId: string,
    companyName: string,
    year: string,
    status: string
  ): Observable<string> {

    const params =
      new HttpParams()
        .set('companyId', companyId)
        .set('companyName', companyName)
        .set('year', year)
        .set('status', status);


    return this.http.get<string>(
      `${this.baseUrl}/company`,
      {
        params
      }
    );

  }


  /*
   * ==========================================
   * SERVICE 5
   * ביקורת פנים
   *
   * FIVE PARAMETERS
   * ==========================================
   */

  searchInspection(
    inspectionId: string,
    department: string,
    year: string,
    status: string,
    inspector: string
  ): Observable<string> {

    const params =
      new HttpParams()
        .set('inspectionId', inspectionId)
        .set('department', department)
        .set('year', year)
        .set('status', status)
        .set('inspector', inspector);


    return this.http.get<string>(
      `${this.baseUrl}/inspection`,
      {
        params
      }
    );

  }


  /*
   * ==========================================
   * FINAL SERVICE
   *
   * HIDDEN PARAMETERS
   *
   * POST
   * ==========================================
   */

  sendHiddenParameters(): Observable<string> {

    const hiddenParameters = {

      systemId: 'ANGULAR',

      clientId: 'WEB',

      requestType: 'DASHBOARD',

      source: 'RICUZ',

      version: '1.0'

    };


    return this.http.post<string>(
      `${this.baseUrl}/hidden`,
      hiddenParameters
    );

  }


  /*
   * ==========================================
   * DELETE READING ITEM
   * ==========================================
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

# 3. Important: the parameters are different for each service

We now have:

### 1. חוזה/תיק

```typescript
searchFolder(
  folderId: string
)
```

One parameter:

```text
folderId
```

Request:

```text
GET https://localhost:8443/api/folder?folderId=123
```

---

### 2. מסמך

```typescript
searchDocument(
  documentId: string,
  documentType: string
)
```

Two parameters:

```text
documentId
documentType
```

Request:

```text
GET https://localhost:8443/api/document?documentId=123&documentType=PDF
```

---

### 3. ועדה

```typescript
searchCommittee(
  committeeId: string,
  year: string,
  status: string
)
```

Three parameters.

---

### 4. ח.פ

```typescript
searchCompany(
  companyId: string,
  companyName: string,
  year: string,
  status: string
)
```

Four parameters.

---

### 5. ביקורת פנים

```typescript
searchInspection(
  inspectionId: string,
  department: string,
  year: string,
  status: string,
  inspector: string
)
```

Five parameters.

---

# 4. Now change `dashboard.component.ts`

Open:

```text
src/app/dashboard/dashboard.component.ts
```

Replace it with:

```typescript
import {
  ChangeDetectionStrategy,
  Component,
  inject
} from '@angular/core';

import {
  DashboardServiceApi
} from '../dashboard.service';

import {
  ServiceId
} from '../dashboard.models';

import {
  IconComponent
} from '../icon/icon.component';


@Component({

  selector: 'app-dashboard',

  standalone: true,

  imports: [
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


  /*
   * Message displayed on screen
   */

  resultMessage = '';


  /*
   * Loading indicator
   */

  loading = false;


  /*
   * ==========================================
   * FOLDER
   * ONE PARAMETER
   * ==========================================
   */

  callFolder(): void {

    this.loading = true;

    this.resultMessage =
      'Calling folder service...';


    this.api
      .searchFolder('12345')
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Folder service: ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Folder service: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * DOCUMENT
   * TWO PARAMETERS
   * ==========================================
   */

  callDocument(): void {

    this.loading = true;

    this.resultMessage =
      'Calling document service...';


    this.api
      .searchDocument(
        '12345',
        'PDF'
      )
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Document service: ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Document service: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * COMMITTEE
   * THREE PARAMETERS
   * ==========================================
   */

  callCommittee(): void {

    this.loading = true;

    this.resultMessage =
      'Calling committee service...';


    this.api
      .searchCommittee(
        '231231824',
        '2026',
        'OPEN'
      )
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Committee service: ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Committee service: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * COMPANY
   * FOUR PARAMETERS
   * ==========================================
   */

  callCompany(): void {

    this.loading = true;

    this.resultMessage =
      'Calling company service...';


    this.api
      .searchCompany(
        '515555555',
        'Test Company',
        '2026',
        'ACTIVE'
      )
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Company service: ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Company service: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * INSPECTION
   * FIVE PARAMETERS
   * ==========================================
   */

  callInspection(): void {

    this.loading = true;

    this.resultMessage =
      'Calling inspection service...';


    this.api
      .searchInspection(
        'INS-100',
        'Finance',
        '2026',
        'OPEN',
        'Yaron'
      )
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Inspection service: ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Inspection service: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * HIDDEN PARAMETERS POST
   * ==========================================
   */

  callHiddenPost(): void {

    this.loading = true;

    this.resultMessage =
      'Sending hidden parameters...';


    this.api
      .sendHiddenParameters()
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.resultMessage =
            `Hidden POST: OK - ${response}`;

        },

        error: (error) => {

          this.loading = false;

          console.error(error);

          this.resultMessage =
            'Hidden POST: NOT OK';

        }

      });

  }


  /*
   * ==========================================
   * BUTTON DISPATCHER
   * ==========================================
   */

  run(
    id: ServiceId
  ): void {

    switch (id) {

      case 'folder':

        this.callFolder();

        break;


      case 'document':

        this.callDocument();

        break;


      case 'committee':

        this.callCommittee();

        break;


      case 'branch':

        this.callCompany();

        break;


      case 'inspection':

        this.callInspection();

        break;


      case 'recent':

        this.resultMessage =
          'Recent searches';

        break;


      case 'reading-list':

        this.resultMessage =
          'Reading list';

        break;


      case 'home':

        this.resultMessage =
          'Home';

        break;

    }

  }


  deleteReading(
    index: number
  ): void {

    this.api.deleteReadingItem(index);

  }

}
```

---

# 5. The buttons already call `run()`

Your existing HTML has:

```html
(click)="run(service.id)"
```

so you don't have to change the service cards.

For example:

```html
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

</button>
```

Now:

```text
חוזה/תיק
     ↓
run('folder')
     ↓
callFolder()
     ↓
searchFolder('12345')
     ↓
HTTPS GET
```

---

# 6. Display the result on the screen

Your existing toast is:

```html
@if (toast) {

  <div class="toast">
    {{ toast }}
  </div>

}
```

Change it to:

```html
@if (resultMessage) {

  <div class="toast">

    @if (loading) {

      <span>
        ⏳
      </span>

    }

    {{ resultMessage }}

  </div>

}
```

And make sure you are **not using `toast` anymore**.

---

# 7. Add a permanent result area

I recommend also putting this directly under:

```html
<section class="search-panel">
```

Add:

```html
@if (resultMessage) {

  <div class="api-result">

    <strong>
      Service result:
    </strong>

    <span>
      {{ resultMessage }}
    </span>

  </div>

}
```

So your search panel becomes:

```html
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

      </button>

    }

  </div>


  @if (resultMessage) {

    <div class="api-result">

      <strong>
        Service result:
      </strong>

      <span>
        {{ resultMessage }}
      </span>

    </div>

  }

</section>
```

---

# 8. Add CSS for the result

At the bottom of:

```text
src/app/dashboard/dashboard.component.css
```

add:

```css
.api-result {
  margin-top: 14px;

  min-height: 42px;

  border: 1px solid #b9ccdf;

  border-radius: 6px;

  background: #f5f9fd;

  color: #244e72;

  display: flex;

  align-items: center;

  gap: 8px;

  padding: 8px 12px;

  font-size: 11px;
}


.api-result strong {
  color: #087bc1;
}


.toast {
  position: fixed;

  bottom: 22px;

  left: 50%;

  transform:
    translateX(-50%);

  background: #123f69;

  color: white;

  border-radius: 7px;

  padding: 10px 18px;

  font-size: 12px;

  z-index: 20;
}
```

---

# 9. The final hidden POST

The last request is deliberately different.

It sends:

```json
{
  "systemId": "ANGULAR",
  "clientId": "WEB",
  "requestType": "DASHBOARD",
  "source": "RICUZ",
  "version": "1.0"
}
```

using:

```typescript
this.http.post<string>(
  `${this.baseUrl}/hidden`,
  hiddenParameters
);
```

So the request is:

```text
POST https://localhost:8443/api/hidden
Content-Type: application/json
```

with the JSON body.

The user doesn't see these parameters in the dashboard.

---

## 10. For testing, you need a backend

Right now:

```typescript
private readonly baseUrl =
  'https://localhost:8443/api';
```

is only an example.

If you click the buttons now, the browser will attempt:

```text
https://localhost:8443/api/folder?folderId=12345
```

If your Java service isn't running there, you'll get:

```text
Folder service: NOT OK
```

That's expected.

For example, if your Java server returns:

```text
OK
```

then Angular displays:

```text
Folder service: OK
```

If it returns:

```text
Folder 12345 found
```

Angular displays:

```text
Folder service: Folder 12345 found
```

---

## One important point about HTTPS + Angular

If your Angular application runs on:

```text
http://localhost:4200
```

and the Java backend runs on:

```text
https://localhost:8443
```

the browser can enforce **CORS** and HTTPS certificate rules.

For development, your Java backend should allow the Angular origin:

```text
http://localhost:4200
```

For production, both should use your proper HTTPS domain/certificate.

### Your resulting architecture

```text
                    Angular 19.1.7
                         │
                         │
                DashboardComponent
                         │
                         ▼
                 DashboardServiceApi
                         │
          ┌──────────────┼──────────────┐
          │              │              │
       GET /folder   GET /document   GET /committee
       1 parameter   2 parameters    3 parameters
          │              │              │
          ├──────────────┼──────────────┤
          │              │
       GET /company   GET /inspection
       4 parameters   5 parameters
          │              │
          └──────────────┼──────────────┘
                         │
                         ▼
                  POST /hidden
                  hidden parameters
                         │
                         ▼
                    OK / NOT OK
```

The next logical step is to make each button **open its own Angular search dialog/page and collect the parameters from the user**, rather than the current test values such as `'12345'`, `'PDF'`, and `'2026'`.
