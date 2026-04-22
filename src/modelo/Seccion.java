/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.io.Serializable;
/**
 *
 * @author salmn
 */
public class Seccion implements Serializable{
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String codigoCurso;
    private String codigoInstructor;
    private String semestre;        // Ej: "2026-1"
    private String horario;

    // Arreglos para estudiantes y notas (sin ArrayList)
    private String[] codigosEstudiantes;
    private Nota[] notas;
    private int totalEstudiantes;
    private int totalNotas;

    public Seccion(String codigo, String codigoCurso,
                   String codigoInstructor, String semestre, String horario) {
        this.codigo = codigo;
        this.codigoCurso = codigoCurso;
        this.codigoInstructor = codigoInstructor;
        this.semestre = semestre;
        this.horario = horario;
        this.codigosEstudiantes = new String[100];
        this.notas = new Nota[500];
        this.totalEstudiantes = 0;
        this.totalNotas = 0;
    }

    // --- Gestión de estudiantes ---
    public void agregarEstudiante(String codigoEstudiante) {
        if (totalEstudiantes < codigosEstudiantes.length) {
            codigosEstudiantes[totalEstudiantes] = codigoEstudiante;
            totalEstudiantes++;
        }
    }

    public boolean tieneEstudiante(String codigoEstudiante) {
        for (int i = 0; i < totalEstudiantes; i++) {
            if (codigosEstudiantes[i].equals(codigoEstudiante)) return true;
        }
        return false;
    }

    // --- Gestión de notas ---
    public void agregarNota(Nota nota) {
        if (totalNotas < notas.length) {
            notas[totalNotas] = nota;
            totalNotas++;
        }
    }

    // Calcula el promedio ponderado de un estudiante en esta sección
    public double calcularPromedio(String codigoEstudiante) {
        double sumaPonderada = 0;
        double sumaPonderacion = 0;
        for (int i = 0; i < totalNotas; i++) {
            if (notas[i].getCodigoEstudiante().equals(codigoEstudiante)) {
                sumaPonderada  += notas[i].getValor() * notas[i].getPonderacion();
                sumaPonderacion += notas[i].getPonderacion();
            }
        }
        if (sumaPonderacion == 0) return 0;
        return sumaPonderada / sumaPonderacion;
    }

    // Getters
    public String getCodigo()             { return codigo; }
    public String getCodigoCurso()        { return codigoCurso; }
    public String getCodigoInstructor()   { return codigoInstructor; }
    public String getSemestre()           { return semestre; }
    public String getHorario()            { return horario; }
    public int getTotalEstudiantes()      { return totalEstudiantes; }
    public int getTotalNotas()            { return totalNotas; }

    public Nota[] getNotas() {
        Nota[] resultado = new Nota[totalNotas];
        for (int i = 0; i < totalNotas; i++) resultado[i] = notas[i];
        return resultado;
    }

    public String[] getCodigosEstudiantes() {
        String[] resultado = new String[totalEstudiantes];
        for (int i = 0; i < totalEstudiantes; i++) resultado[i] = codigosEstudiantes[i];
        return resultado;
    }

    @Override
    public String toString() {
        return codigo + " | Curso: " + codigoCurso + " | " + semestre;
    }
}


