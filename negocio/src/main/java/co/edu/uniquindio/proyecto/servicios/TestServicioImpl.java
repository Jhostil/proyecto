package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TestServicioImpl implements TestServicio {


    private TestRepo testRepo;

    public TestServicioImpl(TestRepo testRepo) {
        this.testRepo = testRepo;
    }


    @Override
    public boolean validarCodigo(String codigo) throws Exception {

        try {
            Test test = testRepo.findById(codigo).orElse(null);
            if (test != null) {
                try {
                    Usuario u = test.getUsuario();
                    if (test.getUsuario() == null) {
                        System.out.println("verda");
                        return true;
                    }
                } catch (Exception f) {
                    throw new Exception("El test ya fué presentado");
                }
            }
        } catch (Exception e) {
            throw new Exception("Código no válido");
        }

        return false;
    }

    @Override
    public Test iniciarTest(String codigo, Usuario usuario) throws Exception {

        try {
            Test test = testRepo.getById(codigo);
            test.setUsuario(usuario);
            test.setFechaTest(LocalDate.now());
            testRepo.save(test);
            return test;
        }catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
