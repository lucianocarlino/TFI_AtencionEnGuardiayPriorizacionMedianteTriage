package domain;

public enum Autoridad {
    MEDICO("Medico"),
    ENFERMERO("Enfermero");

    private String nombre;
    Autoridad(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){ return nombre; }
}
