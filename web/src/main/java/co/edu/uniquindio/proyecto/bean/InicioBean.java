package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
public class InicioBean implements Serializable {

     private String iniciarTest ()
    {
        return "/usuario/test.xhtml?faces-redirect=true";
    }

    public String irADetalle(String id){

        return "/profesor/detallePregunta.xhtml?faces-redirect=true&amp;pregunta=" + id;
    }

}
