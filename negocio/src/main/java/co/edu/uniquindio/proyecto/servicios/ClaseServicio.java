package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;

public interface ClaseServicio {

    Clase crearClase(String nombre, Profesor profesor) throws Exception;
}
