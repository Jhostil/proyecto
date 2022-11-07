package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.TestClase;

import java.util.List;

public interface ClaseServicio {

    Clase crearClase(String nombre, Profesor profesor) throws Exception;
    Clase obtenerClase(String codigo) throws Exception;
    List<TestClase> obtenerTestsActivosClase(String codigoClase) throws Exception;
    List<TestClase> obtenerTestsProfesor(String codigoClase) throws Exception;
    List<Clase> obtenerClasesSeleccionadas(Profesor profesorSesion, String[] nombreClasesTest) throws Exception;
}
