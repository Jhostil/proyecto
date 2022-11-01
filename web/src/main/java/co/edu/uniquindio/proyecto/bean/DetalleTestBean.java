package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.DetalleTestServicio;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

//Clase que comunica la capa web y la capa de negocio,
// este bean nos ayuda a realizar la validación de si la respuesta seleccionada es la correcta
@Component
@Scope("session")
public class DetalleTestBean implements Serializable {

    @Autowired
    private DetalleTestServicio detalleTestServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Value("#{param['test']}")
    private String codigoTest;

    @Value("#{seguridadBean.usuarioSesion}")
    private Usuario usuario;

    @Getter @Setter
    private List<DetalleTest> detalleTestList;

    @PostConstruct
    public void inicializar(){
        try {

            detalleTestList = detalleTestServicio.obtenerDetallesTestPresentados(codigoTest, usuario.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que sirve para obtener el índice de la posición de la respuesta seleccionada.
     * @param respuesta Respuesta seleccionada por el usuario
     * @param pregunta Pregunta respondida por el usuario
     * @return Retorna la posición de la respuesta seleccionada por el usuario
     */
    public int respSeleccionada(String respuesta, Pregunta pregunta){
        int resp = 0;
        if (!pregunta.getCorrecta().equals(respuesta))
        {
            List<String> incorrectas = pregunta.getIncorrecta();
            for (int i= 0; i < incorrectas.size(); i++)

                if (incorrectas.get(i).equals(respuesta)){
                    resp = i+1;
                    return resp;
                }
            }
    return resp;
    }

}
