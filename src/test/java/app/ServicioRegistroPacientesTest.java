package app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.*;
import mock.DBPrueba;
import org.junit.jupiter.api.*;

import java.util.Optional;

import  static  org.assertj.core.api.Assertions.*;

class ServicioRegistroPacientesTest {

    private DBPrueba dbMock;
    private ServicioRegistroPacientes servicio;

    @BeforeEach
    void setUp() {
        dbMock = new DBPrueba();
        servicio = new ServicioRegistroPacientes(dbMock);

        // Configurar obras sociales de prueba
        dbMock.guardarObraSocial(new ObraSocial("OSDE", "OS"));
        dbMock.guardarObraSocial(new ObraSocial("Swiss Medical", "SM"));
        dbMock.guardarObraSocial(new ObraSocial("Subsidio de salud", "SS"));
    }

    @Test
    @DisplayName("Registro exitoso de paciente con todos los datos mandatorios y obra social existente")
    void registroExitosoConObraSocial() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombre = "OSDE";
        ObraSocial obraSocial = dbMock.buscarObraSocial(obraSocialNombre);
        String nroAfiliado = "12345678";
        Afiliado afiliado = new Afiliado(nroAfiliado,obraSocial);

        dbMock.registrarAfiliacion(cuil, obraSocialNombre, nroAfiliado);

        // When
        servicio.registrarPaciente(cuil, nombre, apellido, domicilio, afiliado);


        // Then
        Optional<Paciente> pacienteOpt = dbMock.buscarPacientePorCuil(cuil);
        assertThat(pacienteOpt).isPresent();

        Paciente pacienteRegistrado = pacienteOpt.get();
        assertThat(pacienteRegistrado.getNombre()).isEqualTo(nombre);
        assertThat(pacienteRegistrado.getObraSocial()).isEqualTo(obraSocial);
    }

    @Test
    @DisplayName("Registro exitoso de paciente sin obra social")
    void registroExitosoSinObraSocial() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When
        servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);

        // Then
        Optional<Paciente> pacienteOpt = dbMock.buscarPacientePorCuil(cuil);
        assertThat(pacienteOpt).isPresent();

        Paciente pacienteRegistrado = pacienteOpt.get();
        assertThat(pacienteRegistrado.getAfiliado()).isNull();
    }

    @Test
    @DisplayName("Registro fallido por obra social inexistente")
    void registroFallidoObraSocialInexistente() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialInexistente = "OBRA_INEXISTENTE";
        ObraSocial obraSocial = dbMock.buscarObraSocial(obraSocialInexistente);
        String nroAfiliado = "12345678";
        var afiliado = new Afiliado(nroAfiliado,obraSocial);
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, afiliado );
        });

        assertThat(exception.getMessage()).isEqualTo("No se puede registrar al paciente con una obra social inexistente");
    }

    @Test
    @DisplayName("Registro fallido por paciente no afiliado a la obra social")
    void registroFallidoPacienteNoAfiliado() {
        // Given
        String cuilPacienteExistente = "27-4567890-3";
        String cuilNuevoPaciente = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombre = "OSDE";
        ObraSocial obraSocial = dbMock.buscarObraSocial(obraSocialNombre);
        String nroAfiliado = "12345678";
        var afiliado = new Afiliado(nroAfiliado,obraSocial);

        // Solo el paciente existente está afiliado, el nuevo no
        dbMock.registrarAfiliacion(cuilPacienteExistente, obraSocialNombre, "87654321");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuilNuevoPaciente, nombre, apellido, domicilio, afiliado);
        });

        assertThat(exception.getMessage()).isEqualTo("No se puede registrar el paciente dado que no esta afiliado a la obra social");
    }

    @Test
    @DisplayName("Registro fallido por CUIL omitido")
    void registroFallidoCuilOmitido() {
        // Given
        String cuil = null;
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
        });

        assertThat(exception.getMessage()).isEqualTo("CUIL es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por apellido omitido")
    void registroFallidoApellidoOmitido() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = null;
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
        });

        assertThat(exception.getMessage()).isEqualTo("Apellido es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por nombre omitido")
    void registroFallidoNombreOmitido() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = null;
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Nombre es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por calle omitida")
    void registroFallidoCalleOmitida() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio(null, 123, "Tucuman");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Calle es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por número omitido")
    void registroFallidoNumeroOmitido() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 0, "Tucuman");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
        });

        assertThat(exception.getMessage()).isEqualTo("Numero es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por localidad omitida")
    void registroFallidoLocalidadOmitida() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Localidad es un campo obligatorio");
    }

    @Test
    @DisplayName("Registro fallido por numero de afiliado no coincide")
    void registroFallidoNumeroAfiliadoNoCoincide() {
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombre = "OSDE";
        ObraSocial obraSocial = dbMock.buscarObraSocial(obraSocialNombre);
        String nroAfiliadoCorrecto = "12345678";
        String nroAfiliadoIncorrecto = "99999999";
        var afiliado = new Afiliado(nroAfiliadoIncorrecto, obraSocial);

        dbMock.registrarAfiliacion(cuil, obraSocialNombre, nroAfiliadoCorrecto);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,afiliado);
        });

        assertThat(exception.getMessage()).isEqualTo("Número de afiliado no válido");
    }
}