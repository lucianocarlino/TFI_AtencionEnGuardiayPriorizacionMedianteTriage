package mock;

import app.interfaces.RepositorioObraSocial;
import app.interfaces.RepositorioPacientes;
import domain.ObraSocial;
import domain.Paciente;

import java.util.*;
import java.util.stream.Collectors;

public class DBPrueba implements RepositorioPacientes, RepositorioObraSocial {
    private List<Paciente> pacientes;
    private Map<String, ObraSocial> obrasociales;
    private Map<String,String> afiliaciones = new HashMap<>();

    public DBPrueba() {

        this.pacientes = new ArrayList<>();
        this.obrasociales = new LinkedHashMap<>();
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

}
