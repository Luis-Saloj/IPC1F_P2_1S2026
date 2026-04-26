/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Instructor;
import modelo.Nota;
import modelo.Seccion;
import java.io.FileOutputStream;
import java.io.File;
/**
 *
 * @author salmn
 */
public class GeneradorPDF {
    private static final String CARPETA = "reportes/";


//Fuentes
    private static Font fuenteTitulo  = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
    private static Font fuenteNormal  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static Font fuenteTabla   = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static Font fuenteHeader  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,
                                                  BaseColor.WHITE);

    private static void crearCarpeta() {
        File f = new File(CARPETA);
        if (!f.exists()) f.mkdirs();
    }

    // Color verde USAC
    private static final BaseColor VERDE = new BaseColor(34, 85, 34);

//Encabezado común para todos los reportes
    private static void agregarEncabezado(Document doc, String titulo)
            throws DocumentException {
//Título principal
        Paragraph tit = new Paragraph("Sancarlista Academy", fuenteTitulo);
        tit.setAlignment(Element.ALIGN_CENTER);
        doc.add(tit);

//Línea separadora
        doc.add(new Chunk(new com.itextpdf.text.pdf.draw.LineSeparator(
                1f, 100f, VERDE, Element.ALIGN_CENTER, -2)));
        doc.add(Chunk.NEWLINE);

//Subtítulo del reporte
        Paragraph sub = new Paragraph(titulo, fuenteSubtitulo);
        sub.setAlignment(Element.ALIGN_CENTER);
        doc.add(sub);

//Fecha de generación
        Paragraph fecha = new Paragraph("Generado: " +
                java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss")), fuenteNormal);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        doc.add(fecha);
        doc.add(Chunk.NEWLINE);
    }

//Crea una celda de encabezado de tabla 

    private static PdfPCell celdaHeader(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteHeader));
        celda.setBackgroundColor(VERDE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        return celda;
    }

//Crea una celda normal de tabla
    private static PdfPCell celdaNormal(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteTabla));
        celda.setPadding(4);
        return celda;
    }

//Lista de instructores

    public static String reporteInstructores() {
        crearCarpeta();
        String ruta = CARPETA + "reporte_instructores.pdf";
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            agregarEncabezado(doc, "Reporte de Instructores");

            BaseDeDatos bd = BaseDeDatos.getInstancia();
            Instructor[] instructores = bd.getInstructores();

            // Resumen
            doc.add(new Paragraph("Total de instructores: " +
                    instructores.length, fuenteNormal));
            doc.add(Chunk.NEWLINE);

            // Tabla
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{15f, 35f, 10f, 20f, 20f});

            tabla.addCell(celdaHeader("Código"));
            tabla.addCell(celdaHeader("Nombre"));
            tabla.addCell(celdaHeader("Género"));
            tabla.addCell(celdaHeader("Fecha Nac."));
            tabla.addCell(celdaHeader("Secciones"));

            for (Instructor i : instructores) {
                tabla.addCell(celdaNormal(i.getCodigo()));
                tabla.addCell(celdaNormal(i.getNombre()));
                tabla.addCell(celdaNormal(i.getGenero()));
                tabla.addCell(celdaNormal(i.getFechaNacimiento()));
                tabla.addCell(celdaNormal(
                        String.valueOf(i.getTotalSecciones())));
            }

            doc.add(tabla);
            doc.close();
            return ruta;

        } catch (Exception e) {
            Bitacora.registrarError("Error PDF instructores: " + e.getMessage());
            return null;
        }
    }

//Lista de estudiantes
    public static String reporteEstudiantes() {
        crearCarpeta();
        String ruta = CARPETA + "reporte_estudiantes.pdf";
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            agregarEncabezado(doc, "Reporte de Estudiantes");

            BaseDeDatos bd = BaseDeDatos.getInstancia();
            Estudiante[] estudiantes = bd.getEstudiantes();

            doc.add(new Paragraph("Total de estudiantes: " +
                    estudiantes.length, fuenteNormal));
            doc.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{15f, 35f, 10f, 20f, 20f});

            tabla.addCell(celdaHeader("Código"));
            tabla.addCell(celdaHeader("Nombre"));
            tabla.addCell(celdaHeader("Género"));
            tabla.addCell(celdaHeader("Fecha Nac."));
            tabla.addCell(celdaHeader("Secciones inscritas"));

            for (Estudiante e : estudiantes) {
                tabla.addCell(celdaNormal(e.getCodigo()));
                tabla.addCell(celdaNormal(e.getNombre()));
                tabla.addCell(celdaNormal(e.getGenero()));
                tabla.addCell(celdaNormal(e.getFechaNacimiento()));
                tabla.addCell(celdaNormal(
                        String.valueOf(e.getTotalSecciones())));
            }

            doc.add(tabla);
            doc.close();
            return ruta;

        } catch (Exception e) {
            Bitacora.registrarError("Error PDF estudiantes: " + e.getMessage());
            return null;
        }
    }

