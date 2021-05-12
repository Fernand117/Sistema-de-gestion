<?php

namespace App\Http\Controllers;

use App\models\PuntosVentas;
use Illuminate\Http\Request;

class PuntosVentasController extends Controller
{
    public function listarPuntosVentas()
    {
        $puntos = PuntosVentas::all();
        if ($puntos != null) {
            return response()->json(['Puntos' => $puntos]);
        } else {
            return response()->json(['Mensaje' => 'Aún no hay puntos de ventas registrados.'], 404);
        }
    }

    public function registrarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $consultaPunto = PuntosVentas::where('nombre', '=', $datos['nombre'])->get();
        if ($consultaPunto != null) {
            return response()->json(['Mensaje', 'Este punto de venta ya está registrado.'], 404);
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

    public function actualizarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $punto = PuntosVentas::find($id);
        if ($punto != null) {
            $puntos = new PuntosVentas();
            if (isset($datos['foto'])) {
                $extension = $request->file('imagen')->getClientOriginalExtension();
                $path = base_path().'/public/img/puntosVentas/';
                $nombre = "foto_".date('Y_m_d_h_i_s').".".$extension;
                $request->file("foto")->move($path, $nombre);
                $puntos->foto = $nombre;
            }
            $punto->nombre = $datos['nombre'];
            $puntos->idRuta = $datos['idRuta'];
            $puntos->update();
            return response()->json(['Mensaje' => 'Punto de venta actualizado correctamente']);
        }
    }

    public function eliminarPuntoVenta(Request $request)
    {
        $datos = $request->all();
        $id = $datos['id'];
        $puntoId = PuntosVentas::find($id);
        if ($puntoId != null) {
            $puntoId->delete();
            return response()->json(['Mensaje' => 'Punto de venta eliminado correctamente']);
        } else {
            return response()->json(['Mensaje' => 'No se pudo encontrar este punto de venta.'], 404);
        }
    }
}
