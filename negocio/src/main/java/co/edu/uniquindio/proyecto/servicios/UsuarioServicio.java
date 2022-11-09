package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UsuarioServicio {

    Usuario registrarUsuario(Usuario u) throws Exception;
    Usuario actualizarUsuario(Usuario u) throws Exception;
    void eliminarUsuario(String cedula) throws Exception;
    List<Usuario> listarUsuarios() throws ExecutionException, InterruptedException;

    Usuario obtenerUsuario (String codigo ) throws Exception;

    Usuario iniciarSesion(String username, String password) throws Exception;

    List<Test> listarTestRealizados(String id) throws ExecutionException, InterruptedException;

    List<UsuarioClase> obtenerClases (Usuario u) throws Exception;
}
