<?php

namespace App\Http\Controllers;

use App\models\Direcciones;
use App\models\PuntosVentas;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class PuntosVentasController extends Controller
{
    public function listarPuntosVentas()
    {
        $puntos = DB::select('select * from ViewPuntosVentas');
        $item = json_decode(json_encode($puntos), true);

        for($i = 0; $i < count($puntos); $i++) {
            $item[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$item[$i]['foto'];            
        }

        if (count($puntos) > 0) {
            return response()->json(['Puntos' => $item]);
        } else {
            return response()->json(['Mensaje' => 'Aún no hay puntos de ventas registrados.'], 404);
        }
    }

    public function listarPuntosVentasID(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $puntos = DB::select('select * from ViewPuntosVentas where IDPunto = ?', [$id]);
        $item = json_decode(json_encode($puntos), true);
        for ($i=0; $i < count($puntos); $i++) { 
                    $item[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$item[$i]['foto'];
        }
        if ($puntos != null) {
            return response()->json(['Puntos' => $item]);
        } else {
            return response()->json(['Mensaje' => 'Aún no hay puntos de ventas registrados.'], 404);
        }
    }

    public function listarPuntosRutas(Request $request)
    {
        $datos = $request->all();
        $idRuta = $datos['idRuta'];

        $consulta = DB::select('select * from ViewRutasPuntos where IDRuta = ?;', [$idRuta]);

        $item = json_decode(json_encode($consulta), true);

        for ($i=0; $i < count($consulta); $i++) { 
            $item[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$item[$i]['foto'];
        }

        if (count($consulta) <= 0){
            return response()->json(['Mensaje' => 'Aún no hay puntos de ventas para esta ruta.'], 404);
        } else {
            return response()->json(['Puntos' => $item]);
        }
    }

    public function registrarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $consultaPunto = PuntosVentas::where('nombre', '=', $datos['nombre'])->get();
        if (count($consultaPunto) > 0) {
            return response()->json(['Mensaje' => 'Este punto de venta ya está registrado.'], 404);
        } else {
            $puntos = new PuntosVentas();
            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/puntosVentas/';
                $nombre = "foto_".date('Y_m_d_h_i_s').".".$extension;
                $request->file("foto")->move($path,$nombre);
                $puntos->foto = $nombre;
            }
            $puntos->nombre = $datos['nombre'];
            $puntos->idRuta = $datos['idRuta'];
            $puntos->save();
            return response()->json(['Mensaje' => 'Punto de venta registrado correctamente']);
        }
    }

    public function registrarPuntoVentaDireccion(Request $request)
    {
        $datos = $request->all();
        $consultaDireccion = Direcciones::where('direccion', '=', $datos['direccion'])->get();
        $consultaPunto = PuntosVentas::where('nombre','=', $datos['nombre'])->get();

        if (count($consultaDireccion) > 0) {
            return response()->json(['Mensaje' => 'Error, esta dirección ya está siendo ocupada'], 404);
        } elseif (count($consultaPunto) > 0) {
            return response()->json(['Mensaje' => 'Error, este punto de venta ya está registrado'], 404);
        } else {
            $punto = new PuntosVentas();
            $direccion = new Direcciones();

            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/puntosVentas/';
                $nombre = "foto_".date('Y_m_d_h_i_s').".".$extension;
                $request->file("foto")->move($path, $nombre);
                $punto->foto = $nombre;
            }
            $punto->nombre = $datos['nombre'];
            $punto->idRuta = $datos['idRuta'];
            $punto->save();
            $direccion->direccion = $datos['direccion'];
            $direccion->localidad = $datos['localidad'];
            $direccion->municipio = $datos['municipio'];
            $direccion->idPVentas = $punto->id;
            $direccion->save();
            return response()->json(['Mensaje' => 'Punto de venta creado correctamente.']);
        }
    }

    public function actualizarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $punto = PuntosVentas::find($id);
        if ($punto != null) {
            if (isset($datos['foto'])) {
                $extension = $request->file('foto')->getClientOriginalExtension();
                $path = base_path().'/public/img/puntosVentas/';
                $nombre = "foto_".date('Y_m_d_h_i_s').".".$extension;
                $request->file("foto")->move($path, $nombre);
                $punto->foto = $nombre;
            }
            if (isset($datos['nombre'])) {
                $punto->nombre = $datos['nombre'];
            }
            if (isset($datos['idRuta'])) {
                $punto->idRuta = $datos['idRuta'];
            }
            $punto->update();
            return response()->json(['Mensaje' => 'Punto de venta actualizado correctamente']);
        }
    }

    public function eliminarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $puntoId = PuntosVentas::find($id);
        $direccion = Direcciones::where('idPVentas','=', $id);
        if ($puntoId != null) {
            $direccion->delete();
            $puntoId->delete();
            return response()->json(['Mensaje' => 'Punto de venta eliminado correctamente']);
        } else {
            return response()->json(['Mensaje' => 'No se pudo encontrar este punto de venta.'], 404);
        }
    }
}
