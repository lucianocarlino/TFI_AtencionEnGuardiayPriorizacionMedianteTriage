package domain.valueobject;

public class FrecuenciaSistolica extends Frecuencia{
    public FrecuenciaSistolica(Float value) {
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new IllegalArgumentException("Frecuencia sistolica no puede ser negativa");
    }

    @Override
    protected String getValorFormateado() {
        return String.format("%.2f mmHg", this.value);
    }
}
