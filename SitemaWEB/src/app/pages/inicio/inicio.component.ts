import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  datos: any;
  usuario: any;

  constructor() { }

  ngOnInit(): void {
    this.getDatosUser();
  }

  getDatosUser() {
    this.datos = localStorage.getItem('Usuario');
    this.usuario = JSON.parse(this.datos);
    console.log(this.usuario[0]);
  }
}
