/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import controlador.AuthController;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author salmn
 */
public class LoginForm extends JFrame {
    private JTextField txtCodigo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblMensaje;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {

        setTitle("Sancarlista Academy - Iniciar Sesión");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Sancarlista Academy");
        lblTitulo.setBounds(90, 20, 260, 35);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(34, 85, 34));

        JLabel lblSub = new JLabel("Ingresa tus credenciales");
        lblSub.setBounds(130, 55, 200, 20);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(60, 95, 80, 20);
        lblCodigo.setFont(new Font("Arial", Font.PLAIN, 13));

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 93, 200, 28);
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(60, 140, 90, 20);
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 13));

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 138, 200, 28);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 13));

        lblMensaje = new JLabel("");
        lblMensaje.setBounds(60, 175, 300, 20);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(150, 205, 200, 35);
        btnIngresar.setBackground(new Color(34, 85, 34));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnIngresar.addActionListener(e -> intentarLogin());
        txtContrasena.addActionListener(e -> intentarLogin());

        panel.add(lblTitulo);
        panel.add(lblSub);
        panel.add(lblCodigo);
        panel.add(txtCodigo);
        panel.add(lblContrasena);
        panel.add(txtContrasena);
        panel.add(lblMensaje);
        panel.add(btnIngresar);

        add(panel);
    }

    private void intentarLogin() {
        String codigo     = txtCodigo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (codigo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor completa todos los campos.");
            return;
        }

        String rol = AuthController.login(codigo, contrasena);

        switch (rol) {
            case "ADMINISTRADOR":
                dispose();
                new AdminPanel().setVisible(true);
                break;
            case "INSTRUCTOR":
                dispose();
                new InstructorPanel(codigo).setVisible(true);
                break;
            case "ESTUDIANTE":
                dispose();
                new EstudiantePanel(codigo).setVisible(true);
                break;
            default:
                lblMensaje.setText("Código o contraseña incorrectos.");
                txtContrasena.setText("");
                break;
        }
    }
}
