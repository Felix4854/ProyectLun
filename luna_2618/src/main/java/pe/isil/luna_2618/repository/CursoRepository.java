package pe.isil.luna_2618.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.luna_2618.model.Curso;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    //1- Selecciones los primeros 8 cursos por fecha de creacion
    List<Curso> findTop8ByOrderByFechaCreacionDesc();//find o select (si fuera sql), me permite buscar los 8 primeros cursos por feca de creacion


    //2- Seleccione los cursos que contengan el titulo = ? y ademas sea pageable
    Page<Curso> findByTituloContaining(String titulo, Pageable pageable);

}
