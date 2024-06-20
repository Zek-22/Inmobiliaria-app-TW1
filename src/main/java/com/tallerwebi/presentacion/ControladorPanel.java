package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Propiedad;
import com.tallerwebi.dominio.ServicioPropiedad;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CRUDPropiedadExcepcion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorPanel {

    private final ServicioPropiedad servicioPropiedad;
    private final ServicioUsuario servicioUsuario;

    public ControladorPanel(ServicioPropiedad servicioPropiedad, ServicioUsuario servicioUsuario) {
        this.servicioPropiedad = servicioPropiedad;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/panel-admin/propiedades", method = RequestMethod.GET)
    public ModelAndView panelAdminPropiedades(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            List<Propiedad> propiedadesPendientes = servicioPropiedad.listarPropiedadesPendientes();
            List<Propiedad> propiedadesAceptadas = servicioPropiedad.listarPropiedadesAceptadas();
            model.put("propiedadesPendientes", propiedadesPendientes);
            model.put("propiedadesAceptadas", propiedadesAceptadas);
        } catch (Exception e) {
            model.put("message", "Ha ocurrido un error inesperado");
        }

        return new ModelAndView("panelAdminPropiedades", model);
    }

    @RequestMapping(path = "/panel-admin/aceptar-propiedad", method = RequestMethod.POST)
    public ModelAndView aceptarPropiedad(@RequestParam("id") Long propiedadId, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            this.servicioPropiedad.aceptarPropiedad(propiedadId);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/panel-admin/propiedades", model);
        }

        return new ModelAndView("redirect:/panel-admin/propiedades");
    }


    @RequestMapping(path = "/panel-admin/rechazar-propiedad", method = RequestMethod.POST)
    public ModelAndView rechazarPropiedad(@RequestParam("id") Long propiedadId, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            this.servicioPropiedad.rechazarPropiedad(propiedadId);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/panel-admin/propiedades", model);
        }
        return new ModelAndView("redirect:/panel-admin/propiedades");
    }


    @RequestMapping(path = "/panel-admin/modificar-propiedad", method = RequestMethod.POST)
    public ModelAndView modificarPropiedad(@ModelAttribute("propiedad") Propiedad propiedad, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            this.servicioPropiedad.modificarPropiedad(propiedad);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/panel-admin/propiedades", model);
        }
        return new ModelAndView("redirect:/panel-admin/propiedades");
    }


    @RequestMapping(path = "/panel-admin/modificar-propiedad/{id}", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioModificarPropiedad(@PathVariable("id") Long id, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        Propiedad propiedad = this.servicioPropiedad.buscarPropiedad(id);
        if (propiedad != null) {
            model.put("propiedad", propiedad);
            return new ModelAndView("modificarPropiedad", model);
        } else {
            model.put("error", "La propiedad no existe.");
            return new ModelAndView("redirect:/panel-admin/propiedades", model);
        }
    }



    @RequestMapping(path = "/panel-admin/usuarios", method = RequestMethod.GET)
    public ModelAndView panelAdminUsuarios(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            List<Usuario> usuariosBloqueados = servicioUsuario.listarUsuariosBloqueados();
            List<Usuario> usuariosDesbloqueados = servicioUsuario.listarUsuariosDesbloqueados();
            model.put("usuariosBloqueados", usuariosBloqueados);
            model.put("usuariosDesbloqueados", usuariosDesbloqueados);
        } catch (Exception e) {
            model.put("message", "Ha ocurrido un error inesperado");
        }

        return new ModelAndView("panelAdminUsuarios", model);
    }


    @RequestMapping(path = "/panel-admin/bloquear-usuario", method = RequestMethod.POST)
    public ModelAndView bloquearUsuario(@RequestParam("id") Long usuarioId, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            this.servicioUsuario.bloquearUsuario(usuarioId);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/panel-admin/usuarios", model);
        }

        return new ModelAndView("redirect:/panel-admin/usuarios");
    }


    @RequestMapping(path = "/panel-admin/desbloquear-usuario", method = RequestMethod.POST)
    public ModelAndView desbloquearUsuario(@RequestParam("id") Long usuarioId, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

        if (usuarioAutenticado == null || !usuarioAutenticado.getRol().equals("ADMIN")) {
            return new ModelAndView("redirect:/home");
        }

        try {
            this.servicioUsuario.desbloquearUsuario(usuarioId);
        } catch (CRUDPropiedadExcepcion e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/panel-admin/usuarios", model);
        }
        return new ModelAndView("redirect:/panel-admin/usuarios");
    }

}


