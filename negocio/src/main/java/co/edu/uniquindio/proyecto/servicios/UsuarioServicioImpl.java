package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class UsuarioServicioImpl implements  UsuarioServicio{

    private final UsuarioRepo usuarioRepo;

    public UsuarioServicioImpl(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Usuario registrarUsuario(Usuario u) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",u.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El id del usuario ya existe.");
        }

        if (u.getEmail() != null) {
            querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("email",u.getEmail()).get();

            if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
                throw new Exception("El email del usuario ya existe.");
            }

        }
        querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("username",u.getUsername()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El username del usuario ya existe.");
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        u.setPassword(passwordEncryptor.encryptPassword(u.getPassword()));

        int fechaAhora = LocalDate.now().getYear();
        int fechaN = Integer.parseInt(u.getFechaNacimiento().split("-")[0]);

        if (fechaAhora - fechaN < 5)
        {
            throw new Exception("Debe tener más de 5 años de edad");
        }

        try {
            dbFirestore.collection("Usuario").document(u.getId()).set(u);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public Usuario actualizarUsuario(Usuario u) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",u.getId()).get();
        Usuario buscado = null;
        for (DocumentSnapshot usuario:querySnapshotApiFuture.get().getDocuments()) {
            buscado = usuario.toObject(Usuario.class);
        }

        if (buscado == null)
        {
            throw new Exception("El usuario no existe");
        }
        buscado = u;
        dbFirestore.collection("Usuario").document(buscado.getId()).set(buscado);
        return buscado;
    }

    private Usuario buscarUsuario(Usuario u) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",u.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El código del usuario ya existe.");
        }
        querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("email",u.getEmail()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El email del usuario ya existe.");
        }
        querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("username",u.getUsername()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El username del usuario ya existe.");
        }
        dbFirestore.collection("Usuario").document(u.getId()).set(u);
        return usuarioRepo.save(u);
    }

    @Override
    public void eliminarUsuario(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",codigo).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El código del usuario no existe.");
        }
        dbFirestore.collection("Usuario").document(codigo).delete();
    }

    @Override
    public List<Usuario> listarUsuarios() throws ExecutionException, InterruptedException {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").get();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            usuario = aux.toObject(Usuario.class);
            usuarios.add(usuario);
        }
        return usuarios;
    }

    @Override
    public Usuario obtenerUsuario(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",codigo).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El usuario no existe.");
        }
        Usuario buscado = null;
        for (DocumentSnapshot usuario:querySnapshotApiFuture.get().getDocuments()) {
            buscado = usuario.toObject(Usuario.class);
        }
        return buscado;
    }

    @Override
    public Usuario iniciarSesion(String username, String password) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("username",username).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("Los datos de autenticación son incorrectos");
        }
        Usuario usuario = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            usuario = aux.toObject(Usuario.class);
        }
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        if (strongPasswordEncryptor.checkPassword(password, usuario.getPassword())){
            return usuario;
        } else {
            throw new Exception("La contraseña es incorrecta");
        }
    }

    @Override
    public List<Test> listarTestRealizados(String id) throws ExecutionException, InterruptedException {
        List<Test> list = new ArrayList<>();
        Test test = null;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").get();
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            test = aux.toObject(Test.class);
            if (test.getUsuario() != null) {
                if (test.getUsuario().equals(id)) {
                    list.add(test);
                }
            }
        }
        return list;
    }

    private Usuario buscarPorEmail (String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("email",email).get();
        Usuario usuario = null;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            usuario = aux.toObject(Usuario.class);
        }
        return usuario;
    }

}
