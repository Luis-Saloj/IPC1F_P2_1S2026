/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import javax.swing.*;
/**
 *
 * @author salmn
 */
public class EstudiantePanel extends JFrame {
    private String codigoEstudiante;
    public EstudiantePanel(String codigo) {
        this.codigoEstudiante = codigo;
        setTitle("Panel Estudiante");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Panel de Estudiante - En construcción", JLabel.CENTER));
    }
}
