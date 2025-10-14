package domain;

import domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Ingreso implements Comparable<Ingreso>{
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String  informe;
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

        validarInformeNoNulo(informe);
        validarNivelIngresoNoNulo(nivelEmergencia);
        validarTemperaturaNoNulo(temperatura);

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


    public EstadoIngreso getEstado(){ return this.estado; }

    public int ObtenerPesoNivel(){
        List<NivelEmergencia> niveles = Arrays.stream(NivelEmergencia.values()).toList();
        int prioridad = niveles.indexOf(this.nivelEmergencia);
        return prioridad;
    }

    public void validarInformeNoNulo(String value){
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("El informe es un campo obligatorio");
        }
    }

    public void validarNivelIngresoNoNulo(NivelEmergencia value){
        if (value == null){
            throw new RuntimeException("El nivel de emergencia es un campo obligatorio");
        }
    }

    public void validarTemperaturaNoNulo(Float value){
        if (value == null){
            throw new RuntimeException("Temperatura es un campo obligatorio");
        }
    }



    @Override
    public int compareTo(Ingreso o) {
        return this.nivelEmergencia.compareTo(o.nivelEmergencia);
    }

}
