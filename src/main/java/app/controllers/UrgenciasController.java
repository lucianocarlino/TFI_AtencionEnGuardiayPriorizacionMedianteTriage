package app.controllers;

import app.Services.ServicioUrgencias;
import app.domain.Ingreso;
import app.dtos.IngresoDTO;
import app.dtos.ReclamarPacienteResponseDTO;
import app.dtos.AtencionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/urgencias")
@CrossOrigin(origins = "http://localhost:3000")
public class UrgenciasController {
    @Autowired
    ServicioUrgencias servicioUrgencias;
    
    @GetMapping
    public ResponseEntity<?> listarUrgencias() {
        List<Ingreso> ingresosPendientes = servicioUrgencias.obtenerIngresosPendientes();
        return ResponseEntity.ok(ingresosPendientes);
    }

    @PostMapping
    public ResponseEntity<?> guardarIngreso(@RequestBody IngresoDTO ingresoDTO ) {
        try {
            servicioUrgencias.registrarUrgencias(
                    ingresoDTO.getCuilPaciente(),
                    ingresoDTO.getEnfermera(),
                    ingresoDTO.getInforme(),
                    ingresoDTO.getNivelEmergencia(),
                    ingresoDTO.getTemperatura(),
                    ingresoDTO.getFrecCardiaca(),
                    ingresoDTO.getFrecRespiratoria(),
                    ingresoDTO.getFrecuenciaSistolica(),
                    ingresoDTO.getFrecuenciaDiastolica()
            );
            return ResponseEntity.ok().body("Ingreso a urgencias registrado correctamente");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reclamar")
    public ResponseEntity<?> reclamarPaciente() {
        try {
            Ingreso ingreso = servicioUrgencias.reclamarSiguientePaciente();
            ReclamarPacienteResponseDTO response = new ReclamarPacienteResponseDTO(ingreso);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/atencion")
    public ResponseEntity<?> registrarAtencion(@RequestBody AtencionDTO atencionDTO) {
        try {
            servicioUrgencias.registrarAtencion(
                    atencionDTO.getCuilPaciente(),
                    atencionDTO.getMedico(),
                    atencionDTO.getInformeAtencion()
            );
            return ResponseEntity.ok().body("Atención registrada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
