package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Test implements Serializable {

    //Llave primaria de la entidad que es autogenerada
    @Id
    @EqualsAndHashCode.Include
    private String id;

    //Relacion de muchos a uno con la entidad Usuario
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Profesor profesor;

    //Relacion de uno a muchos con detalleTest
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleTest> detalleTestList;

    public Test ( Profesor profesor, List<DetalleTest> detalleTestList)
    {
        this.profesor = profesor;
        this.detalleTestList = detalleTestList;
    }

    public Test (String id, Profesor profesor)
    {
        this.id = id;
        this.profesor = profesor;
    }
}

