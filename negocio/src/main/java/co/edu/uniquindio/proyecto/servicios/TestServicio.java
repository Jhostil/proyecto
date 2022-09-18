package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Test;

public interface TestServicio {


    boolean validarCodigo (String codigo) throws Exception;

    Test iniciarTest(String codigo) throws Exception;

}
