package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
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

    private static final String COLECCIONPROFESOR = "Profesor";

    public ProfesorServicioImpl(ProfesorRepo profesorRepo)
    {
        this.profesorRepo = profesorRepo;
    }

    /**
     * Método que dado un id, devulve un objeto de tipo Profesor
     * @param codigo Identificador del Profesor
     * @return Retorna un objeto de tipo profesor.
     */
    @Override
    public Profesor obtenerProfesor(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("id",codigo).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El profesor no existe.");
        }
        Profesor buscado = new Profesor();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            buscado = aux.toObject(Profesor.class);
        }
        querySnapshotApiFuture = dbFirestore.collection("Test").get();
        Test test;
        List<Test> list = new ArrayList<>();
        buscado.setTestsConfigurados(list);
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            test = aux.toObject(Test.class);
            if (test.getProfesor() != null && test.getProfesor().getId().equals(codigo)) {
                buscado.getTestsConfigurados().add(test);
            }
        }
        return buscado;
    }


    /**
     * Método que dado un username y password, valida si existe un Profesor con ese username,
     * y si existe, valida que la contraseña ingresada coincida con la existente
     * @param username Username del usuario
     * @param password Contraseña del usuario
     * @return Retorna un objeto de tipo Profesor.
     */
    @Override
    public Profesor iniciarSesion(String username, String password) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("username",username).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("Los datos de autenticación son incorrectos");
        }
        Profesor profesor = new Profesor();
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

    /**
     * Método que sirve para guardar un objeto de tipo profesor en la base de datos.
     * @param p Objeto de tipo Profesor a guardar.
     * @return Retorna el Profesor guardado.
     */
    @Override
    public Profesor registrarProfesor(Profesor p) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("id",p.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("El id del profesor ya existe.");
        }

        if (p.getEmail() != null) {
            querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("email",p.getEmail()).get();

            if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
                throw new Exception("El email del profesor ya existe.");
            }

        }

        querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("username",p.getUsername()).get();

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
            dbFirestore.collection(COLECCIONPROFESOR).document(p.getId()).set(p);
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return p;
    }

    @Override
    public List<Clase> obtenerClases(Profesor p) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Clase").whereEqualTo("profesor.id",p.getId()).get();
        List<Clase> clases = new ArrayList<>();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            clases.add(aux.toObject(Clase.class));
        }
        return  clases;
    }

    /**
     * Método que sirve para buscar un Profesor dado su email
     * @param email Email del Profesor a buscar
     * @return Retorna un objeto de tipo Profesor el cual está asociado al correo ingresado.
     */
    private Profesor buscarPorEmail (String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONPROFESOR).whereEqualTo("email",email).get();
        Profesor profesor = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            profesor = aux.toObject(Profesor.class);
        }
        return  profesor;
    }
}
