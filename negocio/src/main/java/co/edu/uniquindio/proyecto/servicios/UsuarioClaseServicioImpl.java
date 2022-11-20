package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.entidades.UsuarioClase;
import co.edu.uniquindio.proyecto.repositorios.UsuarioClaseRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UsuarioClaseServicioImpl implements UsuarioClaseServicio{

    private final UsuarioClaseRepo usuarioClaseRepo;

    private static final String COLECCIONUSUARIOCLASE = "UsuarioClase";


    public UsuarioClaseServicioImpl(UsuarioClaseRepo usuarioClaseRepo) {
        this.usuarioClaseRepo = usuarioClaseRepo;
    }

    @Override
    public UsuarioClase registrarClase(String codigoClase, Usuario usuario) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Clase").whereEqualTo("id",codigoClase).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new InterruptedException("No existe una clase con ese código");
        }

        UsuarioClase usuarioClase = new UsuarioClase();
        DocumentSnapshot aux=querySnapshotApiFuture.get().getDocuments().get(0);
        usuarioClase.setClase(aux.toObject(Clase.class));

        querySnapshotApiFuture = dbFirestore.collection(COLECCIONUSUARIOCLASE).whereEqualTo("usuario.id",usuario.getId()).whereEqualTo("clase.id", codigoClase).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new InterruptedException("Ya está registrado en esa clase");
        }
        usuarioClase.setUsuario(usuario);
        usuarioClase.setId(dbFirestore.collection(COLECCIONUSUARIOCLASE).get().get().getDocuments().size()+1);
        dbFirestore.collection(COLECCIONUSUARIOCLASE).document(Integer.toString(usuarioClase.getId())).set(usuarioClase);

        return usuarioClase;
    }

    @Override
    public List<Usuario> obtenerAlumnos(String codigoClase) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONUSUARIOCLASE).whereEqualTo("clase.id",codigoClase).get();

        List<Usuario> alumnos = new ArrayList<>();

        for (DocumentSnapshot aux : querySnapshotApiFuture.get().getDocuments()) {
            alumnos.add(aux.toObject(UsuarioClase.class).getUsuario());
        }

        return  alumnos;
    }
}
