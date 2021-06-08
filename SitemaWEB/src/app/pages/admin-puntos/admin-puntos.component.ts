import Swal from 'sweetalert2';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import { PuntosModelModule } from '../../models/puntos-model/puntos-model.module';
import { DireccionModelModule } from '../../models/direccion-model/direccion-model.module';

@Component({
  selector: 'app-admin-puntos',
  templateUrl: './admin-puntos.component.html',
  styleUrls: ['./admin-puntos.component.scss']
})
export class AdminPuntosComponent implements OnInit {

  puntosModel: PuntosModelModule = new PuntosModelModule();
  direccionModel: DireccionModelModule = new DireccionModelModule();
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
      document.getElementById("titulo")!.innerHTML = 'Editar punto de venta ' + this.id;
      this.apiService.listaPuntosVentasID(this.formData).subscribe(
        res => {
          this.itemDatos = res;
          this.listDatos = this.itemDatos['Puntos'][0];
          console.log(this.listDatos);
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
    if (this.listDatos['IDRuta'] !== this.puntosModel.idRuta && this.puntosModel.idRuta > 0) {
      this.formData.append('idRuta', this.puntosModel.idRuta.toString());
    } else {
      this.formData.append('idRuta', this.listDatos['IDRuta']);
    }

    if (this.listDatos['Punto'] !== this.puntosModel.nombre && this.puntosModel.nombre !== ''){
      this.formData.append('nombre', this.puntosModel.nombre);
    } else {
      this.formData.append('nombre', this.listDatos['Punto']);
    }

    if (this.listDatos['direccion'] !== this.direccionModel.direccion && this.direccionModel.direccion !== '') {
      this.formData.append('direccion', this.direccionModel.direccion);
    } else {
      this.formData.append('direccion', this.listDatos['direccion']);
    }

    if (this.listDatos['localidad'] !== this.direccionModel.localidad && this.direccionModel.localidad !== '') {
      this.formData.append('localidad', this.direccionModel.localidad);
    } else {
      this.formData.append('localidad', this.listDatos['localidad']);
    }

    if (this.listDatos['municipio'] !== this.direccionModel.municipio && this.direccionModel.municipio !== '') {
      this.formData.append('municipio', this.direccionModel.municipio);
    } else {
      this.formData.append('municipio', this.listDatos['municipio']);
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
