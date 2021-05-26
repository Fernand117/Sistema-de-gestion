create database dbGestion;

use dbGestion;

create table TIPOS_USUARIOS(
    id int not null auto_increment,
    tipo varchar(100),
    primary key (id)
);

create table USUARIOS(
    id int not null auto_increment,
    nombre varchar(50),
    paterno varchar(50),
    materno varchar(50),
    fecha_nac varchar(50),
    usuario varchar(50),
    clave varchar(50),
    foto_perfil varchar(250),
    idTipo int,
    primary key (id),
    foreign key (idTipo) references TIPOS_USUARIOS (id)
);

create table RUTAS(
    id int not null auto_increment,
    nombre varchar(50),
    idUsuario int,
    primary key (id),
    foreign key (idUsuario) references USUARIOS(id)
);

create table PUNTOS_VENTAS(
    id int not null auto_increment,
    nombre varchar(50),
    foto varchar(250),
    idRuta int,
    primary key (id),
    foreign key (idRuta) references RUTAS(id)
);

create table DIRECCIONES(
    id int not null auto_increment,
    direccion varchar(250),
    localidad varchar(150),
    municipio varchar(50),
    idPVentas int,
    primary key (id),
    foreign key (idPVentas) references PUNTOS_VENTAS(id)
);

create table TIROS(
    id int not null auto_increment,
    fecha timestamp,
    salida int,
    devolucion int,
    venta int,
    total varchar(250),
    idPVenta int,
    idUsuario int,
    primary key (id),
    foreign key (idPVenta) references PUNTOS_VENTAS(id),
    foreign key (idUsuario) references USUARIOS(id)
);

select * from PUNTOS_VENTAS;
select  * from RUTAS;

create view ViewRutasPuntos as
select R.id as IDRuta, R.nombre as Ruta, PV.id as IDPUnto, PV.nombre as Punto, PV.foto from RUTAS R inner join PUNTOS_VENTAS PV on R.id = PV.idRuta;

select * from ViewRutasPuntos;

select * from USUARIOS;
select * from TIPOS_USUARIOS;

create view ViewLoginUsuario as
select u.id as IDUsuario, u.nombre, u.paterno, u.materno, u.fecha_nac, u.usuario, u.clave, u.foto_perfil, t.id as IDTipo, t.tipo from USUARIOS u, TIPOS_USUARIOS t where u.idTipo = t.id;

select * from ViewLoginUsuario where tipo = 'Administrador';
select * from RUTAS;
create view ViewRutas as
select r.id, r.nombre as Ruta, r.idUsuario, u.nombre, u.paterno, u.foto_perfil from RUTAS r, USUARIOS u where r.idUsuario = u.id order by u.nombre;
drop view ViewRutas;
select * from ViewRutas;

select * from RUTAS;

create view ViewPuntosVentas as
select p.id as IDPunto, p.nombre as Punto, p.foto, r.id as IDRuta, r.nombre as Ruta, u.nombre, u.paterno  from PUNTOS_VENTAS p, RUTAS r, USUARIOS u where p.idRuta = r.id and r.idUsuario = u.id;

select * from ViewPuntosVentas where IDPunto = 13;

select * from USUARIOS;
select * from TIPOS_USUARIOS;

create view ViewUsuarios as
select u.id, u.nombre, u.paterno, u.materno, u.fecha_nac, u.usuario, u.clave, u.foto_perfil, u.idTipo, t.tipo from USUARIOS u, TIPOS_USUARIOS t where u.idTipo = t.id;
select * from ViewUsuarios where usuario = 'Fernand117';

select * from USUARIOS, RUTAS, PUNTOS_VENTAS, DIRECCIONES;
select * from DIRECCIONES;