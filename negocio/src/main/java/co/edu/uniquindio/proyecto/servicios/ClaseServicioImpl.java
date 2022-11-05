package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.repositorios.ClaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaseServicioImpl implements ClaseServicio{

    private final ClaseRepo claseRepo;

    public ClaseServicioImpl(ClaseRepo claseRepo) {
        this.claseRepo = claseRepo;
    }
}
