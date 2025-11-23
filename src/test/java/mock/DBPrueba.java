package mock;

import app.interfaces.RepositorioObraSocial;
import app.interfaces.RepositorioPacientes;
import app.interfaces.RepositorioUsuarios;
import app.domain.ObraSocial;
import app.domain.Paciente;
import app.domain.Usuario;

import java.util.*;

public class DBPrueba implements RepositorioPacientes, RepositorioObraSocial, RepositorioUsuarios {
    private List<Paciente> pacientes;
    private Map<String, ObraSocial> obrasociales;
    private Map<String,String> afiliaciones = new HashMap<>();
    private List<Usuario> usuarios;
    private Usuario usuarioActual;

    public DBPrueba() {

        this.pacientes = new ArrayList<>();
        this.obrasociales = new LinkedHashMap<>();
        this.usuarios = new ArrayList<>();
        this.usuarioActual = null;
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.stream()
                .filter(paciente -> paciente.getCuil().equals(cuil))
                .findFirst()
                .orElse(null));
    }

    @Override
    public boolean existeObraSocial(String obraSocialNombre) {
        var a = buscarObraSocial(obraSocialNombre);
        return a != null;

    }

    @Override
    public boolean estaAfiliado(String cuil, String obraSocial) {
        String key = cuil + "|" + obraSocial;
        return afiliaciones.containsKey(key);
    }

    public List<Paciente> obtenerTodosLosPacientes() {
        return pacientes;
    }

    @Override
    public void guardarObraSocial(ObraSocial obraSocial) {
        this.obrasociales.put(obraSocial.getIdentificador(), obraSocial);
    }

    @Override
    public ObraSocial buscarObraSocial(String nombre) {
        return obrasociales.values().stream()
                .filter(obraSocial -> obraSocial.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void registrarAfiliacion(String cuil, String obraSocial, String numeroAfiliado) {
        String key = cuil + "|" + obraSocial;
        afiliaciones.put(key, numeroAfiliado);
    }

    public boolean verificarNumeroAfiliado(String cuil, String obraSocial, String numeroAfiliado) {
        String key = cuil + "|" + obraSocial;
        String numeroGuardado = afiliaciones.get(key);
        return numeroAfiliado.equals(numeroGuardado);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuario(String email) {
        return Optional.ofNullable(usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst()
                .orElse(null));
    }

    @Override
    public Optional<Usuario> getUsuarioActual(String email, String contrasena) {
        return Optional.ofNullable(usuarioActual);
    }


    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
