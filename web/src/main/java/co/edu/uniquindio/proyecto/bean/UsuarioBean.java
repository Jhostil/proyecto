package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.entidades.UsuarioClase;
import co.edu.uniquindio.proyecto.servicios.ProfesorServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioClaseServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
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
import java.time.LocalDate;
import java.util.List;
@Component
@ViewScoped
public class UsuarioBean implements Serializable {

    @Getter
    @Setter
    private Usuario usuario;

    @Getter @Setter
    @Value("#{seguridadBean.usuarioSesion}")
    private Usuario usuarioSesion;

    @Getter
    @Setter
    private LocalDate localDate;

    @Getter @Setter
    private Profesor profesor;

    @Getter @Setter
    private String rol;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProfesorServicio profesorServicio;

    @Getter @Setter
    private List<UsuarioClase> usuarioClases;

    @Getter @Setter
    private UsuarioClase usuarioClaseSeleccionada;

    @Getter @Setter
    private String codigoClase;

    @Autowired
    private UsuarioClaseServicio usuarioClaseServicio;
    private static final String CONSTANTALERTA = "Alerta";
    private static final String CONSTANTGROWLMSJBEAN = "msj-bean";
    private static final String CONSTANTGROWLCODCLASE = "codigo-clase";


    @PostConstruct
    public void inicializar()
    {
        try {
        usuario = new Usuario();
        profesor = new Profesor();
        rol = "";
        Usuario u = null;
        if (usuarioSesion != null)
        {
            u = usuarioServicio.obtenerUsuario(usuarioSesion.getId());
            usuarioClases = usuarioServicio.obtenerClases(u);
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Método controlador que permite inciar el proceso para registrar un nuevo usuario
     */
    public void registrarUsuario ()
    {
        if (rol.equals("Estudiante")) {
            try {
                usuario.setFechaNacimiento(localDate.toString());
                usuarioServicio.registrarUsuario(usuario);
                usuario = new Usuario();
                rol = "";
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Registro exitoso");
                FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLMSJBEAN, fm);
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLMSJBEAN, fm);
            }
        } else {
            try {
                profesor.setPassword(usuario.getPassword());
                profesor.setApellido(usuario.getApellido());
                profesor.setUsername(usuario.getUsername());
                profesor.setEmail(usuario.getEmail());
                profesor.setFechaNacimiento(localDate.toString());
                profesor.setId(usuario.getId());
                profesor.setNombre(usuario.getNombre());

                profesorServicio.registrarProfesor(profesor);
                usuario = new Usuario();
                profesor = new Profesor();
                rol = "";
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Registro exitoso");
                FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLMSJBEAN, fm);
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
                FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLMSJBEAN, fm);
            }
        }
    }

    /**
     * Método que permite a un alumno entrar a una clase mediante un código
     * @return Retorna la vista xhtml de la clase
     */
    public String registrarClase () throws Exception {
        if (codigoClase.isEmpty()) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, "Ingrese el código de la clase");
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODCLASE, fm);
        }
        try {
            UsuarioClase usuarioClase = usuarioClaseServicio.registrarClase(codigoClase, usuarioSesion);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Clase agregada con éxito");
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODCLASE, fm);
            return "/index?faces-redirect=true";
        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
            FacesContext.getCurrentInstance().addMessage(CONSTANTGROWLCODCLASE, fm);
        }
        return "";
    }

    public String verClase () {
        return "/usuario/clase.xhtml?faces-redirect=true&amp;codigoClase=" + usuarioClaseSeleccionada.getClase().getId();
    }
}
