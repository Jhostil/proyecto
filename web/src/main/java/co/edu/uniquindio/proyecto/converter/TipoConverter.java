package co.edu.uniquindio.proyecto.converter;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.servicios.TipoPreguntaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;

@Component
public class TipoConverter implements Converter<TipoPregunta>, Serializable {

    @Autowired
    private TipoPreguntaServicio tipoPreguntaServicio;

    @Override
    public TipoPregunta getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        TipoPregunta tipoPregunta = null;
        try {
            tipoPregunta = tipoPreguntaServicio.obtenerTipoPregunta(Integer.parseInt(s));
        } catch (Exception e){
            System.out.println(e);
        }
        return tipoPregunta;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, TipoPregunta tipoPregunta) {
        if (tipoPregunta != null)
        {
            return tipoPregunta.getId().toString();
        }
        return "";
    }

}
