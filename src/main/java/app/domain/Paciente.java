package app.domain;

public class Paciente {
    public String cuil;
    public String nombre;
    public String apellido;
    public Afiliado afiliado;
    public Domicilio direccion;

    public Paciente(String cuil, String nombre, String apellido, Domicilio direccion) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        validarCampos();
    }

    public Paciente(String cuil, String nombre, String apellido, Afiliado afiliado, Domicilio direccion) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.afiliado = afiliado;
        this.direccion = direccion;
        validarCampos();
    }

    public String getCuil() {
        return cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public Domicilio getDireccion() {
        return direccion;
    }

    public void validarCampos() {
        if (this.cuil == null || cuil.length() == 0) {
            throw new RuntimeException("Cuil es un campo obligatorio");
        }

        if (nombre == null || nombre.length() == 0) {
            throw new RuntimeException("Nombre es un campo obligatorio");
        }
        if (apellido == null || apellido.length() == 0) {
            throw new RuntimeException("Apellido es un campo obligatorio");
        }

        if (direccion == null) {
            return;
        }
        var calle = this.direccion.getCalle();
        if (calle == null) {
            throw new RuntimeException("Calle es un campo obligatorio");
        }
        var numero = this.direccion.getNumero();
        if (numero == 0) {
            throw new RuntimeException("Numero es un campo obligatorio");
        }
        var localidad = this.direccion.getLocalidad();
        if (localidad == null) {
            throw new RuntimeException("Localidad es un campo obligatorio");
        }
    }

    public ObraSocial getObraSocial() {
        return afiliado != null ? afiliado.getObraSocial() : null;
    }

    public String getObraSocialNombre() {
        if (afiliado != null && afiliado.getObraSocial() != null) {
            return afiliado.getObraSocial().getNombre();
        }
        return null;
    }
}
