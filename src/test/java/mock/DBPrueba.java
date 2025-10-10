package mock;

import app.interfaces.RepositorioPacientes;
import domain.Paciente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DBPrueba implements RepositorioPacientes {
    private Map<String, Paciente> pacientes;

    public DBPrueba() {
        this.pacientes = new HashMap<>();
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        this.pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }
}
