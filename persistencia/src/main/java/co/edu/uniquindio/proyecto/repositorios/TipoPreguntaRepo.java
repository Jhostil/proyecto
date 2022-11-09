package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPreguntaRepo extends JpaRepository<TipoPregunta, Integer> {

    /**
     * Método que busca un objeto de tipo TipoPregunta a partir de un nombre ingresado a partir de una consulta en la
     * base de datos
     * @param nombre Nombre del TipoPregunta a buscar
     * @return Retorna un objeto de tipo TipoPregunta asociado al nombre ingresado por parámetro
     */
    @Query("select t from TipoPregunta t where t.descripcion = :nombre")
    TipoPregunta buscar(String nombre);

}
