<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class PuntosVentas extends Model
{
    protected $table = 'PUNTOS_VENTAS';
    public $timestamps = false;
    protected $fillabel = [
        'nombre', 'foto', 'idRuta'
    ];
}
