package app;

import static org.junit.jupiter.api.Assertions.*;

import app.Services.ServicioRegistroPacientes;
import app.domain.Afiliado;
import app.domain.Domicilio;
import app.domain.ObraSocial;
import app.domain.Paciente;
import org.junit.jupiter.api.Test;
import app.mock.DBPrueba;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import  static  org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ServicioRegistroPacientesTest {

    private ServicioRegistroPacientes servicio;
    private DBPrueba dbPacientes;

    @BeforeEach
    public void setUp() {
        this.dbPacientes = mock(DBPrueba.class);
        this.servicio = new ServicioRegistroPacientes(dbPacientes);
    }


    @Test
    @DisplayName("Registro exitoso de paciente con todos los datos mandatorios y obra social existente")
    void registroExitosoConObraSocial() {

        String cuil = "23-1234567-9";
        String obraSocial = "OSDE";

        // Simulas que la obra social existe
        when(dbPacientes.buscarObraSocial("OSDE"))
                .thenReturn(new ObraSocial("OSDE", "OS123"));

        when(dbPacientes.existeObraSocial("OSDE")).thenReturn(true);

        // Simulas que el paciente está afiliado
        when(dbPacientes.estaAfiliado(cuil, obraSocial))
                .thenReturn(true);

        when(dbPacientes.verificarNumeroAfiliado(cuil,obraSocial,"12345678")).thenReturn(true);

        // When
        servicio.registrarPaciente(cuil, "Marcelo", "Nunez",
                new Domicilio("Calle", 123, "Tucuman"), new Afiliado("12345678",(new ObraSocial("OSDE", "OS123")) ));

        // Then - Verificas el comportamiento
        verify(dbPacientes, times(1)).guardarPaciente(any(Paciente.class));


    }

    @Test
    @DisplayName("Registro exitoso de paciente sin obra social")
    void registroExitosoSinObraSocial() {
//        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//
//        // When
//        servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
//
//        // Then
//        Optional<Paciente> pacienteOpt = dbPacientes.buscarPacientePorCuil(cuil);
//        assertThat(pacienteOpt).isPresent();
//
//        Paciente pacienteRegistrado = pacienteOpt.get();
//        assertThat(pacienteRegistrado.getAfiliado()).isNull();
        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When
        servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);

        // Then - Verificar que se guardó el paciente sin interacciones con obra social
        verify(dbPacientes, times(1)).guardarPaciente(any(Paciente.class));
        verify(dbPacientes, never()).buscarObraSocial(anyString());
        verify(dbPacientes, never()).estaAfiliado(anyString(), anyString());
    }

    @Test
    @DisplayName("Registro fallido por obra social inexistente")
    void registroFallidoObraSocialInexistente() {
        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//        String obraSocialInexistente = "OBRA_INEXISTENTE";
//        ObraSocial obraSocial = dbPacientes.buscarObraSocial(obraSocialInexistente);
//        String nroAfiliado = "12345678";
//        var afiliado = new Afiliado(nroAfiliado,obraSocial);
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, afiliado );
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("No se puede registrar al paciente con una obra social inexistente");

        // Given
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombreInexistente = "OBRA_INEXISTENTE";
        ObraSocial obraSocialInexistente = new ObraSocial("OBRA_INEXISTENTE", "OB123");
        String nroAfiliado = "12345678";

        // Simular que la obra social NO existe
        when(dbPacientes.buscarObraSocial(obraSocialNombreInexistente))
                .thenReturn(null);

        var afiliado = new Afiliado(nroAfiliado, obraSocialInexistente);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, afiliado);
        });

        assertThat(exception.getMessage()).isEqualTo("No se puede registrar al paciente con una obra social inexistente");

        // Verificar que se buscó la obra social pero NO se guardó el paciente
        verify(dbPacientes, times(1)).existeObraSocial(obraSocialNombreInexistente);
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));


    }

    @Test
    @DisplayName("Registro fallido por paciente no afiliado a la obra social")
    void registroFallidoPacienteNoAfiliado() {
        // Given
//        String cuilPacienteExistente = "27-4567890-3";
//        String cuilNuevoPaciente = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//        String obraSocialNombre = "OSDE";
//        ObraSocial obraSocial = dbPacientes.buscarObraSocial(obraSocialNombre);
//        String nroAfiliado = "12345678";
//        var afiliado = new Afiliado(nroAfiliado,obraSocial);
//
//        // Solo el paciente existente está afiliado, el nuevo no
//        dbPacientes.registrarAfiliacion(cuilPacienteExistente, obraSocialNombre, "87654321");
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            servicio.registrarPaciente(cuilNuevoPaciente, nombre, apellido, domicilio, afiliado);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("No se puede registrar el paciente dado que no esta afiliado a la obra social");

        String cuilNuevoPaciente = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombre = "OSDE";
        var obraSocial = new ObraSocial("OSDE", "OS123");
        String nroAfiliado = "12345678";

        // Simular que la obra social existe pero el paciente NO está afiliado
        when(dbPacientes.buscarObraSocial(obraSocialNombre))
                .thenReturn(new ObraSocial("OSDE", "OS123"));
        when (dbPacientes.existeObraSocial(obraSocialNombre)).thenReturn(true);
        when(dbPacientes.estaAfiliado(cuilNuevoPaciente, obraSocialNombre))
                .thenReturn(false); // ← NO está afiliado

        var afiliado = new Afiliado(nroAfiliado, obraSocial);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuilNuevoPaciente, nombre, apellido, domicilio, afiliado);
        });

        assertThat(exception.getMessage()).isEqualTo("No se puede registrar el paciente dado que no esta afiliado a la obra social");

        // Verificar interacciones
        verify(dbPacientes, times(1)).existeObraSocial(obraSocialNombre);
        verify(dbPacientes, times(1)).estaAfiliado(cuilNuevoPaciente, obraSocialNombre);
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));


    }

    @Test
    @DisplayName("Registro fallido por CUIL omitido")
    void registroFallidoCuilOmitido() {
        // Given
//        String cuil = null;
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("CUIL es un campo obligatorio");
        // Given
        String cuil = null;
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("CUIL es un campo obligatorio");

        // Verificar que NO hubo interacciones con la base de datos
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));
    }

    @Test
    @DisplayName("Registro fallido por apellido omitido")
    void registroFallidoApellidoOmitido() {
//        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = null;
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Apellido es un campo obligatorio");
        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = null;
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Apellido es un campo obligatorio");
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));

    }

    @Test
    @DisplayName("Registro fallido por nombre omitido")
    void registroFallidoNombreOmitido() {
        // Given
//        String cuil = "23-1234567-9";
//        String nombre = null;
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Nombre es un campo obligatorio");

        String cuil = "23-1234567-9";
        String nombre = null;
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Nombre es un campo obligatorio");
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));
    }

    @Test
    @DisplayName("Registro fallido por calle omitida")
    void registroFallidoCalleOmitida() {
        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio(null, 123, "Tucuman");
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Calle es un campo obligatorio");

        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio(null, 123, "Tucuman");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Calle es un campo obligatorio");
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));

    }

    @Test
    @DisplayName("Registro fallido por número omitido")
    void registroFallidoNumeroOmitido() {
//        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 0, "Tucuman");
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,  null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Numero es un campo obligatorio");

        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 0, "Tucuman");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Numero es un campo obligatorio");
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));

    }

    @Test
    @DisplayName("Registro fallido por localidad omitida")
    void registroFallidoLocalidadOmitida() {
//        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, null);
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Localidad es un campo obligatorio");

        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Localidad es un campo obligatorio");
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));

    }

    @Test
    @DisplayName("Registro fallido por numero de afiliado no coincide")
    void registroFallidoNumeroAfiliadoNoCoincide() {
        // Given
//        String cuil = "23-1234567-9";
//        String nombre = "Marcelo";
//        String apellido = "Nunez";
//        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
//        String obraSocialNombre = "OSDE";
//        ObraSocial obraSocial = dbPacientes.buscarObraSocial(obraSocialNombre);
//        String nroAfiliadoCorrecto = "12345678";
//        String nroAfiliadoIncorrecto = "99999999";
//        var afiliado = new Afiliado(nroAfiliadoIncorrecto, obraSocial);
//
//        dbPacientes.registrarAfiliacion(cuil, obraSocialNombre, nroAfiliadoCorrecto);
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            servicio.registrarPaciente(cuil, nombre, apellido, domicilio,afiliado);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("Número de afiliado no válido");

        String cuil = "23-1234567-9";
        String nombre = "Marcelo";
        String apellido = "Nunez";
        Domicilio domicilio = new Domicilio("San Martin", 123, "Tucuman");
        String obraSocialNombre = "OSDE";
        var obraSocial = new ObraSocial("OSDE", "OS");
        String nroAfiliadoIncorrecto = "99999999";

        // Simular que la obra social existe y el paciente está afiliado...
        when(dbPacientes.buscarObraSocial(obraSocialNombre))
                .thenReturn(new ObraSocial("OSDE", "OS123"));

        when(dbPacientes.existeObraSocial(obraSocialNombre)).thenReturn(true);
        when(dbPacientes.estaAfiliado(cuil, obraSocialNombre))
                .thenReturn(true);
        // ...pero el número de afiliado NO coincide
        when(dbPacientes.verificarNumeroAfiliado(cuil, obraSocialNombre, nroAfiliadoIncorrecto))
                .thenReturn(false);

        var afiliado = new Afiliado(nroAfiliadoIncorrecto, obraSocial);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicio.registrarPaciente(cuil, nombre, apellido, domicilio, afiliado);
        });

        assertThat(exception.getMessage()).isEqualTo("Número de afiliado no válido");

        // Verificar interacciones
        verify(dbPacientes, times(1)).existeObraSocial(obraSocialNombre);
        verify(dbPacientes, times(2)).estaAfiliado(cuil, obraSocialNombre);
        verify(dbPacientes, times(1)).verificarNumeroAfiliado(cuil, obraSocialNombre, nroAfiliadoIncorrecto);
        verify(dbPacientes, never()).guardarPaciente(any(Paciente.class));
    }


}