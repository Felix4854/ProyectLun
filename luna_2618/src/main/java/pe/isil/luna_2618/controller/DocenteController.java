package pe.isil.luna_2618.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import pe.isil.luna_2618.model.Curso;
import pe.isil.luna_2618.model.Curso;
import pe.isil.luna_2618.model.Docente;
import pe.isil.luna_2618.repository.DocenteRepository;
import pe.isil.luna_2618.service.FileSystemStorageService;

import java.util.List;

@Controller
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    //index
    @GetMapping("")
    public String index(Model model){
        List<Docente> docentes = docenteRepository.findAll();

        model.addAttribute("docentes", docentes);
        return "docentes/index";
    }

    //store
    @GetMapping("/insertar") //http://localhost:8080/docentes/insertar
    String nuevo(Model model){
        model.addAttribute("docente", new Docente());
        return "docentes/nuevo";
        //return: dirige al ruta de nuevo.html
    }

    @PostMapping("/insertar")
    String insertar(@Validated Docente docente, BindingResult bindingResult, Model model, RedirectAttributes ra){
        //genera un error si la imagen es vacía
        if (docente.getImagen().isEmpty()){
            bindingResult.rejectValue("imagen", "MultipartNotEmpty");
        }

        //Si existe errores, retornamos el modelo curso con  los errores a la vista nuevo.html
        if (bindingResult.hasErrors()){
            model.addAttribute("docente", docente);
            return "docentes/nuevo";
        }

        String foto = fileSystemStorageService.store(docente.getImagen());
        docente.setFoto(foto);
        docenteRepository.save(docente);

        ra.addFlashAttribute("msgExito", "El docente has sido creado correctamente");

        return "redirect:/docentes";
    }

    @GetMapping("/editar/{id}")
    String editar(@PathVariable Integer id, Model model){
        Docente docenteFromDB = docenteRepository.getById(id);

        model.addAttribute("docente", docenteFromDB);
        return "docentes/editar";
    }

    @PostMapping("/editar/{id}")
    String actualizar(@PathVariable Integer id, @Validated Docente docente, Model model, BindingResult bindingResult, RedirectAttributes ra){
        Docente docenteFromDB = docenteRepository.getById(id);

        //genera un error si la imagen es vacía
        if (docente.getImagen().isEmpty()){
            bindingResult.rejectValue("imagen", "MultipartNotEmpty");
        }

        //Si existe errores, retornamos el modelo curso con  los errores a la vista editar.html
        if (bindingResult.hasErrors()){
            model.addAttribute("docente", docente);
            return "docentes/editar";
        }

        String foto = fileSystemStorageService.store(docente.getImagen());
        docenteFromDB.setFoto(foto);


        docenteFromDB.setNombre(docente.getNombre());
        docenteFromDB.setApellidos(docente.getApellidos());
        docenteFromDB.setDni(docente.getDni());
        docenteFromDB.setDireccion(docente.getDireccion());
        docenteFromDB.setTelefono(docente.getTelefono());

        docenteRepository.save(docenteFromDB);
        ra.addFlashAttribute("msgExito", "El docente se ha actualizado correctamente");

        return "redirect:/docentes";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra){
        Docente docente = docenteRepository.getById(id); //SELECT * FROM CURSO WHERE ID = ?
        docenteRepository.delete(docente); //DELETE FROM CURSO WHERE CURSO = ?

        //cursoRepository.deleteById(id);//DELETE FROM CURSO WHERE ID=?
        ra.addFlashAttribute("msgExito", "El docente ha sido eliminado");
        return "redirect:/docentes";
    }


}
