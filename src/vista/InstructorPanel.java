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
public class InstructorPanel extends JFrame {
    private String codigoInstructor;
    public InstructorPanel(String codigo) {
        this.codigoInstructor = codigo;
        setTitle("Panel Instructor");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Panel de Instructor - En construcción", JLabel.CENTER));
    }
}
