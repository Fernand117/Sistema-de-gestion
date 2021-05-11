<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Direcciones extends Model
{
	protected $table = 'DIRECCIONES';
	public $timestamps = false;
	protected $fillabel = [
		'direccion', 'localidad', 'municipio','idPVentas'
	];
}
