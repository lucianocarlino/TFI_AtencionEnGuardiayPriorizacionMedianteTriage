package app.domain.valueobject;

public class FrecuenciaSistolica extends Frecuencia{
    public FrecuenciaSistolica(Float value) {
        super(value);
    }


    public float getValue(){return value;}

    @Override
    protected RuntimeException notificarError() {
        return new IllegalArgumentException("Frecuencia sistolica no puede ser negativa");
    }

    @Override
    protected RuntimeException notificarCampoObligatorio() {
        return new IllegalArgumentException("Frecuencia sistolica es un campo obligatorio");
    }

    @Override
    protected String getValorFormateado() {
        return String.format("%.2f mmHg", this.value);
    }
}
