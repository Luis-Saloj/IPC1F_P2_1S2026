package vista;

import controlador.AdminController;
import modelo.Curso;
import modelo.Instructor;
import modelo.Estudiante;
import modelo.Seccion;
import utilidades.BaseDeDatos;
import utilidades.Bitacora;
import utilidades.serializador;
import utilidades.HilosMonitor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JFrame {

    private BaseDeDatos bd = BaseDeDatos.getInstancia();
    private JTabbedPane pestanas;
    // Hilos
private HilosMonitor monitor;

    // Labels del monitor
    private JLabel lblMonitorInstructores;
    private JLabel lblMonitorEstudiantes;
    private JLabel lblMonitorCursos;
    private JLabel lblMonitorSecciones;
    private JLabel lblMonitorTiempo;
    // ---- Tablas ----
    private JTable tablaInstructores, tablaEstudiantes, tablaCursos, tablaSecciones;
    private DefaultTableModel modeloInstructores, modeloEstudiantes,
                               modeloCursos, modeloSecciones;

    public AdminPanel() {
        initComponents();
        cargarTablas();
    }

    private void initComponents() {
        setTitle("Sancarlista Academy - Administrador");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanas = new JTabbedPane();
        pestanas.addTab("Instructores", panelInstructores());
        pestanas.addTab("Estudiantes",  panelEstudiantes());
        pestanas.addTab("Cursos",       panelCursos());
        pestanas.addTab("Secciones",    panelSecciones());
        pestanas.addTab("Bitácora",     panelBitacora());
        pestanas.addTab("Monitor", panelMonitor());
        pestanas.addTab("Reportes PDF", panelReportes());
        // Botón cerrar sesión
        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.addActionListener(e -> {
        monitor.detener();
        serializador.guardar();
        dispose();
        new LoginForm().setVisible(true);
    });

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnCerrar);

        add(pestanas, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
        // Inicia el monitor en tiempo real
        monitor = new HilosMonitor((inst, est, cur, sec, tiempo) -> {
            lblMonitorInstructores.setText(String.valueOf(inst));
            lblMonitorEstudiantes.setText(String.valueOf(est));
            lblMonitorCursos.setText(String.valueOf(cur));
            lblMonitorSecciones.setText(String.valueOf(sec));
            lblMonitorTiempo.setText(tiempo);
        });
        monitor.iniciar();
    }

    //PESTAÑA INSTRUCTORES

    private JPanel panelInstructores() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla
        modeloInstructores = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Género", "Fecha Nac.", "Secciones"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaInstructores = new JTable(modeloInstructores);
        tablaInstructores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaInstructores);

        // Formulario
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField txtCod   = new JTextField();
        JTextField txtNom   = new JTextField();
        JTextField txtPass  = new JTextField();
        JComboBox<String> cmbGen = new JComboBox<>(new String[]{"M", "F"});
        JTextField txtFecha = new JTextField("DD/MM/YYYY");

        form.add(new JLabel("Código:"));       form.add(txtCod);
        form.add(new JLabel("Nombre:"));       form.add(txtNom);
        form.add(new JLabel("Contraseña:"));   form.add(txtPass);
        form.add(new JLabel("Género:"));       form.add(cmbGen);
        form.add(new JLabel("Fecha Nac:"));    form.add(txtFecha);

        // Botones
        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);
        derecha.setPreferredSize(new Dimension(280, 0));

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

// Al seleccionar fila, llena el formulario
        tablaInstructores.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaInstructores.getSelectedRow();
            if (fila >= 0) {
                txtCod.setText((String) modeloInstructores.getValueAt(fila, 0));
                txtNom.setText((String) modeloInstructores.getValueAt(fila, 1));
                txtCod.setEditable(false);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtCod.setText(""); txtNom.setText("");
            txtPass.setText(""); txtFecha.setText("DD/MM/YYYY");
            txtCod.setEditable(true);
            tablaInstructores.clearSelection();
        });

        btnAgregar.addActionListener(e -> {
            boolean ok = AdminController.crearInstructor(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtPass.getText().trim(), cmbGen.getSelectedItem().toString(),
                txtFecha.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Instructor creado correctamente.");
                cargarTablaInstructores();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error: código duplicado o campos vacíos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            boolean ok = AdminController.editarInstructor(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtPass.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Instructor actualizado.");
                cargarTablaInstructores();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Instructor no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            String cod = txtCod.getText().trim();
            if (cod.isEmpty()) return;
            int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar instructor " + cod + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                AdminController.eliminarInstructor(cod);
                cargarTablaInstructores();
                btnLimpiar.doClick();
            }
        });

        return panel;
    }

