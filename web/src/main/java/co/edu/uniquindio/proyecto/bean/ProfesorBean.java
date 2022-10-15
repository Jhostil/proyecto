package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
public class ProfesorBean implements Serializable {

    @Getter @Setter
    private Profesor profesor;

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Getter @Setter
    private List<Pregunta> preguntas;

    @Getter @Setter
    private Test test;


    @PostConstruct
    public void inicializar ()
    {
        profesor = new Profesor();
        preguntas = preguntaServicio.listarPreguntas();
        this.test = new Test();
    }

    /*public void agregarPregunta (){

        if (test.getDetalleTestList().size() <= 6)
        {


        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Solo se pueden configurar 6 preguntas por test");
            FacesContext.getCurrentInstance().addMessage("msj-bean", msg);
        }
    }*/

}
