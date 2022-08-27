package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioTest {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void registrarUsuarioTest() {
        Usuario u = new Usuario();
        u.setId("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
//Guardamos el registro
        Usuario guardado = usuarioRepo.save(u);
//Comprobamos que si haya quedado
        Assertions.assertNotNull(guardado);
    }

    @Test
    public void eliminarUsuarioTest() {
        Usuario u = new Usuario();
        u.setId("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
//Primero lo guardamos
        Usuario registrado = usuarioRepo.save(u);
//Luego lo eliminamos
        usuarioRepo.delete(registrado);
//Por último, verificamos que si haya quedado borrado
        Usuario buscado = usuarioRepo.findById("111").orElse(null);
        Assertions.assertNull(buscado);
    }

    @Test
    public void actualizarUsuarioTest() {
        Usuario u = new Usuario();
        u.setId("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
//Primero lo guardamos
        Usuario registrado = usuarioRepo.save(u);
//Modificamos el nombre
        registrado.setNombre("Juanita Lopez");
//Con save guardamos el registro modificado
        usuarioRepo.save(registrado);
//Por último, verificamos que si haya quedado actualizado
        Usuario buscado = usuarioRepo.findById("111").orElse(null);
        Assertions.assertEquals("Juanita Lopez", buscado.getNombre());
    }


    @Test
    @Sql("classpath:usuarios.sql" )
    public void listarUsuariosTest(){
        List<Usuario> lista = usuarioRepo.findAll();
        System.out.println(lista);
    }


}
