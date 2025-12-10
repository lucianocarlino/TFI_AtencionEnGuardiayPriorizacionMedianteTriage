package app.domain;

import java.time.LocalDateTime;

public class Atencion {
    private static Long contadorId = 1L;
    
    private Long id;
    private Ingreso ingreso;
    private String informe;
    private Medico medico;
    private LocalDateTime fechaAtencion;

    public Atencion(Ingreso ingreso, Medico medico, String informe) {
        if (informe == null || informe.trim().isEmpty()) {
            throw new RuntimeException("El informe de atención es obligatorio");
        }
        if (ingreso == null) {
            throw new RuntimeException("El ingreso es obligatorio");
        }
        if (medico == null) {
            throw new RuntimeException("El médico es obligatorio");
        }
        
        this.id = contadorId++;
        this.ingreso = ingreso;
        this.medico = medico;
        this.informe = informe;
        this.fechaAtencion = LocalDateTime.now();
        
        // Cambiar el estado del ingreso a FINALIZADO
        ingreso.cambiarEstado(EstadoIngreso.FINALIZADO);
    }

    public Long getId() {
        return id;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public String getInforme() {
        return informe;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }
}
