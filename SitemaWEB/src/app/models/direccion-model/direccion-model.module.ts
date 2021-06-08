import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class DireccionModelModule {
  id: number;
  direccion: string;
  localidad: string;
  municipio: string;
  constructor() {
     this.id = 0;
     this.direccion = '';
     this.localidad = '';
     this.municipio = '';
   }
}
