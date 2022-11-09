package co.edu.uniquindio.proyecto.bean;

import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.PreguntaServicio;
import co.edu.uniquindio.proyecto.servicios.TipoPreguntaServicio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Scope("session")
@Component
public class PreguntaBean implements Serializable {

    @Getter
    @Setter
    private Pregunta pregunta;

    @Getter @Setter
    private String tipo;

    @Autowired
    private PreguntaServicio preguntaServicio;

    @Autowired
    private TipoPreguntaServicio tipoPreguntaServicio;

    @Value("#{seguridadBean.profesorSesion}")
    private Profesor profesorSesion;

    @Getter @Setter
    private String preguntaP;

    @Getter
    @Setter
    private String correcta;

    @Value("src/main/resources/META-INF/resources/uploads")
    private String urlUpload;

    @Getter @Setter
    private List<TipoPregunta> tipoPreguntaList;

    private ArrayList<String> incorrectas;
    private static final String CONSTANTALERTA = "Alerta";
    private static final String CONSTANTMSJBEAN = "msj-bean";

    @PostConstruct
    public void inicializar() throws ExecutionException, InterruptedException {

        this.pregunta = new Pregunta();
        this.preguntaP = "";
        this.correcta = "";
        this.incorrectas = new ArrayList<>();
        this.tipoPreguntaList = preguntaServicio.listarTiposPregunta();
        this.tipo = "";
    }

    /**
     * Método que sirve para crear una nueva pregunta previamente configurada por el profesor
     */
    public void crearPregunta(){
        try {
            if(profesorSesion != null){
                if(!incorrectas.isEmpty() && incorrectas.size() == 3){

                    TipoPregunta tipoPregunta = tipoPreguntaServicio.obtenerTipoPorNombre("cuestionario");
                    if (tipo.equals("logica"))
                    {
                         tipoPregunta = tipoPreguntaServicio.obtenerTipoPorNombre("logica");
                    }
                    if (tipo.equals("laberinto"))
                    {
                        tipoPregunta = tipoPreguntaServicio.obtenerTipoPorNombre("laberinto");
                    }
                    pregunta.setPregunta(preguntaP);
                    pregunta.setCorrecta(correcta);
                    pregunta.setIncorrecta(incorrectas);
                    pregunta.setTipo(tipoPregunta);
                    preguntaServicio.guardarPregunta(pregunta);

                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Pregunta creado con éxito");
                    FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, msg);
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, CONSTANTALERTA, "Pregunta creado con éxito");
                    FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, msg);
                }
                pregunta = new Pregunta();
                correcta = "";
                preguntaP = "";
                incorrectas = new ArrayList<>();
            }


        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, CONSTANTALERTA, e.getMessage());
            FacesContext.getCurrentInstance().addMessage(CONSTANTMSJBEAN, fm);
        }
    }

    /**
     * Método que permite subir una imágen
     * @param fileUploadEvent Imagen a guardar
     */
    public void subirImagenPregunta (FileUploadEvent fileUploadEvent) {
        UploadedFile imagen = fileUploadEvent.getFile();
        String nombreImg = subirImagen(imagen);

        if (nombreImg != null){
            preguntaP = nombreImg;
        }
    }

    /**
     * Método que permite cargar la imagen correspondiente a la respuesta correcta
     * @param fileUploadEvent Imágen a subir al sistema
     */
    public void subirImagenCorrecta (FileUploadEvent fileUploadEvent) {
        UploadedFile imagen = fileUploadEvent.getFile();
        String nombreImg = subirImagen(imagen);

        if (nombreImg != null){
            correcta = nombreImg;
        }
    }

    /**
     * Método que permite subir las imágenes correspondientes a las respuestas incorrectas de una pregunta
     * @param fileUploadEvent Imágen a subir
     */
    public void subirImagenesIncorrectas (FileUploadEvent fileUploadEvent) {
        UploadedFile imagen = fileUploadEvent.getFile();
        String nombreImg = subirImagen(imagen);

        if (nombreImg != null){
            incorrectas.add(nombreImg);
        }
    }


    public String subirImagen (UploadedFile imagen){

        try {
            File archivo = new File(urlUpload + "/" + imagen.getFileName());
           try ( OutputStream outputStream = new FileOutputStream(archivo)) { //Donde quiero que copie el archivo
               IOUtils.copy(imagen.getInputStream(), outputStream );
           }
            return imagen.getFileName();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
