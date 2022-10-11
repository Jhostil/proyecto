package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.DetalleTestRepo;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestServicioImpl implements TestServicio {


    private TestRepo testRepo;

    private DetalleTestRepo detalleTestRepo;

    public TestServicioImpl(TestRepo testRepo, DetalleTestRepo detalleTestRepo) {
        this.testRepo = testRepo;
        this.detalleTestRepo = detalleTestRepo;
    }


    @Override
    public String validarCodigo(String codigo, String idUsuario) throws Exception {

        try {
            Test test = testRepo.findById(codigo).orElse(null);
            if (test != null) {
                    Usuario u = detalleTestRepo.obtenerUsuario(idUsuario, codigo);
                    if (u == null)
                    {
                        return "valido";
                    } else {
                         return "El test ya fué presentado";
                    }
            }
        } catch (Exception e) {
            return "invalido";
        }
        return "invalido";
    }

    @Override
    public List<DetalleTest> iniciarTest(String codigo, Usuario usuario) throws Exception {

        try {
            Test test = testRepo.getById(codigo);
            List<DetalleTest> detalleTestList = test.getDetalleTestList();
            List<DetalleTest> nuevoTest = new ArrayList<>();

            for (DetalleTest dt: detalleTestList) {

                Pregunta pregunta = dt.getPregunta();
                DetalleTest detalleTest = new DetalleTest();
                detalleTest.setTest(test);
                detalleTest.setPregunta(pregunta);
                detalleTest.setUsuario(usuario);
                detalleTest.setFechaTest(LocalDate.now());
                DetalleTest guardado = detalleTestRepo.save(detalleTest);
                nuevoTest.add(guardado);
            }

            return nuevoTest;
        }catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
