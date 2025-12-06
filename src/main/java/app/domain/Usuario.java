package app.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Usuario {
    private String email;
    private String contrasena;
    private Autoridad autoridad;
    private String nombre;
    private String apellido;

    public Usuario(String email, String contrasena, Autoridad autoridad){
        this.email = email;
        this.contrasena = contrasena;
        this.autoridad = autoridad;
        this.nombre = "";
        this.apellido = "";
        validarCampos();
    }

    public Usuario(String email, String contrasena, Autoridad autoridad, String nombre, String apellido){
        this.email = email;
        this.contrasena = contrasena;
        this.autoridad = autoridad;
        this.nombre = nombre;
        this.apellido = apellido;
        validarCampos();
    }

    public String getEmail(){ return email; }
    public String getContrasena(){ return contrasena; }
    public Autoridad getAutoridad(){ return autoridad; }
    public String getNombre(){ return nombre; }
    public String getApellido(){ return apellido; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public void setApellido(String apellido){ this.apellido = apellido; }

    public void validarCampos(){
        Pattern emailPattern= Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(this.email);
        if (!emailMatcher.find()){
            throw new RuntimeException("Email invalido");
        }
        if (this.contrasena == null ){
            throw new RuntimeException("Contrasena es un campo obligatorio");
        } else if (this.contrasena.length() < 4 ) {
            throw new RuntimeException("Contrasena demasiado corta");
        }

    }
}
