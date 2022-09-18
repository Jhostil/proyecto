package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;

public interface ProfesorServicio {

    Profesor obtenerProfesor (String codigo ) throws Exception;

    Profesor iniciarSesion(String username, String password) throws Exception;

    Profesor registrarProfesor(Profesor p) throws Exception;
}
