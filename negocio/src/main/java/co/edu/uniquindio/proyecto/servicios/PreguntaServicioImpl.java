package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.repositorios.DetalleTestRepo;
import co.edu.uniquindio.proyecto.repositorios.PreguntaRepo;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class PreguntaServicioImpl implements PreguntaServicio{

    private final PreguntaRepo preguntaRepo;

    private final TipoPreguntaRepo tipoPreguntaRepo;

    private final TestRepo testRepo;

    private final DetalleTestRepo detalleTestRepo;

    private static final String COLECCIONPREGUNTA = "Pregunta";

    public PreguntaServicioImpl (PreguntaRepo preguntaRepo, TipoPreguntaRepo tipoPreguntaRepo, TestRepo testRepo, DetalleTestRepo detalleTestRepo)
    {
        this.preguntaRepo = preguntaRepo;
        this.tipoPreguntaRepo = tipoPreguntaRepo;
        this.testRepo = testRepo;
        this.detalleTestRepo = detalleTestRepo;
    }

    /**
     * Método que obtiene los tipos de preguntas existentes.
     * @return Retorna la lista con los tipo de preguntas que hay.
     */
    @Override
    public List<TipoPregunta> listarTiposPregunta() throws ExecutionException, InterruptedException {
        List<TipoPregunta> list = new ArrayList<>();
        TipoPregunta tipoPregunta;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Tipo").get();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            tipoPregunta = aux.toObject(TipoPregunta.class);
            list.add(tipoPregunta);
        }
        return list;
    }

    /**
     * Método que obtiene las preguntas existentes
     * @return Retorna una lista con las preguntas que hay.
     */
    @Override
    public List<Pregunta> listarPreguntas() throws ExecutionException, InterruptedException {
        List<Pregunta> list = new ArrayList<>();
        Pregunta pregunta;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPREGUNTA).get();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            pregunta = aux.toObject(Pregunta.class);
            list.add(pregunta);
        }
        return list;
    }

    /**
     * Método que guarda una pregunta a la base de datos.
     * @param p Objeto de tipo Pregunta que se va a guardar
     * @return Retorna la pregunta guardada.
     */
    @Override
    public Pregunta guardarPregunta(Pregunta p) throws Exception {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            p.setId(dbFirestore.collection(COLECCIONPREGUNTA).get().get().getDocuments().size()+1);
            dbFirestore.collection(COLECCIONPREGUNTA).document(Integer.toString(p.getId())).set(p);
            return p;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que dado un id obtiene una pregunta
     * @param codigo Identificador de la pregunta a devolver
     * @return Retorna la pregunta asociada al id.
     */
    @Override
    public Pregunta obtenerPregunta(Integer codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPREGUNTA).whereEqualTo("id",codigo).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El código de la pregunta no es válido");
        }
        Pregunta pregunta = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            pregunta = aux.toObject(Pregunta.class);
        }
        return pregunta;
    }

    /**
     * Método que sirve para generar un nuevo test previamente configurado por un usuario con el rol de profesor
     * Este método crea y guarda los detalleTest asosciados al nuevo Test.
     * @param clases Arraylist que contiene las clases a las cuales se les va a asignar el nuevo test
     * @param profesor Objeto de tipo Profesor quien fué el que creó el Test
     * @param preguntaTests arraylist que contiene los detallesTest a guardar
     * @return Retorna el Test guardado
     */
    @Override
    public Test generarTest(List<Clase> clases, Profesor profesor, ArrayList<PreguntaTest> preguntaTests) throws Exception{

        try {
            Test test = new Test();
            test.setProfesor(profesor);

            String idTest = getRandomString();

            while (!verificarId(idTest))
            {
                idTest = getRandomString();
            }

            //Se guarda el test
            test.setId(idTest);
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection("Test").document(test.getId()).set(test);
            Test testGuardado = test;

            //Se asocia el test a las clases seleccionadas
            if(!clases.isEmpty()) {
                for (Clase clase:clases) {
                    TestClase testClase = new TestClase();
                    testClase.setClase(clase);
                    testClase.setTest(testGuardado);
                    testClase.setActivo(true);
                    testClase.setId(dbFirestore.collection("TestClase").get().get().getDocuments().size()+1);
                    dbFirestore.collection("TestClase").document(Integer.toString(testClase.getId())).set(testClase);
                }
            }

            //Se crean y guardan las preguntas del test
            DetalleTest dt;
            for (PreguntaTest p : preguntaTests){
                dt = new DetalleTest();
                dt.setPregunta(obtenerPregunta(p.getId()));
                dt.setTest(testGuardado);
                dt.setId(dbFirestore.collection("DetalleTest").get().get().getDocuments().size()+1);
                dbFirestore.collection("DetalleTest").document().set(dt);
            }
            return testGuardado;
        }catch (Exception e){
            Thread.currentThread().interrupt();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que genera un códgo aleatorio de tamaño 5, combinando letras mayúsuculas y números
     * @return Retorna un string con el código aleatorio generado
     * Método tomado de: https://www.delftstack.com/howto/java/random-alphanumeric-string-in-java/
     */
    public String getRandomString()
    {
        String theAlphaNumericS;
        StringBuilder builder;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(5);

        for (int m = 0; m < 5; m++) {

            SecureRandom secureRandom = new SecureRandom();
            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * secureRandom.nextDouble());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    /**
     * Método que dado un id, verifica si está disponible para asignarlo a un nevo Test.
     * Si la búsqueda retorna un arreglo vacío quiere decir que no se encontró un Test con ese id,
     * lo que quiere decir que dicho id está disponible para ser asignado como identificador a un Test.
     * @param id Identificador del Test a buscar
     * @return Retorna true si el Test con ese id existe. Retorna false si no existe un Test con el id dado
     */
    public boolean verificarId (String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id", id).get();
        return (querySnapshotApiFuture.get().getDocuments().isEmpty());
    }

}
