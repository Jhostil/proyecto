package co.edu.uniquindio.proyecto.entidades;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TestClase implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    //Atributo que permite conocer si un test está activo o no
    private boolean activo;

    //Relacion de muchos a uno con la entidad Clase
    @ToString.Exclude
    @ManyToOne
    private Clase clase;

    //Relacion de muchos a uno con la entidad Clase
    @ToString.Exclude
    @ManyToOne
    private Test test;

}
