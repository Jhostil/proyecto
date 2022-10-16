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


    public DetalleTestServicioImpl (DetalleTestRepo detalleTestRepo){

        this.detalleTestRepo = detalleTestRepo;
    }

    @Override
    public List<DetalleTest> obtenerDetallesTest(String codigoTest) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("DetalleTest").get();
        List<DetalleTest> list = new ArrayList<>();
        DetalleTest detalleTest;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            detalleTest = aux.toObject(DetalleTest.class);
            if (detalleTest.getTest() != null) {
                if (detalleTest.getTest().getId().equals(codigoTest)){
                    list.add(detalleTest);
                }
            }
        }
        return list;
    }

    @Override
    public void guardarDetalle(DetalleTest detalleTest) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("DetalleTest").document(Integer.toString(detalleTest.getId())).set(detalleTest);
    }

    @Override
    public List<DetalleTest> obtenerDetallesTestPresentados(String codigoTest, String idUsuario) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("DetalleTest").get();
        List<DetalleTest> list = new ArrayList<>();
        DetalleTest detalleTest;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            detalleTest = aux.toObject(DetalleTest.class);
            if (detalleTest.getTest() != null) {
                if (detalleTest.getTest().getId().equals(codigoTest)){
                    if (detalleTest.getUsuario() != null) {
                        if (detalleTest.getUsuario().getId().equals(idUsuario)){
                            list.add(detalleTest);
                        }
                    }
                }
            }
        }
        return list;
    }
}