//PESTAÑA ESTUDIANTES
    private JPanel panelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloEstudiantes = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Género", "Fecha Nac.", "Secciones"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEstudiantes = new JTable(modeloEstudiantes);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField txtCod   = new JTextField();
        JTextField txtNom   = new JTextField();
        JTextField txtPass  = new JTextField();
        JComboBox<String> cmbGen = new JComboBox<>(new String[]{"M", "F"});
        JTextField txtFecha = new JTextField("DD/MM/YYYY");

        form.add(new JLabel("Código:"));     form.add(txtCod);
        form.add(new JLabel("Nombre:"));     form.add(txtNom);
        form.add(new JLabel("Contraseña:")); form.add(txtPass);
        form.add(new JLabel("Género:"));     form.add(cmbGen);
        form.add(new JLabel("Fecha Nac:"));  form.add(txtFecha);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar); botones.add(btnEditar);
        botones.add(btnEliminar); botones.add(btnLimpiar);

        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);
        derecha.setPreferredSize(new Dimension(280, 0));

        panel.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaEstudiantes.getSelectedRow();
            if (fila >= 0) {
                txtCod.setText((String) modeloEstudiantes.getValueAt(fila, 0));
                txtNom.setText((String) modeloEstudiantes.getValueAt(fila, 1));
                txtCod.setEditable(false);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtCod.setText(""); txtNom.setText("");
            txtPass.setText(""); txtFecha.setText("DD/MM/YYYY");
            txtCod.setEditable(true);
            tablaEstudiantes.clearSelection();
        });

        btnAgregar.addActionListener(e -> {
            boolean ok = AdminController.crearEstudiante(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtPass.getText().trim(), cmbGen.getSelectedItem().toString(),
                txtFecha.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Estudiante creado correctamente.");
                cargarTablaEstudiantes();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error: código duplicado o campos vacíos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            boolean ok = AdminController.editarEstudiante(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtPass.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
                cargarTablaEstudiantes();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Estudiante no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            String cod = txtCod.getText().trim();
            if (cod.isEmpty()) return;
            int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar estudiante " + cod + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                AdminController.eliminarEstudiante(cod);
                cargarTablaEstudiantes();
                btnLimpiar.doClick();
            }
        });

        return panel;
    }

