package app.controllers;

import app.Services.ServicioAutenticacion;
import app.dtos.RegistroDTO;
import app.dtos.LoginResponseDTO;
import app.domain.Autoridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistroController {
    
    @Autowired
    ServicioAutenticacion servicioAutenticacion;

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroDTO registroDTO) {
        try {
            Autoridad autoridad = registroDTO.getAutoridad().equals("MEDICO") 
                ? Autoridad.MEDICO 
                : Autoridad.ENFERMERO;
            
            servicioAutenticacion.crearUsuario(
                registroDTO.getEmail(), 
                registroDTO.getContrasena(), 
                autoridad,
                registroDTO.getNombre(),
                registroDTO.getApellido()
            );
            
            LoginResponseDTO response = new LoginResponseDTO(
                registroDTO.getEmail(),
                autoridad,
                "Usuario registrado exitosamente",
                registroDTO.getNombre(),
                registroDTO.getApellido()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
