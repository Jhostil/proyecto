package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.repositorios.DetalleTestRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleTestServicioImpl implements DetalleTestServicio{

    private final DetalleTestRepo detalleTestRepo;


    public DetalleTestServicioImpl (DetalleTestRepo detalleTestRepo){

        this.detalleTestRepo = detalleTestRepo;
    }

    @Override
    public List<DetalleTest> obtenerDetallesTest(String codigoTest) throws Exception {
        return detalleTestRepo.obtenerDetallesTest(codigoTest);
    }

    @Override
    public void guardarDetalle(DetalleTest detalleTest) throws Exception {
        detalleTestRepo.save(detalleTest);
    }

    @Override
    public List<DetalleTest> obtenerDetallesTestPresentados(String codigoTest, String idUsuario) throws Exception {
        return detalleTestRepo.obtenerDetallesTestsPresentados(codigoTest, idUsuario);
    }


}
