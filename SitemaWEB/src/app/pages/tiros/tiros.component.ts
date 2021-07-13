import { AdminPuntosComponent } from './../admin-puntos/admin-puntos.component';
import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-tiros',
  templateUrl: './tiros.component.html',
  styleUrls: ['./tiros.component.scss']
})
export class TirosComponent implements OnInit {

  tirosResponse: any;
  itemTiros: any;

  formData: FormData = new FormData();

  constructor(
    private apiService: ApiServiceService
  ) { }

  ngOnInit(): void {
    this.listaTiros();
  }

  listaTiros(): void {
    this.apiService.listaTiros().subscribe(
      res => {
        this.tirosResponse = res;
        this.itemTiros = this.tirosResponse['Tiros'];
      }
    );
  }

  eliminarTiro(id: any): void {
    Swal.fire({
      title: 'Alerta',
      text: "Desea eliminar el tiro con ID: " + id + "?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.formData.append('idTiro', id);
        this.apiService.eliminarTiro(this.formData).subscribe(
          res => {
            Swal.fire(
              'Eliminado!',
              'El tiro con ID ' + id + " ha sido eliminado",
              'success'
            );
            this.listaTiros();
          }, err => {
            if (err['status'] === 404) {
              Swal.fire(
                'Error!',
                'No puede eliminar este tiro',
                'error'
              );
            }
          }
        );
      }
    });
  }
}
