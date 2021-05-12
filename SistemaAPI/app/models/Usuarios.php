<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Usuarios extends Model
{
    protected $table = 'USUARIOS';
    public $timestamps = false;
    protected $fillabel = [
        'nombre','paterno','materno','fecha_nac','usuario','clave','foto_perfil','idTipo'
    ];
}
