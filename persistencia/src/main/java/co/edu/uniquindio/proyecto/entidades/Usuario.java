package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Usuario extends Persona implements Serializable {

    //Atributo que represneta el usurname del usuario
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El campo está vacío, debe ingresar un username")
    private String username;

    //Relacion de uno a muchos con DetalleTest
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleTest> testRealizados;

    //Relacion de uno a muchos con UsuarioClase
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<UsuarioClase> clases;

    //Constructor con argumentos
    public Usuario (String id, String nombre, String apellido, String email, String username, String password, String fechaNacimiento)
    {
        super(id, nombre, apellido, email, password, fechaNacimiento);
        this.username = username;
    }
}
