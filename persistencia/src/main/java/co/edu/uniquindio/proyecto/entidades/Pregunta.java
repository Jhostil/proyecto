package co.edu.uniquindio.proyecto.entidades;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Pregunta implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    //Atributo que representa la pregunta
    @Column(nullable = false)
    @Length(min = 2,  message = "La pregunta debe tener mínimo 2 caracteres")
    @NotBlank(message = "El campo está vacío, debe ingresar la pregunta")
    private String descripcion;

    @ToString.Exclude
    private String pregunta;

    @ToString.Exclude
    private String respuesta;

    @ToString.Exclude
    @ElementCollection
    private List<String> incorrecta;
}
