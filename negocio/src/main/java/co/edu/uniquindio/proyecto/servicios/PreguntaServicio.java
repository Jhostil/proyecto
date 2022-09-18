package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.*;

import java.util.ArrayList;
import java.util.List;

public interface PreguntaServicio {


    Pregunta guardarPregunta (Pregunta p) throws Exception;

    List<TipoPregunta> listarTiposPregunta();

    List<Pregunta> listarPreguntas() ;

    Pregunta obtenerPregunta(Integer codigo) throws Exception;

    Test generarTest(Profesor profesor, ArrayList<PreguntaTest> preguntaTests) throws Exception;

}
