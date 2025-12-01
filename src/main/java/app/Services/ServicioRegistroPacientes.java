package app.Services;

import app.domain.Afiliado;
import app.domain.Domicilio;
import app.domain.Paciente;
import app.interfaces.RepositorioPacientes;
import app.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServicioRegistroPacientes {
    private RepositorioPacientes dbPacientes;

    @Autowired
    public ServicioRegistroPacientes(RepositorioPacientes db) {
        this.dbPacientes = db;
    }

    public void registrarPaciente(String cuil, String nombre, String apellido,
                                  Domicilio domicilio, Afiliado afiliado) {
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
        // Validar obra social si se proporciona
        if (afiliado != null) {
            var obraSocial = afiliado.getObraSocial();
            if (obraSocial == null) { throw new IllegalArgumentException("No se puede registrar al paciente con una obra social inexistente"); }
            var obraSocialNombre = obraSocial.getNombre();
            if (obraSocialNombre.trim().isEmpty()) {
                Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, domicilio);
                dbPacientes.guardarPaciente(paciente);
                return;
            }
            if (obraSocialNombre != null && !obraSocialNombre.trim().isEmpty()) {
                if (!dbPacientes.existeObraSocial(obraSocialNombre)) {
                    throw new IllegalArgumentException("No se puede registrar al paciente con una obra social inexistente");
                }
                if (!dbPacientes.estaAfiliado(cuil, obraSocialNombre)) {
                    throw new IllegalArgumentException("No se puede registrar el paciente dado que no esta afiliado a la obra social");
                }
            }
            // 2. Verificar que el paciente está afiliado a esa obra social
            if (!dbPacientes.estaAfiliado(cuil, obraSocialNombre)) {
                throw new IllegalArgumentException("No se puede registrar el paciente dado que no esta afiliado a la obra social");
            }

            // 3. Verificar que el número de afiliado coincide
            if (!dbPacientes.verificarNumeroAfiliado(cuil, obraSocialNombre, afiliado.getNumAfiliado())) {
                throw new IllegalArgumentException("Número de afiliado no válido");
            }
        }

        // Crear y guardar paciente
        Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, domicilio);
        dbPacientes.guardarPaciente(paciente);
    }
}
