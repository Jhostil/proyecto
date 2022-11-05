package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.repositorios.UsuarioClaseRepo;
import org.springframework.stereotype.Service;

@Service
public class UsuarioClaseServicioImpl implements UsuarioClaseServicio{

    private final UsuarioClaseRepo usuarioClaseRepo;


    public UsuarioClaseServicioImpl(UsuarioClaseRepo usuarioClaseRepo) {
        this.usuarioClaseRepo = usuarioClaseRepo;
    }
}
