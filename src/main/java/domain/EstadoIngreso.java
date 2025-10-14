package domain;

import java.util.Arrays;

public enum EstadoIngreso {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En proceso"),
    FINALIZADO("Finalizado");

    private String nombre;
    EstadoIngreso(String nombre){
        this.nombre = nombre;

    }
    public String getNombre(){
        return nombre;
    }

  /*  public static String aTexto(EstadoIngreso estado) {
        //return estado.toString().trim().replace("_", " ").toLowerCase();
        return estado.toString().substring(0, 1).toUpperCase() + estado.toString().trim().replace("_", " ").toLowerCase().substring(1);

    }*/




}
