/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.Estudiante;
import modelo.Seccion;
import modelo.Curso;
import modelo.Nota;
import utilidades.BaseDeDatos;
import utilidades.Bitacora;
import utilidades.serializador;
/**
 *
 * @author salmn
 */
public class EstudianteController {
    private static BaseDeDatos bd = BaseDeDatos.getInstancia();

    // Devuelve todas las secciones disponibles para inscribirse
    public static Seccion[] getSeccionesDisponibles(String codigoEstudiante) {
        Seccion[] todas = bd.getSecciones();
        // Contamos las que NO está inscrito
        int count = 0;
        for (Seccion s : todas) {
            if (!s.tieneEstudiante(codigoEstudiante)) count++;
        }
        Seccion[] resultado = new Seccion[count];
        int idx = 0;
        for (Seccion s : todas) {
            if (!s.tieneEstudiante(codigoEstudiante)) {
                resultado[idx++] = s;
            }
        }
        return resultado;
    }

    // Devuelve las secciones en las que ya está inscrito
    public static Seccion[] getSeccionesInscritas(String codigoEstudiante) {
        Estudiante est = bd.buscarEstudiante(codigoEstudiante);
        if (est == null) return new Seccion[0];

        String[] codigos = est.getSeccionesInscritas();
        Seccion[] resultado = new Seccion[codigos.length];
        for (int i = 0; i < codigos.length; i++) {
            resultado[i] = bd.buscarSeccion(codigos[i]);
        }
        return resultado;
    }

    // Inscribe al estudiante en una sección
    public static boolean inscribirse(String codigoEstudiante,
            String codigoSeccion) {
        Estudiante est = bd.buscarEstudiante(codigoEstudiante);
        Seccion sec    = bd.buscarSeccion(codigoSeccion);

        if (est == null || sec == null) return false;
        if (est.estaInscrito(codigoSeccion)) return false;

        est.inscribirSeccion(codigoSeccion);
        sec.agregarEstudiante(codigoEstudiante);

        Bitacora.registrar("ESTUDIANTE", codigoEstudiante,
                "INSCRIPCION", true, "Sección: " + codigoSeccion);
        serializador.guardar();
        return true;
    }

    // Devuelve las notas del estudiante en una sección específica
    public static Nota[] getNotas(String codigoEstudiante,
            String codigoSeccion) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return new Nota[0];

        Nota[] todas = s.getNotas();
        int count = 0;
        for (Nota n : todas) {
            if (n.getCodigoEstudiante().equals(codigoEstudiante)) count++;
        }
        Nota[] resultado = new Nota[count];
        int idx = 0;
        for (Nota n : todas) {
            if (n.getCodigoEstudiante().equals(codigoEstudiante)) {
                resultado[idx++] = n;
            }
        }
        return resultado;
    }

    public static double getPromedio(String codigoEstudiante,
            String codigoSeccion) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return 0;
        return s.calcularPromedio(codigoEstudiante);
    }

    // Devuelve el nombre del curso de una sección
    public static String getNombreCurso(String codigoSeccion) {
        Seccion s = bd.buscarSeccion(codigoSeccion);
        if (s == null) return "Desconocido";
        Curso c = bd.buscarCurso(s.getCodigoCurso());
        if (c == null) return s.getCodigoCurso();
        return c.getNombre();
    }
}
