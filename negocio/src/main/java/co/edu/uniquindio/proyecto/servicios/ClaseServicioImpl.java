package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Clase;
import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.TipoPregunta;
import co.edu.uniquindio.proyecto.repositorios.ClaseRepo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ClaseServicioImpl implements ClaseServicio{

    private final ClaseRepo claseRepo;

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
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Clase").whereEqualTo("nombre",nombre).whereEqualTo("profesor.id", profesor.getId()).get();

        if (!querySnapshotApiFuture.get().getDocuments().isEmpty()) {
            throw new Exception("Ya existe una clase con ese nombre.");
        }

        String codigoClase = getRandomString();
        boolean codigo = verificarId(codigoClase);

        while (codigo == false)
        {
            codigoClase = getRandomString();
            codigo = verificarId(codigoClase);
        }

        clase.setId(codigoClase);
        dbFirestore.collection("Clase").document(clase.getId()).set(clase);

        return clase;
    }

    /**
     * Método que genera un códgo aleatorio de tamaño 5, combinando letras mayúsuculas y números
     * @return Retorna un string con el código aleatorio generado
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

            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * Math.random());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    /**
     * Método que dado un id de una Clase verifica si existe o no.
     * @param id Identificador de la Clase a buscar
     * @return Retorna true si la Clase con ese id existe. Retorna false si no existe una Clase con el id dado
     */
    public boolean verificarId (String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Test").whereEqualTo("id",id).get();
        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            return true; //ID está disponible
        }
        return false; //EL ID ya existe

    }
}
