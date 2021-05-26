import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class RutasModelModule {
  idVendedor: number;
  nombre: string;
  constructor() {
    this.idVendedor = 0;
    this.nombre = "";
  }
}
