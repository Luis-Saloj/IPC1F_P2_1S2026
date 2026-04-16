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
// Serializable permite guardar objetos en archivos .ser
public abstract class Usuario implements Serializable {

    private String codigo;
    private String nombre;
    private String contrasena;
    private String genero;
    private String fechaNacimiento;
    
    // Constructor: se ejecuta cuando creamos un objeto nuevo
    public Usuario(String codigo, String nombre, String contrasena, String genero, String fechaNacimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        
//Getters 
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getGenero() {
        return genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
//Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    //public abstract, cada clase debe implementar este metodo a su manera
    public abstract String getTipoUsuario();
    @Override
    public String toString() {
        return codigo + " | " + nombre + " | " + getTipoUsuario();
    }
}
