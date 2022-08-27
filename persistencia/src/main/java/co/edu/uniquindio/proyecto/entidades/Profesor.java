package co.edu.uniquindio.proyecto.entidades;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class Profesor extends Persona implements Serializable {
}
