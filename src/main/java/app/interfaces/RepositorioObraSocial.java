package app.interfaces;

import domain.ObraSocial;

import java.util.Optional;

public interface RepositorioObraSocial {
    public void guardarObraSocial(ObraSocial obraSocial);
    public ObraSocial buscarObraSocial(String nombre);
}
