package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleTestRepo extends JpaRepository<DetalleTest, Integer> {


    @Query("select d from DetalleTest d where d.test.id = :idTest and d.usuario = null")
    List<DetalleTest> obtenerDetallesTest(String idTest);
    @Query("select d.usuario from DetalleTest d where d.usuario.id = :idUsuario and d.test.id = :idTest")
    Usuario obtenerUsuario (String idUsuario, String idTest);

    @Query("select d from DetalleTest d where d.test.id = :idTest and d.usuario.id = :idUsuario")
    List<DetalleTest> obtenerDetallesTestsPresentados(String idTest, String idUsuario);

}
