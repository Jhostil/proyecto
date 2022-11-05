package co.edu.uniquindio.proyecto.entidades;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class UsuarioClase implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    //Relacion de muchos a uno con la entidad Usuario
    @ToString.Exclude
    @ManyToOne
    private Usuario usuario;

    //Relacion de muchos a uno con la entidad Clase
    @ToString.Exclude
    @ManyToOne
    private Clase clase;

}
