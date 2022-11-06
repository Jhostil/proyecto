package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.*;
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

    /**
     * Método que guarda un nuevo registro de un nuevo Usuario.
     * @param u Objeti de tipo Usuario que se va a guardar
     * @return Retorna el objeto guardado
     */
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

    /**
     * Método que sirve para actualizar un objeto de tipo Usuario
     * @param u Usuario actualizado
     * @return Retorna el objeto actualizado
     */
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

    /**
     * Método que sirve para eliminar un usuario de la base de datos
     * @param codigo Identificador del usuario a eliminar
     */
    @Override
    public void eliminarUsuario(String codigo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("id",codigo).get();

        if (querySnapshotApiFuture.get().getDocuments().isEmpty()){
            throw new Exception("El código del usuario no existe.");
        }
        dbFirestore.collection("Usuario").document(codigo).delete();
    }

    /**
     * Metodo que sirve para listar todos los usuarios existentes en la base de datos
     * @return Retorna una lista con los usuarios existentes
     */
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

    /**
     * Método que sirve para obtener un usuario dado si id
     * @param codigo Identificador del usuario a buscar
     * @return Retorna un objeto de tipo Usuario asociado al identificador ingreado
     */
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

    /**
     * Método que sirve para validar que las credencialos ingresadas estén asociadas a un usuiario y que sean correctas
     * @param username Username de usuario
     * @param password Contraseña del usuario
     * @return Retorna el usuario asociado a las credenciales ingrsadas
     */
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

    /**
     * Método que srive para listar los test realizados por un usuario en particular
     * @param id Identificador del usuario
     * @return Retorna una lista con los test presentados por el usuario
     */
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

    /**
     * Método que busca un usuario asociado el email ingresado por parámetro
     * @param email Email del usuario a buscar
     * @return Retorna un objeto de tipo Usuario asociado al email ingreado
     */
    private Usuario buscarPorEmail (String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("Usuario").whereEqualTo("email",email).get();
        Usuario usuario = null;
        for (DocumentSnapshot aux: querySnapshotApiFuture.get().getDocuments()) {
            usuario = aux.toObject(Usuario.class);
        }
        return usuario;
    }

    @Override
    public List<UsuarioClase> obtenerClases(Usuario u) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection("UsuarioClase").whereEqualTo("usuario.id",u.getId()).get();
        List<UsuarioClase> clases = new ArrayList<>();
        for (DocumentSnapshot aux:querySnapshotApiFuture.get().getDocuments()) {
            clases.add(aux.toObject(UsuarioClase.class));
        }
        return  clases;
    }
}
