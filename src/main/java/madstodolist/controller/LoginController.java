package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.LoginData;
import madstodolist.dto.RegistroData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "formLogin";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, HttpSession session) {

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.geteMail(), loginData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.geteMail());

            if(usuario.isBloqueado()) {
                model.addAttribute("error", "Usuario bloqueado. Contacta con el administrador.");
                return "formLogin";  // mostrar el formulario con el error
            }
            if (usuario.isAdministrador()){
                managerUserSession.logearUsuario(usuario.getId());
                return "redirect:/registrados";
            }

            managerUserSession.logearUsuario(usuario.getId());
            return "redirect:/usuarios/" + usuario.getId() + "/tareas";
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "formLogin";
        }
        return "formLogin";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        boolean existeAdmin = usuarioService.existeAdministrador();
        model.addAttribute("registroData", new RegistroData());
        model.addAttribute("existeAdmin", existeAdmin);  // <- aquí
        return "formRegistro";
    }

    @PostMapping("/registro")
    public String registroSubmit(@Valid RegistroData registroData, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "formRegistro";
        }

        if (usuarioService.findByEmail(registroData.getEmail()) != null) {
            model.addAttribute("registroData", registroData);
            model.addAttribute("error", "El usuario " + registroData.getEmail() + " ya existe");
            return "formRegistro";
        }

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail(registroData.getEmail());
        usuario.setPassword(registroData.getPassword());
        usuario.setFechaNacimiento(registroData.getFechaNacimiento());
        usuario.setNombre(registroData.getNombre());

        // Este es el cambio clave, pasamos el valor administrador:
        usuario.setAdministrador(registroData.getAdministrador() != null && registroData.getAdministrador());

        usuarioService.registrar(usuario);

        // Si es admin, redirige a la lista de usuarios, sino a login
        if (Boolean.TRUE.equals(usuario.isAdministrador())) {
            LoginData loginData = new LoginData();
            loginData.seteMail(registroData.getEmail());
            loginData.setPassword(registroData.getPassword());
            loginSubmit(loginData,model,null);
            return "redirect:/registrados";
        } else {
            return "redirect:/login";
        }
    }

   @GetMapping("/logout")
   public String logout(HttpSession session) {
        managerUserSession.logout();
        return "redirect:/login";
   }
}
