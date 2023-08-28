package pe.isil.luna_2618.controller.admin;

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
import pe.isil.luna_2618.model.Curso;
import pe.isil.luna_2618.repository.CursoRepository;
import pe.isil.luna_2618.service.FileSystemStorageService;


@Controller
@RequestMapping("/admin/cursos")
public class AdminCursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @GetMapping("")
    String index(
                 Model model,
                 @PageableDefault(size = 5, sort = "titulo") Pageable pageable,
                 @RequestParam(required = false) String titulo){
        Page<Curso> cursos; //listado de cursoso del tipo page
        if(titulo != null && !titulo.trim().isEmpty()){
            cursos = cursoRepository.findByTituloContaining(titulo, pageable);
        }else {
            cursos = cursoRepository.findAll(pageable);
        }

        model.addAttribute("cursos", cursos);

        return "admin/cursos/index";

    }
    @GetMapping("/nuevo")
    String nuevo(Model model){
        model.addAttribute("curso", new Curso());
        return "admin/cursos/nuevo";
    }

    @PostMapping("/nuevo")
    String crear(@Validated Curso curso, BindingResult bindingResult, Model model, RedirectAttributes ra){
        if(curso.getImagen().isEmpty()){
            bindingResult.rejectValue("imagen", "MultipartNotEmpty");
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("curso", curso);
            return "admin/cursos/nuevo";
        }
        String rutaImagen = fileSystemStorageService.store(curso.getImagen());
        curso.setRutaImagen(rutaImagen);
        cursoRepository.save(curso);

        ra.addFlashAttribute("msgExito", "El curso fue creado exitosamente");
        return "redirect:/admin/cursos";
    }

    @GetMapping("/editar/{id}")
    String editar(@PathVariable Integer id, Model model){
        Curso curso = cursoRepository.getById(id);

        model.addAttribute("curso", curso);
        return "admin/cursos/editar";
    }
    @PostMapping("/editar/{id}")
    String actualizar(@PathVariable Integer id, @Validated Curso curso, Model model, BindingResult bindingResult, RedirectAttributes ra){
        Curso cursoFromDB = cursoRepository.getById(id);

        //genera un error si la imagen es vacía
        if (curso.getImagen().isEmpty()){
            bindingResult.rejectValue("imagen", "MultipartNotEmpty");
        }

        //Si existe errores, retornamos el modelo curso con  los errores a la vista editar.html
        if (bindingResult.hasErrors()){
            model.addAttribute("curso", curso);
            return "admin/cursos/editar";
        }

        String rutaImagen = fileSystemStorageService.store(curso.getImagen());
        cursoFromDB.setRutaImagen(rutaImagen);


        cursoFromDB.setTitulo(curso.getTitulo());
        cursoFromDB.setDescripcion(curso.getDescripcion());
        cursoFromDB.setPrecio(curso.getPrecio());

        cursoRepository.save(cursoFromDB);
        ra.addFlashAttribute("msgExito", "El curso se ha actualizado correctamente");

        return "redirect:/admin/cursos";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra){
        Curso curso = cursoRepository.getById(id); //SELECT * FROM CURSO WHERE ID = ?
        cursoRepository.delete(curso); //DELETE FROM CURSO WHERE CURSO = ?

        //cursoRepository.deleteById(id);//DELETE FROM CURSO WHERE ID=?
        ra.addFlashAttribute("msgExito", "El curso ha sido eliminado");
        return "redirect:/admin/cursos";
    }

}
