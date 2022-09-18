package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Profesor;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.ProfesorRepo;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProfesorServicioImpl implements ProfesorServicio{

    private final ProfesorRepo profesorRepo;

    public ProfesorServicioImpl(ProfesorRepo profesorRepo)
    {
        this.profesorRepo = profesorRepo;
    }

    @Override
    public Profesor obtenerProfesor(String codigo) throws Exception {
        Optional<Profesor> buscado = profesorRepo.findById(codigo);

        if (buscado.isEmpty()){
            throw new Exception("El profesor no existe.");
        }
        return buscado.get();
    }


    @Override
    public Profesor iniciarSesion(String username, String password) throws Exception {
        Profesor profesorEmail = profesorRepo.findByUsername(username).orElseThrow(() -> new Exception("Los datos de autenticación son incorrectos"));
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        if (strongPasswordEncryptor.checkPassword(password, profesorEmail.getPassword())){
            return profesorEmail;
        } else {
            throw new Exception("La contraseña es incorrecta");
        }
    }

    @Override
    public Profesor registrarProfesor(Profesor p) throws Exception {

        Optional<Profesor> buscado = profesorRepo.findById(p.getId());

        if (buscado.isPresent()) {
            throw new Exception("El id del profesor ya existe.");
        }

        if (p.getEmail() != null) {
            buscado = buscarPorEmail(p.getEmail());

            if (buscado.isPresent()) {
                throw new Exception("El email del profesor ya existe.");
            }

        }

        buscado = profesorRepo.findByUsername(p.getUsername());

        if (buscado.isPresent()) {
            throw new Exception("El username del profesor ya existe.");
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        p.setPassword(passwordEncryptor.encryptPassword(p.getPassword()));

        LocalDate fechaN = p.getFechaNacimiento();


        if (LocalDate.now().compareTo(fechaN) < 18)
        {
            throw new Exception("Debe tener más de 18 años de edad");
        }

        Profesor pr = new Profesor();
        try {
            pr = profesorRepo.save(p);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return pr;
    }

    private Optional<Profesor> buscarPorEmail (String email)
    {
        return  profesorRepo.findByEmail(email);
    }
}
