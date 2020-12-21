
package com.mycompany.mavenproject2;

/**
 *
 * @author alexis
 */
public class producto {
    private String Descripcion;
    private Integer codigo;
    private Integer cantidad;
    private String iva;
    private Float precio;
    private Integer marca;

    public producto(String Descripcion, Integer codigo, Integer cantidad, String iva, Float precio, Integer marca) {
        this.Descripcion = Descripcion;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.iva = iva;
        this.precio = precio;
        this.marca = marca;
    }


    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getMarca() {
        return marca;
    }

    public void setMarca(Integer marca) {
        this.marca = marca;
    }

    

    
}
