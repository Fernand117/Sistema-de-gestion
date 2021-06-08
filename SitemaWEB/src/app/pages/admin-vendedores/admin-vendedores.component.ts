import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';
import { UsuarioModelModule } from '../../models/usuario-model/usuario-model.module';

@Component({
  selector: 'app-admin-vendedores',
  templateUrl: './admin-vendedores.component.html',
  styleUrls: ['./admin-vendedores.component.scss']
})
export class AdminVendedoresComponent implements OnInit {

  usuario: UsuarioModelModule = new UsuarioModelModule();
  formData: FormData = new FormData();
  listUsuarios: any;
  itemUsuarios: any;

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {
    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
    this.getUsuarioID();
  }

  getUsuarioID() {
    this.formData.append('id', this.id);

    if (this.id === 'add') {
      this.itemUsuarios = this.id;
    } else {
      this.apiService.usuarioID(this.formData).subscribe(
        res => {
          this.listUsuarios = res;
          this.itemUsuarios = this.listUsuarios['Usuarios'];
          console.log(this.itemUsuarios);
        }
      );
    }
  }

  guardarUsuario() {
    if (this.id === 'add') {
      //función de guardar
    } else {
      //función editar
    }
  }
}
