package co.edu.uniquindio.proyecto.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PreguntaTest {

    @EqualsAndHashCode.Include
    private Integer id;
    private String pregunta;
    private String descripcion;

}
