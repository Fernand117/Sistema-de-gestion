<?php

namespace App\Http\Controllers;

use App\models\Rutas;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class RutasController extends Controller
{
    public function listarRutas()
    {
        $rutas = DB::select('select * from ViewRutas');
        $itemsRutas = json_decode(json_encode($rutas), true);

        for($i = 0; $i < count($rutas); $i++) {
            $itemsRutas[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/img/perfiles/'.$itemsRutas[$i]['foto_perfil'];
        }

        if (count($rutas) <= 0) {
		    return response()->json(['Mensaje' => 'Aún no hay rutas registradas.'], 404);
        } else {
		    return response()->json(['Rutas' => $itemsRutas]);
        }
    }

    public function listarRutaID(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $consultaRuta = DB::select('select * from ViewRutas where id = ?', [$id]);
        if (count($consultaRuta) <= 0) {
            return response()->json(['Mensaje' => 'Esta ruta no existe'], 404);
        } else {
            return response()->json(['Rutas' => $consultaRuta]);
        }
    }

    public function listaRutasUsuario(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];

        $rutas = Rutas::where('idUsuario', '=', $id)->get();
        if (count($rutas) <= 0){
            return response()->json(['Mensaje' => 'Usted no tiene rutas asignadas.'], 404);
        } else {
            return response()->json(['Rutas' => $rutas]);
        }
    }

    public function registrarRuta(Request $request)
    {
        $datos = $request->all();
        $consultaRuta = Rutas::where('nombre','=', $datos['nombre'])->get();
        if (count($consultaRuta) > 0) {
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
            return response()->json(['Mensaje' => 'Ruta actualizada correctamente']);
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
                return response()->json(['Mensaje' => 'No se pudo eliminar la ruta selecionada'], 404);
            } else {
                return response()->json(['Mensaje' => 'Ruta eliminada correctamente']);
            }
        } else {
            return response()->json(['Mensaje' => 'No se ha podido localiar esta ruta.'], 404);
        }
    }
}
