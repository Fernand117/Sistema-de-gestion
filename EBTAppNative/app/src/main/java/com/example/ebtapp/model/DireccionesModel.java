package com.example.ebtapp.model;

public class DireccionesModel {
    private int id;
    private String direccion;
    private String localidad;
    private String municipio;
    private int idPVenta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getIdPVenta() {
        return idPVenta;
    }

    public void setIdPVenta(int idPVenta) {
        this.idPVenta = idPVenta;
    }
}
