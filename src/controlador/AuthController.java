/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.Administrador;
import modelo.Instructor;
import modelo.Estudiante;
import utilidades.BaseDeDatos;
import utilidades.Bitacora;
/**
 *
 * @author salmn
 */
public class AuthController {
    // Recibe código y contraseña, devuelve el rol si son correctos
    // Devuelve "ERROR" si las credenciales son incorrectas
    public static String login(String codigo, String contrasena) {

        BaseDeDatos bd = BaseDeDatos.getInstancia();

        // 1. ¿Es el administrador?
        Administrador admin = bd.getAdmin();
        if (admin.getCodigo().equals(codigo) &&
            admin.getContrasena().equals(contrasena)) {

            Bitacora.registrar("ADMINISTRADOR", codigo,
                "LOGIN", true, "Inicio de sesión exitoso");
            return "ADMINISTRADOR";
        }

        // 2. ¿Es un instructor?
        Instructor instructor = bd.buscarInstructor(codigo);
        if (instructor != null &&
            instructor.getContrasena().equals(contrasena)) {

            Bitacora.registrar("INSTRUCTOR", codigo,
                "LOGIN", true, "Inicio de sesión exitoso");
            return "INSTRUCTOR";
        }

        // 3. ¿Es un estudiante?
        Estudiante estudiante = bd.buscarEstudiante(codigo);
        if (estudiante != null &&
            estudiante.getContrasena().equals(contrasena)) {

            Bitacora.registrar("ESTUDIANTE", codigo,
                "LOGIN", true, "Inicio de sesión exitoso");
            return "ESTUDIANTE";
        }

        // 4. Credenciales incorrectas
        Bitacora.registrar("DESCONOCIDO", codigo,
            "LOGIN", false, "Credenciales incorrectas");
        return "ERROR";
    }
}
