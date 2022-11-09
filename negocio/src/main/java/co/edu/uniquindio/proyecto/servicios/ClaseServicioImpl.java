package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.repositorios.ClaseRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ClaseServicioImpl implements ClaseServicio{

    private final ClaseRepo claseRepo;

    private static final String COLECCIONCLASE = "Clase";
    private static final String COLECCIONTESTCLASE = "TestClase";

    public ClaseServicioImpl(ClaseRepo claseRepo) {
        this.claseRepo = claseRepo;
    }

    /**
     * Método que almacena una nueva clase creada por un profesor en la base de datos
     * @param nombre Nombre de la clase a guardar
     * @param profesor Profesor quién creó la clase
     * @return Retorna la clase guardada
     */
    @Override
    public Clase crearClase(String nombre, Profesor profesor) throws Exception {
        Clase clase = new Clase();
        clase.setNombre(nombre);
        clase.setProfesor(profesor);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONCLASE).whereEqualTo("nombre",nombre).whereEqualTo("profesor.id", profesor.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("Ya existe una clase con ese nombre.");
        }

        String codigoClase = getRandomString();

        while (!verificarId(codigoClase))
        {
            codigoClase = getRandomString();
        }

        clase.setId(codigoClase);
        dbFirestore.collection(COLECCIONCLASE).document(clase.getId()).set(clase);

        return clase;
    }

    /**
     * Método para buscar una clase a partir de su identificador
     * @param codigo Identificador de la clase a buscar
     * @return Retorna la clase correspondiente al código ingresado
     */
    @Override
    public Clase obtenerClase(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONCLASE).whereEqualTo("id",codigo).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("No existe una clase con ese código");
        }
        Clase clase = null;
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            clase = aux.toObject(Clase.class);
        }
        return clase;
    }

    /**
     * Método que permite obtener los test activos que tiene una clase en la que está un alumno
     * @param codigoClase Identificador de la clase
     * @return Retorna una lista de tipo TestClase la cual contiene los test activos de la clase}
     */
    @Override
    public List<TestClase> obtenerTestsActivosClase(String codigoClase) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONTESTCLASE).whereEqualTo("clase.id",codigoClase).whereEqualTo("activo", true).get();

        List<TestClase> testClase = new ArrayList<>();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            testClase.add(aux.toObject(TestClase.class));
        }
        return testClase;
    }

    /**
     * Método que permite obtener los test que tiene una clase de un profesor
     * @param codigoClase Identificador de la clase
     * @return Retorna una lista de tipo TestClase la cual contiene los test de la clase}
     */
    @Override
    public List<TestClase> obtenerTestsProfesor(String codigoClase) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONTESTCLASE).whereEqualTo("clase.id",codigoClase).get();

        List<TestClase> testClase = new ArrayList<>();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            testClase.add(aux.toObject(TestClase.class));
        }
        return testClase;
    }

    /**
     * Método que obtiene los objetos de tipo Clase dado un arreglo con sus nombre
     * @param profesorSesion Profesor al cual pertenecen las clases
     * @param nombreClasesTest Arraylist que contiene los nombres de las clases a obtener
     * @return Retorna un arreglo con los objetos de tipo Clase
     */
    @Override
    public List<Clase> obtenerClasesSeleccionadas(Profesor profesorSesion, String[] nombreClasesTest) throws Exception {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(COLECCIONCLASE).whereEqualTo("profesor.id",profesorSesion.getId()).get();

        List<Clase> clases = new ArrayList<>();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            for (String nombreClase: nombreClasesTest) {
                Clase aux1 = aux.toObject(Clase.class);
                if (nombreClase.equals(aux1.getNombre())){
                    clases.add(aux1);
                }
            }
        }
        return clases;
    }

    /**
     * Método que genera un códgo aleatorio de tamaño 5, combinando letras mayúsuculas y números
     * @return Retorna un string con el código aleatorio generado
     * Método tomado de: https://www.delftstack.com/howto/java/random-alphanumeric-string-in-java/
     */
    public String getRandomString()
    {
        String theAlphaNumericS;
        StringBuilder builder;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(5);

        for (int m = 0; m < 5; m++) {

            SecureRandom secureRandom = new SecureRandom();
            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * secureRandom.nextDouble());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    /**
     * Método que dado un id, verifica si está disponible para asignarlo a una nueva clase
     * Si la busqueda retorna un arreglo vacío quiere decir que no se encontró una clase con ese id,
     * lo que quiere decir que dicho id está disponible para ser asignado como identificador a una clase.
     * @param id Identificador de la Clase a buscar
     * @return Retorna true si la Clase con ese id existe. Retorna false si no existe una Clase con el id dado
     */
    public boolean verificarId (String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",id).get();

        return (querySnapshotApiFuture.get().getDocuments().isEmpty());

    }
}
