Here’s an example of how you can create a simple Angular app with a navbar containing 3 menu items that navigate to different pages on click. This example uses Angular's built-in routing:

### Steps to Create

#### 1. **Set up Angular Routing**
Generate a new Angular project (if you don't have one):
```bash
ng new navbar-example
cd navbar-example
```

Add the Angular Router when prompted or install it:
```bash
ng add @angular/router
```

#### 2. **Generate Components**
Create three components for the pages:
```bash
ng generate component home
ng generate component about
ng generate component contact
```

#### 3. **Update `app-routing.module.ts`**
Configure routes for the pages in the routing module:
```typescript
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';

const routes: Routes = [
  { path: '', component: HomeComponent }, // Default route
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

#### 4. **Add Navbar in `app.component.html`**
Here’s an example of the navbar menu:
```html
<nav>
  <ul>
    <li><a routerLink="/">Home</a></li>
    <li><a routerLink="/about">About</a></li>
    <li><a routerLink="/contact">Contact</a></li>
  </ul>
</nav>

<!-- Add this to load the page content dynamically -->
<router-outlet></router-outlet>
```

#### 5. **Style the Navbar (Optional)**
You can add some CSS to style the navbar. For example, in `styles.css` or `app.component.css`:
```css
nav {
  background-color: #333;
  color: white;
  padding: 1rem;
}

nav ul {
  list-style: none;
  display: flex;
  gap: 1rem;
}

nav a {
  text-decoration: none;
  color: white;
}

nav a:hover {
  text-decoration: underline;
}
```

#### 6. **Run the App**
Start your Angular app with:
```bash
ng serve
```

Visit `http://localhost:4200` in your browser, and you’ll see the navbar. Clicking on **Home**, **About**, or **Contact** changes the page content dynamically!

Let me know if you need help extending this further. 😊
