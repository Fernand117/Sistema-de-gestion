import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  datos: any;
  usuario: any;

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getDatosUser();
    if (this.datos === null) {
      this.router.navigateByUrl('/login');
    }
  }

  getDatosUser() {
    this.datos = localStorage.getItem('Usuario');
    this.usuario = JSON.parse(this.datos);
  }
}
