package app;

import app.interfaces.RepositorioPacientes;
import domain.Enfermera;
import domain.Ingreso;
import domain.NivelEmergencia;
import domain.Paciente;
import domain.valueobject.FrecuenciaDiastolica;
import domain.valueobject.FrecuenciaSistolica;
import domain.valueobject.NivelEmergenciaValue;

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
        try{
        NivelEmergenciaValue nivel = new NivelEmergenciaValue(nivelEmergencia);
            FrecuenciaSistolica frecuenciaS = new FrecuenciaSistolica(frecuenciaSistolica);
            FrecuenciaDiastolica frecuenciaD = new FrecuenciaDiastolica(frecuenciaDiastolica);

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
        listaEspera.sort(Ingreso::compareTo);
        } catch (RuntimeException e){
            throw new RuntimeException( e.getMessage());
        }
    }

    public List<Ingreso> obtenerIngresosPendientes() {
        return this.listaEspera;
    }
}
