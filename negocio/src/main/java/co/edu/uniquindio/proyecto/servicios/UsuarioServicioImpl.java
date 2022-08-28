package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements  UsuarioServicio{

    private final UsuarioRepo usuarioRepo;

    public UsuarioServicioImpl(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Usuario registrarUsuario(Usuario u) throws Exception {

        Optional<Usuario> buscado = usuarioRepo.findById(u.getId());

        if (buscado.isPresent()){
            throw new Exception("El id del usuario ya existe.");
        }

        buscado = buscarPorEmail(u.getEmail());

        if (buscado.isPresent()){
            throw new Exception("El email del usuario ya existe.");
        }


        buscado = usuarioRepo.findByUsername(u.getUsername());

        if (buscado.isPresent()){
            throw new Exception("El username del usuario ya existe.");
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        u.setPassword(passwordEncryptor.encryptPassword(u.getPassword()));

        return usuarioRepo.save(u);
    }

    @Override
    public Usuario actualizarUsuario(Usuario u) throws Exception {

        Usuario buscado = usuarioRepo.buscar(u.getId());

        if (buscado == null)
        {
            throw new Exception("El usuario no existe");
        }


        buscado = u;
        return usuarioRepo.save(buscado);
    }

    private Usuario buscarUsuario(Usuario u) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(u.getId());

        if (buscado.isPresent()){
            throw new Exception("El código del usuario ya existe.");
        }

        buscado = buscarPorEmail(u.getEmail());

        if (buscado.isPresent()){
            throw new Exception("El email del usuario ya existe.");
        }


        buscado = usuarioRepo.findByUsername(u.getUsername());

        if (buscado.isPresent()){
            throw new Exception("El username del usuario ya existe.");
        }

        return usuarioRepo.save(u);
    }

    @Override
    public void eliminarUsuario(String codigo) throws Exception {

        Optional<Usuario> buscado = usuarioRepo.findById(codigo);

        if (buscado.isEmpty()){
            throw new Exception("El código del usuario no existe.");
        }

        usuarioRepo.deleteById(codigo);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario obtenerUsuario(String codigo) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(codigo);

        if (buscado.isEmpty()){
            throw new Exception("El usuario no existe.");
        }

        return buscado.get();
    }

    @Override
    public Usuario iniciarSesion(String email, String password) throws Exception {
        Usuario usuarioEmail = usuarioRepo.findByEmail(email).orElseThrow(() -> new Exception("Los datos de autenticación son incorrectos"));
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        if (strongPasswordEncryptor.checkPassword(password, usuarioEmail.getPassword())){
            return usuarioEmail;
        } else {
            throw new Exception("La contraseña es incorrecta");
        }
    }

    private Optional<Usuario> buscarPorEmail (String email)
    {
        return  usuarioRepo.findByEmail(email);
    }
}
