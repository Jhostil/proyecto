package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TestServicioImpl implements TestServicio {


    private TestRepo testRepo;

    public TestServicioImpl(TestRepo testRepo) {
        this.testRepo = testRepo;
    }


    @Override
    public boolean validarCodigo(String codigo) throws Exception {

        try {
            System.out.println(codigo);
            Test test = null;
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",codigo).get();
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                test = aux.toObject(Test.class);
            }

            if (test != null) {
                try {
                    Usuario u = test.getUsuario();
                    if (test.getUsuario() == null) {
                        return true;
                    }
                } catch (Exception f) {
                    throw new Exception("El test ya fué presentado");
                }
            }
        } catch (Exception e) {
            throw new Exception("Código no válido");
        }

        return false;
    }

    @Override
    public Test iniciarTest(String codigo, Usuario usuario) throws Exception {

        try {
            Test test = null;
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",codigo).get();
            for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
                test = aux.toObject(Test.class);
            }
            test.setUsuario(usuario);
            test.setFechaTest(LocalDate.now().toString());
            //testRepo.save(test);
            return test;
        }catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
