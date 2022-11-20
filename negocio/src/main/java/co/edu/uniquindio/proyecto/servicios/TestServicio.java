package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Usuario;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TestServicio {

    String validarCodigo (String codigo, String idUsuario) ;
    List<DetalleTest> iniciarTest(String codigo, Usuario usuario) throws ExecutionException, InterruptedException ;



}
