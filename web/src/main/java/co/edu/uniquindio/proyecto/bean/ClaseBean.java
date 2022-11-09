package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.servicios.ClaseServicio;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;

@Component
@ViewScoped
public class ClaseBean implements Serializable {

    @Getter
    @Setter
    @Value("#{seguridadBean.profesorSesion}")
    private Profesor profesor;

    @Getter @Setter
    private String nombreClase;


    @Autowired
    private ClaseServicio claseServicio;

    /**
     * Método que permite a un profesor crear una nueva clase
     */
    public void crearClase (){

        if (nombreClase.isEmpty()) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "Ingrese un nombre");
            FacesContext.getCurrentInstance().addMessage("nombre_clase", fm);
        }
        try {
            Clase clase = claseServicio.crearClase(nombreClase, profesor);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Clase creada con éxito. El código de acceso es: \n" + clase.getId());
            PrimeFaces.current().dialog().showMessageDynamic(message);
        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("nombre_clase", fm);
        }
    }
}
