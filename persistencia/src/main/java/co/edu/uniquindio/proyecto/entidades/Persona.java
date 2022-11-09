package co.edu.uniquindio.proyecto.entidades;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class Persona implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    //Atributo nombre de la persona
    @Column(nullable = false,length = 150)
    @Length(min = 2, max = 150, message = "El nombre debe tener mínimo 2 caracteres y máximo 150")
    @NotBlank(message = "El campo está vacío, debe ingresar un nombre")
    private String nombre;

    //Atributo apellido de la persona
    @Column(nullable = false,length = 150)
    @Length(min = 2, max = 150, message = "El apellido debe tener mínimo 2 caracteres y máximo 150")
    @NotBlank(message = "El campo está vacío, debe ingresar un apellido")
    private String apellido;

    //Atributo email de la persona
    @Column(nullable = true, unique = true)
    @Email(message = "Ingrese un email válido")
    private String email;

    //Atributo contrasena de la persona
    @Column(nullable = false, length = 100)
    @NotBlank(message = "El campo está vacío, debe ingresar una password")
    @Length(max = 100, message = "La contraseña no debe tener más de 100 carcteres")
    //@JsonIgnore
    private String password;

    //Atributo que representa la fecha de nacimiento de la persona
    @NotNull(message = "Ingrese la fecha de nacimiento")
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String fechaNacimiento;
}
