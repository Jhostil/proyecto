package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PreguntaServicio {


    Pregunta guardarPregunta (Pregunta p) throws ExecutionException, InterruptedException;

    List<TipoPregunta> listarTiposPregunta() throws ExecutionException, InterruptedException;

    List<Pregunta> listarPreguntas() throws ExecutionException, InterruptedException;

    Pregunta obtenerPregunta(Integer codigo) throws ExecutionException, InterruptedException;

    Test generarTest(List<Clase> clases, Profesor profesor, ArrayList<PreguntaTest> preguntaTests) throws ExecutionException, InterruptedException;

}
