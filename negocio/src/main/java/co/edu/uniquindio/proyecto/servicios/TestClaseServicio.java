package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TestClase;

public interface TestClaseServicio {

    void habilitarTest(TestClase testClase) throws Exception;
    void deshabilitarTest(TestClase testClase) throws Exception;
}
