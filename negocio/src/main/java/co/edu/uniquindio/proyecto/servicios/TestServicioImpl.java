package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.repositorios.DetalleTestRepo;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestServicioImpl implements TestServicio {


    private TestRepo testRepo;
    private DetalleTestRepo detalleTestRepo;

    private static final String COLECCIONDETALLETEST = "DetalleTest";

    public TestServicioImpl(TestRepo testRepo, DetalleTestRepo detalleTestRepo) {
        this.testRepo = testRepo;
        this.detalleTestRepo = detalleTestRepo;
    }

    /**
     * Método que valida si un usuario ya presentó o no un determinado Test.
     * @param codigo Identificador del Test a presentar
     * @param idUsuario Identificador del Usuario que desea presentar el Test
     * @return Retorna una cadena de tipo String indicando el resultado de la validación
     */
    @Override
    public String validarCodigo(String codigo, String idUsuario) throws Exception {

        try {
            Test test = null;
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",codigo).get();
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                test = aux.toObject(Test.class);
            }
            if (test != null) {
                querySnapshotApiFuture = dbFirestore.collection(COLECCIONDETALLETEST).whereEqualTo("test.id", codigo).whereEqualTo("usuario.id", idUsuario).get();
                if (!querySnapshotApiFuture.get().getDocuments().isEmpty()){
                    return "El test ya fué presentado";
                } else {
                    return "valido";
                }

            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return "invalido";
        }
        return "invalido";
    }

    /**
     * Méetodo que dado un código de un Test,
     * crea y guarda los DetalleTest asociados a ese Test y asociados al usuario que va a respoder las preguntas.
     * @param codigo Identificador del Test a presentar-
     * @param usuario Identificador del Usuario que va a presentat el Test.
     * @return Retorna una lista con los DetalleTest asociados al usuario y al Test.
     */
    @Override
    public List<DetalleTest> iniciarTest(String codigo, Usuario usuario) throws Exception {

        try {
            Test test = new Test();
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",codigo).get();
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                test = aux.toObject(Test.class);
            }
            querySnapshotApiFuture = dbFirestore.collection(COLECCIONDETALLETEST).whereEqualTo("test.id", codigo).whereEqualTo("usuario", null).get();

            List<DetalleTest> nuevoTest = new ArrayList<>();

            DetalleTest dt = new DetalleTest();
            int id = dbFirestore.collection(COLECCIONDETALLETEST).get().get().getDocuments().size()+1;

            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                dt = aux.toObject(DetalleTest.class);

                Pregunta pregunta = dt.getPregunta();
                DetalleTest detalleTest = new DetalleTest();
                detalleTest.setTest(test);
                detalleTest.setPregunta(pregunta);
                detalleTest.setUsuario(usuario);
                detalleTest.setFechaTest(LocalDate.now().toString());
                detalleTest.setId(id);
                id++;
                dbFirestore.collection(COLECCIONDETALLETEST).document(Integer.toString(detalleTest.getId())).set(detalleTest);
                nuevoTest.add(detalleTest);
            }

            return nuevoTest;
        }catch (Exception e)
        {
            Thread.currentThread().interrupt();
            throw new Exception(e.getMessage());
        }
    }
}
