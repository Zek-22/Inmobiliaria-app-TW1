package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPropiedadTest {
    private RepositorioPropiedad repositorioPropiedad;
    private ServicioPropiedad servicioPropiedad;

    @BeforeEach
    public void init() {
        this.repositorioPropiedad = mock(RepositorioPropiedad.class);
        this.servicioPropiedad = new ServicioPropiedad(this.repositorioPropiedad);
    }

    @Test
    public void queSeDevuelvaLaPropiedadBuscada() {

        Long idMock = 1L;
        Propiedad propiedadMock = mock(Propiedad.class);

        when(this.repositorioPropiedad.buscarPropiedad(idMock)).thenReturn(propiedadMock);
        Propiedad propiedadBuscada = (Propiedad) this.servicioPropiedad.buscarPropiedad(idMock).getModel().get("propiedad");

        assertThat(propiedadBuscada, equalTo(propiedadMock));
    }

    @Test
    public void queSeDevuelvanLasPropiedadesListadas() {

        List<Propiedad> propiedadesMock = new ArrayList<>();
        propiedadesMock.add(new Propiedad(1L, "Casa 1", 2, 3, 4, 200.0, 150000.0, "Ubicacion 1"));
        propiedadesMock.add(new Propiedad(2L, "Casa 2", 3, 2, 5, 250.0, 180000.0, "Ubicacion 2"));
        propiedadesMock.add(new Propiedad(3L, "Casa 3", 1, 1, 2, 120.0, 90000.0, "Ubicacion 3"));

        when(this.repositorioPropiedad.listarPropiedades()).thenReturn(propiedadesMock);
        List<Propiedad> propiedadesListadas = this.servicioPropiedad.listarPropiedades();

        assertThat(propiedadesListadas, equalTo(propiedadesMock));
    }
}
