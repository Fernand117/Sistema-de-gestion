import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { InicioComponent } from './pages/inicio/inicio.component';
import { RutasComponent } from './pages/rutas/rutas.component';

const routes: Routes = [
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'inicio', component: InicioComponent
  },
  {
    path: 'rutas', component: RutasComponent
  },
  {
    path: '**', redirectTo: 'login', pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
