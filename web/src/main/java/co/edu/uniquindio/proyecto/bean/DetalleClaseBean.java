package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.TestClase;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.ClaseServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private Clase clase;

    @Autowired
    private ClaseServicio claseServicio;

    @PostConstruct
    public void inicializar(){

        if (codigoClase != null && !codigoClase.isEmpty()){
            try {
                clase = claseServicio.obtenerClase(codigoClase);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<TestClase> obtenerTestsActivosAlumno () throws Exception {
        return claseServicio.obtenerTestsActivosClase( codigoClase);
    }

}
