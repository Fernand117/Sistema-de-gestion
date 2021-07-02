import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {ApiServiceService} from 'src/app/services/api-service.service';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  datos: any;
  usuario: any;

  dtsTotal: any;
  totalPuntos: any;
  totalRutas: any;
  totalUsuarios: any;
  listaPuntos: any;
  formData: FormData = new FormData();

  constructor(
    private router: Router,
    private apiService: ApiServiceService
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
    this.formData.append('idUsuario', '');
    this.formData.append('usuario', '');
    this.apiService.totalDatosVendedor(this.formData).subscribe(
      res => {
        this.dtsTotal = res;
        this.totalPuntos = this.dtsTotal['Puntos'];
        this.totalRutas = this.dtsTotal['Rutas'];
        this.totalUsuarios = this.dtsTotal['Usuarios'];
        this.listaPuntos = this.dtsTotal['Lista'];
        console.log(this.dtsTotal);
      });
  }
}
