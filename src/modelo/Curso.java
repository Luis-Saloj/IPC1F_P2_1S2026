/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.io.Serializable;
/**
 *
 * @author salmn
 */
public class Curso implements Serializable{
    private String codigo;
    private String nombre;
    private String descripcion;
    private int creditos;

    public Curso(String codigo, String nombre, String descripcion, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
        
    }
    @Override
    public String toString() {
        return codigo + " | " + nombre + " | " + creditos + " créditos";
    }
}
