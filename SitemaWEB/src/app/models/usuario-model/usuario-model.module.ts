import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class UsuarioModelModule {
  nombre: string;
  paterno: string;
  materno: string;
  fecha_nac: string;
  usuario: string;
  clave: string;
  foto: Object;
  idTipo: number;

  constructor(){
    this.nombre = "";
    this.paterno = "";
    this.materno = "";
    this.fecha_nac = "";
    this.usuario = "";
    this.clave = "";
    this.foto = Object;
    this.idTipo = 0;
  }
}
