package app.dtos;

import app.domain.Atencion;
import app.dtos.ReclamarPacienteResponseDTO.IngresoDTO;

public class AtencionResponseDTO {
    private Long id;
    private IngresoDTO ingreso;
    private String informe;
    private MedicoDTO medico;
    private String fechaAtencion;

    public AtencionResponseDTO(Atencion atencion) {
        this.id = atencion.getId();
        this.ingreso = new IngresoDTO(atencion.getIngreso());
        this.informe = atencion.getInforme();
        this.medico = new MedicoDTO(atencion.getMedico());
        this.fechaAtencion = atencion.getFechaAtencion().toString();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public IngresoDTO getIngreso() {
        return ingreso;
    }

    public String getInforme() {
        return informe;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public String getFechaAtencion() {
        return fechaAtencion;
    }

    // Inner DTO for Medico
    public static class MedicoDTO {
        private String cuil;
        private String nombre;
        private String apellido;
        private String email;

        public MedicoDTO(app.domain.Medico medico) {
            this.cuil = medico.getCuil();
            this.nombre = medico.getNombre();
            this.apellido = medico.getApellido();
            this.email = medico.getEmail();
        }

        // Getters
        public String getCuil() {
            return cuil;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getEmail() {
            return email;
        }
    }
}
