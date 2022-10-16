package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;

import java.util.concurrent.ExecutionException;

public interface TipoPreguntaServicio {

    TipoPregunta obtenerTipoPregunta(Integer id) throws Exception;

    TipoPregunta obtenerTipoPorNombre(String nombre) throws Exception;
    boolean existenTipos() throws ExecutionException, InterruptedException;

    TipoPregunta registrarTipo (TipoPregunta tipoPregunta) throws Exception;
}
