package app.dtos; // O el paquete que prefieras

import app.domain.Enfermera;
import app.domain.NivelEmergencia;

public class IngresoDTO {
    private String cuilPaciente;
    private Enfermera enfermera;
    private String informe;
    private NivelEmergencia nivelEmergencia;
    private Float temperatura;
    private Float frecCardiaca;
    private Float frecRespiratoria;
    private Float frecuenciaSistolica;
    private Float frecuenciaDiastolica;

    // Getters y Setters
    public String getCuilPaciente() { return cuilPaciente; }
    public void setCuilPaciente(String cuilPaciente) { this.cuilPaciente = cuilPaciente; }

    public Enfermera getEnfermera() { return enfermera; }
    public void setEnfermera(Enfermera enfermera) { this.enfermera = enfermera; }

    public String getInforme() { return informe; }
    public void setInforme(String informe) { this.informe = informe; }

    public NivelEmergencia getNivelEmergencia() { return nivelEmergencia; }
    public void setNivelEmergencia(NivelEmergencia nivelEmergencia) { this.nivelEmergencia = nivelEmergencia; }

    public Float getTemperatura() { return temperatura; }
    public void setTemperatura(Float temperatura) { this.temperatura = temperatura; }

    public Float getFrecCardiaca() { return frecCardiaca; }
    public void setFrecCardiaca(Float frecCardiaca) { this.frecCardiaca = frecCardiaca; }

    public Float getFrecRespiratoria() { return frecRespiratoria; }
    public void setFrecRespiratoria(Float frecRespiratoria) { this.frecRespiratoria = frecRespiratoria; }

    public Float getFrecuenciaSistolica() { return frecuenciaSistolica; }
    public void setFrecuenciaSistolica(Float frecuenciaSistolica) { this.frecuenciaSistolica = frecuenciaSistolica; }

    public Float getFrecuenciaDiastolica() { return frecuenciaDiastolica; }
    public void setFrecuenciaDiastolica(Float frecuenciaDiastolica) { this.frecuenciaDiastolica = frecuenciaDiastolica; }
}
