import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { InicioComponent } from './pages/inicio/inicio.component';
import { RutasComponent } from './pages/rutas/rutas.component';
import { AdminRutasComponent } from './pages/admin-rutas/admin-rutas.component';
import { PuntosVentasComponent } from './pages/puntos-ventas/puntos-ventas.component';
import { AdminPuntosComponent } from './pages/admin-puntos/admin-puntos.component';
import { VendedoresComponent } from './pages/vendedores/vendedores.component';
import { AdminVendedoresComponent } from './pages/admin-vendedores/admin-vendedores.component';
import { TirosComponent } from './pages/tiros/tiros.component';

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
    path: 'admin/rutas/:id', component: AdminRutasComponent
  },
  {
    path: 'puntos', component: PuntosVentasComponent
  },
  {
    path: 'admin/puntos/:id', component: AdminPuntosComponent
  },
  {
    path: 'vendedores', component: VendedoresComponent
  },
  {
    path: 'tiros', component: TirosComponent
  },
  {
    path: 'admin/vendedor/:id', component: AdminVendedoresComponent
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
