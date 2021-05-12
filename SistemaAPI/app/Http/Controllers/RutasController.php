<?php

namespace App\Http\Controllers;

use App\models\Rutas;
use Illuminate\Http\Request;

class RutasController extends Controller
{
    public function listarRutas()
    {
        $rutas = Rutas::all();
        
        if ($rutas != null) {
            return response()->json(['Rutas' => $rutas]);
        } else {
            return response()->json(['Mensaje' => 'Aún no hay rutas registradas.'], 404);
        }
    }

    public function registrarRuta(Request $request)
    {
        $datos = $request->all();
        $consultaRuta = Rutas::where('nombre','=', $datos['nombre'])->get();
        if ($consultaRuta != null) {
            return response()->json(['Mensaje' => 'Esta ruta ya está registrada']);
        } else {
            $rutas = new Rutas();
            $rutas->nombre = $datos['nombre'];
            $rutas->idUsuario = $datos['idUsuario'];
            $rutas->save();
            $consultaRuta = Rutas::where('nombre','=', $datos['nombre'])->get();
            if ($consultaRuta != null) {
                return response()->json(['Mensaje' => 'La ruta ha sido registrada correctamente']);
            } else {
                return response()->json(['Mensaje' => 'No se pudo registrar la ruta'], 404);
            }
        }
    }

    public function editarRuta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $ruta = Rutas::find($id);
        if ($ruta != null) {
            $ruta->nombre = $datos['nombre'];
            $ruta->idUsuario = $datos['idUsuario'];
            $ruta->update();
            $ruta = Rutas::find($id);
            if ($ruta != null) {
                return response()->json(['Mensaje' => 'No se ha podido actualizar la ruta'], 404);
            } else {
                return response()->json(['Mensaje' => 'Ruta actualizada correctamente']);
            }
        } else {
            return response()->json(['Mensaje' => 'Esta ruta no existe']);
        }
    }

    public function eliminarRuta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $ruta = Rutas::find($id);

        if ($ruta != null) {
            $ruta->delete();
            $ruta = Rutas::find($id);

            if ($ruta != null) {
                return response()->json(['Mensaje' => 'No se pudo eliminar la ruta selecionada']);
            } else {
                return response()->json(['Mensaje' => 'Ruta eliminada correctamente']);
            }
        } else {
            return response()->json(['Mensaje' => 'No se ha podido localiar esta ruta.']);
        }
    }
}