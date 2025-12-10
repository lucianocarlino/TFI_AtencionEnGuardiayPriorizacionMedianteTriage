package app.mock;

import app.domain.*;
import app.interfaces.RepositorioObraSocial;
import app.interfaces.RepositorioPacientes;
import app.interfaces.RepositorioUsuarios;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import app.domain.Paciente;


import java.util.*;

@Repository
public class DBPrueba implements RepositorioPacientes, RepositorioObraSocial, RepositorioUsuarios {
    private List<Paciente> pacientes;
    private Map<String, ObraSocial> obrasociales;
    private Map<String,String> afiliaciones = new HashMap<>();
    private List<Usuario> usuarios;
    private Usuario usuarioActual;

    public DBPrueba() {
        this.pacientes = new ArrayList<>();
        this.obrasociales = new LinkedHashMap<>();

        this.usuarios = new ArrayList<>();

        ObraSocial osde = new ObraSocial("OSDE", "osd1");
        ObraSocial pami = new ObraSocial("PAMI", "pm1");

        this.obrasociales.put(osde.getIdentificador(), osde);
        this.obrasociales.put(pami.getIdentificador(), pami);

        try {
            Paciente p1 = new Paciente(
                    "20-12345678-9",
                    "Juan",
                    "Perez",
                    new Afiliado("12", osde),
                    new Domicilio("Chacabuco", 1138, "San Miguel de Tucuman")


            );

            Paciente p2 = new Paciente(
                    "20-98765432-1",
                    "Ana",
                    "Martinez",
                    new Afiliado("12", pami),
                    new Domicilio("Ayacucho", 1138, "San Miguel de Tucuman")
            );
            Paciente p3 = new Paciente(
                    "30-12345678-9",
                    "Lionel",
                    "Messi",
                    new Afiliado("13", osde),
                    new Domicilio("Jujuy", 1138, "San Miguel de Tucuman")
            );

            Afiliado a1 = null;

            Paciente p4 = new Paciente(
                    "30-98765432-1",
                    "Maria",
                    "Becerra",
                    new Afiliado("10", osde),
                    new Domicilio("Catamarca", 1138, "San Miguel de Tucuman")
            );

            this.pacientes.add(p1);
            this.pacientes.add(p2);
            this.pacientes.add(p3);
            this.pacientes.add(p4);

            //hasheo de contraseñas
            String hash1 = BCrypt.hashpw("password1", BCrypt.gensalt(10));
            String hash2 = BCrypt.hashpw("password2", BCrypt.gensalt(10));
            String hash3 = BCrypt.hashpw("password3", BCrypt.gensalt(10));
            String hash4 = BCrypt.hashpw("password4", BCrypt.gensalt(10));

            Usuario enfermero1 = new Usuario("enfermero@hospital.com", hash1, Autoridad.ENFERMERO, "Leandro","Paredes");
            Usuario enfermero2 = new Usuario("maria.lopez@hospital.com", hash2, Autoridad.ENFERMERO, "Agustin","Marchesin");
            Usuario medico1 = new Usuario("medico@hospital.com", hash3, Autoridad.MEDICO, "Miguel","Merentiel");
            Usuario medico2 = new Usuario("dr.garcia@hospital.com", hash4, Autoridad.MEDICO, "Pablo","Garcia");

            this.usuarios.add(enfermero1);
            this.usuarios.add(enfermero2);
            this.usuarios.add(medico1);
            this.usuarios.add(medico2);

            this.registrarAfiliacion(p1.getCuil(),p1.getObraSocialNombre(), p1.getAfiliado().getNumAfiliado());
            this.registrarAfiliacion(p2.getCuil(),p2.getObraSocialNombre(), p2.getAfiliado().getNumAfiliado());
            this.registrarAfiliacion(p3.getCuil(),p3.getObraSocialNombre(), p3.getAfiliado().getNumAfiliado());
            this.registrarAfiliacion(p4.getCuil(),p4.getObraSocialNombre(), p4.getAfiliado().getNumAfiliado());

            this.registrarAfiliacion("20-12345678-1", pami.getNombre(), "1");
            this.registrarAfiliacion("20-12345678-2", pami.getNombre(), "2");
            this.registrarAfiliacion("20-12345678-3", pami.getNombre(), "3");
            this.registrarAfiliacion("20-12345678-4", pami.getNombre(), "4");
            this.registrarAfiliacion("20-12345678-5", pami.getNombre(), "5");
            this.registrarAfiliacion("20-12345678-6", pami.getNombre(), "6");
            this.registrarAfiliacion("20-12345678-7", pami.getNombre(), "7");
            this.registrarAfiliacion("20-12345678-8", pami.getNombre(), "8");
            this.registrarAfiliacion("20-12345678-9", pami.getNombre(), "9");
            this.registrarAfiliacion("20-12345679-1", pami.getNombre(), "10");
            this.registrarAfiliacion("20-12345679-2", pami.getNombre(), "11");

            this.registrarAfiliacion("20-87654321-1", osde.getNombre(), "1");
            this.registrarAfiliacion("20-87654321-2", osde.getNombre(), "2");
            this.registrarAfiliacion("20-87654321-3", osde.getNombre(), "3");
            this.registrarAfiliacion("20-87654321-4", osde.getNombre(), "4");
            this.registrarAfiliacion("20-87654321-5", osde.getNombre(), "5");
            this.registrarAfiliacion("20-87654321-6", osde.getNombre(), "6");
            this.registrarAfiliacion("20-87654321-7", osde.getNombre(), "7");
            this.registrarAfiliacion("20-87654321-8", osde.getNombre(), "8");
            this.registrarAfiliacion("20-87654321-9", osde.getNombre(), "9");

        } catch (Exception e) {
            throw new RuntimeException( e.getMessage());
        }
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.stream()
                .filter(paciente -> paciente.getCuil().equals(cuil))
                .findFirst()
                .orElse(null));
    }

    @Override
    public boolean existeObraSocial(String obraSocialNombre) {
        var a = buscarObraSocial(obraSocialNombre);
        return a != null;

    }

    @Override
    public boolean estaAfiliado(String cuil, String obraSocial) {
        String key = cuil + "|" + obraSocial;
        return afiliaciones.containsKey(key);
    }

    public List<Paciente> obtenerTodosLosPacientes() {
        return pacientes;
    }

    @Override
    public void guardarObraSocial(ObraSocial obraSocial) {
        this.obrasociales.put(obraSocial.getIdentificador(), obraSocial);
    }

    @Override
    public ObraSocial buscarObraSocial(String nombre) {
        return obrasociales.values().stream()
                .filter(obraSocial -> obraSocial.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void registrarAfiliacion(String cuil, String obraSocial, String numeroAfiliado) {
        String key = cuil + "|" + obraSocial;
        afiliaciones.put(key, numeroAfiliado);
    }

    public boolean verificarNumeroAfiliado(String cuil, String obraSocial, String numeroAfiliado) {
        String key = cuil + "|" + obraSocial;
        String numeroGuardado = afiliaciones.get(key);
        return numeroAfiliado.equals(numeroGuardado);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuario(String email) {
        return Optional.ofNullable(usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst()
                .orElse(null));
    }

    @Override
    public Optional<Usuario> getUsuarioActual(String email, String contrasena) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email) && usuario.getContrasena().equals(contrasena))
                .findFirst();
    }


    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
