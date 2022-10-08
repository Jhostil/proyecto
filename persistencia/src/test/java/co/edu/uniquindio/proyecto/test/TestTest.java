package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestTest {

    @Autowired
    private TestRepo testRepo;

    @Test
    public void crearTest() {
        co.edu.uniquindio.proyecto.entidades.Test test = new co.edu.uniquindio.proyecto.entidades.Test();
        test.setFechaTest(LocalDate.of(2022, 01, 16));

        co.edu.uniquindio.proyecto.entidades.Test guardado = testRepo.save(test);

        Assertions.assertNotNull(guardado);
    }

    @Test
    public void eliminarTest() {
        co.edu.uniquindio.proyecto.entidades.Test test = new co.edu.uniquindio.proyecto.entidades.Test();
        test.setFechaTest(LocalDate.of(2022, 01, 16));

        co.edu.uniquindio.proyecto.entidades.Test guardado = testRepo.save(test);

        String id = guardado.getId();

        testRepo.delete(guardado);

        co.edu.uniquindio.proyecto.entidades.Test buscado = testRepo.findById(id).orElse(null);

        Assertions.assertNull(buscado);
    }

    @Test
    public void modificarTest()
    {
        co.edu.uniquindio.proyecto.entidades.Test test = new co.edu.uniquindio.proyecto.entidades.Test();
        test.setFechaTest(LocalDate.of(2022, 01, 16));

        co.edu.uniquindio.proyecto.entidades.Test guardado = testRepo.save(test);

        guardado.setFechaTest(LocalDate.of(2023,02,16));

        guardado = testRepo.save(guardado);

        Assertions.assertEquals(LocalDate.of(2023,02,16), guardado.getFechaTest());
    }

    @Test
    public void listarTest()
    {
        co.edu.uniquindio.proyecto.entidades.Test test = new co.edu.uniquindio.proyecto.entidades.Test();
        test.setFechaTest(LocalDate.of(2022, 01, 16));
        testRepo.save(test);

        test = new co.edu.uniquindio.proyecto.entidades.Test();
        test.setFechaTest(LocalDate.of(2024, 11, 25));
        testRepo.save(test);

        List<co.edu.uniquindio.proyecto.entidades.Test> tests = testRepo.findAll();

        System.out.println(tests);


    }

}