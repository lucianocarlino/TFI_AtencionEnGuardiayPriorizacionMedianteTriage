package app.controllers;

import app.Services.ServicioRegistroPacientes;
import app.domain.Afiliado;
import app.domain.Domicilio;
import app.domain.ObraSocial;
import app.domain.Paciente;
import app.dtos.PacienteDTO;
import app.mock.DBPrueba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("pacientes")
@CrossOrigin(origins = "http://localhost:3000")
public class PacientesController {

    private final ServicioRegistroPacientes servicioPacientes;
    private final DBPrueba dbPrueba;

    @Autowired
    public PacientesController(ServicioRegistroPacientes servicioPacientes, DBPrueba dbPrueba) {
        this.servicioPacientes = servicioPacientes;
        this.dbPrueba = dbPrueba;
    }

    @PostMapping
    public ResponseEntity<String> registrarPaciente(@RequestBody PacienteDTO pacienteDTO) {
        try {
            Domicilio domicilio = null;
            if (pacienteDTO.getCalle() != null && !pacienteDTO.getCalle().isEmpty()) {
                domicilio = new Domicilio(
                    pacienteDTO.getCalle(),
                    pacienteDTO.getNumero() != null ? Integer.parseInt(pacienteDTO.getNumero()) : 0,
                    pacienteDTO.getLocalidad()
                );
            }

            Afiliado afiliado = null;
            if (pacienteDTO.getObraSocial() != null && !pacienteDTO.getObraSocial().isEmpty()) {
                ObraSocial obraSocial = dbPrueba.buscarObraSocial(pacienteDTO.getObraSocial());
                if (obraSocial != null) {
                    afiliado = new Afiliado(pacienteDTO.getNumAfiliado(), obraSocial);
                }
            }

            servicioPacientes.registrarPaciente(
                pacienteDTO.getCuil(),
                pacienteDTO.getNombre(),
                pacienteDTO.getApellido(),
                domicilio,
                afiliado
            );

            return ResponseEntity.ok("Paciente registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar paciente: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        try {
            List<Paciente> pacientes = dbPrueba.obtenerTodosLosPacientes();
            pacientes.sort(Comparator.comparing(Paciente::getApellido)
                    .thenComparing(Paciente::getNombre));

            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.internalServerError().build();
        }
    }
}
