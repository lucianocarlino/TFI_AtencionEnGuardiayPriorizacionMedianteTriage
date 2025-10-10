package domain.valueobject;

public class FrecuenciaCardiaca extends Frecuencia {
    public FrecuenciaCardiaca(Float value) {
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new RuntimeException("Frecuencia cardiaca no puede ser negativa");
    }

    @Override
    protected String getValorFormateado() {
        return String.format("%.2f lpm", this.value);
    }

}
