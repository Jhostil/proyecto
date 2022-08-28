package co.edu.uniquindio.proyecto.bean;

import org.springframework.stereotype.Component;

import javax.faces.view.ViewScoped;

@Component
@ViewScoped
public class InicioBean {

    private String mensaje = "Mi primera página en JSF";
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
