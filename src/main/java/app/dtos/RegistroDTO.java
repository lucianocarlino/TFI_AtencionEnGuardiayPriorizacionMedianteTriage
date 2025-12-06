package app.dtos;

public class RegistroDTO {
    private String email;
    private String contrasena;
    private String autoridad;
    private String nombre;
    private String apellido;

    public RegistroDTO() {}

    public RegistroDTO(String email, String contrasena, String autoridad, String nombre, String apellido) {
        this.email = email;
        this.contrasena = contrasena;
        this.autoridad = autoridad;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
