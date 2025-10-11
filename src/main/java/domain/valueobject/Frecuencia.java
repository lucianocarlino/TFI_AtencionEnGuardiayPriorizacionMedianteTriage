package domain.valueobject;

public  abstract class Frecuencia {
    protected Float value;
    public Frecuencia(Float value) {
        if (value == null) {
            throw notificarCampoObligatorio();
        }
        if (value < 0) {
            throw notificarError();
        }
        this.value = value;

    }


    protected abstract RuntimeException notificarError();
    protected abstract RuntimeException notificarCampoObligatorio();
    protected abstract String getValorFormateado();

}
