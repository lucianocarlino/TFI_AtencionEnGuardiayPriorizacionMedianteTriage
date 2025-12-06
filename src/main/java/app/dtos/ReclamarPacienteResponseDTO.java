package app.dtos;

import app.domain.*;

public class ReclamarPacienteResponseDTO {
    private IngresoDTO ingreso;
    
    public ReclamarPacienteResponseDTO(Ingreso ingreso) {
        this.ingreso = new IngresoDTO(ingreso);
    }
    
    public IngresoDTO getIngreso() {
        return ingreso;
    }
    
    // Nested DTO to represent the complete ingreso information
    public static class IngresoDTO {
        private PacienteDTO paciente;
        private EnfermeraDTO enfermera;
        private String fechaIngreso;
        private String informe;
        private String nivelEmergencia;
        private String estado;
        private Float temperatura;
        private FrecuenciaCardiacaDTO frecuenciaCardiaca;
        private FrecuenciaRespiratoriaDTO frecuenciaRespiratoria;
        private TensionArterialDTO tensionArterial;
        
        public IngresoDTO(Ingreso ingreso) {
            Paciente p = ingreso.getPaciente();
            this.paciente = new PacienteDTO(p);
            this.enfermera = new EnfermeraDTO(ingreso.getEnfermera());
            this.fechaIngreso = ingreso.getFechaIngreso().toString();
            this.informe = ingreso.getInforme();
            this.nivelEmergencia = ingreso.getNivelEmergencia().getNombre();
            this.estado = ingreso.getEstado().getNombre();
            this.temperatura = ingreso.getTemperatura();
            this.frecuenciaCardiaca = new FrecuenciaCardiacaDTO(ingreso.getFrecuenciaCardiaca().getValue());
            this.frecuenciaRespiratoria = new FrecuenciaRespiratoriaDTO(ingreso.getFrecuenciaRespiratoria().getValue());
            this.tensionArterial = new TensionArterialDTO(
                ingreso.getTensionArterial().getFrecuenciaSistolica(),
                ingreso.getTensionArterial().getFrecuenciaDiastolica()
            );
        }
        
        // Getters
        public PacienteDTO getPaciente() { return paciente; }
        public EnfermeraDTO getEnfermera() { return enfermera; }
        public String getFechaIngreso() { return fechaIngreso; }
        public String getInforme() { return informe; }
        public String getNivelEmergencia() { return nivelEmergencia; }
        public String getEstado() { return estado; }
        public Float getTemperatura() { return temperatura; }
        public FrecuenciaCardiacaDTO getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
        public FrecuenciaRespiratoriaDTO getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
        public TensionArterialDTO getTensionArterial() { return tensionArterial; }
    }
    
    public static class PacienteDTO {
        private String cuil;
        private String nombre;
        private String apellido;
        private AfiliadoDTO afiliado;
        private DomicilioDTO direccion;
        
        public PacienteDTO(Paciente p) {
            this.cuil = p.getCuil();
            this.nombre = p.getNombre();
            this.apellido = p.getApellido();
            if (p.getAfiliado() != null) {
                this.afiliado = new AfiliadoDTO(p.getAfiliado());
            }
            if (p.getDireccion() != null) {
                this.direccion = new DomicilioDTO(p.getDireccion());
            }
        }
        
        // Getters
        public String getCuil() { return cuil; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public AfiliadoDTO getAfiliado() { return afiliado; }
        public DomicilioDTO getDireccion() { return direccion; }
    }
    
    public static class EnfermeraDTO {
        private String cuil;
        private String nombre;
        private String apellido;
        private String email;
        private String matricula;
        
        public EnfermeraDTO(Enfermera e) {
            this.cuil = String.valueOf(e.getCuil());
            this.nombre = e.getNombre();
            this.apellido = e.getApellido();
            this.email = e.getEmail();
            this.matricula = e.getMatricula();
        }
        
        // Getters
        public String getCuil() { return cuil; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public String getMatricula() { return matricula; }
    }
    
    public static class AfiliadoDTO {
        private ObraSocialDTO obraSocial;
        private String numAfiliado;
        
        public AfiliadoDTO(Afiliado a) {
            this.obraSocial = new ObraSocialDTO(a.getObraSocial());
            this.numAfiliado = a.getNumAfiliado();
        }
        
        // Getters
        public ObraSocialDTO getObraSocial() { return obraSocial; }
        public String getNumAfiliado() { return numAfiliado; }
    }
    
    public static class ObraSocialDTO {
        private String identificador;
        private String nombre;
        
        public ObraSocialDTO(ObraSocial os) {
            this.identificador = os.getIdentificador();
            this.nombre = os.getNombre();
        }
        
        // Getters
        public String getIdentificador() { return identificador; }
        public String getNombre() { return nombre; }
    }
    
    public static class DomicilioDTO {
        private String calle;
        private Integer numero;
        private String localidad;
        
        public DomicilioDTO(Domicilio d) {
            this.calle = d.getCalle();
            this.numero = d.getNumero();
            this.localidad = d.getLocalidad();
        }
        
        // Getters
        public String getCalle() { return calle; }
        public Integer getNumero() { return numero; }
        public String getLocalidad() { return localidad; }
    }
    
    public static class FrecuenciaCardiacaDTO {
        private Float value;
        
        public FrecuenciaCardiacaDTO(Float value) {
            this.value = value;
        }
        
        public Float getValue() { return value; }
    }
    
    public static class FrecuenciaRespiratoriaDTO {
        private Float value;
        
        public FrecuenciaRespiratoriaDTO(Float value) {
            this.value = value;
        }
        
        public Float getValue() { return value; }
    }
    
    public static class TensionArterialDTO {
        private Float frecuenciaSistolica;
        private Float frecuenciaDiastolica;
        
        public TensionArterialDTO(Float sistolica, Float diastolica) {
            this.frecuenciaSistolica = sistolica;
            this.frecuenciaDiastolica = diastolica;
        }
        
        // Getters
        public Float getFrecuenciaSistolica() { return frecuenciaSistolica; }
        public Float getFrecuenciaDiastolica() { return frecuenciaDiastolica; }
    }
}
