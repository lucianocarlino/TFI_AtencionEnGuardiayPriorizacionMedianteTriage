package app.domain;

public class ObraSocial {
    private String nombre;
    private String Identificador;
    public ObraSocial( String nombre, String identificador ) {
        this.nombre = nombre;
        this.Identificador = identificador;
    }

    public String getIdentificador() {
            return Identificador;
    }

    public String getNombre() {
        return nombre;
    }
}
