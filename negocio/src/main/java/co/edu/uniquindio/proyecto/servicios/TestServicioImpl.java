package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import org.springframework.stereotype.Service;

@Service
public class TestServicioImpl implements TestServicio{


    private TestRepo testRepo;

    public TestServicioImpl (TestRepo testRepo)
    {
        this.testRepo = testRepo;
    }


    @Override
    public boolean validarCodigo(String codigo) throws Exception {

        Test test = testRepo.findById(codigo).orElseThrow(() -> new Exception("Código inválido"));

        if (test.getUsuario() != null)
        {
            return true;
        }

        return false;
    }

    @Override
    public Test iniciarTest(String codigo) throws Exception {

        Test test = testRepo.getById(codigo);


        return test;
    }
}
