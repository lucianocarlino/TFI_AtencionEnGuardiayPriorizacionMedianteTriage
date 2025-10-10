package domain.valueobject;

public  abstract class Frecuencia {
    protected Float value;
    public Frecuencia(Float value) {
        validarFrecuencia(value);
        this.value = value;
    }
    private void validarFrecuencia(Float value) {
        if (value < 0) {
            throw this.notificarError();
        }
    }

    protected abstract RuntimeException notificarError();
    protected abstract String getValorFormateado();

}
