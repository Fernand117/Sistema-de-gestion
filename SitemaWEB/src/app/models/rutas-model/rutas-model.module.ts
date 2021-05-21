import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class RutasModelModule {
  idVendedor: string;
  nombre: string;
  constructor() {
    this.idVendedor = "";
    this.nombre = "";
  }
}
