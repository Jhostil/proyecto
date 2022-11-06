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

@Service
public class UsuarioClaseServicioImpl implements UsuarioClaseServicio{

    private final UsuarioClaseRepo usuarioClaseRepo;


    public UsuarioClaseServicioImpl(UsuarioClaseRepo usuarioClaseRepo) {
        this.usuarioClaseRepo = usuarioClaseRepo;
    }

    @Override
    public UsuarioClase registrarClase(String codigoClase, Usuario usuario) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Clase").whereEqualTo("id",codigoClase).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("No existe una clase con ese código");
        }

        UsuarioClase usuarioClase = new UsuarioClase();
        DocumentSnapshot aux=querySnapshotApiFuture.get().getDocuments().get(0);
        usuarioClase.setClase(aux.toObject(Clase.class));

        querySnapshotApiFuture = dbFirestore.collection("UsuarioClase").whereEqualTo("usuario.id",usuario.getId()).whereEqualTo("clase.id", codigoClase).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("Ya está registrado en esa clase");
        }
        usuarioClase.setUsuario(usuario);
        usuarioClase.setId(dbFirestore.collection("UsuarioClase").get().get().getDocuments().size()+1);
        dbFirestore.collection("UsuarioClase").document(Integer.toString(usuarioClase.getId())).set(usuarioClase);

        return usuarioClase;
    }
}