//PESTAÑA CURSOS
    private JPanel panelCursos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloCursos = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Descripción", "Créditos"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCursos = new JTable(modeloCursos);
        tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField txtCod  = new JTextField();
        JTextField txtNom  = new JTextField();
        JTextField txtDesc = new JTextField();
        JSpinner   spnCred = new JSpinner(new SpinnerNumberModel(3, 1, 12, 1));

        form.add(new JLabel("Código:"));      form.add(txtCod);
        form.add(new JLabel("Nombre:"));      form.add(txtNom);
        form.add(new JLabel("Descripción:")); form.add(txtDesc);
        form.add(new JLabel("Créditos:"));    form.add(spnCred);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar); botones.add(btnEditar);
        botones.add(btnEliminar); botones.add(btnLimpiar);

        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);
        derecha.setPreferredSize(new Dimension(280, 0));

        panel.add(new JScrollPane(tablaCursos), BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        tablaCursos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaCursos.getSelectedRow();
            if (fila >= 0) {
                txtCod.setText((String) modeloCursos.getValueAt(fila, 0));
                txtNom.setText((String) modeloCursos.getValueAt(fila, 1));
                txtDesc.setText((String) modeloCursos.getValueAt(fila, 2));
                spnCred.setValue(modeloCursos.getValueAt(fila, 3));
                txtCod.setEditable(false);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtCod.setText(""); txtNom.setText(""); txtDesc.setText("");
            spnCred.setValue(3); txtCod.setEditable(true);
            tablaCursos.clearSelection();
        });

        btnAgregar.addActionListener(e -> {
            boolean ok = AdminController.crearCurso(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtDesc.getText().trim(), (int) spnCred.getValue());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Curso creado correctamente.");
                cargarTablaCursos();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error: código duplicado o campos vacíos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            boolean ok = AdminController.editarCurso(
                txtCod.getText().trim(), txtNom.getText().trim(),
                txtDesc.getText().trim(), (int) spnCred.getValue());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Curso actualizado.");
                cargarTablaCursos();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Curso no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            String cod = txtCod.getText().trim();
            if (cod.isEmpty()) return;
            int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar curso " + cod + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                AdminController.eliminarCurso(cod);
                cargarTablaCursos();
                btnLimpiar.doClick();
            }
        });

        return panel;
    }

    //PESTAÑA SECCIONES
 
    private JPanel panelSecciones() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloSecciones = new DefaultTableModel(
            new String[]{"Código", "Curso", "Instructor", "Semestre", "Horario", "Estudiantes"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaSecciones = new JTable(modeloSecciones);
        tablaSecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField txtCod   = new JTextField();
        JTextField txtCurso = new JTextField();
        JTextField txtInst  = new JTextField();
        JTextField txtSem   = new JTextField("2026-1");
        JTextField txtHor   = new JTextField("Lunes 10:00-12:00");

        form.add(new JLabel("Código:"));      form.add(txtCod);
        form.add(new JLabel("Cód. Curso:"));  form.add(txtCurso);
        form.add(new JLabel("Cód. Instructor:")); form.add(txtInst);
        form.add(new JLabel("Semestre:"));    form.add(txtSem);
        form.add(new JLabel("Horario:"));     form.add(txtHor);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar); botones.add(btnEliminar); botones.add(btnLimpiar);

        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);
        derecha.setPreferredSize(new Dimension(280, 0));

        panel.add(new JScrollPane(tablaSecciones), BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        tablaSecciones.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaSecciones.getSelectedRow();
            if (fila >= 0) {
                txtCod.setText((String) modeloSecciones.getValueAt(fila, 0));
                txtCod.setEditable(false);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtCod.setText(""); txtCurso.setText("");
            txtInst.setText(""); txtSem.setText("2026-1");
            txtHor.setText("Lunes 10:00-12:00");
            txtCod.setEditable(true);
            tablaSecciones.clearSelection();
        });

        btnAgregar.addActionListener(e -> {
            boolean ok = AdminController.crearSeccion(
                txtCod.getText().trim(), txtCurso.getText().trim(),
                txtInst.getText().trim(), txtSem.getText().trim(),
                txtHor.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Sección creada correctamente.");
                cargarTablaSecciones();
                btnLimpiar.doClick();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error: verifique que el curso e instructor existan.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            String cod = txtCod.getText().trim();
            if (cod.isEmpty()) return;
            int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar sección " + cod + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                AdminController.eliminarSeccion(cod);
                cargarTablaSecciones();
                btnLimpiar.doClick();
            }
        });

        return panel;
    }
//PESTAÑA BITÁCORA

    private JPanel panelBitacora() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> {
            areaLog.setText("");
            String[] lineas = Bitacora.leerBitacora();
            for (String linea : lineas) areaLog.append(linea + "\n");
        });

        panel.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        return panel;
    }

//CARGA DE TABLAS
    private void cargarTablas() {
        cargarTablaInstructores();
        cargarTablaEstudiantes();
        cargarTablaCursos();
        cargarTablaSecciones();
    }

    private void cargarTablaInstructores() {
        modeloInstructores.setRowCount(0);
        for (Instructor i : bd.getInstructores()) {
            modeloInstructores.addRow(new Object[]{
                i.getCodigo(), i.getNombre(), i.getGenero(),
                i.getFechaNacimiento(), i.getTotalSecciones()
            });
        }
    }

    private void cargarTablaEstudiantes() {
        modeloEstudiantes.setRowCount(0);
        for (Estudiante e : bd.getEstudiantes()) {
            modeloEstudiantes.addRow(new Object[]{
                e.getCodigo(), e.getNombre(), e.getGenero(),
                e.getFechaNacimiento(), e.getTotalSecciones()
            });
        }
    }

    private void cargarTablaCursos() {
        modeloCursos.setRowCount(0);
        for (Curso c : bd.getCursos()) {
            modeloCursos.addRow(new Object[]{
                c.getCodigo(), c.getNombre(), c.getDescripcion(), c.getCreditos()
            });
        }
    }
