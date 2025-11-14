import domain.Autoridad;
import domain.Usuario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.DBPrueba;

import java.util.List;
import java.util.Map;

public class ModuloAutenticacionStepDefinitions {
    private DBPrueba dbMockeada;

    public ModuloAutenticacionStepDefinitions() {
        this.dbMockeada = new DBPrueba();
    }

    @Given("La lista de usuarios es:")
    public void laListaDeUsuariosEs(List<Map<String, String>> tabla){
        for(Map<String, String> fila : tabla){
            String email = fila.get("Email");
            String contrasena = fila.get("Contrasena");
            Autoridad autoridad = Autoridad.valueOf(fila.get("Autoridad"));

            Usuario usuario = new Usuario(email, contrasena, autoridad);
            dbMockeada.guardarUsuario(usuario);
        }
    }

    @And("Que el usuario actual es:")
    public void queElUsuarioActualEs(Map<String, String> fila) {
        String email = fila.get("Email");
        String contrasena = fila.get("Contrasena");
        Autoridad autoridad = Autoridad.valueOf(fila.get("Autoridad"));

        Usuario usuarioActual = new Usuario(email, contrasena, autoridad);

        dbMockeada.setUsuarioActual(usuarioActual);
    }

    @When("Intenta iniciar sesion el siguiente usuario:")
    public void intentaIniciarSesionElSiguienteUsuario() {

    }

    @Then("El usuario actual es:")
    public void elUsuarioActualEs() {
    }

    @Then("El sistema muestra el mensaje de error {string}")
    public void elSistemaMuestraElMensajeDeError(String arg0) {
    }

    @When("Intenta crearse el siguiente usuario:")
    public void intentaCrearseElSiguienteUsuario() {
    }
}
