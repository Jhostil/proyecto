package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.ClaseServicio;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.ProfesorServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import lombok.Getter;
import lombok.Setter;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Scope("session")
@Component
public class SeguridadBean implements Serializable {

    @Getter
    @Setter
    private boolean autenticado;

    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProfesorServicio profesorServicio;

    @Autowired
    private ClaseServicio claseServicio;

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Getter @Setter
    private Usuario usuarioSesion;

    @Getter @Setter
    private Profesor profesorSesion;
    @Getter @Setter
    private boolean profesor;

    @Getter @Setter
    private ArrayList<PreguntaTest> preguntaTests;

    @Getter @Setter
    private double subTotal;

    @Getter @Setter
    private List<Test> testRealizados;
    private static final String CONSTANTALERTA = "Alerta";
    private static final String CONSTANTMSJBEAN = "msj-bean";

    private static final String CONSTANTREDIRECTINDEX = "/index.xhtml?faces-redirect=true";

    @PostConstruct
    public void inicializar(){

        this.preguntaTests = new ArrayList<>();

    }


    /**
     * M??todo controlador que permite validar la iniciada de sesi??n
     * @return Retorna una cadena con la redirecci??n a la p??gina index.
     */
    public String iniciarSesion(){

        if(!password.isEmpty()){
            if(!username.isEmpty()){
                try {
                    profesorSesion = profesorServicio.iniciarSesion(username,password);
                    autenticado=true;
                    profesor = true;
                    return CONSTANTREDIRECTINDEX;
                } catch (InterruptedException | ExecutionException g) {
                    try{
                    usuarioSesion = usuarioServicio.iniciarSesion(username, password);

                    autenticado=true;
                    profesor = false;

                        Thread.currentThread().interrupt();

                        return CONSTANTREDIRECTINDEX;

                } catch (InterruptedException | ExecutionException e) {
                    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                    FacesContext.getCurrentInstance().addMessage("login-bean", fm);
                        Thread.currentThread().interrupt();
                }
                }
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "Ingrese una contrase??a");
            FacesContext.getCurrentInstance().addMessage("login-bean", fm);
        }
        return null;
    }


    /**
     * M??todo que permite invalidar la sesi??n abierta
     * @return Retorna una cadena con la redirecci??n a la p??gina principal
     */
    public String cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return CONSTANTREDIRECTINDEX;
    }

    /**
     * M??todo que permite agregar una pregunta a un nuevo Test
     * @param id Identificador de la pregunta.
     * @param pregunta Ruta de la im??gen de la pregunta
     * @param descripcion Descripci??n de la pregunta
     */
    public void agregarAlTest(Integer id, String pregunta, String descripcion){

        PreguntaTest preguntaTest =  new PreguntaTest(id, pregunta, descripcion);
        if(!preguntaTests.contains(preguntaTest)){
            preguntaTests.add(preguntaTest);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Pregunta agregada al test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, CONSTANTALERTA, "Esa pregunta ya est?? en el test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        }
    }

    /**
     * M??todo que permite generar un nuevo test previamente configurado por el profesor
     * @param nombreClasesTest Arraylist de tipo String el cual contiene el nombre de las clases a las cuales se
     *                         les va a asociar el nuevo test.
     */
    public void generarTest(String[] nombreClasesTest){

        if(profesorServicio != null && preguntaTests.size() == 6){
            try {
                List<Clase> clases = new ArrayList<>();
                if (nombreClasesTest.length != 0 ){
                    clases = claseServicio.obtenerClasesSeleccionadas(profesorSesion, nombreClasesTest);
                }
                Test test = preguntaServicio.generarTest(clases, profesorSesion, preguntaTests);
                preguntaTests.clear();
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Test creado con ??xito");
                FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "El c??digo de acceso al test es: \n" + test.getId());
                PrimeFaces.current().dialog().showMessageDynamic(message);

            } catch (InterruptedException | ExecutionException e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);
                Thread.currentThread().interrupt();
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "El test debe tener 6 preguntas");
            FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);
        }
    }


}
