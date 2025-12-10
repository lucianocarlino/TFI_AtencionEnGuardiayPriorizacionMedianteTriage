package app;

import static org.junit.jupiter.api.Assertions.*;

import app.Services.ServicioAutenticacion;
import app.domain.Autoridad;
import app.domain.Usuario;
import app.interfaces.RepositorioUsuarios;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

class ServicioAutenticacionTest {

    private ServicioAutenticacion servicio;
    private RepositorioUsuarios dbUsuarios;

    @BeforeEach
    public void setUp() {
        this.dbUsuarios = mock(RepositorioUsuarios.class);
        this.servicio = new ServicioAutenticacion(dbUsuarios);
    }

    // ========== TESTS DE REGISTRO DE USUARIO ==========

    @Test
    @DisplayName("Registro exitoso de usuario con email y contraseña válidos")
    void registroExitosoUsuario() {
        // Given
        String email = "medico@hospital.com";
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.MEDICO;

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.empty());

        // When
        servicio.crearUsuario(email, contrasena, autoridad);

        // Then - Verificar que se guardó el usuario
        verify(dbUsuarios, times(1)).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro exitoso de usuario con nombre y apellido")
    void registroExitosoUsuarioCompleto() {
        // Given
        String email = "enfermero@hospital.com";
        String contrasena = "password456";
        Autoridad autoridad = Autoridad.ENFERMERO;
        String nombre = "Juan";
        String apellido = "Pérez";

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.empty());

        // When
        servicio.crearUsuario(email, contrasena, autoridad, nombre, apellido);

