package market.secondhandmarket.controladores;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.servicios.UsuarioServicio;
import market.secondhandmarket.upload.StorageService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@Controller
public class LoginController {

    final UsuarioServicio usuarioServicio;
    final AuthenticationManager authenticationManager;
    final StorageService storageService;

    LoginController(UsuarioServicio usuarioServicio, AuthenticationManager authenticationManager, StorageService storageService) {
        this.usuarioServicio = usuarioServicio;
        this.authenticationManager = authenticationManager;
        this.storageService = storageService;   
    }

    @GetMapping("/")
    public String welcome(){
        return "forward:/public/";
    }

    @GetMapping("/auth/login")
    public String login(Model model){
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute Usuario usuario, HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if(!file.isEmpty()){
            String imagen = storageService.store(file);
            usuario.setAvatar(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "serveFile", imagen).build().toUriString());
        }

        String rawPassword = usuario.getPassword();
        Usuario registrado = usuarioServicio.registrar(usuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrado.getEmail(), rawPassword)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return "redirect:/public/index";
    }

}
