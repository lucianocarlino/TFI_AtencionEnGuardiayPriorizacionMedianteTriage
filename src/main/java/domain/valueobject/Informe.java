package domain.valueobject;

public class Informe {
    private final String value;

    public Informe(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("El informe es un campo obligatorio");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }
}

