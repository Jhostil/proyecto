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

    //Llave primaria de la entidad
    @Id
    @EqualsAndHashCode.Include
    private String id;

    //Atributo que sirve para guardar la hora y fecha de cuando se hace el test
    @Column(name = "fechaTest", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String fechaTest;

    //Relacion de muchos a uno con la entidad Usuario
    @ToString.Exclude
    @ManyToOne
    private Usuario usuario;

    //Relacion de muchos a uno con la entidad Profesor
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Profesor profesor;

    //Relacion de uno a muchos con detalleTest
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleTest> detalleTestList;

    //Relacion de uno a muchos con testClase
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    @ToString.Exclude
    private List<TestClase> testClases;

    public Test (Usuario usuario, Profesor profesor, List<DetalleTest> detalleTestList)
    {
        this.profesor = profesor;
        this.detalleTestList = detalleTestList;
        this.usuario = usuario;
    }

    public Test (String id, Profesor profesor)
    {
        this.id = id;
        this.profesor = profesor;
    }
}

