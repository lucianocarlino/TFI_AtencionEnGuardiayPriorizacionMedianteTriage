package app.dtos;

import app.domain.Medico;

public class AtencionDTO {
    private String cuilPaciente;
    private Medico medico;
    private String informeAtencion;

    public String getCuilPaciente() {
        return cuilPaciente;
    }

    public void setCuilPaciente(String cuilPaciente) {
        this.cuilPaciente = cuilPaciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getInformeAtencion() {
        return informeAtencion;
    }

    public void setInformeAtencion(String informeAtencion) {
        this.informeAtencion = informeAtencion;
    }
}
