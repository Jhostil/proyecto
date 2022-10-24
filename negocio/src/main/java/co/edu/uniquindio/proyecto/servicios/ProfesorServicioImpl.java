package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.ProfesorRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class ProfesorServicioImpl implements ProfesorServicio{

    private final ProfesorRepo profesorRepo;

    public ProfesorServicioImpl(ProfesorRepo profesorRepo)
    {
        this.profesorRepo = profesorRepo;
    }

    @Override
    public Profesor obtenerProfesor(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("id",codigo).get();
        FirebaseDatabase storage = FirebaseDatabase.getInstance();
        DatabaseReference ref = storage.getReference();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El profesor no existe.");
        }
        Profesor buscado = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            buscado = aux.toObject(Profesor.class);
        }
        querySnapshotApiFuture = dbFirestore.collection("Test").get();
        Test test;
        List<Test> list = new ArrayList<>();
        buscado.setTestsConfigurados(list);
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            test = aux.toObject(Test.class);
            if (test.getProfesor() != null) {
                if (test.getProfesor().getId().equals(codigo)) {
                    buscado.getTestsConfigurados().add(test);
                }
            }
        }
        return buscado;
    }


    @Override
    public Profesor iniciarSesion(String username, String password) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("username",username).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("Los datos de autenticación son incorrectos");
        }
        Profesor profesor = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            profesor = aux.toObject(Profesor.class);
        }
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        if (strongPasswordEncryptor.checkPassword(password, profesor.getPassword())){
            return profesor;
        } else {
            throw new Exception("La contraseña es incorrecta");
        }
    }

    @Override
    public Profesor registrarProfesor(Profesor p) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("id",p.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El id del profesor ya existe.");
        }

        if (p.getEmail() != null) {
            querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("email",p.getEmail()).get();

            if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
                throw new Exception("El email del profesor ya existe.");
            }

        }

        querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("username",p.getUsername()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El username del profesor ya existe.");
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        p.setPassword(passwordEncryptor.encryptPassword(p.getPassword()));

        int fechaAhora = LocalDate.now().getYear();
        int fechaN = Integer.parseInt(p.getFechaNacimiento().split("-")[0]);

        if (fechaAhora - fechaN < 18)
        {
            throw new Exception("Debe tener más de 18 años de edad");
        }

        try {
            dbFirestore.collection("Profesor").document(p.getId()).set(p);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return p;
    }

    private Profesor buscarPorEmail (String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Profesor").whereEqualTo("email",email).get();
        Profesor profesor = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            profesor = aux.toObject(Profesor.class);
        }
        return  profesor;
    }
}
