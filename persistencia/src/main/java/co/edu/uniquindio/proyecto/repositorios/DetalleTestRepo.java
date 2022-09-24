package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleTestRepo extends JpaRepository<DetalleTest, Integer> {

    @Query("select d from DetalleTest d where d.test.id = :idTest")
    List<DetalleTest> obtenerDetallesTest(String idTest);
}
