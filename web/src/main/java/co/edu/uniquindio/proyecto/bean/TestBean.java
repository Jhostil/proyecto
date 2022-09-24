package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.DetalleTestServicio;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.TestServicio;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Scope("session")
@Component
public class TestBean implements Serializable {

    @Getter
    @Setter
    private Test test;

    @Getter @Setter
    private List<DetalleTest> detalleTestList;
    @Getter @Setter
    public boolean testenproceso;

    @Getter @Setter
    public String codigo;

    @Autowired
    private TestServicio testServicio;

    @Autowired
    private DetalleTestServicio detalleTestServicio;


    @PostConstruct
    public void inicializar(){
        testenproceso = false;
        codigo = "";

    }

    public String validarCodigo (Usuario usuario)
    {
        if (codigo == "")
        {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "Ingrese un código");
            FacesContext.getCurrentInstance().addMessage("codigo_test", fm);
            return "";
        }
        try {
            boolean valido = testServicio.validarCodigo(this.codigo);

            if (valido)
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message", "El test iniciará");
                PrimeFaces.current().dialog().showMessageDynamic(message);
                iniciarTest(usuario);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message", "Código inválido");
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }
            return "";
        }catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("codigo_test", fm);

        }
        return "";
    }

    public void iniciarTest(Usuario usuario) throws Exception
    {
        test = testServicio.iniciarTest(codigo, usuario) ;
        detalleTestList = detalleTestServicio.obtenerDetallesTest(test.getId());
        this.testenproceso = true;
    }

    public void marcarRespuesta(int idDetalle, String respuesta) throws Exception
    {
        for (int i = 0; i < detalleTestList.size(); i++)
        {
            DetalleTest detalleTest = detalleTestList.get(i);
            if (detalleTest.getId() == idDetalle)
            {
                detalleTest.setRespuesta(respuesta);

                Pregunta pregunta = detalleTest.getPregunta();

                if (pregunta.getCorrecta().equals(respuesta))
                {
                    detalleTest.setCalificacion(5);
                } else {
                    detalleTest.setCalificacion(0);
                }
                detalleTestServicio.guardarDetalle(detalleTest);
            }
        }
    }

    public String cerrarTest(){
        this.test = null;
        this.detalleTestList = new ArrayList<>();
        this.testenproceso = false;
        this.codigo = "";

        return "";//Falta retornar a revisión de respuestas
    }


}
