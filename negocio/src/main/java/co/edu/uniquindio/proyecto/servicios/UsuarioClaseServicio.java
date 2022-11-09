package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.entidades.UsuarioClase;

import java.util.List;

public interface UsuarioClaseServicio {

    UsuarioClase registrarClase (String codigoClase, Usuario usuario) throws Exception;

    List<Usuario> obtenerAlumnos (String codigoClase) throws Exception;
}