        // Then - Verificar que se guardó el usuario
        verify(dbUsuarios, times(1)).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro fallido por email inválido - sin @")
    void registroFallidoEmailInvalidoSinArroba() {
        // Given
        String emailInvalido = "medicohospital.com"; // Sin @
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.MEDICO;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.crearUsuario(emailInvalido, contrasena, autoridad);
        });

        assertThat(exception.getMessage()).isEqualTo("Email invalido");
        verify(dbUsuarios, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro fallido por email inválido - sin dominio")
    void registroFallidoEmailInvalidoSinDominio() {
        // Given
        String emailInvalido = "medico@"; // Sin dominio
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.MEDICO;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.crearUsuario(emailInvalido, contrasena, autoridad);
        });

        assertThat(exception.getMessage()).isEqualTo("Email invalido");
        verify(dbUsuarios, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro fallido por contraseña menor a 8 caracteres")
    void registroFallidoContrasenaCorta() {
        // Given
        String email = "medico@hospital.com";
        String contrasenaCorta = "pass12"; // Solo 6 caracteres
        Autoridad autoridad = Autoridad.MEDICO;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.crearUsuario(email, contrasenaCorta, autoridad);
        });

        assertThat(exception.getMessage()).isEqualTo("Contrasena demasiado corta");
        verify(dbUsuarios, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro fallido por contraseña nula")
    void registroFallidoContrasenaNula() {
        // Given
        String email = "medico@hospital.com";
        String contrasenaNula = null;
        Autoridad autoridad = Autoridad.MEDICO;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.crearUsuario(email, contrasenaNula, autoridad);
        });

        assertThat(exception.getMessage()).isEqualTo("Contrasena es un campo obligatorio");
        verify(dbUsuarios, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro fallido por email ya existente")
    void registroFallidoEmailExistente() {
        // Given
        String email = "medico@hospital.com";
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.MEDICO;

        // Simular que el usuario YA existe
        Usuario usuarioExistente = new Usuario(email, BCrypt.hashpw(contrasena, BCrypt.gensalt()), autoridad);
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.of(usuarioExistente));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.crearUsuario(email, contrasena, autoridad);
        });

        assertThat(exception.getMessage()).isEqualTo("Email existente");
        verify(dbUsuarios, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro exitoso con contraseña de exactamente 8 caracteres")
    void registroExitosoContrasenaMinima() {
        // Given
        String email = "medico@hospital.com";
        String contrasenaMinima = "pass1234"; // Exactamente 8 caracteres
        Autoridad autoridad = Autoridad.MEDICO;

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.empty());

        // When
        servicio.crearUsuario(email, contrasenaMinima, autoridad);

        // Then - Verificar que se guardó el usuario
        verify(dbUsuarios, times(1)).guardarUsuario(any(Usuario.class));
    }

    // ========== TESTS DE INICIO DE SESIÓN ==========

    @Test
    @DisplayName("Login exitoso con credenciales válidas")
    void loginExitoso() {
        // Given
        String email = "medico@hospital.com";
        String contrasena = "password123";
        String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        Autoridad autoridad = Autoridad.MEDICO;

        Usuario usuarioExistente = new Usuario(email, contrasenaHasheada, autoridad);

        // Simular que el usuario existe en la base de datos
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.of(usuarioExistente));

        // When
        servicio.iniciarSesion(email, contrasena);

        // Then - Verificar que se estableció el usuario actual
        verify(dbUsuarios, times(1)).setUsuarioActual(any(Usuario.class));
        assertThat(servicio.getUsuarioActual()).isNotNull();
        assertThat(servicio.getUsuarioActual().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Login fallido con contraseña incorrecta - mensaje genérico")
    void loginFallidoContrasenaIncorrecta() {
        // Given
        String email = "medico@hospital.com";
        String contrasenaCorrecta = "password123";
        String contrasenaIncorrecta = "wrongpassword";
        String contrasenaHasheada = BCrypt.hashpw(contrasenaCorrecta, BCrypt.gensalt());
        Autoridad autoridad = Autoridad.MEDICO;

        Usuario usuarioExistente = new Usuario(email, contrasenaHasheada, autoridad);

        // Simular que el usuario existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.of(usuarioExistente));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.iniciarSesion(email, contrasenaIncorrecta);
        });

        // Verificar que NO revela que el usuario existe
        assertThat(exception.getMessage()).isEqualTo("Usuario o contrasena invalido");
        verify(dbUsuarios, never()).setUsuarioActual(any(Usuario.class));
    }

    @Test
    @DisplayName("Login fallido con usuario inexistente - mensaje genérico")
    void loginFallidoUsuarioInexistente() {
        // Given
        String emailInexistente = "noexiste@hospital.com";
        String contrasena = "password123";

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(emailInexistente)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.iniciarSesion(emailInexistente, contrasena);
        });

        // Verificar que NO revela que el usuario no existe
        assertThat(exception.getMessage()).isEqualTo("Usuario o contrasena invalido");
        verify(dbUsuarios, never()).setUsuarioActual(any(Usuario.class));
    }

    @Test
    @DisplayName("Login fallido con contraseña nula - mensaje genérico")
    void loginFallidoContrasenaNula() {
        // Given
        String email = "medico@hospital.com";
        String contrasenaNula = null;
        String contrasenaHasheada = BCrypt.hashpw("password123", BCrypt.gensalt());
        Autoridad autoridad = Autoridad.MEDICO;

        Usuario usuarioExistente = new Usuario(email, contrasenaHasheada, autoridad);

        // Simular que el usuario existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.of(usuarioExistente));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.iniciarSesion(email, contrasenaNula);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuario o contrasena invalido");
        verify(dbUsuarios, never()).setUsuarioActual(any(Usuario.class));
    }

    @Test
    @DisplayName("Login fallido con email vacío - mensaje genérico")
    void loginFallidoEmailVacio() {
        // Given
        String emailVacio = "";
        String contrasena = "password123";

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(emailVacio)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicio.iniciarSesion(emailVacio, contrasena);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuario o contrasena invalido");
        verify(dbUsuarios, never()).setUsuarioActual(any(Usuario.class));
    }

    // ========== TESTS DE AUTORIDADES Y PERMISOS ==========

    @Test
    @DisplayName("Usuario médico registrado correctamente con autoridad MEDICO")
    void registroUsuarioMedicoConAutoridadCorrecta() {
        // Given
        String email = "medico@hospital.com";
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.MEDICO;

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.empty());

        // When
        servicio.crearUsuario(email, contrasena, autoridad);

        // Then
        verify(dbUsuarios, times(1)).guardarUsuario(argThat(usuario ->
                usuario.getAutoridad() == Autoridad.MEDICO
        ));
    }

    @Test
    @DisplayName("Usuario enfermero registrado correctamente con autoridad ENFERMERO")
    void registroUsuarioEnfermeroConAutoridadCorrecta() {
        // Given
        String email = "enfermero@hospital.com";
        String contrasena = "password123";
        Autoridad autoridad = Autoridad.ENFERMERO;

        // Simular que el usuario NO existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.empty());

        // When
        servicio.crearUsuario(email, contrasena, autoridad);

        // Then
        verify(dbUsuarios, times(1)).guardarUsuario(argThat(usuario ->
                usuario.getAutoridad() == Autoridad.ENFERMERO
        ));
    }

    @Test
    @DisplayName("Login exitoso mantiene la autoridad del usuario")
    void loginExitosoMantieneAutoridad() {
        // Given
        String email = "medico@hospital.com";
        String contrasena = "password123";
        String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        Autoridad autoridad = Autoridad.MEDICO;

        Usuario usuarioExistente = new Usuario(email, contrasenaHasheada, autoridad);

        // Simular que el usuario existe
        when(dbUsuarios.buscarUsuario(email)).thenReturn(Optional.of(usuarioExistente));

        // When
        servicio.iniciarSesion(email, contrasena);

        // Then
        assertThat(servicio.getUsuarioActual().getAutoridad()).isEqualTo(Autoridad.MEDICO);
    }



}

