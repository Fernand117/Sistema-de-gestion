import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.scss']
})
export class MenuBarComponent implements OnInit {
  datos: any;
  usuario: any;

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getDatosUser();
  }

  getDatosUser() {
    this.datos = localStorage.getItem('Usuario');
    this.usuario = JSON.parse(this.datos);
  }

  closeSession() {
    Swal.fire({
      title: 'Alerta',
      text: "Desea terminar la sesión?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmar'
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.removeItem('Usuario');
        this.router.navigateByUrl('/login');
      }});
  }
}
