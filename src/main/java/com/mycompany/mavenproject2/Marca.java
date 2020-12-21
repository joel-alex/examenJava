
package com.mycompany.mavenproject2;

/**
 *
 * @author alexis
 */
public class Marca {
    
    private String Descripcion;
    private Integer Codigo;

    public Marca(Integer Codigo, String Descripcion) {
        this.Descripcion = Descripcion;
        this.Codigo = Codigo;
    }
    

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer Codigo) {
        this.Codigo = Codigo;
    }

    
}
