/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import controlador.EstudianteController;
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
public class EstudiantePanel extends JFrame {

    private String codigoEstudiante;

    private JTabbedPane pestanas;

    // ---- Pestaña inscripciones ----
    private JTable tablaDisponibles;
    private DefaultTableModel modeloDisponibles;

    // ---- Pestaña mis cursos ----
    private JTable tablaMisCursos;
    private DefaultTableModel modeloMisCursos;
    private JTable tablaNotas;
    private DefaultTableModel modeloNotas;
    private JLabel lblPromedio;

    public EstudiantePanel(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
        initComponents();
        cargarTodo();
    }

    private void initComponents() {
        setTitle("Sancarlista Academy - Estudiante: " + codigoEstudiante);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanas = new JTabbedPane();
        pestanas.addTab("Inscribirse a secciones", panelInscripciones());
        pestanas.addTab("Mis cursos y notas",      panelMisCursos());

        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> {
            serializador.guardar();
            dispose();
            new LoginForm().setVisible(true);
        });

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnCerrar);

        add(pestanas,  BorderLayout.CENTER);
        add(sur,       BorderLayout.SOUTH);
    }

    // ================================================================
    //  PESTAÑA INSCRIPCIONES
    // ================================================================
    private JPanel panelInscripciones() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloDisponibles = new DefaultTableModel(
                new String[]{"Código", "Curso", "Instructor",
                             "Semestre", "Horario"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDisponibles = new JTable(modeloDisponibles);
        tablaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnInscribirse = new JButton("Inscribirse en sección seleccionada");
        btnInscribirse.setBackground(new Color(34, 85, 34));
        btnInscribirse.setForeground(Color.WHITE);
        btnInscribirse.setFocusPainted(false);

        btnInscribirse.addActionListener(e -> {
            int fila = tablaDisponibles.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona una sección primero.");
                return;
            }
            String codSeccion = (String) modeloDisponibles.getValueAt(fila, 0);
            boolean ok = EstudianteController.inscribirse(
                    codigoEstudiante, codSeccion);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "¡Inscripción exitosa en " + codSeccion + "!");
                cargarTodo();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo inscribir.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel lblInfo = new JLabel(
                "Selecciona una sección y presiona el botón para inscribirte.");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);

        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(lblInfo,         BorderLayout.WEST);
        sur.add(btnInscribirse,  BorderLayout.EAST);

        panel.add(new JScrollPane(tablaDisponibles), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    // ================================================================
    //  PESTAÑA MIS CURSOS Y NOTAS
    // ================================================================
    private JPanel panelMisCursos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla de secciones inscritas
        modeloMisCursos = new DefaultTableModel(
                new String[]{"Código sección", "Curso",
                             "Semestre", "Promedio"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMisCursos = new JTable(modeloMisCursos);
        tablaMisCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollCursos = new JScrollPane(tablaMisCursos);
        scrollCursos.setBorder(BorderFactory.createTitledBorder(
                "Mis secciones inscritas"));
        scrollCursos.setPreferredSize(new Dimension(0, 180));

        // Tabla de notas de la sección seleccionada
        modeloNotas = new DefaultTableModel(
                new String[]{"Etiqueta", "Ponderación %",
                             "Valor", "Fecha"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaNotas = new JTable(modeloNotas);
        JScrollPane scrollNotas = new JScrollPane(tablaNotas);
        scrollNotas.setBorder(BorderFactory.createTitledBorder(
                "Notas de la sección seleccionada"));

        // Promedio
        lblPromedio = new JLabel("Promedio: --");
        lblPromedio.setFont(new Font("Arial", Font.BOLD, 15));
        lblPromedio.setForeground(new Color(34, 85, 34));
        lblPromedio.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Al seleccionar una sección, carga sus notas
        tablaMisCursos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMisCursos.getSelectedRow();
            if (fila >= 0) {
                String codSec = (String) modeloMisCursos.getValueAt(fila, 0);
                cargarNotasDeSeccion(codSec);
            }
        });

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(scrollNotas, BorderLayout.CENTER);
        centro.add(lblPromedio, BorderLayout.SOUTH);

        panel.add(scrollCursos, BorderLayout.NORTH);
        panel.add(centro,       BorderLayout.CENTER);

        return panel;
    }

    // ================================================================
    //  CARGA DE DATOS
    // ================================================================
    private void cargarTodo() {
        cargarSeccionesDisponibles();
        cargarMisCursos();
    }

    private void cargarSeccionesDisponibles() {
        modeloDisponibles.setRowCount(0);
        Seccion[] disponibles = EstudianteController
                .getSeccionesDisponibles(codigoEstudiante);
        for (Seccion s : disponibles) {
            modeloDisponibles.addRow(new Object[]{
                s.getCodigo(),
                EstudianteController.getNombreCurso(s.getCodigo()),
                s.getCodigoInstructor(),
                s.getSemestre(),
                s.getHorario()
            });
        }
    }

    private void cargarMisCursos() {
        modeloMisCursos.setRowCount(0);
        modeloNotas.setRowCount(0);
        lblPromedio.setText("Promedio: --");

        Seccion[] inscritas = EstudianteController
                .getSeccionesInscritas(codigoEstudiante);
        for (Seccion s : inscritas) {
            if (s != null) {
                double prom = EstudianteController.getPromedio(
                        codigoEstudiante, s.getCodigo());
                modeloMisCursos.addRow(new Object[]{
                    s.getCodigo(),
                    EstudianteController.getNombreCurso(s.getCodigo()),
                    s.getSemestre(),
                    String.format("%.2f", prom)
                });
            }
        }
    }

    private void cargarNotasDeSeccion(String codigoSeccion) {
        modeloNotas.setRowCount(0);
        Nota[] notas = EstudianteController.getNotas(
                codigoEstudiante, codigoSeccion);
        for (Nota n : notas) {
            modeloNotas.addRow(new Object[]{
                n.getEtiqueta(),
                n.getPonderacion() + "%",
                n.getValor(),
                n.getFecha()
            });
        }
        double prom = EstudianteController.getPromedio(
                codigoEstudiante, codigoSeccion);
        lblPromedio.setText("Promedio ponderado: " +
                String.format("%.2f", prom));
    }
}
