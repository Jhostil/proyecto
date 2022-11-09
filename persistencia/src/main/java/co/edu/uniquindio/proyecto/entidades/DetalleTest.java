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
public class DetalleTest implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    //Atributo que guarda la respuesta del usuario
    @ToString.Exclude
    private String respuesta;

    //Relacion de muchos a uno con la entidad Test
    @ToString.Exclude
    @ManyToOne
    private Test test;

    //Relacion de muchos a uno con la entidad Usuario
    @ToString.Exclude
    @ManyToOne
    private Usuario usuario;

    //Relacion de muchos a uno con la entidad Pregunta
    @ToString.Exclude
    @ManyToOne
    private Pregunta pregunta;

    //Atributo que representa la calificacion de la pregunta contestada
    @Column(name = "calificacion")
    private int calificacion;

    //Atributo que sirve para guardar la hora y fecha de cuando se contesta el test
    @Column(name = "fechaTest", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String fechaTest;
}
