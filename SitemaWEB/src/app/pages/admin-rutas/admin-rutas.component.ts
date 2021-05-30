import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import { RutasModelModule } from '../../models/rutas-model/rutas-model.module';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-rutas',
  templateUrl: './admin-rutas.component.html',
  styleUrls: ['./admin-rutas.component.scss']
})
export class AdminRutasComponent implements OnInit {

  rutasModel: RutasModelModule = new RutasModelModule();
  formData: FormData = new FormData();
  datosUsuarios: any;
  listaUsuarios: any;
  datos: any;
  ruta: any;

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {
    this.getRutaID();
    this.getlistaUsuarios();

    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
  }

  getlistaUsuarios() {
    this.apiService.listaUsuarios().subscribe(
      res => {
        this.datosUsuarios = res;
        this.listaUsuarios = this.datosUsuarios['Usuarios'];
      }
    );
  }

  getRutaID() {
    this.formData.append('id', this.id);
    if (this.id === 'add') {
      this.ruta = this.id;
      document.getElementById("nTitle")!.innerHTML = '<h4>Registrar nueva ruta.</h4>';
    } else {
      this.apiService.listRutaID(this.formData).subscribe(
        res => {
          this.datos = res;
          this.ruta = this.datos['Ruta'];
        }
      );
    }
  }

  mensaje(mensaje: string, header: string){
    Swal.fire(
      header,
      mensaje,
      'success'
    );
  }

  guardarRuta() {
    this.formData.append('id', this.id);
    if (this.ruta[0]['idUsuario'] !== this.rutasModel.idVendedor && this.rutasModel.idVendedor > 0) {
      this.formData.append('idUsuario', this.rutasModel.idVendedor.toString());
    } else {
      this.formData.append('idUsuario', this.ruta[0]['idUsuario']);
    }
    if (this.ruta[0]['Ruta'] !== this.rutasModel.nombre && this.rutasModel.nombre !== ''){
      this.formData.append('nombre', this.rutasModel.nombre);
    } else {
      this.formData.append('nombre', this.ruta[0]['Ruta']);
    }

    if (this.id === 'add') {
      this.apiService.registrarRuta(this.formData).subscribe(
        res => {
          this.mensaje('Ruta registrada correctamente.','Completado!');
          this.router.navigateByUrl('/rutas');
        }
      );
    } else {
      this.apiService.editarRuta(this.formData).subscribe(
        res => {
          this.mensaje('Ruta actualizada correctamente.','Completado!');
          this.router.navigateByUrl('/rutas');
        }
      );
    }
  }
}
