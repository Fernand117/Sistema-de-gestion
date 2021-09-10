import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuBarComponent } from './components/menu-bar/menu-bar.component';
import { LoginComponent } from './pages/login/login.component';
import { InicioComponent } from './pages/inicio/inicio.component';
import { FormsModule } from '@angular/forms';
import { RutasComponent } from './pages/rutas/rutas.component';
import { HeaderComponent } from './components/header/header.component';
import { AdminRutasComponent } from './pages/admin-rutas/admin-rutas.component';
import { PuntosVentasComponent } from './pages/puntos-ventas/puntos-ventas.component';
import { AdminPuntosComponent } from './pages/admin-puntos/admin-puntos.component';
import { VendedoresComponent } from './pages/vendedores/vendedores.component';
import { AdminVendedoresComponent } from './pages/admin-vendedores/admin-vendedores.component';
import { NgFallimgModule } from 'ng-fallimg';
import { TirosComponent } from './pages/tiros/tiros.component';
import { AdminTirosComponent } from './pages/admin-tiros/admin-tiros.component';
import { ReporteVentaComponent } from './pages/reporte-venta/reporte-venta.component';
import { ExcelService } from './services/excel.service';

@NgModule({
  declarations: [
    AppComponent,
    MenuBarComponent,
    LoginComponent,
    InicioComponent,
    RutasComponent,
    HeaderComponent,
    AdminRutasComponent,
    PuntosVentasComponent,
    AdminPuntosComponent,
    VendedoresComponent,
    AdminVendedoresComponent,
    TirosComponent,
    AdminTirosComponent,
    ReporteVentaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgFallimgModule.forRoot({
      default: '/assets/default_user.jpg',
      profile: '/assets/default_user.jpg'
    }),
    FormsModule,
    HttpClientModule
  ],
  providers: [ExcelService],
  bootstrap: [AppComponent]
})
export class AppModule { }
