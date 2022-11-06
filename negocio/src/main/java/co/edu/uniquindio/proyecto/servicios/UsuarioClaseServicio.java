package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.entidades.UsuarioClase;

public interface UsuarioClaseServicio {

    UsuarioClase registrarClase (String codigoClase, Usuario usuario) throws Exception;
}
