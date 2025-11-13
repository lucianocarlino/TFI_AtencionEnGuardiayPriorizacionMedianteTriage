import app.ServicioRegistroPacientes;
import app.ServicioUrgencias;
import domain.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.DBPrueba;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.*;

public class ModuloRegPacientesStepDef {

    private ObraSocial obraSocial;
    private List<ObraSocial> obraSocialesRegistradas = new ArrayList<ObraSocial>();
    private DBPrueba dbMockeada;
    private ServicioRegistroPacientes servicioRegistroPacientes;
    private Exception exceptionEsperada;

    public ModuloRegPacientesStepDef() {
        this.dbMockeada = new DBPrueba();
        this.servicioRegistroPacientes = new ServicioRegistroPacientes(dbMockeada);
    }

    @And("Las siguientes obras sociales están registradas en el sistema:")
    public void lasSiguientesObrasSocialesEstanRegistradasEnElSistema(List<Map<String, String>> tabla) {
        for (Map<String, String> fila : tabla) {
            String nombre = fila.get("Nombre");
            String Identificador = fila.get("Identificador");
            obraSocial = new ObraSocial(nombre, Identificador);
            obraSocialesRegistradas.add(obraSocial);
            dbMockeada.guardarObraSocial(obraSocial);
        }


    }


    @Then("la lista de pacientes es :")
    public void laListaDePacientesEs(List<Map<String, String>> pacientesEsperados) {
        List<Paciente> pacientesRegistrados = dbMockeada.obtenerTodosLosPacientes();
        Map<String, String> pacienteEsperado = pacientesEsperados.get(0);

        Paciente pacienteEncontrado = pacientesRegistrados.stream()
                .filter(p -> p.getCuil().equals(pacienteEsperado.get("Cuil")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Paciente no encontrado"));

        assertThat(pacienteEncontrado.getApellido()).isEqualTo(pacienteEsperado.get("Apellido"));
        assertThat(pacienteEncontrado.getNombre()).isEqualTo(pacienteEsperado.get("Nombre"));
    }


    @And("El paciente aparece en la lista de pacientes sin obra social:")
    public void elPacienteApareceEnLaListaDePacientesSinObraSocial(List<Map<String, String>> pacientesEsperados) {
        List<Paciente> pacientesRegistrados = dbMockeada.obtenerTodosLosPacientes();
        Map<String, String> pacienteEsperado = pacientesEsperados.get(0);

        Paciente pacienteEncontrado = pacientesRegistrados.stream()
                .filter(p -> p.getCuil().equals(pacienteEsperado.get("Cuil")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Paciente no encontrado"));

        assertThat(pacienteEncontrado.getApellido()).isEqualTo(pacienteEsperado.get("Apellido"));
        assertThat(pacienteEncontrado.getNombre()).isEqualTo(pacienteEsperado.get("Nombre"));
        assertThat(pacienteEncontrado.getObraSocialNombre()).isEmpty();
    }



    @Then("El sistema muestra el mensaje de error: {string}")
    public void elSistemaMuestraElMensajeDeError(String mensajeEsperado) {
        assertThat(exceptionEsperada).isNotNull();

        // Verificar que el mensaje de error sea el esperado
        assertThat(exceptionEsperada.getMessage()).isEqualTo(mensajeEsperado);

    }

    @Given("Existen los siguientes pacientes afiliados a obras sociales:")
    public void existenLosSiguientesPacientesAfiliadosAObrasSociales(List<Map<String, String>> pacientesAfiliados) {
        for (Map<String, String> afiliado : pacientesAfiliados) {
            String cuil = afiliado.get("Cuil");
            String obraSocial = afiliado.get("Obra social");
            String numeroAfiliado = afiliado.get("Numero afiliado");

            // Guardar la afiliación en tu sistema
            dbMockeada.registrarAfiliacion(cuil, obraSocial, numeroAfiliado);
        }
    }


    @When("Se intenta registrar el siguiente paciente:")
    public void seIntentaRegistrarElSiguientePaciente(List<Map<String, String>> pacienteData) {
        int numero;
        Afiliado afiliado;
        try {
            Map<String, String> paciente = pacienteData.get(0);
            String cuil = paciente.get("Cuil");
            String apellido = paciente.get("Apellido");
            String nombre = paciente.get("Nombre");
            String calle = paciente.get("Calle");
            if (paciente.get("Numero") != null) {
                 numero = Integer.parseInt(paciente.get("Numero"));
            }
            else numero = 0;
            String localidad = paciente.get("Localidad");
            Domicilio domicilio = new Domicilio(calle, numero, localidad);
            String obraSocialNombre = paciente.get("Obra social");
            if (obraSocialNombre == null) {
                var obraSocialNoIngresada = new ObraSocial("","");
                afiliado = new Afiliado("",obraSocialNoIngresada);
                servicioRegistroPacientes.registrarPaciente(cuil,nombre,apellido,domicilio,afiliado);
                return;
            }
            var obraSocial = dbMockeada.buscarObraSocial(obraSocialNombre);

            String nroAfiliado = paciente.get("Numero afiliado");
            afiliado = new Afiliado(nroAfiliado, obraSocial);

            servicioRegistroPacientes.registrarPaciente(
                    cuil, nombre, apellido, domicilio, afiliado
            );

        } catch (Exception e) {
            this.exceptionEsperada = e;
        }
    }


    @Then("El paciente es registrado exitosamente")
    public void elPacienteEsRegistradoExitosamente() {
        assertThat(exceptionEsperada).isNull();
    }
}


