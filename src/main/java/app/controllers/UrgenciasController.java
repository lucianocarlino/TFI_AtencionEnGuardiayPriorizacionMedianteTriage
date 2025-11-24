package app.controllers;

import app.Services.ServicioUrgencias;
import app.domain.Ingreso;
import app.dtos.IngresoDTO;
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
}
