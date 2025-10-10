package domain;

import domain.valueobject.FrecuenciaCardiaca;
import domain.valueobject.FrecuenciaRespiratoria;
import domain.valueobject.TensionArterial;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Ingreso implements Comparable<Ingreso>{
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Float temperatura;
    FrecuenciaCardiaca frecuenciaCardiaca;
    FrecuenciaRespiratoria frecuenciaRespiratoria;
    TensionArterial tensionArterial;

    public Ingreso(Paciente paciente,
                   Enfermera enfermra,
                   String informe,
                   NivelEmergencia nivelEmergencia,
                   Float temperatura,
                   Float frecuenciaCardiaca,
                   Float frecuenciaRespiratoria,
                   Float frecuenciaSistolica,
                   Float frecuenciaDiastolica){
        this.paciente = paciente;
        this.enfermera = enfermra;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.estado = EstadoIngreso.PENDIENTE;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = new FrecuenciaCardiaca(frecuenciaCardiaca);
        this.frecuenciaRespiratoria = new FrecuenciaRespiratoria(frecuenciaRespiratoria);
        this.tensionArterial = new TensionArterial(frecuenciaSistolica, frecuenciaDiastolica);
    }

    public String getCuilPaciente(){
        return this.paciente.getCuil();
    }

    public int ObtenerPesoNivel(){
        List<NivelEmergencia> niveles = Arrays.stream(NivelEmergencia.values()).toList();
        int prioridad = niveles.indexOf(this.nivelEmergencia);
        return prioridad;
    }

    public int compararNiveles(NivelEmergencia nivelEmergencia){
        return this.nivelEmergencia.compararCon(nivelEmergencia);
    }

    @Override
    public int compareTo(Ingreso o) {
        return this.nivelEmergencia.compareTo(o.nivelEmergencia);
    }

}
