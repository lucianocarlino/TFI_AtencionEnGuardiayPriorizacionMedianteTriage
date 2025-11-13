
import app.ServicioUrgencias;
import domain.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.DBPrueba;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ModuloUrgStepDefinitions {

    private Enfermera enfermera;
    private DBPrueba dbMockeada;
    private ServicioUrgencias servicioUrgencias;
    private Exception excepcionEsperada;

    public ModuloUrgStepDefinitions() {
        this.dbMockeada = new DBPrueba();
        this.servicioUrgencias = new ServicioUrgencias(dbMockeada);
    }

    private Float parseFloatOpcional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Given("Que la siguiente enfermera esta registrada:")
    public void queLaSiguienteEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
        String cuil = tabla.getFirst().get("Cuil");
        String nombre = tabla.getFirst().get("Nombre");
        String apellido = tabla.getFirst().get("Apellido");
        String correo = tabla.getFirst().get("Correo");
        String matricula = tabla.getFirst().get("Matricula");

        enfermera = new Enfermera(cuil,nombre, apellido, correo, matricula);

    }

    @Given("que estan registrados los siguientes pacientes en el sistema:")
    public void dadoQueEstanRegistradosLosSiguientesPacientesEnElSistema(List<Map<String, String>> tabla) {
        for  (Map<String, String> fila : tabla) {
            String cuil  = fila.get("Cuil");
            String nombre = fila.get("Nombre");
            String apellido = fila.get("Apellido");
            String obraSocial =  fila.get("Obra social");

            Paciente paciente = new  Paciente(cuil, nombre, apellido, obraSocial);

            dbMockeada.guardarPaciente(paciente);
        }
    }

    @When("Ingresan a urgencia los siguientes pacientes:")
    public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        for (Map<String, String> fila : tabla) {

            String cuil   = fila.get("Cuil");
            String informe = fila.get("Informe");
            String nivelStr = fila.get("Nivel de emergencia");
            NivelEmergencia nivelEmergencia = Optional.ofNullable(nivelStr)
                    .filter(str -> !str.trim().isEmpty())
                    .map(str -> Arrays.stream(NivelEmergencia.values())
                            .filter(nivel -> nivel.tieneNombre(str.trim()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Nivel Emergencia no encontrada: " + str)))
                    .orElse(null);
            Float temperatura =  parseFloatOpcional(fila.get("Temperatura"));
            Float frecuenciaCardiaca  = parseFloatOpcional(fila.get("Frecuencia cardiaca"));
            Float frecuenciaRespiratoria = parseFloatOpcional(fila.get("Frecuencia respiratoria"));

            Float sistolica = null;
            Float diastolica = null;
            String tensionArterialStr = fila.get("Presion arterial");
            if (tensionArterialStr != null && !tensionArterialStr.trim().isEmpty()) {
                String[] partes = tensionArterialStr.split("/",-1);
                sistolica = parseFloatOpcional(partes[0].trim());
                diastolica = parseFloatOpcional(partes[1].trim());
                //System.out.println("Presion sistolica: " + sistolica + ", diastolica: " + diastolica);
            }
            try {
                servicioUrgencias.registrarUrgencias(cuil, enfermera, informe, nivelEmergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, sistolica, diastolica);
            } catch (RuntimeException e) {
                this.excepcionEsperada = e;
            }
        }
    }

    @Then("La lista de espera esta ordenada por cuil de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuilDeLaSiguienteManera(List<Map<String, String>> lista) {


        List<Map<String, String>> ingresosPendientes = servicioUrgencias.obtenerIngresosPendientes()
                .stream()
                .map(ingreso -> Map.of(
                        "Cuil", ingreso.getCuilPaciente(),
                        "Estado",ingreso.getEstado().getNombre()
                ))
                .toList();

        assertThat(ingresosPendientes).isEqualTo(lista);
    }


    @Then("el sistema muestra el siguiente mensaje de error: {string}")
    public void elSistemaMuestraElSiguienteMensajeDeError(String arg0) {
        assertThat(this.excepcionEsperada).isNotNull();
        assertThat(this.excepcionEsperada.getMessage()).isEqualTo(arg0);
    }

    @When("Ingresan a urgencia los siguientes pacientes no registrados:")
    public void ingresanAUrgenciaLosSiguientesPacientesNoRegistrados(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        for  (Map<String, String> fila : tabla) {
            String cuil  = fila.get("Cuil");
            try{
                dbMockeada.buscarPacientePorCuil(cuil);
            }
            catch (RuntimeException e){
                this.excepcionEsperada = e;
            }
        }

    }

    @Then("el sistema registra los pacientes con sus respectivos datos en el sistema:")
    public void elSistemaRegistraLosPacientesConSusRespectivosDatosEnElSistema(List<Map<String, String>> tabla) {
        for (Map<String, String> fila : tabla) {
            String cuil  = fila.get("Cuil");
            String nombre = fila.get("Nombre");
            String apellido = fila.get("Apellido");
            String obraSocial =  fila.get("Obra social");
            Paciente paciente = new  Paciente(cuil, nombre, apellido, obraSocial);
            dbMockeada.guardarPaciente(paciente);
        }
    }

    @And("la lista de pacientes registrados en el sistema es la siguiente:")
    public void laListaDePacientesRegistradosEnElSistemaEsLaSiguiente( List<Map <String, String>> lista) {
        List<Map<String,String>> pacientesRegistrados = dbMockeada.obtenerTodosLosPacientes()
                .stream()
                .map(paciente -> Map.of(
                        "Cuil", paciente.getCuil(),
                        "Nombre", paciente.getNombre(),
                        "Apellido", paciente.getApellido(),
                        "Obra social", paciente.getObraSocial()
                ))
                .toList();

        assertThat(pacientesRegistrados).isEqualTo(lista);

    }

}
