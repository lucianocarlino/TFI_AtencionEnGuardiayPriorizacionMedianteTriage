package mock;

import app.interfaces.RepositorioPacientes;
import domain.Paciente;

import java.util.*;

public class DBPrueba implements RepositorioPacientes {
    private Map<String, Paciente> pacientes;

    public DBPrueba() {
        this.pacientes = new LinkedHashMap<>();
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        this.pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }

    public List<Paciente> obtenerTodosLosPacientes() {
        return new ArrayList<>(pacientes.values());
    }
}
