package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UsuarioServicio {

    Usuario registrarUsuario(Usuario u) throws ExecutionException, InterruptedException ;
    Usuario actualizarUsuario(Usuario u) throws ExecutionException, InterruptedException ;
    void eliminarUsuario(String cedula) throws ExecutionException, InterruptedException ;
    List<Usuario> listarUsuarios() throws ExecutionException, InterruptedException;

    Usuario obtenerUsuario (String codigo ) throws ExecutionException, InterruptedException ;

    Usuario iniciarSesion(String username, String password) throws ExecutionException, InterruptedException ;

    List<Test> listarTestRealizados(String id) throws ExecutionException, InterruptedException;

    List<UsuarioClase> obtenerClases (Usuario u) throws ExecutionException, InterruptedException ;
}
