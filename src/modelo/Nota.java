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
public class Nota implements Serializable{
    private static final long serialVersionUID = 1L;
    private String codigoEstudiante;
    private String etiqueta;       // Ej: "Parcial 1", "Tarea 2"
    private double ponderacion;    // Porcentaje que vale (ej: 30.0)
    private double valor;          // Nota obtenida (0-100)
    private String fecha;          // Formato YYYY-MM-DD
    
    public Nota(String codigoEstudiante, String etiqueta,
                double ponderacion, double valor, String fecha) {
        this.codigoEstudiante = codigoEstudiante;
        this.etiqueta = etiqueta;
        this.ponderacion = ponderacion;
        this.valor = valor;
        this.fecha = fecha;
    }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public String getEtiqueta()         { return etiqueta; }
    public double getPonderacion()      { return ponderacion; }
    public double getValor()            { return valor; }
    public String getFecha()            { return fecha; }

    public void setPonderacion(double p){ this.ponderacion = p; }
    public void setValor(double v)      { this.valor = v; }

    @Override
    public String toString() {
        return etiqueta + " | " + valor + " (" + ponderacion + "%) | " + fecha;
    }
}