/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.Administrador;
import modelo.Instructor;
import modelo.Estudiante;
import modelo.Curso;
import modelo.Seccion;
import utilidades.BaseDeDatos;
import utilidades.Bitacora;
import utilidades.serializador;
/**
 *
 * @author salmn
 */
public class AdminController {
    private static BaseDeDatos bd = BaseDeDatos.getInstancia();

    // ========== INSTRUCTORES ==========

    public static boolean crearInstructor(String codigo, String nombre,
            String contrasena, String genero, String fechaNac) {
        if (codigo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) return false;

        Instructor i = new Instructor(codigo, nombre, contrasena, genero, fechaNac);
        boolean ok = bd.agregarInstructor(i);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "CREAR_INSTRUCTOR", ok, "Instructor: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    public static boolean editarInstructor(String codigo, String nuevoNombre,
            String nuevaContrasena) {
        Instructor i = bd.buscarInstructor(codigo);
        if (i == null) return false;
        if (!nuevoNombre.isEmpty())    i.setNombre(nuevoNombre);
        if (!nuevaContrasena.isEmpty()) i.setContrasena(nuevaContrasena);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "EDITAR_INSTRUCTOR", true, "Instructor: " + codigo);
        serializador.guardar();
        return true;
    }

    public static boolean eliminarInstructor(String codigo) {
        boolean ok = bd.eliminarInstructor(codigo);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "ELIMINAR_INSTRUCTOR", ok, "Instructor: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    // ========== ESTUDIANTES ==========

    public static boolean crearEstudiante(String codigo, String nombre,
            String contrasena, String genero, String fechaNac) {
        if (codigo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) return false;

        Estudiante e = new Estudiante(codigo, nombre, contrasena, genero, fechaNac);
        boolean ok = bd.agregarEstudiante(e);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "CREAR_ESTUDIANTE", ok, "Estudiante: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    public static boolean editarEstudiante(String codigo, String nuevoNombre,
            String nuevaContrasena) {
        Estudiante e = bd.buscarEstudiante(codigo);
        if (e == null) return false;
        if (!nuevoNombre.isEmpty())     e.setNombre(nuevoNombre);
        if (!nuevaContrasena.isEmpty()) e.setContrasena(nuevaContrasena);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "EDITAR_ESTUDIANTE", true, "Estudiante: " + codigo);
        serializador.guardar();
        return true;
    }

    public static boolean eliminarEstudiante(String codigo) {
        boolean ok = bd.eliminarEstudiante(codigo);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "ELIMINAR_ESTUDIANTE", ok, "Estudiante: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    // ========== CURSOS ==========

    public static boolean crearCurso(String codigo, String nombre,
            String descripcion, int creditos) {
        if (codigo.isEmpty() || nombre.isEmpty()) return false;

        Curso c = new Curso(codigo, nombre, descripcion, creditos);
        boolean ok = bd.agregarCurso(c);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "CREAR_CURSO", ok, "Curso: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    public static boolean editarCurso(String codigo, String nuevoNombre,
            String nuevaDesc, int creditos) {
        Curso c = bd.buscarCurso(codigo);
        if (c == null) return false;
        if (!nuevoNombre.isEmpty()) c.setNombre(nuevoNombre);
        if (!nuevaDesc.isEmpty())   c.setDescripcion(nuevaDesc);
        if (creditos > 0)           c.setCreditos(creditos);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "EDITAR_CURSO", true, "Curso: " + codigo);
        serializador.guardar();
        return true;
    }

    public static boolean eliminarCurso(String codigo) {
        boolean ok = bd.eliminarCurso(codigo);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "ELIMINAR_CURSO", ok, "Curso: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }

    // ========== SECCIONES ==========

    public static boolean crearSeccion(String codigo, String codigoCurso,
            String codigoInstructor, String semestre, String horario) {
        if (codigo.isEmpty() || codigoCurso.isEmpty() || codigoInstructor.isEmpty()) return false;
        if (bd.buscarCurso(codigoCurso) == null) return false;
        if (bd.buscarInstructor(codigoInstructor) == null) return false;

        Seccion s = new Seccion(codigo, codigoCurso, codigoInstructor, semestre, horario);
        boolean ok = bd.agregarSeccion(s);

        if (ok) {
            // Asigna la sección al instructor
            bd.buscarInstructor(codigoInstructor).agregarSeccion(codigo);
            Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                    "CREAR_SECCION", true, "Sección: " + codigo);
            serializador.guardar();
        }
        return ok;
    }

    public static boolean eliminarSeccion(String codigo) {
        boolean ok = bd.eliminarSeccion(codigo);
        Bitacora.registrar("ADMINISTRADOR", bd.getAdmin().getCodigo(),
                "ELIMINAR_SECCION", ok, "Sección: " + codigo);
        if (ok) serializador.guardar();
        return ok;
    }
}
