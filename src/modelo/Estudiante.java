/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author salmn
 */
public class Estudiante extends Usuario{
    private String[] seccionesInscritas;
    private int totalSecciones;

    public Estudiante(String codigo, String nombre, String contrasena,
                      String genero, String fechaNacimiento) {
        super(codigo, nombre, contrasena, genero, fechaNacimiento);
        this.seccionesInscritas = new String[50];
        this.totalSecciones = 0;
    }

    public void inscribirSeccion(String codigoSeccion) {
        if (totalSecciones < seccionesInscritas.length) {
            seccionesInscritas[totalSecciones] = codigoSeccion;
            totalSecciones++;
        }
    }

    // Verifica si ya está inscrito en una sección (evita duplicados)
    public boolean estaInscrito(String codigoSeccion) {
        for (int i = 0; i < totalSecciones; i++) {
            if (seccionesInscritas[i].equals(codigoSeccion)) {
                return true;
            }
        }
        return false;
    }

    public void desasignarSeccion(String codigoSeccion) {
        for (int i = 0; i < totalSecciones; i++) {
            if (seccionesInscritas[i].equals(codigoSeccion)) {
                // Corre todos los elementos un lugar hacia la izquierda
                for (int j = i; j < totalSecciones - 1; j++) {
                    seccionesInscritas[j] = seccionesInscritas[j + 1];
                }
                seccionesInscritas[totalSecciones - 1] = null;
                totalSecciones--;
                return;
            }
        }
    }

    public String[] getSeccionesInscritas() {
        String[] resultado = new String[totalSecciones];
        for (int i = 0; i < totalSecciones; i++) {
            resultado[i] = seccionesInscritas[i];
        }
        return resultado;
    }

    public int getTotalSecciones() { return totalSecciones; }

    @Override
    public String getTipoUsuario() {
        return "ESTUDIANTE";
    }
}
