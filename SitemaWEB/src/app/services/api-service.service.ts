import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiServiceService {

  private url = 'http://192.168.1.75/sistemaAPI/api';

  constructor(
    private http: HttpClient
  ) { }

  loginUsuario(datos: any) {
    return this.http.post(`${this.url}/login/usuarios`, datos);
  }

  listaRutas() {
    return this.http.get(`${this.url}/lista/rutas`);
  }
}
