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

    public PreguntaServicioImpl (PreguntaRepo preguntaRepo, TipoPreguntaRepo tipoPreguntaRepo, TestRepo testRepo, DetalleTestRepo detalleTestRepo)
    {
        this.preguntaRepo = preguntaRepo;
        this.tipoPreguntaRepo = tipoPreguntaRepo;
        this.testRepo = testRepo;
        this.detalleTestRepo = detalleTestRepo;
    }

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

    @Override
    public List<Pregunta> listarPreguntas() throws ExecutionException, InterruptedException {
        List<Pregunta> list = new ArrayList<>();
        Pregunta pregunta;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Pregunta").get();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            pregunta = aux.toObject(Pregunta.class);
            list.add(pregunta);
        }
        return list;
    }

    @Override
    public Pregunta guardarPregunta(Pregunta p) throws Exception {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            p.setId(dbFirestore.collection("Pregunta").get().get().getDocuments().size()+1);
            dbFirestore.collection("Pregunta").document(Integer.toString(p.getId())).set(p);
            return p;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Pregunta obtenerPregunta(Integer codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Pregunta").whereEqualTo("id",codigo).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El código de la pregunta no es válido");
        }
        Pregunta pregunta = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            pregunta = aux.toObject(Pregunta.class);
        }
        return pregunta;
    }

    @Override
    public Test generarTest(Profesor profesor, ArrayList<PreguntaTest> preguntaTests) throws Exception{

        try {
            Test test = new Test();
            test.setProfesor(profesor);

            String idTest = getRandomString();
            boolean codigo = verificarId(idTest);

            while (codigo == false)
            {
                idTest = getRandomString();
                codigo = verificarId(idTest);
            }

            test.setId(idTest);
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection("Test").document(test.getId()).set(test);
            Test testGuardado = test;

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
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public String getRandomString()
    {
        String theAlphaNumericS;
        StringBuilder builder;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(5);

        for (int m = 0; m < 5; m++) {

            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * Math.random());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    public boolean verificarId (String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",id).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            return true; //ID está disponible
        }
        return false; //EL ID ya existe

    }

}
