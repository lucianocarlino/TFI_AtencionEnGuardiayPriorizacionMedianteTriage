package app;

import app.interfaces.RepositorioPacientes;
import app.domain.Enfermera;
import app.domain.Ingreso;
import app.domain.NivelEmergencia;
import app.domain.Paciente;

import java.util.ArrayList;
import java.util.List;

public class ServicioUrgencias {
    private RepositorioPacientes dbPacientes;
    private final List<Ingreso> listaEspera;
    public ServicioUrgencias(RepositorioPacientes dbPacientes) {
        this.dbPacientes = dbPacientes;
        this.listaEspera = new ArrayList<>();
    }
    public void registrarUrgencias(String cuilPaciente,
                                    Enfermera enfermera,
                                    String informe,
                                   NivelEmergencia nivelEmergencia,
                                    Float temperatura,
                                    Float frecCardiaca  ,
                                    Float frecRespiratoria,
                                    Float frecuenciaSistolica,
                                    Float frecuenciaDiastolica) {
        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilPaciente).
                orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Ingreso ingreso = new Ingreso(paciente,
                enfermera,
                informe,
                nivelEmergencia,
                temperatura,
                frecCardiaca,
                frecRespiratoria,
                frecuenciaSistolica,
                frecuenciaDiastolica);
        listaEspera.add(ingreso);
    }

    public List<Ingreso> obtenerIngresosPendientes() {
        return this.listaEspera;
    }
}
