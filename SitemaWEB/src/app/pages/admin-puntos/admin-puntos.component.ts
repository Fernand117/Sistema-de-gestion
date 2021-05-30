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

    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
  }

  getListaDetalles() {
    this.formData.append('id', this.id);
    if (this.id === 'add') {
      this.listDatos = this.id;
    } else {
      this.apiService.listaPuntosVentasID(this.formData).subscribe(
        res => {
          this.itemDatos = res;
          this.listDatos = this.itemDatos['Puntos'][0];
          console.log(this.listDatos.Punto)
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
    if (this.listDatos[0]['IDRuta'] !== this.puntosModel.idRuta && this.puntosModel.idRuta > 0) {
      this.formData.append('idRuta', this.puntosModel.idRuta.toString());
    } else {
      this.formData.append('idRuta', this.listDatos[0]['IDRuta']);
    }

    if (this.listDatos[0]['Punto'] !== this.puntosModel.nombre && this.puntosModel.nombre !== ''){
      this.formData.append('nombre', this.puntosModel.nombre);
    } else {
      this.formData.append('nombre', this.listDatos[0]['Punto']);
    }
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
