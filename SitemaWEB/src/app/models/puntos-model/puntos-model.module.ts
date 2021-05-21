import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class PuntosModelModule {
  id: number;
  nombre: string;
  foto: Object;
  idRuta: number;

  constructor() {
    this.id = 0;
    this.nombre = "";
    this.foto = Object;
    this.idRuta = 0;
  }
}
