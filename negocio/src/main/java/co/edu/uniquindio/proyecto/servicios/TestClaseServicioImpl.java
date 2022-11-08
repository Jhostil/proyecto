package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TestClase;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class TestClaseServicioImpl implements TestClaseServicio{


    private static final String COLECCIONTESTCLASE = "TestClase";
    @Override
    public void habilitarTest(TestClase testClase) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONTESTCLASE).whereEqualTo("id",testClase.getId()).get();
        dbFirestore.collection(COLECCIONTESTCLASE).document(querySnapshotApiFuture.get().getDocuments().get(0).getId()).update("activo", true);

    }

    @Override
    public void deshabilitarTest(TestClase testClase) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONTESTCLASE).whereEqualTo("id",testClase.getId()).get();
        dbFirestore.collection(COLECCIONTESTCLASE).document(querySnapshotApiFuture.get().getDocuments().get(0).getId()).update("activo", false);

    }
}
