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
import org.primefaces.model.DialogFrameworkOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Scope("session")
@Component
public class TestBean implements Serializable {

    @Getter @Setter
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

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Getter @Setter
    private String respSeleccionada;

    @Getter @Setter
    private int indiceDetalleTestActual;

    @Getter @Setter
    private ArrayList<String> respuestas;

    @Getter @Setter
    private String descripcionPregunta;

    @Getter @Setter
    private boolean enRevision;

    @Getter @Setter
    private boolean esCorrecta;

    @Getter @Setter
    private String respCorrecta;

    @Getter @Setter
    private int calificacion;

    @Getter @Setter
    private String calificacionFinal;

    @Getter @Setter
    private boolean pregFinal;
    @PostConstruct
    public void inicializar() {
        testenproceso = false;
        codigo = "";
        respSeleccionada = "";
        indiceDetalleTestActual = 0;
        descripcionPregunta = "";
        enRevision = false;
        esCorrecta = false;
        respCorrecta = "";
        calificacion = 0;
        calificacionFinal = "";
        pregFinal = false;

    }

    public int getHeight(int id) throws IOException {
        if (id == -1) {
            BufferedImage image = ImageIO.read(new File("src/main/resources/META-INF/resources/uploads/"+detalleTestList.get(indiceDetalleTestActual).getPregunta().getPregunta()));
            return image.getHeight();
        }
        BufferedImage image = ImageIO.read(new File("src/main/resources/META-INF/resources/uploads/"+respuestas.get(id)));
        return image.getHeight();
    }

    public int getWidth(int id) throws IOException {
        if (id == -1) {
            BufferedImage image = ImageIO.read(new File("src/main/resources/META-INF/resources/uploads/"+detalleTestList.get(indiceDetalleTestActual).getPregunta().getPregunta()));
            return image.getWidth();
        }
        BufferedImage image = ImageIO.read(new File("src/main/resources/META-INF/resources/uploads/"+respuestas.get(id)));
        return image.getWidth();
    }

    public String validarCodigo(Usuario usuario) {
        if (codigo == "") {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "Ingrese un código");
            FacesContext.getCurrentInstance().addMessage("codigo_test", fm);
            return "";
        }
        try {
            String valido = "";
            try {
                valido = testServicio.validarCodigo(this.codigo, usuario.getId());
            } catch (Exception e)
            {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
                FacesContext.getCurrentInstance().addMessage("codigo_test", fm);
            }
            if (valido.equals("valido")) {
                iniciarTest(usuario);
                return "responderTest.xhtml?faces-redirect=true";
            }
            if (valido.equals("invalido")){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message", "Código inválido");
                PrimeFaces.current().dialog().showMessageDynamic(message);
                return "";
            }
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message", valido);
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }
        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("codigo_test", fm);

        }
        return "";
    }

    public void iniciarTest(Usuario usuario) throws Exception {

        detalleTestList = testServicio.iniciarTest(codigo, usuario);
        obtenerRespuestas();
        this.testenproceso = true;
    }

    public void obtenerRespuestas()  throws Exception{
        respuestas = new ArrayList<>();
        Pregunta pregunta = preguntaServicio.obtenerPregunta(detalleTestList.get(indiceDetalleTestActual).getPregunta().getId());
        String respuesta = pregunta.getCorrecta();
        respuestas.add(respuesta);
        descripcionPregunta = pregunta.getDescripcion();

        for (String res : pregunta.getIncorrecta()) {
            respuestas.add(res);
        }
        Collections.shuffle(respuestas); //desordena las respuestas
    }

    /**
     * Metodo que me permite saber que respuesta selecciono un usuario en un Quiz
     * @return respuesta
     */
    public String marcarRespuesta() {

        String respuesta = "";
        try {
            respuesta = respuestas.get(Integer.parseInt(respSeleccionada) - 1);
            DetalleTest detalleTest = detalleTestList.get(indiceDetalleTestActual);

            detalleTest.setRespuesta(respuesta);

            Pregunta pregunta = detalleTest.getPregunta();
            //Obtengo la respuesta correcta para su posterior revisión
            respCorrecta = pregunta.getCorrecta();

            //Valido si la respuesta es correcta
            if (respCorrecta.equals(respuesta)) {
                //De ser correcta la respuesta se califica con 5 y se guarda en el detalle del test
                detalleTest.setCalificacion(5);
                detalleTestServicio.guardarDetalle(detalleTest);
                esCorrecta = true;
                calificacion++;
                if(calificacion == 1)
                {
                    calificacionFinal = "Sacaste " + calificacion + " pregunta buena de 6.";
                } else {
                    calificacionFinal = "Sacaste " + calificacion + " preguntas buenas de 6";
                }
            } else {
                //De ser correcta la respuesta se califica con 0 y se guarda en el detalle del test
                detalleTest.setCalificacion(0);
                detalleTestServicio.guardarDetalle(detalleTest);

            }
            enRevision = true;
            if(indiceDetalleTestActual == 5){
                pregFinal = true;
            }
            //direcciona la usuario a la siguiente pregunta
            return "responderTest.xhtml?faces-redirect=true";

        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "Debe seleccionar una respuesta");
            FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
        }
        return "";
    }

    public String continuarTest() throws Exception{

        indiceDetalleTestActual++;
        esCorrecta = false;
        enRevision = false;
        respSeleccionada = "";

        if (indiceDetalleTestActual == 6) {
            return cerrarTest();
        }
        obtenerRespuestas();
        return "responderTest.xhtml?faces-redirect=true";
    }

    public String cerrarTest() {
        this.test = null;
        this.detalleTestList = new ArrayList<>();
        this.testenproceso = false;
        String aux = codigo;
        this.codigo = "";
        this.respSeleccionada="";
        this.indiceDetalleTestActual = 0;
        calificacion = 0;
        calificacionFinal = "";

        return "/index.xhtml?faces-redirect=true";
        //return "/usuario/testPresentado.xhtml?faces-redirect=true&amp;test=" + aux;
    }
}



