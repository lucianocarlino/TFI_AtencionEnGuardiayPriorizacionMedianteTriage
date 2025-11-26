package app.domain;

public class Enfermera{

    private String cuil;
    private String nombre;
    private String apellido;
    private String email;
    private String matricula;

    public Enfermera(String cuil, String nombre, String apellido, String email, String matricula) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.matricula = matricula;
    }

    public String getCuil() {
        return cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }
}
