package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.TestClase;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.ClaseServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioClaseServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@ViewScoped
public class DetalleClaseBean implements Serializable {

    @Value("#{param['codigoClase']}")
    private String codigoClase;

    @Value("#{seguridadBean.profesorSesion}")
    private Profesor profesorSesion;

    @Value("#{seguridadBean.usuarioSesion}")
    private Usuario usuarioSesion;

    @Getter @Setter
    private List<Usuario> alumnosClase;

    @Getter @Setter
    private Clase clase;

    @Autowired
    private ClaseServicio claseServicio;

    @Autowired
    private UsuarioClaseServicio usuarioClaseServicio;


    @PostConstruct
    public void inicializar(){

        if (codigoClase != null && !codigoClase.isEmpty()){
            try {
                clase = claseServicio.obtenerClase(codigoClase);
                alumnosClase = usuarioClaseServicio.obtenerAlumnos(codigoClase);
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();

                throw new RuntimeException(e);
            }
        }
    }

    public List<TestClase> obtenerTestsActivosAlumno () throws ExecutionException, InterruptedException {
        return claseServicio.obtenerTestsActivosClase( codigoClase);
    }

    public List<TestClase> obtenerTestsProfesor() throws ExecutionException, InterruptedException {
        return claseServicio.obtenerTestsProfesor(codigoClase);
    }

}
