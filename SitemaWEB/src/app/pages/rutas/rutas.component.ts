import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-rutas',
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss']
})
export class RutasComponent implements OnInit {

  datos: any;
  listRutas: any;
  formData: FormData = new FormData();

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getListRutas();
    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
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
        this.formData.append('id', id);
        this.apiService.eliminarRutas(this.formData).subscribe(
          response => {
            console.log(response);
            Swal.fire(
              'Eliminado!',
              'La ruta ' + id + " ha sido eliminada",
              'success'
            );
            this.getListRutas();
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
