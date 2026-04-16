/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author salmn
 */
public class Administrador extends Usuario {

    //hereda lo de Usuario

    public Administrador(String codigo, String nombre, String contrasena, String genero, String fechaNacimiento) {
        // llama al constructor de Usuario
        super(codigo, nombre, contrasena, genero, fechaNacimiento);
    }

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
    }
}
