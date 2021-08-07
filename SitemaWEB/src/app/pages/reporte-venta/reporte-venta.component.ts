import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-reporte-venta',
  templateUrl: './reporte-venta.component.html',
  styleUrls: ['./reporte-venta.component.scss']
})
export class ReporteVentaComponent implements OnInit {

  formData: FormData = new FormData();
  today = new Date();
  jstoday: any;
  fecha: any;
  resDatos: any;
  resItem: any;
  totalItem: any;
  msjError: any;

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {
    this.reporteVenta();
  }

  reporteVenta(): void {
    this.formData.append('idUsuario', this.id);
    this.jstoday = formatDate(this.today, 'yyyy-MM-dd', 'en-Us', 'GMT-5');
    if (this.fecha !== 'undefined') {
      this.formData.append('fecha', this.fecha);
    } else {
      this.formData.append('fecha', this.jstoday.toString());
    }

    this.apiService.reporteVenta(this.formData).subscribe(
      res => {
        this.resDatos = res;
        this.resItem = this.resDatos['Tiros'];
        this.totalItem = this.resDatos['Total'];
        document.getElementById('msj')!.innerHTML = '';
      }, err => {
        if (err['status'] === 404) {
          document.getElementById('msj')!.innerHTML = err['error']['Mensaje'];
        } else {
          document.getElementById('msj')!.innerHTML = 'Ocurrió un error en el servidor!!';
        }
      }
    );
  }
}
