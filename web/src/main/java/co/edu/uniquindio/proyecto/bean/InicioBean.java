package co.edu.uniquindio.proyecto.bean;

import org.springframework.stereotype.Component;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Component
@ViewScoped
public class InicioBean implements Serializable {

    /**
     * Método que redirecciona a la vista de responder un test
     * @return Retorna una cadena con la redirección de página
     */
     private String iniciarTest ()
    {
        return "/usuario/test.xhtml?faces-redirect=true";
    }

    /**
     * Método que redirecciona a la vista de detalle de una pregunta
     * @param id Identificador de la pregunta a ver
     * @return Retorna una cadena con la redirección de página
     */
    public String irADetalle(String id){
        return "/profesor/detallePregunta.xhtml?faces-redirect=true&amp;pregunta=" + id;
    }

}
