package app.Services;

import app.interfaces.RepositorioPacientes;
import app.domain.Enfermera;
import app.domain.Ingreso;
import app.domain.NivelEmergencia;
import app.domain.Paciente;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioUrgencias {
    private RepositorioPacientes dbPacientes;
    private final List<Ingreso> listaEspera;
    
    @Autowired
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
        return List.copyOf(this.listaEspera);
    }
}
