package app.domain.valueobject;

public class FrecuenciaRespiratoria extends Frecuencia {

    public FrecuenciaRespiratoria(Float value) {
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new IllegalArgumentException("Frecuencia respiratoria no puede ser negativa");
    }

    @Override
    protected RuntimeException notificarCampoObligatorio() {
        return new IllegalArgumentException("Frecuencia respiratoria es un campo obligatorio");
    }

    @Override
    protected String getValorFormateado() {
        return String.format("%.2f rpm", this.value);
    }
}
