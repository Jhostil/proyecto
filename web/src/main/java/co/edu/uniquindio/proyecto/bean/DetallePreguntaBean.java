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

    @PostConstruct
    public void inicializar(){


        if (codigoPregunta != null && !codigoPregunta.isEmpty()){
            Integer codigo = Integer.parseInt(codigoPregunta);
            try {
                pregunta = preguntaServicio.obtenerPregunta(codigo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


    }



}
