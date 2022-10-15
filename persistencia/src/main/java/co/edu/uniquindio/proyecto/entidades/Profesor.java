package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class Profesor extends Persona implements Serializable {

    @OneToMany(mappedBy = "profesor")
    @JsonIgnore
    @ToString.Exclude
    private List<Test> testsConfigurados;

    //Atributo que represneta el usurname del usuario
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El campo está vacío, debe ingresar un username")
    private String username;

    public Profesor (String id, String nombre, String apellido, String email, String username, String password, String fechaNacimiento)
    {
        super(id, nombre, apellido, email, password, fechaNacimiento);
        this.username = username;
    }
}
