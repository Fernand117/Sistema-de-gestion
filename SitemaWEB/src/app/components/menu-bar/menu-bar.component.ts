import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.scss']
})
export class MenuBarComponent implements OnInit {
  datos: any;
  usuario: any;

  constructor() { }

  ngOnInit(): void {
    this.getDatosUser();
  }

  getDatosUser() {
    this.datos = localStorage.getItem('Usuario');
    this.usuario = JSON.parse(this.datos);
  }
}
