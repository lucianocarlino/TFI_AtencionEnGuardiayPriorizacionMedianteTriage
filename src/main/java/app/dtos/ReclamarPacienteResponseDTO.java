package app.dtos;

import app.domain.Ingreso;
import app.domain.Paciente;

public class ReclamarPacienteResponseDTO {
    private String cuilPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private String informe;
    private String nivelEmergencia;
    private Float temperatura;
    private Float frecuenciaCardiaca;
    private Float frecuenciaRespiratoria;
    private Float sistolica;
    private Float diastolica;

    public ReclamarPacienteResponseDTO(Ingreso ingreso) {
        Paciente p = ingreso.getPaciente();
        this.cuilPaciente = p.getCuil();
        this.nombrePaciente = p.getNombre();
        this.apellidoPaciente = p.getApellido();
        this.informe = ingreso.getInforme();
        this.nivelEmergencia = ingreso.getNivelEmergencia().getNombre();
        this.temperatura = ingreso.getTemperatura();
        this.frecuenciaCardiaca = ingreso.getFrecuenciaCardiaca().getValue();
        this.frecuenciaRespiratoria = ingreso.getFrecuenciaRespiratoria().getValue();
        this.sistolica = ingreso.getTensionArterial().getFrecuenciaSistolica();
        this.diastolica = ingreso.getTensionArterial().getFrecuenciaDiastolica();
    }

    // Getters
    public String getCuilPaciente() { return cuilPaciente; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getApellidoPaciente() { return apellidoPaciente; }
    public String getInforme() { return informe; }
    public String getNivelEmergencia() { return nivelEmergencia; }
    public Float getTemperatura() { return temperatura; }
    public Float getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public Float getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public Float getSistolica() { return sistolica; }
    public Float getDiastolica() { return diastolica; }
}
