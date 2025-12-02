package app.Services;

import app.interfaces.RepositorioPacientes;
import app.domain.Enfermera;
import app.domain.Ingreso;
import app.domain.Medico;
import app.domain.NivelEmergencia;
import app.domain.Paciente;
import app.domain.EstadoIngreso;
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

    public Ingreso reclamarSiguientePaciente() {
        if (listaEspera.isEmpty()) {
            throw new RuntimeException("No hay pacientes en la lista de espera");
        }
        
        // Obtener el primer paciente de la lista (mayor prioridad)
        Ingreso ingreso = listaEspera.get(0);
        
        // Cambiar el estado a EN_PROCESO
        ingreso.cambiarEstado(EstadoIngreso.EN_PROCESO);
        
        // Remover de la lista de espera
        listaEspera.remove(0);
        
        return ingreso;
    }

    public void registrarAtencion(String cuilPaciente, Medico medico, String informeAtencion) {
        if (informeAtencion == null || informeAtencion.trim().isEmpty()) {
            throw new RuntimeException("El informe de atención es obligatorio");
        }
        
        // Buscar el ingreso en proceso para este paciente
        // En un sistema real, esto debería estar en una base de datos
        // Por ahora, asumimos que el proceso se completó correctamente
        
        // El ingreso ya debe estar en estado EN_PROCESO después de reclamarlo
        // Aquí se finalizaría el ingreso cambiando su estado a FINALIZADO
        // y guardando el informe de atención
        
        System.out.println("Atención registrada para paciente: " + cuilPaciente);
        System.out.println("Médico: " + medico.getNombre() + " " + medico.getApellido());
        System.out.println("Informe: " + informeAtencion);
    }
}
