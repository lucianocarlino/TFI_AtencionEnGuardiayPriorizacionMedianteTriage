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
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        Afiliado afiliado = new Afiliado("12334", obraSocial);
        Domicilio domicilio = new Domicilio("Fed", 123, "San mig");


        // Ejecucion
        Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, domicilio);
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
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        Afiliado afiliado = new Afiliado("12334", obraSocial);
        Domicilio domicilio = new Domicilio("Fed", 123, "San mig");


        // Ejecucion y verifiacion
        Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, domicilio);
        assertNull(paciente.getNombre());


    }

    @Test
    public void crearPacienteConNombreEspaciado() {
        // Preparacion
        String cuil = "20-12345678-9";
        String nombre = "   ";
        String apellido = "Perez";
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        Afiliado afiliado = new Afiliado("12334", obraSocial);
        Domicilio domicilio = new Domicilio("Fed", 123, "San mig");


        // Ejecucion y verifiacion
        Paciente paciente = new Paciente(cuil, nombre, apellido, afiliado, domicilio);
        assertEquals(nombre, paciente.getNombre());
    }

}
