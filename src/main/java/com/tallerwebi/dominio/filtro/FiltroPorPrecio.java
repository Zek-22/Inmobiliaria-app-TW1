package com.tallerwebi.dominio.filtro;

import com.tallerwebi.dominio.Propiedad;
import com.tallerwebi.presentacion.DatosFiltro;
import com.tallerwebi.presentacion.FiltrarPorPrecio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorPrecio implements FiltroPropiedad {
    @Override
    public List<Propiedad> filtrar(List<Propiedad> propiedades, DatosFiltro datosFiltro) {
        FiltrarPorPrecio filtro = datosFiltro.getFiltrarPorPrecio();
        Double precioDelFiltro = datosFiltro.getPrecio();

        if(precioDelFiltro == null) {
            precioDelFiltro = 0.0;
        }

        if (filtro == FiltrarPorPrecio.MINIMO) {
            return filtrarPorMinimo(propiedades, precioDelFiltro);
        } else {
            return filtrarPorMaximo(propiedades, precioDelFiltro);
        }
    }

    private static List<Propiedad> filtrarPorMaximo(List<Propiedad> propiedades, Double precioDelFiltro) {
        List<Propiedad> propiedadesFiltradas = new ArrayList<>();
        for (Propiedad prop : propiedades) {
            if (prop.getPrecio() <= precioDelFiltro) {
                propiedadesFiltradas.add(prop);
            }
        }
        return propiedadesFiltradas;
    }

    private static List<Propiedad> filtrarPorMinimo(List<Propiedad> propiedades, Double precioDelFiltro) {
        List<Propiedad> propiedadesFiltradas = new ArrayList<>();
        for (Propiedad prop : propiedades) {
            if (prop.getPrecio() >= precioDelFiltro) {
                propiedadesFiltradas.add(prop);
            }
        }
        return propiedadesFiltradas;
    }
}
