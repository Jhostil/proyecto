package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.PreguntaTest;
import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.repositorios.PreguntaRepo;
import co.edu.uniquindio.proyecto.repositorios.TestRepo;
import co.edu.uniquindio.proyecto.repositorios.TipoPreguntaRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PreguntaServicioImpl implements PreguntaServicio{

    private final PreguntaRepo preguntaRepo;

    private final TipoPreguntaRepo tipoPreguntaRepo;

    private final TestRepo testRepo;

    public PreguntaServicioImpl (PreguntaRepo preguntaRepo, TipoPreguntaRepo tipoPreguntaRepo, TestRepo testRepo)
    {
        this.preguntaRepo = preguntaRepo;
        this.tipoPreguntaRepo = tipoPreguntaRepo;
        this.testRepo = testRepo;
    }

    @Override
    public List<TipoPregunta> listarTiposPregunta() {
        return tipoPreguntaRepo.findAll();
    }

    @Override
    public List<Pregunta> listarPreguntas() {
        return preguntaRepo.findAll();
    }

    @Override
    public Pregunta guardarPregunta(Pregunta p) throws Exception {
        try {
            return preguntaRepo.save(p);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Pregunta obtenerPregunta(Integer codigo) throws Exception {
        return preguntaRepo.findById(codigo).orElseThrow(() -> new Exception("El código del producto no es válido"));
    }

    @Override
    public Test generarTest(Profesor profesor, ArrayList<PreguntaTest> preguntaTests) throws Exception{

        try {
            Test test = new Test();
            test.setProfesor(profesor);

            String idTest = getRandomString();
            boolean codigo = verificarId(idTest);

            while (codigo == false)
            {
                idTest = getRandomString();
                codigo = verificarId(idTest);
            }

            test.setId(idTest);
            Test testGuardado = testRepo.save(test);

            DetalleTest dt;
            for (PreguntaTest p : preguntaTests){
                dt = new DetalleTest();
                dt.setPregunta(preguntaRepo.getById(p.getId()));
                dt.setTest(testGuardado);
                System.out.println(p.getId());
            }
            return testGuardado;
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public String getRandomString()
    {
        String theAlphaNumericS;
        StringBuilder builder;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(5);

        for (int m = 0; m < 5; m++) {

            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * Math.random());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    public boolean verificarId (String id)
    {
        //Test t = testRepo.findById(id).orElseThrow(() -> new Exception("Los datos de autenticación son incorrectos"));
        Optional<Test> buscado = testRepo.findById(id);

        if (buscado.isEmpty()){
            return true; //ID está disponible
        }
        return false; //EL ID ya existe

    }

}
