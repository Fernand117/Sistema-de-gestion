<?php

namespace App\Http\Controllers;

use App\models\TipoUsuarios;
use Illuminate\Http\Request;
#use Illuminate\Support\Facades\DB;

class TipoUsuariosController extends Controller
{
    public function listarTiposUsuarios()
    {
        $tipos = TipoUsuarios::all();
        if(count($tipos) <= 0){
            return response()->json(['Mensaje' => 'Aún no hay ningún tipo de usuario registrado.'], 404);
        } else {
            return response()->json(['Tipos' => $tipos]);
        }
    }

    public function registrarTipo(Request $request){
        $datos = $request->all();
        $tipo = new TipoUsuarios();
        $tipo->tipo = $datos['tipo'];
	    #$consulta = DB::select('select * from TIPOS_USUARIOS where tipo = ?', [$datos['tipo']]);
	    $consulta = TipoUsuarios::where('tipo','=',$datos['tipo'])->get();
        if(count($consulta) <= 0){
            $tipo->save();
            return response()->json(['Mensaje' => 'Tipo de usuario registrado.']);
        } else {
            return response()->json(['Mensaje' => 'El tipo de usuario que intentó registrar no se pudo dar de alta porque ya existe.'], 404);
        }
    }

    public function actualizarTipoUsuario(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];

        $tipo = TipoUsuarios::find($id);
        $tipoActual = $tipo->tipo;
        $tipo->tipo = $datos['tipo'];
        $tipo->update();
        $tipo = TipoUsuarios::find($id);
        $tipoNuevo = $tipo->tipo();
        if ($tipoNuevo == $tipoActual) {
            response()->json(['Mensaje' => 'El tipo de usuario ' + $datos['tipo']  + ' se a actualizado correctamente.']);
        } else {
            response()->json(['Mensaje' => 'No se a podido actualizar el tipo de usuario que intenta ingresar.'], 404);
        }
    }

    public function eliminarTipo(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $tipo = TipoUsuarios::find($id);
        $tipo->delete();
        $tipo = TipoUsuarios::find($id);
        if(count($tipo) <= 0){
            return response()->json(['Mensaje' => 'Usuarios eliminado correctamente.']);
        } else {
            return response()->json(['Mensaje' => 'No se pudo eliminar el tipo de usuario seleccionado.'], 404);
        }
    }
}
