package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.servicios.TestServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Scope("session")
@Component
public class TestBean implements Serializable {

    @Getter
    @Setter
    private Test test;
    @Getter @Setter
    public boolean testenproceso;

    @Getter @Setter
    public String codigo;

    @Autowired
    private TestServicio testServicio;

    @PostConstruct
    public void inicializar(){
        testenproceso = false;
        System.out.println(testenproceso);
    }

    public String validarCodigo ()
    {
        /*try {
            boolean valido = testServicio.validarCodigo(this.codigo);

            if (valido)
            {
                testEnProceso = true;
                iniciarTest();
                return "/usuario/test?faces-redirect=true";
            }

            return "";
        }catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("codigo_test", fm);

        }*/

        testenproceso = true;
        return "/usuario/test?faces-redirect=true";
    }

    public void iniciarTest() throws Exception
    {
        test = testServicio.iniciarTest(codigo) ;
    }


}
