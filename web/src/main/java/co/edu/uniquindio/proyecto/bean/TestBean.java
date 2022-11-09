package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.servicios.DetalleTestServicio;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.TestClaseServicio;
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

    private static final String CONSTANTE = "responderTest.xhtml?faces-redirect=true";
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

    @Getter @Setter
    private TestClase testClaseSeleccionado;

    @Autowired
    private TestClaseServicio testClaseServicio;
    private static final String RUTAIMAGENES = "src/main/resources/META-INF/resources/uploads/";
    private static final String CONSTANTALERTA = "Alerta";
    private static final String CONSTANTGROWLCODTEST = "codigo_test";

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

    /**
     * Método que obtiene la altura de una imágen
     * @param id Identificador de la pregunta a la cual pertenece la imágen
     * @return Retorna un valor entero que representa la altura de la imágen
     */
    public int getHeight(int id) throws IOException {
        if (id == -1) {
            BufferedImage image = ImageIO.read(new File(RUTAIMAGENES+detalleTestList.get(indiceDetalleTestActual).getPregunta().getPregunta()));
            return image.getHeight();
        }
        BufferedImage image = ImageIO.read(new File(RUTAIMAGENES+respuestas.get(id)));
        return image.getHeight();
    }

    /**
     * Método que obtiene el ancho de una impagen
     * @param id Identificador de la pregunta a la cual pertenece la imágen
     * @return Retorna un valor entero que representa la altura de la imágen
     */
    public int getWidth(int id) throws IOException {
        if (id == -1) {
            BufferedImage image = ImageIO.read(new File(RUTAIMAGENES+detalleTestList.get(indiceDetalleTestActual).getPregunta().getPregunta()));
            return image.getWidth();
        }
        BufferedImage image = ImageIO.read(new File(RUTAIMAGENES+respuestas.get(id)));
        return image.getWidth();
    }

    /**
     * Método controlador que permite validar si existe un objeto de tipo Test asociado a un código
     * @param usuario Usuario que desea realizar el test
     * @return Retorna una cadena con la redirección de página
     */
    public String validarCodigo(Usuario usuario) {

        if (testClaseSeleccionado != null) {
            codigo = testClaseSeleccionado.getTest().getId();
        }

        if (codigo.equals("")) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "Ingrese un código");
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODTEST, fm);
            return "";
        }
        try {
            String valido = "";
            try {
                valido = testServicio.validarCodigo(this.codigo, usuario.getId());
            } catch (Exception e)
            {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODTEST, fm);
            }
            if (valido.equals("valido")) {
                iniciarTest(usuario);
                return CONSTANTE;
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
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODTEST, fm);

        }
        return "";
    }

    /**
     * Método controlador que permite iniciar el proceso para responder un Test
     * @param usuario Usuario que desea reponder el Test
     */
    public void iniciarTest(Usuario usuario) throws Exception {

        detalleTestList = testServicio.iniciarTest(codigo, usuario);
        obtenerRespuestas();
        this.testenproceso = true;
    }

    /**
     * Método que permite obtener las respuestas de una pregunta desordenándolas
     */
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
     * Método que permite marcar la respuesta seleccionada de una pregunta
     * Si es la última pregunta del test, se le notifica al usuario cuanto puntaje sacó.
     * @return Retorna una cadena con la redirección de página
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
            return CONSTANTE;

        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "Debe seleccionar una respuesta");
            FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
        }
        return "";
    }

    /**
     * Método que permite obtener la siguiente pregunta del test y redireccionar la página para que cargue la nueva pregunta
     * @return Retorna una cadena con la redirección de página
     */
    public String continuarTest() throws Exception{

        indiceDetalleTestActual++;
        esCorrecta = false;
        enRevision = false;
        respSeleccionada = "";

        if (indiceDetalleTestActual == 6) {
            return cerrarTest();
        }
        obtenerRespuestas();
        return CONSTANTE;
    }

    /**
     * Método que permite invalidar el proceso de reponder un test cuando este haya sido repondido completamente
     * @return Retorna una cadena con la redirección de página
     */
    public String cerrarTest() {
        this.test = null;
        this.detalleTestList = new ArrayList<>();
        this.testenproceso = false;
        this.codigo = "";
        this.respSeleccionada="";
        this.indiceDetalleTestActual = 0;
        calificacion = 0;
        calificacionFinal = "";

        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * Método que permite activar un Quiz
     */
    public void habilitarTest () throws Exception {
        if (testClaseSeleccionado.isActivo())
        {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "El Quiz ya está habilitado");
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODTEST, fm);
        } else {
            testClaseServicio.habilitarTest(testClaseSeleccionado);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quiz habilitado con éxito");
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODTEST, fm);
        }

    }

    /**
     * Método que permite desactivar un Quiz
     */
    public void deshabilitarTest () throws Exception {
        if (!testClaseSeleccionado.isActivo())
        {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "El Quiz ya está deshabilitado");
            FacesContext.getCurrentInstance().addMessage("growl", fm);
        } else {
            testClaseServicio.deshabilitarTest(testClaseSeleccionado);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Quiz deshabilitado con éxito");
            FacesContext.getCurrentInstance().addMessage("growl", fm);
        }
    }

}



