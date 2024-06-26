package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Propiedad;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioPropiedad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CRUDPropiedadExcepcion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorPropiedad {

    private final ServicioPropiedad servicioPropiedad;
    private final RepositorioUsuario repositorioUsuario;

    public ControladorPropiedad(ServicioPropiedad servicioPropiedad, RepositorioUsuario repositorioUsuario) {
        this.servicioPropiedad = servicioPropiedad;
        this.repositorioUsuario = repositorioUsuario;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView vistaHome(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        try {
            List<Propiedad> propiedades = servicioPropiedad.listarPropiedades();
            model.put("propiedades", propiedades);
        } catch (Exception e){
            model.put("message", "Ha Ocurrido un Error Inesperado");
        }

        if(usuarioAutenticado != null){
            try{
                Set<Propiedad> favoritos =  repositorioUsuario.listarFavoritos(usuarioAutenticado);
                model.put("favoritos", favoritos);
            }catch(CRUDPropiedadExcepcion e){
                model.put("error", e.getMessage());
            }
        }

        return new ModelAndView("home", model);
    }

    @RequestMapping(path = "/filtro/precio", method = RequestMethod.POST)
    public ModelAndView filtrarPropiedadesPorPrecio(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max,
            HttpSession session
    ) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");
        try {
            List<Propiedad> propiedadesFiltradas = servicioPropiedad.listarPropiedadesPorPrecio(min, max);
            model.put("propiedades", propiedadesFiltradas);
        }catch(CRUDPropiedadExcepcion e){
            model.put("message", e.getMessage());
        }catch (Exception e){
            model.put("message", "Ha Ocurrido un Error Inesperado");
        }

        if(usuarioAutenticado != null){
            try{
                Set<Propiedad> favoritos =  repositorioUsuario.listarFavoritos(usuarioAutenticado);
                model.put("favoritos", favoritos);
            }catch(CRUDPropiedadExcepcion e){
                model.put("error", e.getMessage());
            }
        }

        return new ModelAndView("home", model);
    }


    @RequestMapping(path = "/filtro/ubicacion", method = RequestMethod.POST)
    public ModelAndView filtrarPropiedadesPorUbicacion(@RequestParam("ubicacion") String ubicacion, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        try {
            List<Propiedad> propiedadesFiltradas = servicioPropiedad.listarPropiedadesPorUbicacion(ubicacion);
            model.put("propiedades", propiedadesFiltradas);
        }catch(CRUDPropiedadExcepcion e){
            model.put("message", e.getMessage());
        }
        catch (Exception e){
            model.put("message", "Ha Ocurrido un Error Inesperado");
        }

        if(usuarioAutenticado != null){
            try{
                Set<Propiedad> favoritos =  repositorioUsuario.listarFavoritos(usuarioAutenticado);
                model.put("favoritos", favoritos);
            }catch(CRUDPropiedadExcepcion e){
                model.put("error", e.getMessage());
            }
        }

        return new ModelAndView("home", model);
    }


    @RequestMapping(path = "/agregar-propiedad", method = RequestMethod.GET)
    public ModelAndView vistaAgregarPropiedad() {
        ModelMap model = new ModelMap();
        model.put("propiedad", new Propiedad());
        return new ModelAndView("nuevaPropiedad", model);
    }


    @RequestMapping(path = "/agregar-propiedad", method = RequestMethod.POST)
    public ModelAndView agregarPropiedad(
            @ModelAttribute("propiedad") Propiedad propiedad ,
            @RequestParam("imagen") MultipartFile imagen
    ){
        ModelMap model = new ModelMap();

        try{
            servicioPropiedad.agregarPropiedad(propiedad, imagen);
        }catch(CRUDPropiedadExcepcion | IOException e){
            model.put("error", e.getMessage());
            return new ModelAndView("nuevaPropiedad", model);
        }

        model.put("success", "La Propiedad ha sido agregada con exito!");
        return new ModelAndView("nuevaPropiedad", model);
    }


    @GetMapping("/propiedad/{id}")
    public ModelAndView verPropiedad(@PathVariable Long id) {
        ModelMap model = new ModelMap();

        try {
            Propiedad propiedad = servicioPropiedad.buscarPropiedad(id);
            model.put("messageSuccess", "Detalles de la Propiedad.");
            model.put("propiedad", propiedad);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("messageError", e.getMessage());
            model.put("propiedad", null);
            return new ModelAndView("propiedad", model);
        } catch (Exception e) {
            model.put("messageError", "Error al encontrar la propiedad seleccionada.");
            model.put("propiedad", null);
            return new ModelAndView("propiedad", model);
        }

        return new ModelAndView("propiedad", model);
    }

}


