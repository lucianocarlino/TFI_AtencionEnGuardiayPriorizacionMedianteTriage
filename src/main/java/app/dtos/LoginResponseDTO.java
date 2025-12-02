package app.dtos;

import app.domain.Autoridad;

public class LoginResponseDTO {
    private String email;
    private String autoridad;
    private String mensaje;
    private String nombre;
    private String apellido;

    public LoginResponseDTO(String email, Autoridad autoridad, String mensaje) {
        this.email = email;
        this.autoridad = autoridad.getNombre();
        this.mensaje = mensaje;
        this.nombre = "";
        this.apellido = "";
    }

    public LoginResponseDTO(String email, Autoridad autoridad, String mensaje, String nombre, String apellido) {
        this.email = email;
        this.autoridad = autoridad.getNombre();
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
