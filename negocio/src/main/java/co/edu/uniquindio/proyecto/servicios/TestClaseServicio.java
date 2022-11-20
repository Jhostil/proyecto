package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TestClase;

import java.util.concurrent.ExecutionException;

public interface TestClaseServicio {

    void habilitarTest(TestClase testClase) throws ExecutionException, InterruptedException ;
    void deshabilitarTest(TestClase testClase) throws ExecutionException, InterruptedException ;
}
