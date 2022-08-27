package co.edu.uniquindio.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    //Atributo que sirve para guardar la hora y fecha de cuando se hace el test
    @Column(name = "fechaTest", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate fechaTest;

    //Relacion de muchos a uno con la entidad Usuario
    @ToString.Exclude
    @ManyToOne
    private Usuario usuario;

    //Relacion de uno a muchos con detalleTest
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    @ToString.Exclude
    private List<DetalleTest> detalleTestList;


}

