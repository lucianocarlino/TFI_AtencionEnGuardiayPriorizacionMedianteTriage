package app.Services;

import app.interfaces.RepositorioUsuarios;
import app.domain.Autoridad;
import app.domain.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioAutenticacion {
    private RepositorioUsuarios dbUsuarios;
    private Usuario usuarioActual;
    private final List<Usuario> usuarios;

    @Autowired
    public ServicioAutenticacion(RepositorioUsuarios dbUsuarios) {
        this.dbUsuarios = dbUsuarios;
        this.usuarioActual = null;
        this.usuarios = new ArrayList<>();
    }

    public void iniciarSesion(String email, String contrasena){
        Optional<Usuario> usuario = dbUsuarios.buscarUsuario(email);

        if (usuario.isPresent()){
            String passwordEnBD = usuario.get().getContrasena();
            try{
                boolean esValido = BCrypt.checkpw(contrasena,passwordEnBD );
                if (contrasena != null && esValido){
                    this.usuarioActual = usuario.get();
                    dbUsuarios.setUsuarioActual(usuarioActual);
                }
                else {
                    throw new RuntimeException("Usuario o contrasena invalido");
                }
            }
            catch(Exception e) {
                throw new RuntimeException("Usuario o contrasena invalido");
            }

        } else {
            throw new RuntimeException("Usuario o contrasena invalido");
        }
    }
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void crearUsuario(String email, String contrasena, Autoridad autoridad){
        Optional<Usuario> usuario = dbUsuarios.buscarUsuario(email);
        if (usuario.isPresent()){
            throw new RuntimeException("Email existente");
        } else {
            Usuario usuarioNuevo = new Usuario(email, contrasena, autoridad);
            this.usuarios.add(usuarioNuevo);
            dbUsuarios.guardarUsuario(usuarioNuevo);
        }
    }

    public void crearUsuario(String email, String contrasena, Autoridad autoridad, String nombre, String apellido){
        Optional<Usuario> usuario = dbUsuarios.buscarUsuario(email);
        if (usuario.isPresent()){
            throw new RuntimeException("Email existente");
        } else {
            Usuario usuarioNuevo = new Usuario(email, contrasena, autoridad, nombre, apellido);
            this.usuarios.add(usuarioNuevo);
            dbUsuarios.guardarUsuario(usuarioNuevo);
        }
    }

    public List<Usuario> obtenerUsuarios(){
        return this.usuarios;
    }
}
