import { Component, OnInit } from '@angular/core';
import { UsuarioModelModule } from '../../models/usuario-model/usuario-model.module';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  usuariosModel: UsuarioModelModule = new UsuarioModelModule();
  formData: FormData = new FormData();
  datos: any;

  constructor(
    private apiService: ApiServiceService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  alertFailed(mensaje: string) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: mensaje
    });
  }

  loginUsuarios() {
    if (this.usuariosModel.usuario === '') {
      this.alertFailed('Ingrese su nombre de usuario por favor!');
    } else if (this.usuariosModel.clave === '') {
      this.alertFailed('Ingrese su contraseña por favor!');
    } else if (this.usuariosModel.usuario === '' && this.usuariosModel.clave === '') {
      this.alertFailed('Por favor ingrese su usuarios y contraseña!');
    } else {
      this.formData.append('usuario', this.usuariosModel.usuario);
      this.formData.append('clave', this.usuariosModel.clave);
      Swal.fire({
        title: 'Iniciando sesión',
        timer: 2000,
        timerProgressBar: true,
        didOpen: () => {
          Swal.showLoading();
          this.apiService.loginUsuario(this.formData).subscribe(
            res => {
              this.datos = res;
              const tipo = this.datos['Usuarios'][0]['tipo'];
              if (tipo === 'Administrador') {
                Swal.fire({
                  position: 'top-end',
                  icon: 'success',
                  title: 'Sesión iniciada.',
                  showConfirmButton: false,
                  timer: 1500
                });
                localStorage.setItem('Usuario', JSON.stringify(this.datos['Usuarios']));
                this.router.navigateByUrl('inicio');
              } else {
                this.alertFailed('Usted no tiene persmisos de usuario para ingresar al sistema, contacte con el administrador del área.');
              }
            }, error => {
              if (error['status'] === 404) {
                this.alertFailed(error['error']['Mensaje']);
              } else {
                this.alertFailed('Servidor no disponible.');
              }
            }
          );
        },
        willClose: () => {
          clearInterval();
        }
      }).then((result) => {
        /* Read more about handling dismissals below */
        if (result.dismiss === Swal.DismissReason.timer) {
          console.log('I was closed by the timer');
        }
      });
    }
  }
}
