package app.domain.valueobject;

public class TensionArterial {
    private FrecuenciaDiastolica frecuenciaDiastolica;
    private FrecuenciaSistolica frecuenciaSistolica;

    public TensionArterial(Float frecuenciaSistolica, Float frecuenciaDiastolica) {
        this.frecuenciaSistolica = new FrecuenciaSistolica(frecuenciaSistolica);
        this.frecuenciaDiastolica = new FrecuenciaDiastolica(frecuenciaDiastolica);

    }

    public float getFrecuenciaDiastolica() {
        return frecuenciaDiastolica.getValue();
    }

    public float getFrecuenciaSistolica() {
        return frecuenciaSistolica.getValue();
    }

    public String getValorFormateado() {
        //return String.format("%s / %s", this.frecuenciaSistolica, this.frecuenciaDiastolica );
        return frecuenciaSistolica.getValue() + " / " + frecuenciaDiastolica.getValue();
    }
}
