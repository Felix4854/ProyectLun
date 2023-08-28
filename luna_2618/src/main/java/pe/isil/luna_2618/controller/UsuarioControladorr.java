package pe.isil.luna_2618.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class UsuarioControladorr {

    @GetMapping("mis-cursos")
    public String misCursos(){
        return "mis-cursos";
    }
}
