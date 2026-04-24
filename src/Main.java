/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import utilidades.serializador;
import vista.LoginForm;
/**
 *
 * @author salmn
 */
public class Main {
    public static void main(String[] args) {

        // Intenta cargar datos guardados del archivo .ser
        // Si no existe (primera vez), simplemente continúa con datos vacíos
        serializador.cargar();

        // SwingUtilities.invokeLater asegura que la ventana se abra
        // en el hilo correcto de Java Swing (buena práctica obligatoria)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    
    }
}

