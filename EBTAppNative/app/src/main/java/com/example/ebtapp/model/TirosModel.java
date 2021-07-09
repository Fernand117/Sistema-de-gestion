package com.example.ebtapp.model;

public class TirosModel {
    private int id;
    private String fecha;
    private int salida;
    private int devolucion;
    private int venta;
    private int total;
    private int idPVenta;
    private int idUsuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getSalida() {
        return salida;
    }

    public void setSalida(int salida) {
        this.salida = salida;
    }

    public int getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(int devolucion) {
        this.devolucion = devolucion;
    }

    public int getVenta() {
        return venta;
    }

    public void setVenta(int venta) {
        this.venta = venta;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIdPVenta() {
        return idPVenta;
    }

    public void setIdPVenta(int idPVenta) {
        this.idPVenta = idPVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
