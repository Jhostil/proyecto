package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;

import java.util.List;

public interface DetalleTestServicio {


    List<DetalleTest> obtenerDetallesTest(String codigoTest) throws Exception;

    void guardarDetalle (DetalleTest detalleTest) throws Exception;

    List<DetalleTest> obtenerDetallesTestPresentados(String codigoTest, String idUsuario) throws Exception;
}
