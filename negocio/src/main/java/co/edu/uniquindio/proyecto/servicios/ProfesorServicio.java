package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;

import java.util.List;

public interface ProfesorServicio {

    Profesor obtenerProfesor (String codigo ) throws Exception;

    Profesor iniciarSesion(String username, String password) throws Exception;

    Profesor registrarProfesor(Profesor p) throws Exception;

    List<Clase> obtenerClases (Profesor p) throws Exception;
}
