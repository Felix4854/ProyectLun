package pe.isil.luna_2618.controller;

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

import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("")
    String index(Model model){
        //Obtener los primeros 8 cursos
        List<Curso> ultimosCursos = cursoRepository.findTop8ByOrderByFechaCreacionDesc();

        model.addAttribute("ultimosCursos", ultimosCursos);
        return "index";
    }

    @GetMapping("/cursos")
    String getCursos(Model model, @PageableDefault(size = 8, sort = "titulo") Pageable pageable){
        Page<Curso> cursos = cursoRepository.findAll(pageable);
        model.addAttribute("cursos", cursos);
        return "lista-cursos";
    }

    @GetMapping("/cursos/{id}")
    String getCurso(@PathVariable Integer id, Model model){
        Curso curso = cursoRepository.getById(id);
        model.addAttribute("curso",curso);
        return "detalle-curso";
    }


}
