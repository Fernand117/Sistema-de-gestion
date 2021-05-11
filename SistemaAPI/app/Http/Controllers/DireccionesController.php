<?php

namespace App\Http\Controllers;

use App\models\Direcciones;
use Illuminate\Http\Request;

class DireccionesController extends Controller
{
	public function listarDirecciones(){
		$direcciones = Direcciones::all();
		if ($direcciones != null){
			return response()->json(['Direcciones' => $direcciones]);
		} else {
			return response()->json(['Mensaje' => 'Aún no hay direcciones agregadas']);
		}
	}

	public function registrarDireccion(Request $request){
		$datos = $request->all();
		$consultaED = Direcciones::where('direccion','=',$datos['direccion'])->get();
		if ($consultaED != null) {
			return response()->json(['Mensaje' => 'Esta dirección ya está registrada.'], 404);
		} else {
			$direccion = new Direcciones();
			$direccion->direccion = $datos['direccion'];
			$direccion->localidad = $datos['localidad'];
			$direccion->municipio = $datos['municipio'];
			$direccion->idPVentas = $datos['idPVentas'];
			$direccion->save();
			return response()->json(['Mensaje' => 'Dirección creada correctamente.']);
		}
	}

	public function actualizarDireccion(Request $request){
		$datos = $request->all();
		$id = $datos['id'];
		$direccion = Direcciones::find($id);
		if ($direccion != null) {
			$direccion->direccion = $datos['direccion'];
			$direccion->localidad = $datos['localidad'];
			$direccion->municipio = $datos['municipio'];
			$direccion->idPVentas = $datos['idPVentas'];
			$direccion->update();
			return response()->json(['Mensaje' => 'Dirección actualizada correctamente.']);
		} else {
			return response()->json(['Mensaje' => 'Esta dirección no se puede encontrar.'], 404);
		}
	}

	public function eliminarDireccion(Request $request){
		$datos = $request->all();
		$id = $datos['id'];
		$direccion = Direcciones::find($id);
		if ($direccion != null) {
			$direccion->delete();
			return response()->json(['Mensaje' => 'Dirección eliminada correctamente.']);
		} else {
			return response()->json(['Mensaje' => 'Esta dirección no está disponible.'], 404);
		}
	}
}
