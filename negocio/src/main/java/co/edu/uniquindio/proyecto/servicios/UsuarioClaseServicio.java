package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.entidades.UsuarioClase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UsuarioClaseServicio {

    UsuarioClase registrarClase (String codigoClase, Usuario usuario) throws ExecutionException, InterruptedException ;

    List<Usuario> obtenerAlumnos (String codigoClase) throws ExecutionException, InterruptedException ;
}
