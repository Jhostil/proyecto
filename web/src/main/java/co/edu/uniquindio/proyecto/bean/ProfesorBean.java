package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Clase;
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
import java.util.ArrayList;
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
    private Clase claseSeleccionada;

    @Getter @Setter
    private List<Clase> clases;

    @Getter @Setter
    private List<Test> tests;

    @Autowired
    private ProfesorServicio profesorServicio;

    @Getter @Setter
    private String[] clasesSeleccionadas;

    @Getter @Setter
    private List<String> nombreClases;


    @PostConstruct
    public void inicializar () throws ExecutionException, InterruptedException {
        preguntas = preguntaServicio.listarPreguntas();
        this.test = new Test();

        try {
            preguntas = preguntaServicio.listarPreguntas();
            tests = profesorServicio.obtenerProfesor(profesor.getId()).getTestsConfigurados();
            Profesor p = profesorServicio.obtenerProfesor(profesor.getId());
            clases = profesorServicio.obtenerClases(p);
            nombreClases = new ArrayList<>();
            obtenerNombreClases();
            this.test = new Test();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que permite obtener el nombre de las clases y las agrega a un arraylist de tipo String
     */
    private void obtenerNombreClases(){
        for (Clase c: this.clases) {
            nombreClases.add(c.getNombre());
        }
    }

    public String verClase () {
        return "/profesor/clase.xhtml?faces-redirect=true&amp;codigoClase=" + claseSeleccionada.getId();
    }
}
