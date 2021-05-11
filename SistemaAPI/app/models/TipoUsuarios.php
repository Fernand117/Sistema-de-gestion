<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class TipoUsuarios extends Model
{
    protected $table = 'TIPOS_USUARIOS';
    public $timestamps = false;
    protected $fillabel = [
        'tipo'
    ];
}
