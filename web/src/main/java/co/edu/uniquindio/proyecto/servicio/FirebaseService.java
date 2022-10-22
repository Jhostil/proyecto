package co.edu.uniquindio.proyecto.servicios;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FirebaseService {

    @PostConstruct
    public void inicializar(){
        try {
            File file = new File(ClassLoader.class.getResource("/serviceAccountKey.json").getFile());
            InputStream serviceAccount = new FileInputStream(file);

            System.out.println(file.getAbsolutePath());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://quiztest-f0839-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
