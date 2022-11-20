package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProfesorServicio {

    Profesor obtenerProfesor (String codigo ) throws ExecutionException, InterruptedException;

    Profesor iniciarSesion(String username, String password) throws ExecutionException, InterruptedException;

    Profesor registrarProfesor(Profesor p) throws ExecutionException, InterruptedException;

    List<Clase> obtenerClases (Profesor p) throws ExecutionException, InterruptedException;
}
