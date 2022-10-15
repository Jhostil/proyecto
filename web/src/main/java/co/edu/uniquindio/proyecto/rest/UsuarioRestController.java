package co.edu.uniquindio.proyecto.rest;

import co.edu.uniquindio.proyecto.dto.Mensaje;
import co.edu.uniquindio.proyecto.entidades.Usuario;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public List<Usuario> listar() throws ExecutionException, InterruptedException {
        return usuarioServicio.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable("id") String idUsuario) {
        try {
            Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);
            return ResponseEntity.status(200).body(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Mensaje(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario){
        try {
            usuarioServicio.registrarUsuario(usuario);
            return ResponseEntity.status(201).body(new Mensaje("El usuario se creó exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Mensaje(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizar(@RequestBody Usuario usuario) {
        try {
            usuarioServicio.actualizarUsuario(usuario);
            return ResponseEntity.status(200).body(new Mensaje("El usuario se actualizó correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Mensaje(e.getMessage()));
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminar (@PathVariable("codigo") String codigo) {
        try {
            usuarioServicio.eliminarUsuario(codigo);
            return ResponseEntity.status(200).body(new Mensaje("El usuario se eliminó exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Mensaje(e.getMessage()));
        }
    }

   /* @GetMapping("/favoritos/{codigo}")
    public ResponseEntity<?> obtenerFavoritos(@PathVariable("codigo") String codigo){
        try {
            List<Producto> lista = usuarioServicio.listarFavoritos(codigo);
            return ResponseEntity.status(200).body(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Mensaje("Error al obtener la lista"));
        }
    }*/
}
