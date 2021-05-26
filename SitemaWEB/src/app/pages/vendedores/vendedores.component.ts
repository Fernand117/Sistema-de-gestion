import { Component, OnInit } from '@angular/core';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-vendedores',
  templateUrl: './vendedores.component.html',
  styleUrls: ['./vendedores.component.scss']
})
export class VendedoresComponent implements OnInit {

  listVendedor: any;
  itemVendedor: any;
  formData: FormData = new FormData();

  constructor(
    private apiService: ApiServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.getListUsuarios();
  }

  getListUsuarios() {
    this.apiService.listaUsuarios().subscribe(
      res => {
        this.listVendedor = res;
        this.itemVendedor = this.listVendedor['Usuarios'];
      }
    );
  }

  deleteUsuario(id: any){
    Swal.fire({
      title: 'Alerta',
      text: "Desea eliminar el usuario con ID: " + id + "?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.formData.append('id', id);
        this.apiService.eliminarUsuario(this.formData).subscribe(
          response => {
            console.log(response);
            Swal.fire(
              'Eliminado!',
              'La ruta ' + id + " ha sido eliminada",
              'success'
            );
            this.getListUsuarios();
          }, err => {
            if (err['status'] === 500) {
              Swal.fire(
                'Error!',
                'No puede eliminar este usuario porque tiene rutas de asignadas',
                'error'
              );
            }
          }
        );
      }
    });
  }
}
