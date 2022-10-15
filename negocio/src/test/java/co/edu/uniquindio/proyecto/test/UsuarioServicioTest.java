package co.edu.uniquindio.proyecto.test;


import co.edu.uniquindio.proyecto.NegocioApplication;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;


@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class UsuarioServicioTest {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Test
    public void registrarTest (){
        Usuario usuario = new Usuario("111", "Juan", "Perez ", "juan@email.com", "juaniquilador", "juan123", LocalDate.of(2001, 03, 12).toString());
        try {
            Usuario respuesta =  usuarioServicio.registrarUsuario(usuario);
            Assertions.assertNotNull(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void eliminarTest(){
        Usuario usuario = new Usuario("111", "Juan", "Perez ", "juan@email.com", "juaniquilador", "juan123", LocalDate.of(2001, 03, 12).toString());
        try {
            usuarioServicio.registrarUsuario(usuario);
            usuarioServicio.eliminarUsuario("111");
            Assertions.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }

    }

    @Test
    public void listarTest() throws ExecutionException, InterruptedException {
        List<Usuario> lista = usuarioServicio.listarUsuarios();
        lista.forEach(System.out::println);
    }

    @Test
    public void actualizarTest ()
    {
        Usuario usuario = new Usuario("111", "Juan", "Perez ", "juan@email.com", "juaniquilador", "juan123", LocalDate.of(2001, 03, 12).toString());
        try {
            usuarioServicio.registrarUsuario(usuario);
            usuario = usuarioServicio.obtenerUsuario("111");

            usuario.setPassword("elpepe123");

            Usuario respuesta =  usuarioServicio.actualizarUsuario(usuario);

            Assertions.assertNotNull(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginTest()
    {
        Usuario usuario = new Usuario("111", "Juan", "Perez ", "juan@email.com", "juaniquilador", "juan123", LocalDate.of(2001, 03, 12).toString());
        try {
            usuarioServicio.registrarUsuario(usuario);
            usuario = usuarioServicio.iniciarSesion("juan@email.com", "juan123");

            Assertions.assertNotNull(usuario);
        } catch (Exception e) {
            Assertions.assertTrue(false,e.getMessage());
        }
    }
}
