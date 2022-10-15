package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.ProfesorServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostConstruct
    public void inicializar()
    {
        usuario = new Usuario();
        profesor = new Profesor();
        rol = "";

    }

    public void registrarUsuario ()
    {
        if (rol.equals("Estudiante")) {
            try {
                usuario.setFechaNacimiento(localDate.toString());
                usuarioServicio.registrarUsuario(usuario);
                usuario = new Usuario();
                rol = "";
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Registro exitoso");
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
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
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Registro exitoso");
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
                FacesContext.getCurrentInstance().addMessage("msj-bean", fm);
            }
        }
    }


}
