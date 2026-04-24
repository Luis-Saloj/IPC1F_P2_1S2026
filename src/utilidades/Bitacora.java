/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author salmn
 */
public class Bitacora {
    private static final String ARCHIVO_LOG = "datos/bitacora.txt";
    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Registra un evento en el archivo de bitácora
    // Formato: [fecha] | TIPO_USUARIO | CODIGO | OPERACION | ESTADO | DESCRIPCION
    public static void registrar(String tipoUsuario, String codigoUsuario,
                                  String operacion, boolean exitosa,
                                  String descripcion) {
        try {
            // Crea la carpeta si no existe
            File carpeta = new File("datos/");
            if (!carpeta.exists()) carpeta.mkdirs();

            // "true" en FileWriter significa que agrega al final del archivo
            // sin borrar lo que ya había (modo append)
            FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
            PrintWriter pw = new PrintWriter(fw);

            String fecha  = LocalDateTime.now().format(FORMATO);
            String estado = exitosa ? "EXITOSA" : "FALLIDA";

            // Escribe la línea en el formato que pide el proyecto
            pw.println("[" + fecha + "] | " + tipoUsuario + " | " +
                       codigoUsuario + " | " + operacion + " | " +
                       estado + " | " + descripcion);

            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println("Error en bitácora: " + e.getMessage());
        }
    }

    // Método de conveniencia para errores del sistema
    public static void registrarError(String descripcion) {
        registrar("SISTEMA", "sistema", "ERROR_SISTEMA", false, descripcion);
    }

    // Lee todas las líneas de la bitácora y las devuelve como arreglo
    public static String[] leerBitacora() {
        try {
            File archivo = new File(ARCHIVO_LOG);
            if (!archivo.exists()) return new String[0];

            // Primero contamos cuántas líneas hay
            java.io.BufferedReader br =
                new java.io.BufferedReader(new java.io.FileReader(archivo));
            int totalLineas = 0;
            while (br.readLine() != null) totalLineas++;
            br.close();

            // Luego las leemos todas
            String[] lineas = new String[totalLineas];
            br = new java.io.BufferedReader(new java.io.FileReader(archivo));
            for (int i = 0; i < totalLineas; i++) {
                lineas[i] = br.readLine();
            }
            br.close();
            return lineas;

        } catch (Exception e) {
            return new String[0];
        }
    }
}
