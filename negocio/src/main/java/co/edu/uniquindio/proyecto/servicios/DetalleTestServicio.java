package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DetalleTestServicio {


    List<DetalleTest> obtenerDetallesTest(String codigoTest) throws ExecutionException, InterruptedException;

    void guardarDetalle (DetalleTest detalleTest) ;

    List<DetalleTest> obtenerDetallesTestPresentados(String codigoTest, String idUsuario) throws ExecutionException, InterruptedException;
}
