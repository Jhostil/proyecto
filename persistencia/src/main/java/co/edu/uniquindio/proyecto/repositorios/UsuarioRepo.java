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

    /**
     * Método que busca un objeto de tipo Usuario dado su id a partir de una consulta a la base de datos
     * @param codigo Identificador del usuario a buscar
     * @return Retorna el Usuario asociado al identificador ingresado
     */
    @Query("select u from Usuario u where u.id = :codigo")
    Usuario buscar(String codigo);

    /**
     * Método que sirve para listar los test realizados por un usuario específico a partir de una consulta a la base de datos
     * @param id Identificador del usuario
     * @return Retorna una lista con los test realizados por el usuario
     */
    @Query("select t from Usuario u join Test t where  u.id = :id" )
    List<Test> listarTestRealizados(String id);
}
