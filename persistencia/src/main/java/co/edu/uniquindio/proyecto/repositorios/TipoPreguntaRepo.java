package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoPreguntaRepo extends JpaRepository<TipoPregunta, Integer> {

    @Query("select t from TipoPregunta t where t.descripcion = :nombre")
    TipoPregunta buscar(String nombre);

}
