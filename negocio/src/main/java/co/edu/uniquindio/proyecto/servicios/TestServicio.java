package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Usuario;

import java.util.List;

public interface TestServicio {

    String validarCodigo (String codigo, String idUsuario) throws Exception;
    List<DetalleTest> iniciarTest(String codigo, Usuario usuario) throws Exception;



}
