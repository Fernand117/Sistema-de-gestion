import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiServiceService {

  private url = "http://www.serverfer.com/sistemaAPI/api";
  //private url = 'http://192.168.1.80/sistemaAPI/api';
  //private url = 'https://ebtapi.herokuapp.com/api';

  constructor(
    private http: HttpClient
  ) { }

  totalDatosVendedor(datos: any) {
    return this.http.post(`${this.url}/total/puntos-ventas`, datos);
  }

  reporteVenta(datos: any) {
    return this.http.post(`${this.url}/total/reporte/ventas`, datos);
  }

  reporteGral(datos: any) {
    return this.http.post(`${this.url}/reporte/general`, datos);
  }

  listaUsuarios() {
    return this.http.get(`${this.url}/lista/usuarios`);
  }

  listaTipoUsuario() {
    return this.http.get(`${this.url}/listar/tipos`);
  }

  usuarioID(datos: any) {
    return this.http.post(`${this.url}/lista/usuarios-id`, datos);
  }

  registrarUsuario(datos: any) {
    return this.http.post(`${this.url}/registrar/usuario`, datos);
  }

  actualizarUsuario(datos: any) {
    return this.http.post(`${this.url}/editar/usuario`, datos);
  }

  eliminarUsuario(datos: any) {
    return this.http.post(`${this.url}/eliminar/usuario`, datos);
  }

  loginUsuario(datos: any) {
    return this.http.post(`${this.url}/login/usuarios`, datos);
  }

  listaRutas() {
    return this.http.get(`${this.url}/lista/rutas`);
  }

  listRutaID(data: any) {
    return this.http.post(`${this.url}/lista/ruta/id`, data);
  }

  listaRutaUsuario(datos: any) {
    return this.http.post(`${this.url}/lista/rutas/usuario`, datos);
  }

  registrarRuta(datos: any) {
    return this.http.post(`${this.url}/registrar/ruta`, datos);
  }

  editarRuta(datos: any) {
    return this.http.post(`${this.url}/editar/ruta`, datos);
  }

  eliminarRutas(datos: any) {
    return this.http.post(`${this.url}/eliminar/ruta`, datos);
  }

  listPuntosVentas() {
    return this.http.get(`${this.url}/listar/puntos-ventas`);
  }

  listaPuntosVentasID(datos: any) {
    return this.http.post(`${this.url}/listar/puntos-ventas/id`, datos);
  }

  listaPuntoVentaRuta(datos: any) {
    return this.http.post(`${this.url}/listar/puntos-rutas`, datos);
  }

  crearPuntoVenta(datos: any) {
    return this.http.post(`${this.url}/registrar/puntos-ventas`, datos);
  }

  editarPuntoVenta(datos: any) {
    return this.http.post(`${this.url}/editar/puntos-ventas`, datos);
  }

  eliminarPuntoVenta(datos: any) {
    return this.http.post(`${this.url}/eliminar/puntos-ventas`, datos);
  }

  listaTiros() {
    return this.http.get(`${this.url}/lista/tiros`);
  }

  generarTiro(datos: any) {
    return this.http.post(`${this.url}/crear/tiro`, datos);
  }

  eliminarTiro(datos: any) {
    return this.http.post(`${this.url}/eliminar/tiro`, datos);
  }
}
