package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.ProfesorServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@ViewScoped
public class ProfesorBean implements Serializable {

    @Getter @Setter
    @Value("#{seguridadBean.profesorSesion}")
    private Profesor profesor;

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Getter @Setter
    private List<Pregunta> preguntas;

    @Getter @Setter
    private Test test;

    @Getter @Setter
    private List<Test> tests;

    @Autowired
    private ProfesorServicio profesorServicio;

    @PostConstruct
    public void inicializar () throws ExecutionException, InterruptedException {
        preguntas = preguntaServicio.listarPreguntas();
        this.test = new Test();

        try {
            preguntas = preguntaServicio.listarPreguntas();
            tests = profesorServicio.obtenerProfesor(profesor.getId()).getTestsConfigurados();
            this.test = new Test();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
