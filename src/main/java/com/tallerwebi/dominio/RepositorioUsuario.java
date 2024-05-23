package com.tallerwebi.dominio;

import java.util.Set;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscarPorEmail(String email);

    Usuario buscarPorId(long usuarioId);

    void modificar(Usuario usuario);
    void agregarFavorito(Usuario usuario, Long propiedadId);
    void eliminarFavorito(Usuario usuario, Long propiedadId);
    Set<Propiedad> listarFavoritos(Usuario usuario);
}

