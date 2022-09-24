package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;

public interface TestServicio {


    boolean validarCodigo (String codigo) throws Exception;

    Test iniciarTest(String codigo, Usuario usuario) throws Exception;


}
