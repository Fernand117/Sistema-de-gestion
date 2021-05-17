import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-rutas',
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss']
})
export class RutasComponent implements OnInit {

  datos: any;
  listRutas: any;

  constructor(
    private apiService: ApiServiceService
  ) { }

  ngOnInit(): void {
    this.getListRutas();
  }

  getListRutas() {
    this.apiService.listaRutas().subscribe(
      respuesta => {
        this.datos = respuesta;
        this.listRutas = this.datos['Rutas'];
      }, err => {
        console.log(err);
      }
    );
  }

  deleteRuta(id: any){
    Swal.fire({
      title: 'Alerta',
      text: "Desea eliminar la ruta con ID: " + id + "?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmar'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire(
          'Eliminado!',
          'La ruta ' + id + " ha sido eliminada",
          'success'
        )
      }
    })
  }
}
