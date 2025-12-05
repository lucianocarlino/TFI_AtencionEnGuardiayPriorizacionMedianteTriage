package domain;

import java.time.LocalDateTime;
import java.util.StringTokenizer;

public class Ingreso{
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Float temperatura;
    Float frecuenciaCardiaca;
    Float frecuenciaRespiratoria;
    Float frecuenciaDiastolica;
    Float frecuenciaSistolica;

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
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
        this.frecuenciaSistolica = frecuenciaSistolica;
        this.frecuenciaDiastolica = frecuenciaDiastolica;
    }

    public String getCuilPaciente(){
        return this.paciente.getCuil();
    }
}
