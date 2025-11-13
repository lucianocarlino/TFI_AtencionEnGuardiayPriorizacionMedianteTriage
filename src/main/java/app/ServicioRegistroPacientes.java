package app;

import app.interfaces.RepositorioPacientes;
import domain.*;

public class ServicioRegistroPacientes {
    private RepositorioPacientes dbPacientes;

    public ServicioRegistroPacientes(RepositorioPacientes db) {
        this.dbPacientes = db;
    }

    public void registrarPaciente(String cuil, String nombre, String apellido,
                                  Domicilio domicilio, String obraSocialNombre,
                                  String nroAfiliado) {
        // Validaciones de campos mandatorios
        if (cuil == null || cuil.trim().isEmpty()) {
            throw new IllegalArgumentException("CUIL es un campo obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido es un campo obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es un campo obligatorio");
        }
        // ... validar otros campos mandatorios

        // Validar obra social si se proporciona
        if (obraSocialNombre != null && !obraSocialNombre.trim().isEmpty()) {
            if (!dbPacientes.existeObraSocial(obraSocialNombre)) {
                throw new IllegalArgumentException("No se puede registrar al paciente con una obra social inexistente");
            }
            if (!dbPacientes.estaAfiliado(cuil, obraSocialNombre)) {
                throw new IllegalArgumentException("No se puede registrar el paciente dado que no esta afiliado a la obra social");
            }
        }

        // Crear y guardar paciente
        Paciente paciente = new Paciente(cuil, nombre, apellido, obraSocialNombre, nroAfiliado, domicilio);
        dbPacientes.guardarPaciente(paciente);
    }
}

