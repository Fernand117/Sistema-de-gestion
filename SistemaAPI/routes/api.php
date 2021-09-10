<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::get('listar/tipos', 'TipoUsuariosController@listarTiposUsuarios');
Route::post('registrar/tipo', 'TipoUsuariosController@registrarTipo');
Route::post('editar/ripo', 'TipoUsuariosController@actualizarTipoUsuario');
Route::post('eliminar/tipo', 'TipoUsuariosController@eliminarTipo');

Route::get('lista/usuarios', 'UsuariosController@listarUsuarios');
Route::post('lista/usuarios-id', 'UsuariosController@usuarioId');
Route::post('login/usuarios', 'UsuariosController@loginUsuarios');
Route::post('registrar/usuario', 'UsuariosController@registrarUsuarios');
Route::post('editar/usuario', 'UsuariosController@actualizarUsuario');
Route::post('eliminar/usuario', 'UsuariosController@eliminarUsuario');

Route::post('total/puntos-ventas', 'UsuariosController@totalPuntosVentas');
Route::post('total/reporte/ventas', 'UsuariosController@totalReporteVenta');
Route::post('reporte/general', 'UsuariosController@reporteGral');

Route::get('lista/tiros', 'TirosController@listaTiros');
Route::post('crear/tiro', 'TirosController@generarTiro');
Route::post('actualizar/tiro', 'TirosController@actualizarTiro');
Route::post('detalles/tiros', 'TirosController@detallesTiros');
Route::post('eliminar/tiro', 'TirosController@eliminarTiro');

Route::get('lista/rutas', 'RutasController@listarRutas');
Route::post('lista/ruta/id', 'RutasController@listarRutaID');
Route::post('lista/rutas/usuario', 'RutasController@listaRutasUsuario');
Route::post('registrar/ruta', 'RutasController@registrarRuta');
Route::post('editar/ruta', 'RutasController@editarRuta');
Route::post('eliminar/ruta', 'RutasController@eliminarRuta');

Route::get('listar/puntos-ventas', 'PuntosVentasController@listarPuntosVentas');
Route::post('listar/puntos-rutas', 'PuntosVentasController@listarPuntosRutas');
Route::post('listar/puntos-ventas/id', 'PuntosVentasController@listarPuntosVentasID');
Route::post('registrar/puntos-ventas', 'PuntosVentasController@registrarPuntoVentaDireccion');
Route::post('editar/puntos-ventas', 'PuntosVentasController@actualizarPuntoVenta');
Route::post('eliminar/puntos-ventas', 'PuntosVentasController@eliminarPuntoVenta');

Route::get('listar/direcciones', 'DireccionesController@listarDirecciones');
Route::post('registrar/direcciones', 'DireccionesController@registrarDireccion');
Route::post('editar/direcciones', 'DireccionesController@actualizarDireccion');
Route::post('eliminar/direccion', 'DireccionesController@eliminarDireccion');