//Notas de una sección completa
    public static String reporteNotasSeccion(String codigoSeccion) {
        crearCarpeta();
        String ruta = CARPETA + "reporte_notas_" + codigoSeccion + ".pdf";
        try {
            Document doc = new Document(PageSize.A4.rotate()); // horizontal
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            BaseDeDatos bd = BaseDeDatos.getInstancia();
            Seccion sec = bd.buscarSeccion(codigoSeccion);
            if (sec == null) { doc.close(); return null; }

            agregarEncabezado(doc, "Reporte de Notas - Sección: " + codigoSeccion);

            // Info de la sección
            doc.add(new Paragraph("Curso: " + sec.getCodigoCurso() +
                    "   |   Instructor: " + sec.getCodigoInstructor() +
                    "   |   Semestre: " + sec.getSemestre(), fuenteNormal));
            doc.add(Chunk.NEWLINE);

            String[] codigos = sec.getCodigosEstudiantes();
            if (codigos.length == 0) {
                doc.add(new Paragraph(
                        "No hay estudiantes inscritos en esta sección.",
                        fuenteNormal));
                doc.close();
                return ruta;
            }

            // Tabla: estudiante + sus notas + promedio
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{15f, 30f, 20f, 15f, 20f});

            tabla.addCell(celdaHeader("Código"));
            tabla.addCell(celdaHeader("Nombre"));
            tabla.addCell(celdaHeader("Notas registradas"));
            tabla.addCell(celdaHeader("Ponderación total"));
            tabla.addCell(celdaHeader("Promedio final"));

            for (String cod : codigos) {
                Estudiante est = bd.buscarEstudiante(cod);
                if (est == null) continue;

                Nota[] notas = sec.getNotas();
                StringBuilder notasStr  = new StringBuilder();
                double sumaPond = 0;

                for (Nota n : notas) {
                    if (n.getCodigoEstudiante().equals(cod)) {
                        notasStr.append(n.getEtiqueta())
                                .append(": ").append(n.getValor())
                                .append("\n");
                        sumaPond += n.getPonderacion();
                    }
                }

                double prom = sec.calcularPromedio(cod);

                tabla.addCell(celdaNormal(est.getCodigo()));
                tabla.addCell(celdaNormal(est.getNombre()));
                tabla.addCell(celdaNormal(notasStr.length() > 0
                        ? notasStr.toString() : "Sin notas"));
                tabla.addCell(celdaNormal(
                        String.format("%.1f%%", sumaPond)));
                tabla.addCell(celdaNormal(
                        String.format("%.2f", prom)));
            }

            doc.add(tabla);
            doc.close();
            return ruta;

        } catch (Exception e) {
            Bitacora.registrarError("Error PDF notas: " + e.getMessage());
            return null;
        }
    }

//Resumen general del sistema

    public static String reporteResumenGeneral() {
        crearCarpeta();
        String ruta = CARPETA + "reporte_resumen_general.pdf";
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            agregarEncabezado(doc, "Resumen General del Sistema");

            BaseDeDatos bd = BaseDeDatos.getInstancia();

            // Estadísticas generales
            PdfPTable stats = new PdfPTable(2);
            stats.setWidthPercentage(60);
            stats.setHorizontalAlignment(Element.ALIGN_LEFT);

            stats.addCell(celdaHeader("Categoría"));
            stats.addCell(celdaHeader("Total"));
            stats.addCell(celdaNormal("Instructores registrados"));
            stats.addCell(celdaNormal(
                    String.valueOf(bd.getTotalInstructores())));
            stats.addCell(celdaNormal("Estudiantes registrados"));
            stats.addCell(celdaNormal(
                    String.valueOf(bd.getTotalEstudiantes())));
            stats.addCell(celdaNormal("Cursos registrados"));
            stats.addCell(celdaNormal(
                    String.valueOf(bd.getTotalCursos())));
            stats.addCell(celdaNormal("Secciones activas"));
            stats.addCell(celdaNormal(
                    String.valueOf(bd.getTotalSecciones())));

            doc.add(stats);
            doc.add(Chunk.NEWLINE);

            // Detalle por sección
            Paragraph titSec = new Paragraph(
                    "Detalle por sección", fuenteSubtitulo);
            doc.add(titSec);
            doc.add(Chunk.NEWLINE);

            Seccion[] secciones = bd.getSecciones();
            for (Seccion s : secciones) {
                PdfPTable tSec = new PdfPTable(4);
                tSec.setWidthPercentage(100);
                tSec.setSpacingBefore(6);

                tSec.addCell(celdaHeader("Sección"));
                tSec.addCell(celdaHeader("Curso"));
                tSec.addCell(celdaHeader("Instructor"));
                tSec.addCell(celdaHeader("Estudiantes"));

                tSec.addCell(celdaNormal(s.getCodigo()));
                tSec.addCell(celdaNormal(s.getCodigoCurso()));
                tSec.addCell(celdaNormal(s.getCodigoInstructor()));
                tSec.addCell(celdaNormal(
                        String.valueOf(s.getTotalEstudiantes())));

                doc.add(tSec);
            }

            doc.close();
            return ruta;

        } catch (Exception e) {
            Bitacora.registrarError("Error PDF resumen: " + e.getMessage());
            return null;
        }
    }
}
