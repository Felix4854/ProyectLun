package pe.isil.luna_2618.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.isil.luna_2618.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{

    Optional<Usuario> findByEmail(String email);//SELECT * FROM USUARIO WHERE EMAIL = ?

    boolean existsByEmail(String email);//RETURN EXISTS(SELECT * FROM USUARIO WHERE EMAIL=?)

    Page<Usuario> findByNombresContaining(String nombres, Pageable pageable);
}
