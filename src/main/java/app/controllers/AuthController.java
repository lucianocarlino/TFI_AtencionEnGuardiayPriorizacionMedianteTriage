package app.controllers;

import app.Services.ServicioAutenticacion;
import app.dtos.LoginDTO;
import app.dtos.LoginResponseDTO;
import app.domain.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    ServicioAutenticacion servicioAutenticacion;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {

            servicioAutenticacion.iniciarSesion(loginDTO.getEmail(), loginDTO.getContrasena());
            Usuario usuario = servicioAutenticacion.getUsuarioActual();
            
            LoginResponseDTO response = new LoginResponseDTO(
                usuario.getEmail(),
                usuario.getAutoridad(),
                "Inicio de sesión exitoso",
                usuario.getNombre(),
                usuario.getApellido()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Usuario usuario = servicioAutenticacion.getUsuarioActual();
            if (usuario == null) {
                return ResponseEntity.status(401).body("No hay usuario autenticado");
            }
            
            LoginResponseDTO response = new LoginResponseDTO(
                usuario.getEmail(),
                usuario.getAutoridad(),
                "Usuario actual",
                usuario.getNombre(),
                usuario.getApellido()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
