package pe.isil.luna_2618;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HolaMundoController {

    @GetMapping("ssa") //http://localhost:8080
    String index(){
        return "index2"; //retorna la vista o el template index.html
    }

}
