/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import modelo.Administrador;
import modelo.Instructor;
import modelo.Estudiante;
import modelo.Curso;
import modelo.Seccion;
/**
 *
 * @author salmn
 */

public class BaseDeDatos {
     // La única instancia que existirá en todo el programa
    private static BaseDeDatos instancia;
     // ---- Arreglos de usuarios ----
    private Instructor[] instructores;
    private Estudiante[] estudiantes;
    private int totalInstructores;
    private int totalEstudiantes;

    // ---- Arreglos de contenido académico ----
    private Curso[] cursos;
    private Seccion[] secciones;
    private int totalCursos;
    private int totalSecciones;

    // ---- Administrador del sistema ----
    private Administrador admin;

    // ---- Constructor privado (nadie puede hacer "new BaseDeDatos()" desde afuera) ----
    private BaseDeDatos() {
        instructores     = new Instructor[200];
        estudiantes      = new Estudiante[500];
        cursos           = new Curso[200];
        secciones        = new Seccion[300];
        totalInstructores = 0;
        totalEstudiantes  = 0;
        totalCursos       = 0;
        totalSecciones    = 0;

        // Usuario admin por defecto que pide el proyecto
        // La sección la debes cambiar por la tuya, ej: "A", "B", etc.
        admin = new Administrador("admin", "Administrador", "IPC1F",
                                  "M", "01/01/2000");
    }

    // Método para obtener la única instancia (la crea si no existe aún)
    public static BaseDeDatos getInstancia() {
        if (instancia == null) {
            instancia = new BaseDeDatos();
        }
        return instancia;
    }

    // ========== INSTRUCTORES ==========

    public boolean agregarInstructor(Instructor instructor) {
        // Verifica que el código no esté repetido
        if (buscarInstructor(instructor.getCodigo()) != null) return false;
        if (totalInstructores >= instructores.length) return false;
        instructores[totalInstructores] = instructor;
        totalInstructores++;
        return true;
    }

    public Instructor buscarInstructor(String codigo) {
        for (int i = 0; i < totalInstructores; i++) {
            if (instructores[i].getCodigo().equals(codigo)) {
                return instructores[i];
            }
        }
        return null;
    }

    public boolean eliminarInstructor(String codigo) {
        for (int i = 0; i < totalInstructores; i++) {
            if (instructores[i].getCodigo().equals(codigo)) {
                // Corre el arreglo hacia la izquierda
                for (int j = i; j < totalInstructores - 1; j++) {
                    instructores[j] = instructores[j + 1];
                }
                instructores[totalInstructores - 1] = null;
                totalInstructores--;
                return true;
            }
        }
        return false;
    }

    public Instructor[] getInstructores() {
        Instructor[] resultado = new Instructor[totalInstructores];
        for (int i = 0; i < totalInstructores; i++) {
            resultado[i] = instructores[i];
        }
        return resultado;
    }

    public int getTotalInstructores() { return totalInstructores; }

    // ========== ESTUDIANTES ==========

    public boolean agregarEstudiante(Estudiante estudiante) {
        if (buscarEstudiante(estudiante.getCodigo()) != null) return false;
        if (totalEstudiantes >= estudiantes.length) return false;
        estudiantes[totalEstudiantes] = estudiante;
        totalEstudiantes++;
        return true;
    }

    public Estudiante buscarEstudiante(String codigo) {
        for (int i = 0; i < totalEstudiantes; i++) {
            if (estudiantes[i].getCodigo().equals(codigo)) {
                return estudiantes[i];
            }
        }
        return null;
    }

    public boolean eliminarEstudiante(String codigo) {
        for (int i = 0; i < totalEstudiantes; i++) {
            if (estudiantes[i].getCodigo().equals(codigo)) {
                for (int j = i; j < totalEstudiantes - 1; j++) {
                    estudiantes[j] = estudiantes[j + 1];
                }
                estudiantes[totalEstudiantes - 1] = null;
                totalEstudiantes--;
                return true;
            }
        }
        return false;
    }

    public Estudiante[] getEstudiantes() {
        Estudiante[] resultado = new Estudiante[totalEstudiantes];
        for (int i = 0; i < totalEstudiantes; i++) {
            resultado[i] = estudiantes[i];
        }
        return resultado;
    }

    public int getTotalEstudiantes() { return totalEstudiantes; }

    // ========== CURSOS ==========

    public boolean agregarCurso(Curso curso) {
        if (buscarCurso(curso.getCodigo()) != null) return false;
        if (totalCursos >= cursos.length) return false;
        cursos[totalCursos] = curso;
        totalCursos++;
        return true;
    }

    public Curso buscarCurso(String codigo) {
        for (int i = 0; i < totalCursos; i++) {
            if (cursos[i].getCodigo().equals(codigo)) {
                return cursos[i];
            }
        }
        return null;
    }

    public boolean eliminarCurso(String codigo) {
        for (int i = 0; i < totalCursos; i++) {
            if (cursos[i].getCodigo().equals(codigo)) {
                for (int j = i; j < totalCursos - 1; j++) {
                    cursos[j] = cursos[j + 1];
                }
                cursos[totalCursos - 1] = null;
                totalCursos--;
                return true;
            }
        }
        return false;
    }

    public Curso[] getCursos() {
        Curso[] resultado = new Curso[totalCursos];
        for (int i = 0; i < totalCursos; i++) {
            resultado[i] = cursos[i];
        }
        return resultado;
    }

    public int getTotalCursos() { return totalCursos; }

    // ========== SECCIONES ==========

    public boolean agregarSeccion(Seccion seccion) {
        if (buscarSeccion(seccion.getCodigo()) != null) return false;
        if (totalSecciones >= secciones.length) return false;
        secciones[totalSecciones] = seccion;
        totalSecciones++;
        return true;
    }

    public Seccion buscarSeccion(String codigo) {
        for (int i = 0; i < totalSecciones; i++) {
            if (secciones[i].getCodigo().equals(codigo)) {
                return secciones[i];
            }
        }
        return null;
    }

    public boolean eliminarSeccion(String codigo) {
        for (int i = 0; i < totalSecciones; i++) {
            if (secciones[i].getCodigo().equals(codigo)) {
                for (int j = i; j < totalSecciones - 1; j++) {
                    secciones[j] = secciones[j + 1];
                }
                secciones[totalSecciones - 1] = null;
                totalSecciones--;
                return true;
            }
        }
        return false;
    }

    public Seccion[] getSecciones() {
        Seccion[] resultado = new Seccion[totalSecciones];
        for (int i = 0; i < totalSecciones; i++) {
            resultado[i] = secciones[i];
        }
        return resultado;
    }

    // Devuelve solo las secciones de un instructor específico
    public Seccion[] getSeccionesDeInstructor(String codigoInstructor) {
        int count = 0;
        for (int i = 0; i < totalSecciones; i++) {
            if (secciones[i].getCodigoInstructor().equals(codigoInstructor)) count++;
        }
        Seccion[] resultado = new Seccion[count];
        int idx = 0;
        for (int i = 0; i < totalSecciones; i++) {
            if (secciones[i].getCodigoInstructor().equals(codigoInstructor)) {
                resultado[idx++] = secciones[i];
            }
        }
        return resultado;
    }

    public int getTotalSecciones() { return totalSecciones; }

    // ========== ADMIN ==========

    public Administrador getAdmin() { return admin; }

    // Permite reemplazar la instancia completa al cargar desde archivo .ser
    public static void setInstancia(BaseDeDatos bd) {
        instancia = bd;
    }
}
