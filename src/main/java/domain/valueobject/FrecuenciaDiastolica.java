package domain.valueobject;

public class FrecuenciaDiastolica extends Frecuencia{
    public FrecuenciaDiastolica(Float value) {
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new IllegalArgumentException("Frecuencia diastolica no puede ser negativa");
    }

    @Override
    protected String getValorFormateado() {
        return String.format("%.2f mmHg", this.value);
    }
}
