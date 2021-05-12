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