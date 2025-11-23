package app.interfaces;

import app.domain.ObraSocial;

public interface RepositorioObraSocial {
    public void guardarObraSocial(ObraSocial obraSocial);
    public ObraSocial buscarObraSocial(String nombre);
}
