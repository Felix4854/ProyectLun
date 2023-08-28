package pe.isil.luna_2618.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data //constructor metodos:set, get
@Entity //homologracion o relacion con tabla de la bd
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinscripcion")
    private Integer id;

    private LocalDateTime fechaInscripcion;

    //Relaciones
    // Inscripcion a usuario (Muchos a uno): Muchas inscripcion tiene un solo usuario
    @ManyToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    private Usuario usuario;

    // Inscripcion a Curso (Muchos a uno): Muchas inscripciones tiene un solo curso
    @ManyToOne
    @JoinColumn(name = "idcurso", referencedColumnName = "idcurso")
    private Curso curso;
}
