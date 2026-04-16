/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author salmn
 */
public class Instructor extends Usuario {

    // Un instructor puede tener varias secciones asignadas
    private String[] seccionesAsignadas;
    private int totalSecciones; // cuántas secciones tiene actualmente

    public Instructor(String codigo, String nombre, String contrasena, String genero, String fechaNacimiento) {
        super(codigo, nombre, contrasena, genero, fechaNacimiento);
        
    // 50 secciones 
        this.seccionesAsignadas = new String[50];
        this.totalSecciones = 0;
    }

    // Agrega una sección al arreglo
    public void agregarSeccion(String codigoSeccion) {
        if (totalSecciones < seccionesAsignadas.length) {
            seccionesAsignadas[totalSecciones] = codigoSeccion;
            totalSecciones++;
        }
    }

    // Devuelve solo las secciones que tienen datos (no los espacios vacíos)
    public String[] getSeccionesAsignadas() {
        String[] resultado = new String[totalSecciones];
        for (int i = 0; i < totalSecciones; i++) {
            resultado[i] = seccionesAsignadas[i];
        }
        return resultado;
    }

    public int getTotalSecciones() { return totalSecciones; }

    @Override
    public String getTipoUsuario() {
        return "INSTRUCTOR";
    }
}