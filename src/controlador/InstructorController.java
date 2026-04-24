/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.Instructor;
import modelo.Seccion;
import modelo.Estudiante;
import modelo.Nota;
import utilidades.BaseDeDatos;
import utilidades.Bitacora;
import utilidades.serializador;
/**
 *
 * @author salmn
 */
public class InstructorController {
    private static BaseDeDatos bd = BaseDeDatos.getInstancia();

    // Devuelve las secciones asignadas a un instructor
    public static Seccion[] getSeccionesDeInstructor(String codigoInstructor) {
        return bd.getSeccionesDeInstructor(codigoInstructor);
    }

    // Devuelve los estudiantes inscritos en una sección
    public static Estudiante[] getEstudiantesDeSeccion(String codigoSeccion) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return new Estudiante[0];

        String[] codigos = s.getCodigosEstudiantes();
        Estudiante[] resultado = new Estudiante[codigos.length];
        for (int i = 0; i < codigos.length; i++) {
            resultado[i] = bd.buscarEstudiante(codigos[i]);
        }
        return resultado;
    }

    // Registra una nota a un estudiante en una sección
    public static boolean registrarNota(String codigoSeccion, String codigoEstudiante,
            String etiqueta, double ponderacion, double valor, String fecha) {

        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return false;
        if (!s.tieneEstudiante(codigoEstudiante)) return false;
        if (etiqueta.isEmpty() || ponderacion <= 0 || valor < 0) return false;

        Nota nota = new Nota(codigoEstudiante, etiqueta, ponderacion, valor, fecha);
        s.agregarNota(nota);

        Bitacora.registrar("INSTRUCTOR", codigoEstudiante,
                "REGISTRAR_NOTA", true,
                "Sección: " + codigoSeccion + " | Nota: " + etiqueta);
        serializador.guardar();
        return true;
    }

    // Devuelve las notas de un estudiante en una sección
    public static Nota[] getNotasDeEstudiante(String codigoSeccion,
            String codigoEstudiante) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return new Nota[0];

        Nota[] todas = s.getNotas();
        // Primero contamos cuántas son de ese estudiante
        int count = 0;
        for (Nota n : todas) {
            if (n.getCodigoEstudiante().equals(codigoEstudiante)) count++;
        }
        // Luego las extraemos
        Nota[] resultado = new Nota[count];
        int idx = 0;
        for (Nota n : todas) {
            if (n.getCodigoEstudiante().equals(codigoEstudiante)) {
                resultado[idx++] = n;
            }
        }
        return resultado;
    }

    public static double calcularPromedio(String codigoSeccion,
            String codigoEstudiante) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return 0;
        return s.calcularPromedio(codigoEstudiante);
    }
}
