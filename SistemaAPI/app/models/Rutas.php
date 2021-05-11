<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Rutas extends Model
{
    protected $table = 'RUTAS';
    public $timestamps = false;
    protected $fillabel = [
        'nombre', 'idUsuario'
    ];
}
