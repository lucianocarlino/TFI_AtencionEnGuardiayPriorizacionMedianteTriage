package domain.valueobject;

import app.domain.valueobject.FrecuenciaCardiaca;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FrecuenciaCardiacaTest {
    @Test
    public void creacionExitosa() {
        //Preparacion del test
        Float frecuencia = 80f;
        //Ejecucion + Verificacion
        assertThatCode(()-> new FrecuenciaCardiaca(frecuencia))
                .doesNotThrowAnyException();
    }

}