package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoPreguntaImpl implements TipoPreguntaServicio {

    @Autowired
    private TipoPreguntaRepo tipoPreguntaRepo;

    @Override
    public TipoPregunta obtenerTipoPregunta(Integer id) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Tipo").whereEqualTo("id",id).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El id no corresponde a ninguna categoría");
        }
        TipoPregunta tipoPregunta = null;
        for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
            tipoPregunta = aux.toObject(TipoPregunta.class);
        }
        return tipoPregunta;
    }

    @Override
    public TipoPregunta obtenerTipoPorNombre(String nombre) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Tipo").whereEqualTo("descripcion",nombre).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El nombre no corresponde a ningun tipo de pregunta");
        }
        TipoPregunta tipoPregunta = null;
        for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
            tipoPregunta = aux.toObject(TipoPregunta.class);
        }
        return tipoPregunta;
    }
}
