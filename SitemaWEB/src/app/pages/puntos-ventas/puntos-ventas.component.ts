import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-puntos-ventas',
  templateUrl: './puntos-ventas.component.html',
  styleUrls: ['./puntos-ventas.component.scss']
})
export class PuntosVentasComponent implements OnInit {

  listPuntos: any;
  itemsPuntos: any;
  formData: FormData = new FormData();

  constructor(
    private apiService: ApiServiceService
  ) { }

  ngOnInit(): void {
    this.getListPuntos();
  }

  getListPuntos() {
    this.apiService.listPuntosVentas().subscribe(
      res => {
        this.itemsPuntos = res;
        this.listPuntos = this.itemsPuntos['Puntos'];
      }
    );
  }

  eliminarPuntoVenta(id: any) {
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
        this.formData.append('id', id);
        this.apiService.eliminarPuntoVenta(this.formData).subscribe(
          response => {
            console.log(response);
            Swal.fire(
              'Eliminado!',
              'El punto de venta con el id ' + id + " ha sido eliminado.",
              'success'
            );
            this.getListPuntos();
          }, err => {
            if (err['status'] === 500) {
              Swal.fire(
                'Error!',
                'No puede eliminar esta ruta porque tiene puntos de ventas asignados',
                'error'
              );
            }
          }
        );
      }
    });
  }
}
