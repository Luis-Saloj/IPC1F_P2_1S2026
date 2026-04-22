/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author salmn
 */
public class serializador {
    // Carpeta donde se guardarán los archivos .ser
    // Se creará automáticamente si no existe
    private static final String CARPETA = "datos/";
    private static final String ARCHIVO_BD = CARPETA + "basededatos.ser";

    // -------------------------------------------------------
    // GUARDAR toda la BaseDeDatos en un archivo .ser
    // -------------------------------------------------------
    public static boolean guardar() {
        try {
            // Crea la carpeta "datos/" si no existe
            File carpeta = new File(CARPETA);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // FileOutputStream abre el archivo para escribir
            // ObjectOutputStream convierte el objeto Java en bytes
            FileOutputStream fos = new FileOutputStream(ARCHIVO_BD);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Guardamos toda la base de datos de un solo golpe
            oos.writeObject(BaseDeDatos.getInstancia());

            oos.close();
            fos.close();
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al guardar datos: " + e.getMessage(),
                "Error de guardado",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // -------------------------------------------------------
    // CARGAR la BaseDeDatos desde el archivo .ser
    // -------------------------------------------------------
    public static boolean cargar() {
        try {
            File archivo = new File(ARCHIVO_BD);

            // Si no existe el archivo aún, es la primera vez que corre el programa
            // No es un error, simplemente empezamos con datos vacíos
            if (!archivo.exists()) {
                return false;
            }

            // FileInputStream abre el archivo para leer
            // ObjectInputStream convierte los bytes de vuelta a objeto Java
            FileInputStream fis = new FileInputStream(ARCHIVO_BD);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Leemos el objeto y lo casteamos a BaseDeDatos
            BaseDeDatos bd = (BaseDeDatos) ois.readObject();

            ois.close();
            fis.close();

            // Reemplazamos la instancia actual con la que cargamos del archivo
            BaseDeDatos.setInstancia(bd);
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al cargar datos: " + e.getMessage(),
                "Error de carga",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // -------------------------------------------------------
    // Verifica si ya existe un archivo guardado
    // -------------------------------------------------------
    public static boolean existeArchivo() {
        return new File(ARCHIVO_BD).exists();
    }
}
