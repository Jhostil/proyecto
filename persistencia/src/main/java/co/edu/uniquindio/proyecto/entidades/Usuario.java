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


    //Relacion de uno a muchos con Test
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<Test> testRealizados;
}
