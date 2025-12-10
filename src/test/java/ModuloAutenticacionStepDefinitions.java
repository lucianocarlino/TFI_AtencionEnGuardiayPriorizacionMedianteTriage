import app.Services.ServicioAutenticacion;
import app.domain.Autoridad;
import app.domain.Usuario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import app.mock.DBPrueba;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.*;

public class ModuloAutenticacionStepDefinitions {
    private DBPrueba dbMockeada;
    private ServicioAutenticacion  servicioAutenticacion;
    private Exception excepcionEsperada;

    public ModuloAutenticacionStepDefinitions() {
        this.dbMockeada = new DBPrueba();
        this.servicioAutenticacion = new ServicioAutenticacion(this.dbMockeada);
    }

    @Then("La lista de usuarios es:")
    public void laListaDeUsuariosEs(List<Map<String, String>> lista){
        List<Map<String, String>> usuarios = servicioAutenticacion.obtenerUsuarios()
                .stream()
                .map(usuario -> {
                    Map<String, String> mapa = new HashMap<>();
                    mapa.put("Email", usuario.getEmail());
                    mapa.put("Contrasena", usuario.getContrasena());
                    mapa.put("Autoridad",
                            usuario.getAutoridad() != null ?
                                    usuario.getAutoridad().getNombre() :
                                    null);
                    return mapa;
                })
                .toList();


        assertThat(usuarios).isEqualTo(lista);
    }

    @And("Que el usuario actual es:")
    public void queElUsuarioActualEs(List<Map<String, String>> tabla) {
        for (Map<String, String> fila : tabla) {
            String email = fila.get("Email");
            String contrasena = fila.get("Contrasena");
            String autoridadstr = fila.get("Autoridad");
            Autoridad autoridad = Optional.ofNullable(autoridadstr)
                    .filter(str -> !str.trim().isEmpty())
                    .map(str -> Arrays.stream(Autoridad.values())
                            .filter(autoridadf-> autoridadf.tieneNombre(str.trim()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Autoridad no encontrada: " + str)))
                    .orElse(null);


        }
    }

    @When("Intenta iniciar sesion el siguiente usuario:")
    public void intentaIniciarSesionElSiguienteUsuario(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        for (Map<String, String> usuario : tabla) {
        try {
            String email =  usuario.get("Email");
            String contrasena = usuario.get("Contrasena");
            servicioAutenticacion.iniciarSesion(email, contrasena);
        } catch(RuntimeException e) {
            this.excepcionEsperada = e;
        }}
    }

    @Then("El usuario actual es:")
    public void elUsuarioActualEs(List<Map<String, String>> tabla) {
        Usuario usuarioActual = null;
        for (Map<String, String> usuario : tabla) {
            usuarioActual = new Usuario(usuario.get("Email"), usuario.get("Contrasena"), null);
        }
        assertThat(servicioAutenticacion.getUsuarioActual().getEmail()).isEqualTo(usuarioActual.getEmail());

    }

    @Then("El sistema muestra el mensaje de error {string}")
    public void elSistemaMuestraElMensajeDeError(String arg0) {
        assertThat(this.excepcionEsperada).isNotNull();
        assertThat(this.excepcionEsperada.getMessage()).isEqualTo(arg0);
    }

    @When("Intenta crearse el siguiente usuario:")
    public void intentaCrearseElSiguienteUsuario(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        Autoridad autoridad = null;
        for (Map<String, String> usuario : tabla) {
        String email =  usuario.get("Email");
        String contrasena = usuario.get("Contrasena");
        String autoridadstr = usuario.get("Autoridad");
        if (autoridadstr != null) {
            autoridad = Optional.of(autoridadstr)
                    .filter(str -> !str.trim().isEmpty())
                    .map(str -> Arrays.stream(Autoridad.values())
                            .filter(autoridadf-> autoridadf.tieneNombre(str.trim()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Autoridad no encontrada: " + str)))
                    .orElse(null);
        }
        try {
            servicioAutenticacion.crearUsuario(email, contrasena, autoridad, "", "");
        } catch(RuntimeException e) {
            this.excepcionEsperada = e;
        }
        }
    }

    @Given("Existen los siguientes usuarios:")
    public void existenLosSiguientesUsuarios(List<Map<String, String>> tabla) {
        for(Map<String, String> fila : tabla){
            String email = fila.get("Email");
            String contrasena = fila.get("Contrasena");
            String autoridadstr = fila.get("Autoridad");
            Autoridad autoridad = Optional.ofNullable(autoridadstr)
                    .filter(str -> !str.trim().isEmpty())
                    .map(str -> Arrays.stream(Autoridad.values())
                            .filter(autoridadf-> autoridadf.tieneNombre(str.trim()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Autoridad no encontrada: " + str)))
                    .orElse(null);

            Usuario usuario = new Usuario(email, contrasena, autoridad);
            servicioAutenticacion.crearUsuario(email, contrasena, autoridad, "", "");
            dbMockeada.guardarUsuario(usuario);
    }
}}
