package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;

public interface TipoPreguntaServicio {

    TipoPregunta obtenerTipoPregunta(Integer id) throws Exception;

    TipoPregunta obtenerTipoPorNombre(String nombre) throws Exception;
    boolean existenTipos();

    TipoPregunta registrarTipo (TipoPregunta tipoPregunta) throws Exception;
}
