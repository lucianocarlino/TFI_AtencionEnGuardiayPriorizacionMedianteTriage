package app.interfaces;

import domain.Usuario;

import java.util.Optional;

public interface RepositorioUsuarios {
    public void guardarUsuario(Usuario usuario);
    public Optional<Usuario> buscarUsuario(String email);

}
