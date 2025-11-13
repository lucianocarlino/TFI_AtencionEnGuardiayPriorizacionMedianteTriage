package domain;

import io.cucumber.java.nl.Stel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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



        // Ejecucion
        Paciente paciente = new Paciente(cuil, nombre, apellido, obraSocial);
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
        // Ejecucion y Verificacion
        Paciente paciente = new Paciente(cuil, nombre, apellido, obraSocial);
        assertNull(paciente.getNombre());


    }

    @Test
    public void crearPacienteConNombreEspaciado() {
        // Preparacion
        String cuil = "20-12345678-9";
        String nombre = "   ";
        String apellido = "Perez";
        ObraSocial obraSocial = new ObraSocial("OSDE","OS");
        // Ejecucion y Verificacion
        Paciente paciente = new Paciente(cuil, nombre, apellido, obraSocial);
        assertEquals(nombre, paciente.getNombre());
    }

}