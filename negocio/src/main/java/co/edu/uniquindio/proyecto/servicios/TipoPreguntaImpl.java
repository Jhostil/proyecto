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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TipoPreguntaImpl implements TipoPreguntaServicio {

    @Autowired
    private TipoPreguntaRepo tipoPreguntaRepo;

    /**
     * Método que obtiene un objeto de tipo TipoPregunta dado su identificador.
     * @param id Identificador del objeto a buscar.
     * @return Retorna el objeto de tipo TipoPregunta asociado al id ingresado
     */
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

    /**
     * Método que sirve para obtener un tipo de pregunta dado su nombre
     * @param nombre Nombre del tipoPregunta a buscar
     * @return Retorna un objeto de tipo TipoPregunta asociado al nombre ingresado
     */
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

    /**
     * Método que sirve para validar si exite o no registros de tipo TipoPregunta
     * @return Retorna true si existe por lo menos un objeti de tipo TipoPregunta
     * o retorna false si no existen tipos TipoPregunta}
     */
    @Override
    public boolean existenTipos() throws ExecutionException, InterruptedException {
        boolean resultado = true;
        List<TipoPregunta> tipos = new ArrayList<>();
        TipoPregunta tipoPregunta;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Tipo").get();
        for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
            tipoPregunta = aux.toObject(TipoPregunta.class);
            tipos.add(tipoPregunta);
        }

        if (tipos == null || tipos.isEmpty()) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Método que sirve para guardar un nuevo registro de tipo TipoPregunta
     * @param tipoPregunta Objeto a guardar en la base de datos
     * @return Retorna el objeto guardado
     */
    @Override
    public TipoPregunta registrarTipo(TipoPregunta tipoPregunta) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("Tipo").document(Integer.toString(tipoPregunta.getId())).set(tipoPregunta);
        return tipoPregunta;
    }
}
