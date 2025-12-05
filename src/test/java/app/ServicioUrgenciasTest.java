package app;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import app.Services.ServicioUrgencias;
import app.domain.*;
import app.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class ServicioUrgenciasTest {
    private ServicioUrgencias servicioUrgencias;
    private List<Ingreso> Ingresos;
    private RepositorioPacientes dbPacientes;

    @BeforeEach
    public void setUp() {
        this.Ingresos = mock(List.class);
        this.dbPacientes = mock(RepositorioPacientes.class);
        this.servicioUrgencias = new ServicioUrgencias(dbPacientes);
    }

    @Test
    public void primerPaciente() {
        ObraSocial obrasocial = new ObraSocial("subsidio", "ss");
        Afiliado afiliado = new Afiliado("123", obrasocial);
        Domicilio direccion = new Domicilio("Federico", 1138, "San Miguel de Tucuman");
        Paciente paciente = new Paciente("12345678", "Juan", "Perez", afiliado, direccion);
        Enfermera enfermera = new Enfermera("87654321", "Ana", "Gomez", "enfermera@gmail", "1234");
        String informePaciente = "Informe de prueba";
        when(dbPacientes.buscarPacientePorCuil("12345678")).thenReturn(java.util.Optional.of(paciente));

        servicioUrgencias.registrarUrgencias(
                paciente.getCuil()
                , enfermera
                , informePaciente
                , NivelEmergencia.CRITICA
                , 37.5f
                , 80f
                , 20f
                , 120f
                , 80f

        );

        //Validacion
        List<Ingreso> ingresosPendientes = servicioUrgencias.obtenerIngresosPendientes();
        assertThat(ingresosPendientes.getFirst().getCuilPaciente()).isEqualTo(paciente.getCuil());
    }

    @Test
    public void pacienteNoEncontrado() {
        Enfermera enfermera = new Enfermera("87654321", "Ana", "Gomez", "enfermera@gmail", "1234");
        String informePaciente = "Informe de prueba";
        when(dbPacientes.buscarPacientePorCuil("99999999")).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioUrgencias.registrarUrgencias(
                    "99999999"
                    , enfermera
                    , informePaciente
                    , NivelEmergencia.CRITICA
                    , 37.5f
                    , 80f
                    , 20f
                    , 120f
                    , 80f
            );
        });

        String expectedMessage = "Paciente no encontrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(dbPacientes, times(1)).buscarPacientePorCuil("99999999");
    }

}
