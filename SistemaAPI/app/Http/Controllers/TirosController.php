<?php

namespace App\Http\Controllers;

use App\models\Tiros;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class TirosController extends Controller
{
    public function listaTiros()
    {
        $tiros = DB::select('select * from ViewDetallesTiros order by IDTiro desc');
        $itemConsultaTiro = json_decode(json_encode($tiros), true);
        for ($i=0; $i < count($tiros); $i++) {
            $itemConsultaTiro[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$itemConsultaTiro[$i]['foto'];
        }
        return response()->json(['Tiros' => $itemConsultaTiro]);
    }

    public function detallesTiros(Request $request)
    {
        $datos = $request->all();
        $idPVenta = $datos['idPVenta'];
        $consultaTiro = DB::select('select * from ViewDetallesTiros where IDPunto = ? order by IDTiro desc limit 1', [$idPVenta]);
        $itemConsultaTiro = json_decode(json_encode($consultaTiro), true);
        for ($i=0; $i < count($consultaTiro); $i++) {
            $itemConsultaTiro[$i]['foto'] = 'http://'.$_SERVER['SERVER_NAME'].'/sistemaAPI/img/puntosVentas/'.$itemConsultaTiro[$i]['foto'];
        }
        return response()->json(['Detalles' => $itemConsultaTiro]);
    }

    public function generarTiro(Request $request)
    {
        $datos = $request->all();
        $idUsuario = $datos['idUsuario'];
        $consultaTotal = DB::select('select * from TIROS where idUsuario = ? order by id desc limit 7', [$idUsuario]);
        $consultaTotalItem = json_decode(json_encode($consultaTotal), true);
        $tiros = new Tiros();
        $tiros->fecha = date('Y_m_d_h_i_s');
        $prom = 0;
        if (count($consultaTotal) > 0) {
            for ($i=0; $i < count($consultaTotal); $i++) { 
                $prom = $prom + $consultaTotalItem[$i]['venta'];
            }
            $tiros->salida = $prom/7;
        } else {
            $tiros->salida = $datos['salida'];
        }
        $tiros->devolucion = 0;
        $tiros->venta = 0;
        $tiros->total = 0;
        $tiros->idPVenta = $datos['idPVenta'];
        $tiros->idUsuario = $datos['idUsuario'];
        $tiros->save();
        $total = DB::select('select count(*) as Total from TIROS');
        return response()->json(['Total' => $total] + ['Mensaje' => Tiros::all()]);
    }

    public function actualizarTiro(Request $request)
    {
        $datos = $request->all();
        $id = $datos['idTiro'];
        $tiro = Tiros::find($id);
        if (isset($datos['venta'])) {
            $tiro->devolucion = $tiro->salida - $datos['venta'];
            $tiro->venta = $datos['venta'];
            $tiro->total = $datos['venta'] * $datos['precio'];
        }
        $tiro->update();
        return response()->json(['Mensaje' => 'Actualizado']);
    }
}
