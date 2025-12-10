package app.Services;

import app.interfaces.RepositorioPacientes;
import app.domain.Enfermera;
import app.domain.Ingreso;
import app.domain.Medico;
import app.domain.NivelEmergencia;
import app.domain.Paciente;
import app.domain.EstadoIngreso;
import app.domain.Atencion;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioUrgencias {
    private RepositorioPacientes dbPacientes;
    private final List<Ingreso> listaEspera;
    private final List<Atencion> atenciones;
    public Ingreso ingreso1 = new Ingreso();
    
    @Autowired
    public ServicioUrgencias(RepositorioPacientes dbPacientes) {
        this.dbPacientes = dbPacientes;
        this.listaEspera = new ArrayList<>();
        this.atenciones = new ArrayList<>();
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
        boolean pacienteEnEspera = listaEspera.stream()
                .anyMatch(ingreso -> ingreso.getCuilPaciente().equals(cuilPaciente));
        
        if (pacienteEnEspera) {
            throw new RuntimeException("El paciente ya se encuentra en la lista de espera");
        }
        
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

    public Ingreso reclamarSiguientePaciente() {
        if (listaEspera.isEmpty()) {
            throw new RuntimeException("No hay pacientes en la lista de espera");
        }
        
        // Obtener el primer paciente de la lista (mayor prioridad)
        Ingreso ingreso = listaEspera.get(0);
        ingreso1 = ingreso;
        if (ingreso.getEstado().equals(EstadoIngreso.EN_PROCESO)){
            throw new RuntimeException("Ya hay un paciente reclamado");
        }
        // Cambiar el estado a EN_PROCESO
        ingreso.cambiarEstado(EstadoIngreso.EN_PROCESO);
        
        // Remover de la lista de espera
        //listaEspera.remove(0); //me parece que habria que borrarlo despues
        
        return ingreso;
    }

    public void registrarAtencion(String cuilPaciente, Medico medico, String informeAtencion) {
        if (informeAtencion == null || informeAtencion.trim().isEmpty()) {
            throw new RuntimeException("El informe de atención es obligatorio");
        }

        Atencion atencion = new Atencion(ingreso1,medico,informeAtencion );
        atenciones.add(atencion);
    }
    
    public void registrarAtencionCompleta(Ingreso ingreso, Medico medico, String informeAtencion) {
        Atencion atencion = new Atencion(ingreso, medico, informeAtencion);
        this.atenciones.add(atencion);
    }
    
    public List<Atencion> obtenerAtencionesPorMedico(String emailMedico) {

        return atenciones.stream()
                .filter(atencion -> atencion.getMedico().getEmail().equals(emailMedico))
                .collect(Collectors.toList());

    }
}
