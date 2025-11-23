package app.controllers;

import app.Services.ServicioUrgencias;
import app.domain.Ingreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/urgencias")
public class UrgenciasController {
    @Autowired
    ServicioUrgencias servicioUrgencias;
    @GetMapping
    public ResponseEntity<?> listarUrgencias() {
        List<Ingreso> ingresosPendientes = servicioUrgencias.obtenerIngresosPendientes();
        return ResponseEntity.ok(ingresosPendientes);
    }
}
