package co.edu.uniquindio.proyecto.bean;


import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

//Clase encargada de la comunicación entre la capa web y capa de negocio,
// esta clase se enmarca en las funcionalidades que implican
@Component
@ViewScoped
public class DetallePreguntaBean implements Serializable {

    //Se declara una variable de tipo Pregunta servicio
    @Autowired
    private PreguntaServicio preguntaServicio;

    @Value("#{param['pregunta']}")
    private String codigoPregunta;

    @Getter
    @Setter
    private Pregunta pregunta;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Value("#{seguridadBean.profesorSesion}")
    private Profesor profesorSesion;

    @Getter @Setter
    private List<String> imagenes;

    //Método inicializar que se ejecuta después de que se contruye una instancia de la clase.
    @PostConstruct
    public void inicializar(){

        if (codigoPregunta != null && !codigoPregunta.isEmpty()){
            Integer codigo = Integer.parseInt(codigoPregunta);
            try {
                pregunta = preguntaServicio.obtenerPregunta(codigo);
                imagenes = new ArrayList<>();
                mostrarImagenes();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Método que captura las imagenes de las preguntas y sus repuestas posibles.
    public void mostrarImagenes ()
    {
        imagenes.add(pregunta.getPregunta());
        imagenes.add(pregunta.getCorrecta());

        for (String p: pregunta.getIncorrecta()) {
            imagenes.add(p);
        }
    }

}
