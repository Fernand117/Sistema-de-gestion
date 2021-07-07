<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Tiros extends Model
{
    protected $table = 'TIROS';
    public $timestamps = false;
    protected $fillabel = [
        'fecha', 'salida', 'devolucion', 'venta', 'total', 'idPVenta', 'idUsuario'
    ];
}
