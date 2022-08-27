package co.edu.uniquindio.proyecto.entidades;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TipoPregunta implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    //Atributo que contiene el tipo de pregunta
    @Column(nullable = false)
    @Length(min = 2,  message = "El tipo debe tener mínimo 2 caracteres")
    @NotBlank(message = "El campo está vacío, debe ingresar un tipo de pregunta")
    private String descripcion;



}
