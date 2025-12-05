package stepdefinitions;

import app.ServicioUrgencias;
import domain.Enfermera;
import domain.Ingreso;
import domain.NivelEmergencia;
import domain.Paciente;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.DBPrueba;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ModuloUrgenciasStepDefinitions {

    private Enfermera enfermera;
    private DBPrueba dbMockeada;
    private ServicioUrgencias servicioUrgencias;

    public ModuloUrgenciasStepDefinitions() {
        this.dbMockeada = new DBPrueba();
        this.servicioUrgencias = new ServicioUrgencias(dbMockeada);
    }

    @Given("Que la siguiente enfermera esta registrada:")
    public void queLaSiguienteEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
        String nombre = tabla.getFirst().get("nombre");
        String apellido = tabla.getFirst().get("apellido");

        enfermera = new Enfermera(nombre, apellido);

    }

    @Given("Dado que estan registrados los siguientes pacientes en el sistema:")
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

    @When("Ingresa a urgencias el siguiente paciente:")
    public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        Map<String,String> fila = tabla.getFirst();
        String cuil   = fila.get("Cuil");
        String informe = fila.get("Informe");
        NivelEmergencia nivelEmergencia = Arrays.stream(NivelEmergencia.values()).
                filter( nivel -> nivel.tieneNombre(fila.get("Nivel de emergencia"))).
                findFirst().
                orElseThrow(() -> new RuntimeException("Nivel Emergencia no encontrada"));
        Float temperatura =  Float.parseFloat(fila.get("Temperatura"));
        Float frecuenciaCardiaca  = Float.parseFloat(fila.get("Frecuencia cardiaca"));
        Float frecuenciaRespiratoria = Float.parseFloat(fila.get("Frecuencia respiratoria"));
        List<Float> presionArterial = Arrays.stream(fila.get("Presion arterial").split("/")).map(Float::parseFloat).toList();

        servicioUrgencias.registrarUrgencias(cuil, enfermera, informe, nivelEmergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, presionArterial.get(0),  presionArterial.get(1));

    }

    @Then("La lista de espera esta ordenada por cuil de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuilDeLaSiguienteManera(List<String> lista) {
        String cuilEsperado = lista.getFirst();
        List<String> cuilsPendientes = servicioUrgencias.obtenerIngresosPendientes()
                .stream()
                .map(Ingreso::getCuilPaciente)
                .toList();

        assertThat(cuilsPendientes.size() == 1 && cuilsPendientes.contains(cuilEsperado)).isTrue();
    }
}
