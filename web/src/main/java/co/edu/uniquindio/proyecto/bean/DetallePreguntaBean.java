package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;
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

@Component
@ViewScoped
public class DetallePreguntaBean implements Serializable {

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

    public void mostrarImagenes ()
    {
        imagenes.add(pregunta.getPregunta());
        imagenes.add(pregunta.getCorrecta());

        for (String p: pregunta.getIncorrecta()) {
            imagenes.add(p);
        }
    }

}
