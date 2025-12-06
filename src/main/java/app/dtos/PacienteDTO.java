package app.dtos;

public class PacienteDTO {
    private String cuil;
    private String nombre;
    private String apellido;
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
    private String obraSocial;
    private String numAfiliado;

    // Constructors
    public PacienteDTO() {}

    public PacienteDTO(String cuil, String nombre, String apellido, String calle, 
                      String numero, String localidad, String provincia, 
                      String obraSocial, String numAfiliado) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.obraSocial = obraSocial;
        this.numAfiliado = numAfiliado;
    }

    // Getters and Setters
    public String getCuil() { return cuil; }
    public void setCuil(String cuil) { this.cuil = cuil; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }

    public String getNumAfiliado() { return numAfiliado; }
    public void setNumAfiliado(String numAfiliado) { this.numAfiliado = numAfiliado; }
}
