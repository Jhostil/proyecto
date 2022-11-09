package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.repositorios.DetalleTestRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleTestServicioImpl implements DetalleTestServicio{

    private final DetalleTestRepo detalleTestRepo;

    private static final String COLECCIONDETALLE = "DetalleTest";


    public DetalleTestServicioImpl (DetalleTestRepo detalleTestRepo){

        this.detalleTestRepo = detalleTestRepo;
    }

    /**
     * Método que permite obtener los detallesTest a partir de un código de un test
     * @param codigoTest Identificador de un Test
     * @return retorna una lista con los detalletest asociados al código del Test.
     * Los detalletest representan las preguntas previamente configuradas al test.
     */
    @Override
    public List<DetalleTest> obtenerDetallesTest(String codigoTest) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONDETALLE).get();
        List<DetalleTest> list = new ArrayList<>();
        DetalleTest detalleTest;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            detalleTest = aux.toObject(DetalleTest.class);
            if (detalleTest.getTest() != null && detalleTest.getTest().getId().equals(codigoTest)) {
                    list.add(detalleTest);
            }
        }
        return list;
    }

    /**
     * Método que permite guardar un detalleTest creado
     * @param detalleTest detalleTest a guardar
     */
    @Override
    public void guardarDetalle(DetalleTest detalleTest) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLECCIONDETALLE).document(Integer.toString(detalleTest.getId())).set(detalleTest);
    }

    /**
     * Método que obtiene las preguntas de un test presentado por un usuario
     * @param codigoTest Código del test al que pertenecen las preguntas
     * @param idUsuario Identificador del usuario
     * @return retorna una lista con los detallesTest pertenecientes al usuario y asociados código del test
     */
    @Override
    public List<DetalleTest> obtenerDetallesTestPresentados(String codigoTest, String idUsuario) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONDETALLE).get();
        List<DetalleTest> list = new ArrayList<>();
        DetalleTest detalleTest;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            detalleTest = aux.toObject(DetalleTest.class);
            if (detalleTest.getTest() != null && detalleTest.getTest().getId().equals(codigoTest) && detalleTest.getUsuario() != null && detalleTest.getUsuario().getId().equals(idUsuario)) {
                list.add(detalleTest);
            }
        }
        return list;
    }
}
