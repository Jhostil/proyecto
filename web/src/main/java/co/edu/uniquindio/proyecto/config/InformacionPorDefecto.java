package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.servicios.TipoPreguntaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InformacionPorDefecto implements CommandLineRunner {

    @Autowired
    private TipoPreguntaServicio tipoPreguntaServicio;

    /**
     * Método que permite crear nuevos tipoPregunta si no existe ninguno cuando la aplicación inicie
     */
    @Override
    public void run(String... args) throws Exception {
        if(!tipoPreguntaServicio.existenTipos()){

            TipoPregunta tipoPregunta = new TipoPregunta();
            tipoPregunta.setDescripcion("logica");
            tipoPreguntaServicio.registrarTipo(tipoPregunta);

            tipoPregunta.setDescripcion("cuestionario");
            tipoPreguntaServicio.registrarTipo(tipoPregunta);

            tipoPregunta.setDescripcion("laberinto");
            tipoPreguntaServicio.registrarTipo(tipoPregunta);
        }

    }

}
