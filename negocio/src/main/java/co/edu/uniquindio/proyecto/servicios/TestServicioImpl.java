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

    public TestServicioImpl(TestRepo testRepo, DetalleTestRepo detalleTestRepo) {
        this.testRepo = testRepo;
        this.detalleTestRepo = detalleTestRepo;
    }

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
                Usuario u = null;
                querySnapshotApiFuture = dbFirestore.collection("DetalleTest").get();
                for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                    DetalleTest dt = aux.toObject(DetalleTest.class);
                    if (dt.getTest() != null) {
                        if (dt.getTest().getId().equals(codigo)) {
                            if (dt.getUsuario() != null) {
                                if (dt.getUsuario().getId().equals(idUsuario)) {
                                    u = dt.getUsuario();
                                }
                            }
                        }
                    }
                }

                if (u == null)
                {
                    return "valido";
                } else {
                    return "El test ya fué presentado";
                }
            }
        } catch (Exception e) {
            return "invalido";
        }
        return "invalido";
    }

    @Override
    public List<DetalleTest> iniciarTest(String codigo, Usuario usuario) throws Exception {

        try {
            Test test = null;
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",codigo).get();
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                test = aux.toObject(Test.class);
            }
            querySnapshotApiFuture = dbFirestore.collection("DetalleTest").get();
            DetalleTest detalle;
            List<DetalleTest> list = new ArrayList<>();
            test.setDetalleTestList(list);
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                detalle = aux.toObject(DetalleTest.class);
                if (detalle.getTest() != null) {
                    if (detalle.getTest().getId().equals(codigo)){
                        test.getDetalleTestList().add(detalle);
                    }
                }
            }
            List<DetalleTest> detalleTestList = test.getDetalleTestList();
            List<DetalleTest> nuevoTest = new ArrayList<>();

            for (DetalleTest dt: detalleTestList) {

                Pregunta pregunta = dt.getPregunta();
                DetalleTest detalleTest = new DetalleTest();
                detalleTest.setTest(test);
                detalleTest.setPregunta(pregunta);
                detalleTest.setUsuario(usuario);
                detalleTest.setFechaTest(LocalDate.now().toString());
                detalleTest.setId(dbFirestore.collection("DetalleTest").get().get().getDocuments().size()+1);
                dbFirestore.collection("DetalleTest").document(Integer.toString(detalleTest.getId())).set(detalleTest);
                nuevoTest.add(detalleTest);
            }

            return nuevoTest;
        }catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
