/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import controlador.InstructorController;
import modelo.Estudiante;
import modelo.Nota;
import modelo.Seccion;
import utilidades.serializador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 *
 * @author salmn
 */
public class InstructorPanel extends JFrame {

    private String codigoInstructor;

    // Sección seleccionada actualmente
    private String seccionActual = null;

    // Componentes principales
    private JComboBox<String> cmbSecciones;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloEstudiantes;
    private JTable tablaNotas;
    private DefaultTableModel modeloNotas;

    // Formulario de nota
    private JTextField txtEtiqueta;
    private JTextField txtPonderacion;
    private JTextField txtValor;
    private JTextField txtFecha;
    private JLabel lblPromedioEstudiante;

    public InstructorPanel(String codigoInstructor) {
        this.codigoInstructor = codigoInstructor;
        initComponents();
        cargarSecciones();
    }

    private void initComponents() {
        setTitle("Sancarlista Academy - Instructor: " + codigoInstructor);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- Panel norte: selector de sección ----
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        norte.add(new JLabel("Sección:"));
        cmbSecciones = new JComboBox<>();
        cmbSecciones.setPreferredSize(new Dimension(220, 28));
        norte.add(cmbSecciones);

        JButton btnCargar = new JButton("Cargar estudiantes");
        norte.add(btnCargar);

        // ---- Panel central: tabla estudiantes (izquierda) + notas (derecha) ----
        // Tabla de estudiantes
        modeloEstudiantes = new DefaultTableModel(
                new String[]{"Código", "Nombre", "Promedio"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEstudiantes = new JTable(modeloEstudiantes);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollEst = new JScrollPane(tablaEstudiantes);
        scrollEst.setPreferredSize(new Dimension(300, 0));
        scrollEst.setBorder(BorderFactory.createTitledBorder("Estudiantes"));

        // Tabla de notas del estudiante seleccionado
        modeloNotas = new DefaultTableModel(
                new String[]{"Etiqueta", "Ponderación %", "Valor", "Fecha"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaNotas = new JTable(modeloNotas);
        JScrollPane scrollNotas = new JScrollPane(tablaNotas);
        scrollNotas.setBorder(BorderFactory.createTitledBorder("Notas del estudiante"));

        // ---- Panel derecho: formulario para agregar nota ----
        JPanel formNota = new JPanel(new GridLayout(6, 2, 5, 5));
        formNota.setBorder(BorderFactory.createTitledBorder("Registrar nota"));

        txtEtiqueta    = new JTextField();
        txtPonderacion = new JTextField();
        txtValor       = new JTextField();
        txtFecha       = new JTextField(fechaHoy());
        lblPromedioEstudiante = new JLabel("--");
        lblPromedioEstudiante.setFont(new Font("Arial", Font.BOLD, 14));
        lblPromedioEstudiante.setForeground(new Color(34, 85, 34));

        formNota.add(new JLabel("Etiqueta:"));      formNota.add(txtEtiqueta);
        formNota.add(new JLabel("Ponderación %:")); formNota.add(txtPonderacion);
        formNota.add(new JLabel("Valor (0-100):")); formNota.add(txtValor);
        formNota.add(new JLabel("Fecha:"));         formNota.add(txtFecha);
        formNota.add(new JLabel("Promedio actual:")); formNota.add(lblPromedioEstudiante);

        JButton btnAgregarNota = new JButton("Registrar nota");
        btnAgregarNota.setBackground(new Color(34, 85, 34));
        btnAgregarNota.setForeground(Color.WHITE);
        btnAgregarNota.setFocusPainted(false);

        JPanel derecho = new JPanel(new BorderLayout(5, 5));
        derecho.add(formNota, BorderLayout.CENTER);
        derecho.add(btnAgregarNota, BorderLayout.SOUTH);
        derecho.setPreferredSize(new Dimension(260, 0));

        // ---- Panel central combinado ----
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centro.add(scrollEst,   BorderLayout.WEST);
        centro.add(scrollNotas, BorderLayout.CENTER);
        centro.add(derecho,     BorderLayout.EAST);

        // ---- Panel sur: cerrar sesión ----
        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> {
            serializador.guardar();
            dispose();
            new LoginForm().setVisible(true);
        });
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnCerrar);

        add(norte,  BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(sur,    BorderLayout.SOUTH);

        // ---- Acciones ----

        // Al cargar sección, muestra sus estudiantes
        btnCargar.addActionListener(e -> {
            if (cmbSecciones.getSelectedItem() == null) return;
            seccionActual = cmbSecciones.getSelectedItem().toString()
                              .split(" ")[0]; // toma solo el código
            cargarEstudiantes();
        });

        // Al seleccionar estudiante, muestra sus notas
        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaEstudiantes.getSelectedRow();
            if (fila >= 0 && seccionActual != null) {
                String codEst = (String) modeloEstudiantes.getValueAt(fila, 0);
                cargarNotasDeEstudiante(codEst);
                double prom = InstructorController.calcularPromedio(
                        seccionActual, codEst);
                lblPromedioEstudiante.setText(
                        String.format("%.2f", prom));
            }
        });

        // Registrar nota
        btnAgregarNota.addActionListener(e -> {
            int fila = tablaEstudiantes.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un estudiante primero.");
                return;
            }
            String codEst = (String) modeloEstudiantes.getValueAt(fila, 0);

            try {
                double pond = Double.parseDouble(txtPonderacion.getText().trim());
                double val  = Double.parseDouble(txtValor.getText().trim());

                boolean ok = InstructorController.registrarNota(
                        seccionActual, codEst,
                        txtEtiqueta.getText().trim(),
                        pond, val, txtFecha.getText().trim());

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Nota registrada.");
                    cargarNotasDeEstudiante(codEst);
                    double prom = InstructorController.calcularPromedio(
                            seccionActual, codEst);
                    lblPromedioEstudiante.setText(String.format("%.2f", prom));
                    // Actualiza el promedio en la tabla
                    modeloEstudiantes.setValueAt(
                            String.format("%.2f", prom), fila, 2);
                    txtEtiqueta.setText("");
                    txtPonderacion.setText("");
                    txtValor.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al registrar. Verifica los datos.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ponderación y valor deben ser números.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Carga las secciones del instructor en el combo
    private void cargarSecciones() {
        cmbSecciones.removeAllItems();
        Seccion[] secciones = InstructorController
                .getSeccionesDeInstructor(codigoInstructor);
        for (Seccion s : secciones) {
            cmbSecciones.addItem(s.getCodigo() + " - " + s.getCodigoCurso()
                    + " (" + s.getSemestre() + ")");
        }
    }

    // Carga los estudiantes de la sección seleccionada
    private void cargarEstudiantes() {
        modeloEstudiantes.setRowCount(0);
        modeloNotas.setRowCount(0);
        Estudiante[] estudiantes = InstructorController
                .getEstudiantesDeSeccion(seccionActual);
        for (Estudiante est : estudiantes) {
            if (est != null) {
                double prom = InstructorController.calcularPromedio(
                        seccionActual, est.getCodigo());
                modeloEstudiantes.addRow(new Object[]{
                    est.getCodigo(), est.getNombre(),
                    String.format("%.2f", prom)
                });
            }
        }
    }

    // Carga las notas de un estudiante en la tabla de notas
    private void cargarNotasDeEstudiante(String codigoEstudiante) {
        modeloNotas.setRowCount(0);
        Nota[] notas = InstructorController.getNotasDeEstudiante(
                seccionActual, codigoEstudiante);
        for (Nota n : notas) {
            modeloNotas.addRow(new Object[]{
                n.getEtiqueta(), n.getPonderacion() + "%",
                n.getValor(), n.getFecha()
            });
        }
    }

    // Devuelve la fecha de hoy en formato DD/MM/YYYY
    private String fechaHoy() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        return String.format("%02d/%02d/%d",
                hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());
    }
}
