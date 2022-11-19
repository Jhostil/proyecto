package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.TestClase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ClaseServicio {

    Clase crearClase(String nombre, Profesor profesor) throws InterruptedException, ExecutionException;
    Clase obtenerClase(String codigo) throws InterruptedException, ExecutionException;
    List<TestClase> obtenerTestsActivosClase(String codigoClase) throws InterruptedException, ExecutionException;
    List<TestClase> obtenerTestsProfesor(String codigoClase) throws InterruptedException, ExecutionException;
    List<Clase> obtenerClasesSeleccionadas(Profesor profesorSesion, String[] nombreClasesTest) throws InterruptedException, ExecutionException;
}
