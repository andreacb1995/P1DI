package com.example.andreacarballidop1di.core;

public class Tarea {
    String textotarea;
    String fecha;

    public Tarea(String textotarea, String fecha) {
        this.textotarea = textotarea;
        this.fecha = fecha;
    }

    public String getTextotarea() {
        return textotarea;
    }

    public void setTextotarea(String textotarea) {
        this.textotarea = textotarea;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Tarea':" + textotarea + '\'' +
                ", fecha:" + fecha;
    }
}
