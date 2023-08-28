package pe.isil.luna_2618.controller.usu;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.luna_2618.model.Usuario;
import pe.isil.luna_2618.repository.UsuarioRepository;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Controller
@RequestMapping("/usu/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("")
    String index(Model model, @PageableDefault(size = 5, sort = "nombres") Pageable pageable,
                 @RequestParam(required = false) String nombres){
        Page<Usuario> usuarios;
        if(nombres != null && !nombres.trim().isEmpty()){
            usuarios = usuarioRepository.findByNombresContaining(nombres, pageable);
        }else{
            usuarios = usuarioRepository.findAll(pageable);
        }
        model.addAttribute("usuarios", usuarios);
        return "usu/usuarios/index";
    }

    @GetMapping("/nuevo")
    String nuevo(Model model){
        model.addAttribute("usuario", new Usuario());
        return "usu/usuarios/nuevo";
    }

    @PostMapping("/nuevo")
    String crear(@Validated Usuario usuario, BindingResult br, Model model, RedirectAttributes ra){

        //Validar la unicidad o unico del correo electronico
        String email = usuario.getEmail();
        boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

        if (usuarioYaExiste){
            br.rejectValue("email","EmailAlreadyExists");
        }
        //Validar que las contraseñas conincidadn
        if(!usuario.getPassword1().equals(usuario.getPassword2())){
            br.rejectValue("password1","PasswordNotEquals");
        }

        usuario.setNombres(usuario.getNombres());
        usuario.setApellidos(usuario.getApellidos());
        usuario.setEmail(usuario.getEmail());
        usuario.setPassword(usuario.getPassword1());
        usuario.setRol(Usuario.Rol.ESTUDIANTE);
        usuarioRepository.save(usuario);

        ra.addFlashAttribute("msgExito", "El Usuario fue creado exitosamente");
        return "redirect:/usu/usuarios/";
    }

    @GetMapping("/editar/{id}")
    String editar(@PathVariable Integer id, Model model){
        Usuario usuario = usuarioRepository.getById(id);

        model.addAttribute("usuario", usuario);
        return "usu/usuarios/editar";
    }
    @PostMapping("/editar/{id}")
    String editarUsuario(@PathVariable Integer id, @Valid Usuario usuario,
                         BindingResult br, Model model, RedirectAttributes ra){
        Usuario usuarioExistente = usuarioRepository.getById(id);
        usuarioExistente.setNombres(usuario.getNombres());
        usuarioExistente.setApellidos(usuario.getApellidos());
        usuarioExistente.setEmail(usuario.getEmail());

        if (!Objects.equals(usuario.getPassword1(), usuario.getPassword2())) {
            br.rejectValue("password1", "PasswordNotEquals", "Las contraseñas no coinciden");
            model.addAttribute("usuario", usuario);
            return "usu/usuarios/editar";
        }
        usuarioExistente.setPassword(usuario.getPassword1());
        usuarioRepository.save(usuarioExistente);
        ra.addFlashAttribute("msgExito", "El Usuario fue actualizado exitosamente");
        return "redirect:/usu/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra){
        Usuario usuario = usuarioRepository.getById(id);
        usuarioRepository.delete(usuario);

        ra.addFlashAttribute("msgExito", "El usuario ha sido eliminado");
        return "redirect:/usu/usuarios";
    }


}