private JPanel panelReportes() {
    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

    JLabel lblTitulo = new JLabel("Generación de Reportes PDF",
            JLabel.CENTER);
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

    JButton btnInstructores = new JButton("Reporte de Instructores");
    JButton btnEstudiantes  = new JButton("Reporte de Estudiantes");
    JButton btnResumen      = new JButton("Resumen General");
    JButton btnNotasSeccion = new JButton("Notas por Sección");

    // Estilo común para botones
    for (JButton btn : new JButton[]{btnInstructores, btnEstudiantes,
                                      btnResumen, btnNotasSeccion}) {
        btn.setBackground(new Color(34, 85, 34));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    btnInstructores.addActionListener(e -> {
        String ruta = utilidades.GeneradorPDF.reporteInstructores();
        mostrarRutaPDF(ruta);
    });

    btnEstudiantes.addActionListener(e -> {
        String ruta = utilidades.GeneradorPDF.reporteEstudiantes();
        mostrarRutaPDF(ruta);
    });

    btnResumen.addActionListener(e -> {
        String ruta = utilidades.GeneradorPDF.reporteResumenGeneral();
        mostrarRutaPDF(ruta);
    });

    btnNotasSeccion.addActionListener(e -> {
        String cod = JOptionPane.showInputDialog(this,
                "Ingresa el código de la sección:");
        if (cod != null && !cod.trim().isEmpty()) {
            String ruta = utilidades.GeneradorPDF
                    .reporteNotasSeccion(cod.trim());
            mostrarRutaPDF(ruta);
        }
    });

    panel.add(lblTitulo);
    panel.add(btnInstructores);
    panel.add(btnEstudiantes);
    panel.add(btnResumen);
    panel.add(btnNotasSeccion);

    return panel;
}

private void mostrarRutaPDF(String ruta) {
    if (ruta != null) {
        int resp = JOptionPane.showConfirmDialog(this,
                "PDF generado en:\n" + ruta +
                "\n\n¿Deseas abrirlo ahora?",
                "PDF generado", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                java.awt.Desktop.getDesktop()
                        .open(new java.io.File(ruta));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo abrir automáticamente.\n" +
                        "Búscalo en la carpeta: reportes/");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this,
                "Error al generar el PDF.", "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
private JPanel panelMonitor() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    JLabel lblTitulo = new JLabel("Monitor del Sistema en Tiempo Real");
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
    lblTitulo.setForeground(new Color(34, 85, 34));
    gbc.gridx = 0; gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    panel.add(lblTitulo, gbc);

    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;

    // Tiempo de sesión
    gbc.gridx = 0; gbc.gridy = 1;
    panel.add(etiquetaMonitor("Tiempo de sesión:"), gbc);
    lblMonitorTiempo = valorMonitor("00:00:00");
    gbc.gridx = 1;
    panel.add(lblMonitorTiempo, gbc);

    // Instructores
    gbc.gridx = 0; gbc.gridy = 2;
    panel.add(etiquetaMonitor("Instructores registrados:"), gbc);
    lblMonitorInstructores = valorMonitor("0");
    gbc.gridx = 1;
    panel.add(lblMonitorInstructores, gbc);

    // Estudiantes
    gbc.gridx = 0; gbc.gridy = 3;
    panel.add(etiquetaMonitor("Estudiantes registrados:"), gbc);
    lblMonitorEstudiantes = valorMonitor("0");
    gbc.gridx = 1;
    panel.add(lblMonitorEstudiantes, gbc);

    // Cursos
    gbc.gridx = 0; gbc.gridy = 4;
    panel.add(etiquetaMonitor("Cursos registrados:"), gbc);
    lblMonitorCursos = valorMonitor("0");
    gbc.gridx = 1;
    panel.add(lblMonitorCursos, gbc);

    // Secciones
    gbc.gridx = 0; gbc.gridy = 5;
    panel.add(etiquetaMonitor("Secciones activas:"), gbc);
    lblMonitorSecciones = valorMonitor("0");
    gbc.gridx = 1;
    panel.add(lblMonitorSecciones, gbc);

    // Nota informativa
    JLabel lblNota = new JLabel(
        "Los contadores se actualizan automáticamente cada segundo.");
    lblNota.setFont(new Font("Arial", Font.ITALIC, 11));
    lblNota.setForeground(Color.GRAY);
    gbc.gridx = 0; gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    panel.add(lblNota, gbc);

    return panel;
}

// Crea una etiqueta de título para el monitor
private JLabel etiquetaMonitor(String texto) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Arial", Font.BOLD, 14));
    return lbl;
}

// Crea una etiqueta de valor grande para el monitor
private JLabel valorMonitor(String valor) {
    JLabel lbl = new JLabel(valor);
    lbl.setFont(new Font("Arial", Font.BOLD, 28));
    lbl.setForeground(new Color(34, 85, 34));
    lbl.setPreferredSize(new Dimension(150, 40));
    return lbl;
}
    private void cargarTablaSecciones() {
        modeloSecciones.setRowCount(0);
        for (Seccion s : bd.getSecciones()) {
            modeloSecciones.addRow(new Object[]{
                s.getCodigo(), s.getCodigoCurso(), s.getCodigoInstructor(),
                s.getSemestre(), s.getHorario(), s.getTotalEstudiantes()
            });
        }
    }
}