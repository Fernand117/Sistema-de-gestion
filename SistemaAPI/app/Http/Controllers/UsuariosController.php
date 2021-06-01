<?php

namespace App\Http\Controllers;

use App\models\Usuarios;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class UsuariosController extends Controller
{
    public function loginUsuarios(Request $request)
    {
        $datos = $request->all();
        $usuario = DB::select('select * from ViewLoginUsuario where usuario = ? and clave = ?',[$datos['usuario'], md5($datos['clave'])]);
        if (count($usuario) <= 0) {
            return response()->json(['Mensaje' => 'Usuario o contraseña incorrectos.'], 404);
        } else {
            $items = json_decode(json_encode($usuario), true);
            for ($i=0; $i < count($usuario); $i++) { 
                $items[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/img/perfiles/'.$items[$i]['foto_perfil'];
            }
            return response()->json(['Usuarios' => $items]);
        }
    }

    public function listarUsuarios()
    {
        $usuarios = DB::select('select * from ViewUsuarios');
        if (count($usuarios) <= 0) {
            return response()->json(['Mensaje' => 'Aún no existen usuarios registrados'], 404);
        } else {
            $items = json_decode(json_encode($usuarios), true);
            for ($i=0; $i < count($usuarios); $i++) { 
                $items[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/img/perfiles/'.$items[$i]['foto_perfil'];
            }
            return response()->json(['Usuarios' => $items]);
        }
    }

    public function registrarUsuarios(Request $request)
    {
        $datos = $request->all();

        $usuarioActual = $datos['usuario'];

        $consultaUsuarioE = Usuarios::where('usuario', '=', $usuarioActual)->get();
        
        if (count($consultaUsuarioE) > 0) {
            return response()->json(['Mensaje' => 'Este usuario ya existe'], 404);
        } else {
            $usuario = new Usuarios();
            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/perfiles/';
                $name = "foto_".date('Y_m_d_g_i_s').".".$extension;
                $request->file("foto")->move($path, $name);
                $usuario->foto_perfil = $name;
            } else {
                $usuario->foto_perfil = 'default';
            }
            $usuario->nombre = $datos['nombre'];
            $usuario->paterno = $datos['paterno'];
            $usuario->materno = $datos['materno'];
            $usuario->fecha_nac = $datos['fecha_nac'];
            $usuario->usuario = $datos['usuario'];
            $usuario->clave = md5($datos['clave']);
            $usuario->idTipo = $datos['tipo'];
            $usuario->save();
	        $consultaUsuario = Usuarios::where('usuario','=', $datos['usuario'])->get();
            if (count($consultaUsuario) <= 0) {
                return response()->json(['Mensaje' => 'No se pudo registrar al usuario'], 404);
            } else {
                return response()->json(['Mensaje' => 'Usuario registrado correctamente']);
            }
        }
    }

    public function actualizarUsuario(Request $request)
    {
        $datos = $request->all();

        $usuarioActual = $datos['usuario'];

        $consultaUsuarioE = Usuarios::where('usuario', '=', $usuarioActual)->get();
        
        if ($consultaUsuarioE != null) {
            $usuario = new Usuarios();
            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/productos';
                $name = "foto_".date('Y_m_d_g_i_s').".".$extension;
                $request->file("foto")->move($path, $name);
                $usuario->foto_perfil = $name;
            } else {
                $usuario->foto_perfil = 'default';
            }
            $usuario->nombre = $datos['nombre'];
            $usuario->paterno = $datos['paterno'];
            $usuario->materno = $datos['materno'];
            $usuario->fecha_nac = $datos['fecha_nac'];
            $usuario->usuario = $datos['usuario'];
            $usuario->clave = md5($datos['clave']);
            $usuario->idTipo = $datos['tipo'];
            $usuario->update();
    
	    #$consultaUsuario = DB::select('select * from USUARIOS where usuario = ?', [$datos['usuario']]);
	    $consultaUsuario = Usuarios::where('usuario', '=' , $datos['usuario'])->get();
            if ($consultaUsuario != null) {
                return response()->json(['Mensaje' => 'No se pudo registrar al usuario'], 404);
            } else {
                return response()->json(['Mensaje' => 'Usuario registrado correctamente']);
            }
        } else {
            return response()->json(['Mensaje' => 'Este usuario no existe'], 404);
        }
    }

    public function eliminarUsuario(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $usuario = Usuarios::find($id);
        
        if ($usuario != null) {
            $usuario->delete();
            $usuario = Usuarios::find($id);
            if ($usuario == null){
                return response()->json(['Mensaje' => 'Usuario eliminado correctamente']);
            } else {
                return response()->json(['Mensaje' => 'No se pudo eliminar este usuario'], 404);
            }
        } else {
            return response()->json(['Mensaje' => 'Este usuario no existe.'], 404);
        }
    }
}
