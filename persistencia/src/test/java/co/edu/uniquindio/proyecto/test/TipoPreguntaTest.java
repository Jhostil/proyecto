package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TipoPreguntaTest {

    @Autowired
    TipoPreguntaRepo tipoPreguntaRepo;

    @Test
    public void agregarTest()
    {
        TipoPregunta tipoPregunta = new TipoPregunta();
        tipoPregunta.setDescripcion("Laberinto");

        TipoPregunta guardado = tipoPreguntaRepo.save(tipoPregunta);

        Assertions.assertNotNull(guardado);
    }

    @Test
    public void eliminarTest()
    {
        TipoPregunta tipoPregunta = new TipoPregunta();
        tipoPregunta.setDescripcion("Laberinto");

        TipoPregunta guardado = tipoPreguntaRepo.save(tipoPregunta);

        int id = guardado.getId();

        tipoPreguntaRepo.delete(guardado);

        TipoPregunta buscado = tipoPreguntaRepo.findById(id).orElse(null);

        Assertions.assertNull(buscado);
    }

    @Test
    public void modificarTest()
    {
        TipoPregunta tipoPregunta = new TipoPregunta();
        tipoPregunta.setDescripcion("Laberinto");

        TipoPregunta guardado = tipoPreguntaRepo.save(tipoPregunta);

        guardado.setDescripcion("Secuencia logica");

        guardado = tipoPreguntaRepo.save(guardado);

        Assertions.assertEquals("Secuencia logica", guardado.getDescripcion());
    }

    @Test
    public void listarTest()
    {
        TipoPregunta tipoPregunta = new TipoPregunta();
        tipoPregunta.setDescripcion("Laberinto");
        tipoPreguntaRepo.save(tipoPregunta);

        tipoPregunta = new TipoPregunta();
        tipoPregunta.setDescripcion("Secuencia logica");
        tipoPreguntaRepo.save(tipoPregunta);

        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepo.findAll();

        System.out.println(tipoPreguntaList);
    }


}
