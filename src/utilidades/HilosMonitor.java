/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

/**
 *
 * @author salmn
 */
public class HilosMonitor {
    // Hilo que actualiza los contadores cada segundo
    private Thread hiloContador;
    private boolean corriendo = false;

    // Interfaz para comunicar los datos al panel sin acoplamiento
    public interface ActualizadorUI {
        void actualizar(int instructores, int estudiantes,
                        int cursos, int secciones, String tiempo);
    }

    private ActualizadorUI actualizador;
    private long tiempoInicio;

    public HilosMonitor(ActualizadorUI actualizador) {
        this.actualizador = actualizador;
        this.tiempoInicio = System.currentTimeMillis();
    }

    // Inicia el hilo de monitoreo
    public void iniciar() {
        corriendo = true;
        hiloContador = new Thread(() -> {
            while (corriendo) {
                try {
                    BaseDeDatos bd = BaseDeDatos.getInstancia();

                    // Calcula el tiempo transcurrido desde que abrió sesión
                    long transcurrido = System.currentTimeMillis() - tiempoInicio;
                    long segundos = (transcurrido / 1000) % 60;
                    long minutos  = (transcurrido / 60000) % 60;
                    long horas    = (transcurrido / 3600000);
                    String tiempo = String.format("%02d:%02d:%02d",
                            horas, minutos, segundos);

                    // SwingUtilities.invokeLater actualiza la UI desde
                    // el hilo de Swing de forma segura
                    javax.swing.SwingUtilities.invokeLater(() ->
                        actualizador.actualizar(
                            bd.getTotalInstructores(),
                            bd.getTotalEstudiantes(),
                            bd.getTotalCursos(),
                            bd.getTotalSecciones(),
                            tiempo)
                    );

                    // Espera 1 segundo antes de actualizar de nuevo
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    corriendo = false;
                }
            }
        });

        // Daemon = el hilo se cierra solo cuando se cierra el programa
        hiloContador.setDaemon(true);
        hiloContador.start();
    }

    // Detiene el hilo limpiamente
    public void detener() {
        corriendo = false;
        if (hiloContador != null) {
            hiloContador.interrupt();
        }
    }
}
