package com.example.andreacarballidop1di.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarea {
    Date fecha;
    String textotarea;


    public Tarea(Date fecha,String textotarea) {
        this.fecha = fecha;
        this.textotarea = textotarea;

    }

    public void modificarTarea(Date fecha, String textotarea) {
        this.fecha = fecha;
        this.textotarea = textotarea;

    }

    public String getTextotarea() {
        return textotarea;
    }

    public void setTextotarea(String textotarea) {
        this.textotarea = textotarea;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public String getFormatoFecha() {


        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
        return formatoFecha.format(fecha);

    }

    public String eliminarTarea(){
        String texto= "Fecha: " + getFormatoFecha()+"\n"+ "Tarea: "+ getTextotarea();
        return texto;
    }
}
