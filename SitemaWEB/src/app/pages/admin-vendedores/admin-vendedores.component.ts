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
  listTiposUsuarios: any;
  itemTiposUsuarios:any;

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  defaultData = {
    id : this.id,
    foto_perfil : '../../../assets/img/default_user.jpg'
  };

  ngOnInit(): void {
    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
    this.getUsuarioID();
    this.getTipoUsuario();
  }

  getUsuarioID() {
    this.formData.append('id', this.id);

    if (this.id === 'add') {
      this.itemUsuarios = this.defaultData;
    } else {
      this.apiService.usuarioID(this.formData).subscribe(
        res => {
          this.listUsuarios = res;
          this.itemUsuarios = this.listUsuarios['Usuarios'][0];
        }
      );
    }
  }

  getTipoUsuario() {
    this.apiService.listaTipoUsuario().subscribe(
      res => {
        this.listTiposUsuarios = res;
        this.itemTiposUsuarios = this.listTiposUsuarios['Tipos'];
      }
    );
  }

  mensaje(mensaje: any) {
    Swal.fire({
      icon: 'info',
      text: mensaje
    });
    this.router.navigateByUrl('/vendedores');
  }

  cargar_imagen(event: any) {
    let elemento = event.target;
    if (elemento.files.length > 0) {
      this.formData.append('foto', elemento.files[0]);
    }
  }

  guardarUsuario() {
    this.formData.append('id', this.id);
    if (this.itemUsuarios['nombre'] !== this.usuario.nombre && this.usuario.nombre !== '') {
      this.formData.append('nombre', this.usuario.nombre);
    } else {
      this.formData.append('nombre', this.itemUsuarios['nombre']);
    }

    if (this.itemUsuarios['paterno'] !== this.usuario.paterno && this.usuario.paterno !== '') {
      this.formData.append('paterno', this.usuario.paterno);
    } else {
      this.formData.append('paterno', this.itemUsuarios['paterno']);
    }

    if (this.itemUsuarios['materno'] !== this.usuario.materno && this.usuario.materno !== '') {
      this.formData.append('materno', this.usuario.materno);
    } else {
      this.formData.append('materno', this.itemUsuarios['materno']);
    }

    if (this.itemUsuarios['fecha_nac'] !== this.usuario.fecha_nac && this.usuario.fecha_nac !== '') {
      this.formData.append('fecha_nac', this.usuario.fecha_nac);
    } else {
      this.formData.append('fecha_nac', this.itemUsuarios['fecha_nac']);
    }

    if (this.itemUsuarios['usuario'] !== this.usuario.usuario && this.usuario.usuario !== '') {
      this.formData.append('usuario', this.usuario.usuario);
    } else {
      this.formData.append('usuario', this.itemUsuarios['usuario']);
    }

    if (this.itemUsuarios['clave'] !== this.usuario.clave && this.usuario.clave !== '') {
      this.formData.append('clave', this.usuario.clave);
    } else {
      this.formData.append('clave', this.itemUsuarios['clave']);
    }

    if (this.itemUsuarios['idTipo'] !== this.usuario.idTipo && this.usuario.idTipo > 0) {
      this.formData.append('tipo', this.usuario.idTipo.toString());
    } else {
      this.formData.append('tipo', this.itemUsuarios['idTipo']);
    }

    if (this.id === 'add') {
      this.apiService.registrarUsuario(this.formData).subscribe(
        res => {
          this.listUsuarios = res;
          this.mensaje(this.listUsuarios['Mensaje']);
        }
      );
    } else {
      this.apiService.actualizarUsuario(this.formData).subscribe(
        res => {
          this.listUsuarios = res;
          this.mensaje(this.listUsuarios['Mensaje']);
        }
      );
    }
  }
}
