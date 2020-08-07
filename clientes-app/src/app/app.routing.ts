import { Routes } from '@angular/router';
import { ClientsComponent } from './clients/clients.component';
import { ErrorComponent } from './error/error.component';
import { FormComponent } from './clients/form/form.component';

export const ROUTES: Routes = [
    {path: '', redirectTo: '/clients', pathMatch: 'full'},
    {path: 'clients', component: ClientsComponent},
    {path: 'clients/form', component: FormComponent },
    {path: 'client/:id', component: FormComponent},

    {path: '**', component: ErrorComponent}
  ];
