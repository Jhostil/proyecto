package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleTestRepo extends JpaRepository<DetalleTest, Integer> {


    /**
     * Método que devuelve los detalleTest asociados a un Test a partir de una consulta a la base de datos
     * @param idTest Identificador del Test
     * @return Retorna una lista con los detalleTest asociados al código del test ingresado
     */
    @Query("select d from DetalleTest d where d.test.id = :idTest and d.usuario = null")
    List<DetalleTest> obtenerDetallesTest(String idTest);

    /**
     * Método que sirve para obtener un usuario asociado a un Test a partir de una consulta a la base de datos
     * @param idUsuario Identificador del usuario
     * @param idTest Identificador del Test
     * @return Retorna un objeto de tipo Usuario
     */
    @Query("select d.usuario from DetalleTest d where d.usuario.id = :idUsuario and d.test.id = :idTest")
    Usuario obtenerUsuario (String idUsuario, String idTest);

    /**
     * Método que devuelve los detalleTest presentados por un usuario en particular
     * @param idTest Identificador del test
     * @param idUsuario Identificador del usuario
     * @return Retorna una lista con los detalleTest que ha presentado el usuario
     */
    @Query("select d from DetalleTest d where d.test.id = :idTest and d.usuario.id = :idUsuario")
    List<DetalleTest> obtenerDetallesTestsPresentados(String idTest, String idUsuario);

}
