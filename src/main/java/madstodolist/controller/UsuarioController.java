package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/registrados")
    public String listarUsuarios(Model model, Principal principal) {
        List<UsuarioData> usuarios = usuarioService.allUsuarios();
        model.addAttribute("usuarios", usuarios);

        UsuarioData usuario = null;
        boolean usuarioLogeado = false;
        if (principal != null) {
            usuario = usuarioService.findByEmail(principal.getName());
            usuarioLogeado = usuario != null;
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioLogeado", usuarioLogeado);

        return "listaUsuarios";
    }

    @GetMapping("/registrados/{id}")
    public String verDetalleUsuario(@PathVariable Long id, Model model, Principal principal) {
        UsuarioData usuarioData = usuarioService.findById(id);
        if (usuarioData == null) {
            return "redirect:/registrados"; // o puedes redirigir a una página de error
        }

        model.addAttribute("usuarioData", usuarioData);

        UsuarioData usuario = null;
        boolean usuarioLogeado = false;
        if (principal != null) {
            usuario = usuarioService.findByEmail(principal.getName());
            usuarioLogeado = usuario != null;
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioLogeado", usuarioLogeado);

        return "UsuarioDetalles";
    }




}
