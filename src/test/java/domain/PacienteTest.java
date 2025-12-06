package domain;

import app.domain.Afiliado;
import app.domain.Domicilio;
import app.domain.ObraSocial;
import app.domain.Paciente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {
    /* Crear un paciente
    Nombre
    ** Nombre nulo
    ** Nombre vacio

     */
    @Test
    public void crearPacienteExitosamente() {
        // Preparacion
        String cuil = "20-12345678-9";
        String nombre = "Juan";
        String apellido = "Perez";
        Domicilio direccion = new Domicilio("Calle", 123, "Localidad");
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        Afiliado afiliado = new Afiliado("123", obraSocial);



        // Ejecucion
        Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, direccion);
        // Verificacion
        assertEquals(cuil, paciente.getCuil());
        assertEquals(nombre, paciente.getNombre());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(obraSocial, paciente.getObraSocial());
    }

    @Test
    public void crearPacienteConNombreNulo() {
        // Preparacion
        String cuil = "20-12345678-9";
        String nombre = null;
        String apellido = "Perez";
        Domicilio direccion = new Domicilio("Calle", 123, "Localidad");
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        // Ejecucion y Verificacion
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            new Paciente(cuil, nombre, apellido, direccion);
        });

        assertEquals(excepcion.getMessage(), "Nombre es un campo obligatorio");

    }

    @Test
    public void crearPacienteConNombreEspaciado() {
        // Preparacion
        String cuil = "20-12345678-9";
        String nombre = "   ";
        String apellido = "Perez";
        Domicilio  direccion = new Domicilio("Calle", 123, "Localidad");
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        // Ejecucion y Verificacion
        Paciente paciente = new Paciente(cuil, nombre, apellido, direccion);
        assertEquals(nombre, paciente.getNombre());
    }

}
