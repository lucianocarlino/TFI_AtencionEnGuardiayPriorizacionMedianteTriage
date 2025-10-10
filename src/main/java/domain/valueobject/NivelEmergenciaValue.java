package domain.valueobject;

import domain.NivelEmergencia;

public class NivelEmergenciaValue {
    private final NivelEmergencia value;

    public NivelEmergenciaValue(NivelEmergencia value) {
        if (value == null) {
            throw new RuntimeException("El nivel de emergencia es un campo obligatorio");
        }
        this.value = value;
    }

    public NivelEmergencia getValue() {
        return value;
    }
}
