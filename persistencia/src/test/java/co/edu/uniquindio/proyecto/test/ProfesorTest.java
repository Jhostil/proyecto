package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.ProfesorRepo;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfesorTest {

    @Autowired
    private ProfesorRepo profesorRepo;

    @Test
    public void registrarProfesorTest() {
        Profesor profesor = new Profesor();
        profesor.setId("111");
        profesor.setNombre("Juanita");
        profesor.setEmail("juana@mail.com");
//Guardamos el registro
        Profesor guardado = profesorRepo.save(profesor);
//Comprobamos que si haya quedado
        Assertions.assertNotNull(guardado);
    }

    @Test
    public void eliminarProfesorTest() {
        Profesor profesor = new Profesor();
        profesor.setId("111");
        profesor.setNombre("Juanita");
        profesor.setEmail("juana@mail.com");
//Primero lo guardamos
        Profesor registrado = profesorRepo.save(profesor);
//Luego lo eliminamos
        profesorRepo.delete(registrado);
//Por último, verificamos que si haya quedado borrado
        Profesor buscado = profesorRepo.findById("111").orElse(null);
        Assertions.assertNull(buscado);
    }

    @Test
    public void actualizarProfesorTest() {
        Profesor profesor = new Profesor();
        profesor.setId("111");
        profesor.setNombre("Juanita");
        profesor.setEmail("juana@mail.com");
//Primero lo guardamos
        Profesor registrado = profesorRepo.save(profesor);
//Modificamos el nombre
        registrado.setNombre("Juanita Lopez");
//Con save guardamos el registro modificado
        profesorRepo.save(registrado);
//Por último, verificamos que si haya quedado actualizado
        Profesor buscado = profesorRepo.findById("111").orElse(null);
        Assertions.assertEquals("Juanita Lopez", buscado.getNombre());
    }


    @Test
    public void listarUsuariosTest(){

        Profesor profesor = new Profesor();
        profesor.setId("111");
        profesor.setNombre("Juanita");
        profesor.setApellido("Fernandez");
        profesor.setPassword("juanita");
        profesor.setEmail("juana@mail.com");
        profesor.setFechaNacimiento(LocalDate.of(1989,01,13));

        profesorRepo.save(profesor);

        profesor = new Profesor();
        profesor.setId("112");
        profesor.setNombre("Predro");
        profesor.setApellido("Gomez");
        profesor.setPassword("pedrito");
        profesor.setEmail("pedro@mail.com");
        profesor.setFechaNacimiento(LocalDate.of(1990,12,24));

        profesorRepo.save(profesor);

        List<Profesor> lista = profesorRepo.findAll();
        System.out.println(lista);
    }


}
