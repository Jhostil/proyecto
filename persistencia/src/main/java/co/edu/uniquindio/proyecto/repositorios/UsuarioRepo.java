package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail (String email);
    Optional<Usuario> findByEmailAndPassword (String email, String clave);
    Optional<Usuario> findByUsername (String username);
    @Query("select u from Usuario u where u.id = :codigo")
    Usuario buscar(String codigo);
    @Query("select t from Usuario u join Test t where  u.id = :id" )
    List<Test> listarTestRealizados(String id);
}
