package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    //Atributo que representa la descripcion de la pregunta
    @Column(nullable = false)
    @Length(min = 2,  message = "La pregunta debe tener mínimo 2 caracteres")
    @NotBlank(message = "El campo está vacío, debe ingresar la descripción de la pregunta")
    private String descripcion;

    //Atributo que representa la ruta de la imagen de la pregunta
    @ToString.Exclude
    private String pregunta;

    //Atributo que representa la ruta de la imagen de la respuesta correcta pregunta
    @ToString.Exclude
    @Column(nullable = false)
    private String correcta;

    //Atributo que representa las rutas de la imagenes de las respuestas incorrectas de la pregunta
    @ToString.Exclude
    @ElementCollection
    @Column(nullable = false)
    private List<String> incorrecta;

    //Relacion de uno a muchos con la entidad DetalleTest
    @OneToMany(mappedBy = "pregunta")
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleTest> detalleTestList;

    //Relacion de muchos a uno con la entidad tipo
    @ToString.Exclude
    @ManyToOne
    private TipoPregunta tipo;
}
