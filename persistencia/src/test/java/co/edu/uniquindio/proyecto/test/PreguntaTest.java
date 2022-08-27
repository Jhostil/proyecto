package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.repositorios.PreguntaRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PreguntaTest {

    @Autowired
    private PreguntaRepo preguntaRepo;

    @Test
    public void agregarPregunta()
    {
        Pregunta pregunta = new Pregunta();
        pregunta.setDescripcion("Cuál animal es el más grande?");
        pregunta.setCorrecta("Elefante");
        ArrayList<String> incorrectas = new ArrayList<>(Arrays.asList("Hormiga", "Perro", "Tigre"));
        pregunta.setIncorrecta(incorrectas);

        Pregunta agregada  = preguntaRepo.save(pregunta);

        Assertions.assertNotNull(agregada);
    }
    @Test
    public void eliminarPregunta ()
    {
        Pregunta pregunta = new Pregunta();
        pregunta.setDescripcion("Cuál animal es el más grande?");
        pregunta.setCorrecta("Elefante");
        ArrayList<String> incorrectas = new ArrayList<>(Arrays.asList("Hormiga", "Perro", "Tigre"));
        pregunta.setIncorrecta(incorrectas);

        Pregunta agregada  = preguntaRepo.save(pregunta);
        int id = agregada.getId();
        preguntaRepo.delete(agregada);

        Pregunta eliminada = preguntaRepo.findById(id).orElse(null);

        Assertions.assertNull(eliminada);
    }
    @Test
    public void actualizarPregunta ()
    {
        Pregunta pregunta = new Pregunta();
        pregunta.setDescripcion("Cuál animal es el más grande?");
        pregunta.setCorrecta("Elefante");
        ArrayList<String> incorrectas = new ArrayList<>(Arrays.asList("Hormiga", "Perro", "Tigre"));
        pregunta.setIncorrecta(incorrectas);

        Pregunta agregada  = preguntaRepo.save(pregunta);
        int id = agregada.getId();

        agregada.setCorrecta("Jirafa");

        preguntaRepo.save(agregada);

        Pregunta buscado = preguntaRepo.findById(id).orElse(null);

        Assertions.assertEquals("Jirafa", buscado.getCorrecta());
    }
    @Test
    public void listarPreguntasTest ()
    {
        Pregunta pregunta = new Pregunta();
        pregunta.setDescripcion("Pregunta 1");
        pregunta.setCorrecta("Pre 1");
        preguntaRepo.save(pregunta);

        pregunta = new Pregunta();
        pregunta.setDescripcion("Pregunta 2");
        pregunta.setCorrecta("Pre 2");
        preguntaRepo.save(pregunta);

        List<Pregunta> preguntaList = preguntaRepo.findAll();

        System.out.println(preguntaList);
    }

}
