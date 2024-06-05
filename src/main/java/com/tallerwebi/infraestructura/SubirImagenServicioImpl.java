package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Propiedad;
import com.tallerwebi.dominio.RepositorioPropiedad;
import com.tallerwebi.dominio.SubirImagenServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class SubirImagenServicioImpl implements SubirImagenServicio {

    @Autowired
    private ServletContext servletContext;
    private final String CARPETA = "src/main/webapp/resources/core/img";
    private final RepositorioPropiedad repositorioPropiedad;

    public SubirImagenServicioImpl(RepositorioPropiedad repositorioPropiedad) {
        this.repositorioPropiedad = repositorioPropiedad;
    }


    @Override
    public String subirImagen(Long propiedadId, MultipartFile imagen) throws IOException {
        Propiedad propiedad = repositorioPropiedad.buscarPropiedad(propiedadId);

        if(propiedad == null){
            throw new IOException("Error! La propiedad no existe.");
        }
        if (imagen.isEmpty()) {
            throw new IOException("Error! No se ha proporcionado una imagen de la propiedad.");
        }

        Path carpetaDestino = Paths.get(CARPETA, String.valueOf(propiedadId));
        if (!Files.exists(carpetaDestino)) {
            try {
                Files.createDirectories(carpetaDestino);
            } catch (IOException e) {
                throw new IOException("Error al crear la carpeta de destino para la propiedad: " + propiedadId);
            }
        }

        try {
            // Obtener el nombre original del archivo
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(imagen.getOriginalFilename()));
            // Construir la ruta completa del archivo
            Path path = carpetaDestino.resolve(fileName);
            // Copiar el contenido del archivo al directorio de destino
            Files.copy(imagen.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            propiedad.setRutaImagen(fileName);
            repositorioPropiedad.editarPropiedad(propiedad);
            // Devolver la ruta del archivo guardado
            return path.toString();
        } catch (IOException e) {
            throw new IOException("Error al subir archivo de la imagen.");
        }
    }
}
