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

    @PostConstruct
    public void inicializar(){

        this.preguntaTests = new ArrayList<>();

    }


    /**
     * Método controlador que permite validar la iniciada de sesión
     * @return Retorna una cadena con la redirección a la página index.
     */
    public String iniciarSesion(){

        if(!password.isEmpty()){
            if(!username.isEmpty()){
                try {
                    profesorSesion = profesorServicio.iniciarSesion(username,password);
                    autenticado=true;
                    profesor = true;
                    return "/index.xhtml?faces-redirect=true";
                } catch (Exception g) {
                    try{
                    usuarioSesion = usuarioServicio.iniciarSesion(username, password);

                    autenticado=true;
                    profesor = false;

                    return "/index.xhtml?faces-redirect=true";

                } catch (Exception e) {
                    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                    FacesContext.getCurrentInstance().addMessage("login-bean", fm);
                }
                }
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "Ingrese una contraseña");
            FacesContext.getCurrentInstance().addMessage("login-bean", fm);
        }
        return null;
    }


    /**
     * Método que permite invalidar la sesión abierta
     * @return Retorna una cadena con la redirección a la página principal
     */
    public String cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * Método que permite agregar una pregunta a un nuevo Test
     * @param id Identificador de la pregunta.
     * @param pregunta Ruta de la imágen de la pregunta
     * @param descripcion Descripción de la pregunta
     */
    public void agregarAlTest(Integer id, String pregunta, String descripcion){

        PreguntaTest preguntaTest =  new PreguntaTest(id, pregunta, descripcion);
        if(!preguntaTests.contains(preguntaTest)){
            preguntaTests.add(preguntaTest);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Pregunta agregada al test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, CONSTANTALERTA, "Esa pregunta ya está en el test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        }
    }

    /**
     * Método que permite generar un nuevo test previamente configurado por el profesor
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
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Test creado con éxito");
                FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "El código de acceso al test es: \n" + test.getId());
                PrimeFaces.current().dialog().showMessageDynamic(message);

            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "El test debe tener 6 preguntas");
            FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);
        }
    }


}
