import Swal from 'sweetalert2';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import { PuntosModelModule } from '../../models/puntos-model/puntos-model.module';

@Component({
  selector: 'app-admin-puntos',
  templateUrl: './admin-puntos.component.html',
  styleUrls: ['./admin-puntos.component.scss']
})
export class AdminPuntosComponent implements OnInit {

  puntosModel: PuntosModelModule = new PuntosModelModule();
  formData: FormData = new FormData();
  itemDatosRutas: any;
  listDatosRutas: any;
  itemDatos: any;
  listDatos: any;

  constructor(
    private apiService: ApiServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {
    this.getListaDetalles();
  }

  getListaDetalles() {
    this.formData.append('id', this.id);
    if (this.id === 'add') {

    } else {
      this.apiService.listaPuntosVentasID(this.formData).subscribe(
        res => {
          this.itemDatos = res;
          this.listDatos = this.itemDatos['Puntos'];
        }
      );
    }
    this.apiService.listaRutas().subscribe(
      res => {
        this.itemDatosRutas = res;
        this.listDatosRutas = this.itemDatosRutas['Rutas'];
      }
    );
  }

  cargar_imagen(event: any) {
    let elemento = event.target;
    if (elemento.files.length > 0) {
      this.formData.append('foto', elemento.files[0]);
    }
  }

  mensaje(mensaje: string, header: string){
    Swal.fire(
      header,
      mensaje,
      'success'
    );
  }

  guardarDatos() {
    this.formData.append('id', this.id);
    if (this.listDatos.idRuta !== this.puntosModel.idRuta && this.puntosModel.idRuta > 0) {
      this.formData.append('idRuta', this.puntosModel.idRuta.toString());
    } else {
      this.formData.append('idRuta', this.listDatos.idRuta);
    }
    this.formData.append('nombre', this.puntosModel.nombre);
    if (this.id === 'add') {
      this.apiService.crearPuntoVenta(this.formData).subscribe(
        res => {
          this.itemDatos = res;
          this.listDatos = this.itemDatos['Mensaje'];
          this.mensaje(this.listDatos, '');
          this.router.navigateByUrl('/puntos');
        }
      );
    } else {
      this.apiService.editarPuntoVenta(this.formData).subscribe(
        res => {
          this.itemDatos = res;
          this.listDatos = this.itemDatos['Mensaje'];
          this.mensaje(this.listDatos, '');
          this.router.navigateByUrl('/puntos');
        }
      );
    }
  }
}
