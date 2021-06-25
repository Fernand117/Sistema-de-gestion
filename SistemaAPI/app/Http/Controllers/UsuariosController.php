<?php

namespace App\Http\Controllers;

use App\models\Usuarios;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class UsuariosController extends Controller
{
    
    public function totalPuntosVentas(Request $request)
    {
        $datos = $request->all();
        $id = $datos['idUsuario'];
        $usuario = $datos['usuario'];
        if (isset($datos['usuario'])) {
            $totalPuntos = DB::select("select count(*) as Total from ViewPuntosVentas where usuario = ?", [$usuario]);
            $totalRutas = DB::select("select count(*) as Total from ViewRutas where idUsuario = ?", [$id]);
            $ultimosPuntos = DB::select("select * from ViewPuntosVentas where usuario = ? order by Punto asc limit 5", [$usuario]);
            $item = json_decode(json_encode($ultimosPuntos), true);
            for($i = 0; $i < count($ultimosPuntos); $i++) {
                $item[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$item[$i]['foto'];            
            }
            return response()->json(['Puntos' => $totalPuntos] + ['Rutas' => $totalRutas] + ['Lista' => $item]);
        } else {
            $totalPuntos = DB::select("select count(*) as Total from ViewPuntosVentas");
            $totalRutas = DB::select("select count(*) as Total from ViewRutas");
            $ultimosPuntos = DB::select("select * from ViewPuntosVentas order by Punto asc limit 5");
            $item = json_decode(json_encode($ultimosPuntos), true);
            for($i = 0; $i < count($ultimosPuntos); $i++) {
                $item[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$item[$i]['foto'];            
            }
            return response()->json(['Puntos' => $totalPuntos] + ['Rutas' => $totalRutas] + ['Lista' => $item]);
        }
    }

    public function loginUsuarios(Request $request)
    {
        $datos = $request->all();
        $usuario = DB::select('select * from ViewLoginUsuario where usuario = ? and clave = ?',[$datos['usuario'], md5($datos['clave'])]);
        if (count($usuario) <= 0) {
            return response()->json(['Mensaje' => 'Usuario o contraseña incorrectos.'], 404);
        } else {
            $items = json_decode(json_encode($usuario), true);
            for ($i=0; $i < count($usuario); $i++) { 
                $items[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/perfiles/'.$items[$i]['foto_perfil'];
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
                $items[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/perfiles/'.$items[$i]['foto_perfil'];
            }
            return response()->json(['Usuarios' => $items]);
        }
    }

    public function usuarioId(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $usuarios = DB::select('select * from ViewUsuarios where id = ?', [$id]);
        if (count($usuarios) <= 0) {
            return response()->json(['Mensaje' => 'Este usuario no existe.'], 404);
        } else {
            $items = json_decode(json_encode($usuarios), true);
            for ($i=0; $i < count($usuarios); $i++) { 
                $items[$i]['foto_perfil'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/perfiles/'.$items[$i]['foto_perfil'];
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

        $id = $datos['id'];

        $consultaUsuarioE = Usuarios::find($id);
        
        if ($consultaUsuarioE != null) {
            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/perfiles/';
                $name = "foto_".date('Y_m_d_g_i_s').".".$extension;
                $request->file("foto")->move($path, $name);
                $consultaUsuarioE->foto_perfil = $name;
            }

            if (isset($datos['nombre'])) {
                $consultaUsuarioE->nombre = $datos['nombre'];
            }
            
            if (isset($datos['paterno'])) {
                $consultaUsuarioE->paterno = $datos['paterno'];
            }

            if (isset($datos['materno'])) {
                $consultaUsuarioE->materno = $datos['materno'];
            }

            if (isset($datos['fecha_nac'])) {
                $consultaUsuarioE->fecha_nac = $datos['fecha_nac'];
            }

            if (isset($datos['usuario'])) {
                $consultaUsuarioE->usuario = $datos['usuario'];
            }

            if (isset($datos['clave'])) {
                $consultaUsuarioE->clave = md5($datos['clave']);
            }

            if (isset($datos['tipo'])) {
                $consultaUsuarioE->idTipo = $datos['tipo'];
            }
            $consultaUsuarioE->update();
            return response()->json(['Mensaje' => 'Usuario actualizado correctamente']);
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
