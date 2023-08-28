package pe.isil.luna_2618.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data //constructor metodos:set, get
@Entity //homologracion o relacion con tabla de la bd
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private Integer id;

    @NotBlank
    private String titulo;

    @Size(max = 500)
    private String descripcion;

    private String rutaImagen; //ruta_imagen

    @NotNull
    @Min(1)
    @Max(1000)
    private Float precio;

    private LocalDateTime fechaCreacion; //fecha_creacion

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @Transient
    private MultipartFile imagen;

    //callbacks de ciclo de vida entidad
    @PrePersist
    void prePersistefechaCreacion(){
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdateFechaActualizacion(){
        fechaActualizacion = LocalDateTime.now();
    }
}
