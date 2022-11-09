package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Clase implements Serializable {

    //Llave primaria de la entidad
    @Id
    @EqualsAndHashCode.Include
    private String id;

    //Atributo que guarda el nombre de la clase
    private String nombre;

    //Relacion de muchos a uno con la entidad Profesor
    @ToString.Exclude
    @ManyToOne
    private Profesor profesor;

    //Relación de uno a muchos con la entidad UsuarioClase
    @OneToMany(mappedBy = "clase")
    @JsonIgnore
    @ToString.Exclude
    private List<UsuarioClase> alumnos;

    //Relación de uno a muchos con la entidad TestClase
    @OneToMany(mappedBy = "clase")
    @JsonIgnore
    @ToString.Exclude
    private List<TestClase> testClases;
}

