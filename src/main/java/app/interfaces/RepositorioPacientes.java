package app.interfaces;

import app.domain.Paciente;

import java.util.Optional;
import java.util.List;

public interface RepositorioPacientes {
    public void guardarPaciente(Paciente paciente);

    public Optional<Paciente> buscarPacientePorCuil(String cuil);

    public boolean existeObraSocial(String obraSocialNombre);

    boolean estaAfiliado(String cuil, String obraSocialNombre);

    boolean verificarNumeroAfiliado(String cuil, String obraSocialNombre, String nroAfiliado);
    
    public List<Paciente> obtenerTodosLosPacientes();
}
