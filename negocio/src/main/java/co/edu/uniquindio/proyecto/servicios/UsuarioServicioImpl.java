package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.entidades.DetalleTest;
import co.edu.uniquindio.proyecto.entidades.Pregunta;
import co.edu.uniquindio.proyecto.entidades.Test;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        if (buscado.isPresent()) {
            throw new Exception("El id del usuario ya existe.");
        }

        if (u.getEmail() != null) {
            buscado = buscarPorEmail(u.getEmail());

            if (buscado.isPresent()) {
                throw new Exception("El email del usuario ya existe.");
            }

        }

        buscado = usuarioRepo.findByUsername(u.getUsername());

        if (buscado.isPresent()) {
            throw new Exception("El username del usuario ya existe.");
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        u.setPassword(passwordEncryptor.encryptPassword(u.getPassword()));

        LocalDate fechaN = u.getFechaNacimiento();


        if (LocalDate.now().compareTo(fechaN) < 5)
        {
            throw new Exception("Debe tener más de 5 años de edad");
        }

        Usuario us = new Usuario();
        try {
          us=  usuarioRepo.save(u);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return us;

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
    public Usuario iniciarSesion(String username, String password) throws Exception {
        Usuario usuarioEmail = usuarioRepo.findByUsername(username).orElseThrow(() -> new Exception("Los datos de autenticación son incorrectos"));
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        if (strongPasswordEncryptor.checkPassword(password, usuarioEmail.getPassword())){
            return usuarioEmail;
        } else {
            throw new Exception("La contraseña es incorrecta");
        }
    }

    @Override
    public List<Test> listarTestRealizados(String id) {
        return usuarioRepo.listarTestRealizados(id);
    }

    private Optional<Usuario> buscarPorEmail (String email)
    {
        return  usuarioRepo.findByEmail(email);
    }

}
