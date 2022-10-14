package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public boolean existenTipos() {
        boolean resultado = true;
        List<TipoPregunta> tipos = tipoPreguntaRepo.findAll();
        if (tipos == null || tipos.size() == 0) {
            resultado = false;
        }
        return resultado;
    }

    @Override
    public TipoPregunta registrarTipo(TipoPregunta tipoPregunta) throws Exception {
        return tipoPreguntaRepo.save(tipoPregunta);
    }
}
