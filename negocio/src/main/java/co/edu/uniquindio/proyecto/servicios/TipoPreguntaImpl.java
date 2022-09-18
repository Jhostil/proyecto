package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoPreguntaImpl implements TipoPreguntaServicio {

    @Autowired
    private TipoPreguntaRepo tipoPreguntaRepo;

    @Override
    public TipoPregunta obtenerTipoPregunta(Integer id) throws Exception {
        return tipoPreguntaRepo.findById(id).orElseThrow(() -> new Exception("El id no corresponde a ninguna categoría"));
    }

    @Override
    public TipoPregunta obtenerTipoPorNombre(String nombre) throws Exception {
        return tipoPreguntaRepo.buscar(nombre);
    }
}
