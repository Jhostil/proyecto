package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
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
    private String username, password;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProfesorServicio profesorServicio;

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

    @PostConstruct
    public void inicializar(){

        this.preguntaTests = new ArrayList<>();
      /*  this.subTotal = 0;
        productosCarrito = new ArrayList<>();
        this.medioPago = "";

        if(usuarioSesion != null)
        {
            actualizarListaProductosPublicados();
            actualizarListaProductosFavoritos();
            actualizarListaProductosComprados();
        }*/

    }


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

                    //actualizarListaProductosPublicados();
                    //actualizarListaProductosFavoritos();
                   // actualizarListaTestRealizados();

                    return "/index.xhtml?faces-redirect=true";

                } catch (Exception e) {
                    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
                    FacesContext.getCurrentInstance().addMessage("login-bean", fm);
                }
                }
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "Ingrese una contraseña");
            FacesContext.getCurrentInstance().addMessage("login-bean", fm);
        }
        return null;
    }


    public String cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public void agregarAlTest(Integer id, String pregunta, String descripcion){

        PreguntaTest preguntaTest =  new PreguntaTest(id, pregunta, descripcion);
        if(!preguntaTests.contains(preguntaTest)){
            preguntaTests.add(preguntaTest);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Pregunta agregada al test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Esa pregunta ya está en el test");
            FacesContext.getCurrentInstance().addMessage("add-cart", fm);
        }
    }

    public void generarTest(){

        if(profesorServicio != null && preguntaTests.size() == 6){
            try {
                Test test = preguntaServicio.generarTest(profesorSesion, preguntaTests);
                preguntaTests.clear();
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Test creado con éxito");
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "El código de acceso al test es: \n" + test.getId());
                PrimeFaces.current().dialog().showMessageDynamic(message);

            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
            }
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", "El test debe tener 6 preguntas");
            FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
        }
    }


}
