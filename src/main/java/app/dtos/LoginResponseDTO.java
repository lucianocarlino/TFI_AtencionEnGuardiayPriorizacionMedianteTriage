package app.dtos;

import app.domain.Autoridad;

public class LoginResponseDTO {
    private String email;
    private String autoridad;
    private String mensaje;

    public LoginResponseDTO(String email, Autoridad autoridad, String mensaje) {
        this.email = email;
        this.autoridad = autoridad.getNombre();
        this.mensaje = mensaje;
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
}
