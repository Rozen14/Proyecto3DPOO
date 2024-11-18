package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.*;
import actividad.*;
import pregunta.Opcion;
import pregunta.Pregunta;
import pregunta.PreguntaAbierta;
import pregunta.PreguntaCerrada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import LPRS.LearningPath;

import static org.junit.jupiter.api.Assertions.*;


public class testExamen {
 
    private Examen examen; // Examen inicial que será usado en todos los tests
    private Profesor profesor;
    private Estudiante estudiante;
    private Map<Estudiante, Status> estadosPorEstudiante;
    private List<Pregunta> listaPreguntas;
    private LearningPath learningPath;

    @BeforeEach
    public void setUp() {
    profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
    estudiante = new Estudiante("Maria", "password", "maria@example.com");
    estadosPorEstudiante = new HashMap<>();
    estadosPorEstudiante.put(estudiante, Status.Incompleto);

        listaPreguntas = new ArrayList<>();

        // Configurar PreguntaCerrada
        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Configurar PreguntaAbierta
        PreguntaAbierta preguntaAbierta = new PreguntaAbierta("¿Qué es Java?");
        
        listaPreguntas.add(preguntaCerrada);
        listaPreguntas.add(preguntaAbierta);

    examen = new Examen(
            "Examen Inicial",
            Nivel.Intermedio,
            "Probar conocimientos básicos",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            estadosPorEstudiante,
            Obligatoria.SI,
            listaPreguntas,
            50.0,
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
    );

    

    // Crear LearningPath y asociarlo con el examen y estudiante
    List<Actividad> actividades = new ArrayList<>();
    actividades.add(examen);
    
    learningPath = new LearningPath(
                "Learning Path Inicial",
                Nivel.Intermedio,
                "Camino de aprendizaje de prueba",
                "Objetivo general",
                120,
                 profesor,
                4.5f,
                actividades
        );
    
    learningPath.inscripcionEstudiante(estudiante);
    }

    @Test
    public void testGetListaPreguntas() {
        assertEquals(listaPreguntas, examen.getListaPreguntas());
    }

    @Test
    public void testGetCalificacionMinima(){
        assertEquals(50.0, examen.getCalificacionMinima());
    }

    @Test
    public void testGetCalificacionObtenida(){

        // Deberia ser 0 y ya al inicio

        assertEquals(0, examen.getCalificacionObtenida());

    }

    @Test
    public void testGetRespuestasCorrectas(){

        // Deberia ser un 0 al inicio

        assertEquals(0, examen.getRespuestasCorrectas());

    }

    @Test
    public void testGetRespuestasAbiertas(){

        // Deberia ser un arraylist vacio al inicio

        assertEquals(new ArrayList<>(), examen.getRespuestasAbiertas());

    }   

    @Test

    public void testSetListaPreguntas(){

        // Crear una nueva lista de preguntas

        List<Pregunta> listaPreguntasNueva = new ArrayList<>();

        // Configurar PreguntaCerrada
        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Configurar PreguntaAbierta
        PreguntaAbierta preguntaAbierta = new PreguntaAbierta("¿Qué es Java?");
        
        listaPreguntasNueva.add(preguntaCerrada);
        listaPreguntasNueva.add(preguntaAbierta);

        examen.setListaPreguntas(listaPreguntasNueva);

        assertEquals(listaPreguntasNueva, examen.getListaPreguntas());

    }

    @Test

    public void testSetCalificacionObtenida(){

        // Cambiar la calificacion obtenida

        examen.setCalificacionObtenida(100);

        assertEquals(100, examen.getCalificacionObtenida());

    }

    @Test

    public void testSetRespuestasCorrectas(){

        // Cambiar la cantidad de respuestas correctas

        examen.setRespuestasCorrectas(1);

        assertEquals(1, examen.getRespuestasCorrectas());

    }

    @Test

    public void testSetRespuestasAbiertas(){

        // Crear una nueva lista de respuestas abiertas

        List<String> respuestasAbiertas = new ArrayList<>();

        respuestasAbiertas.add("Java es un lenguaje de programación");

        examen.setRespuestasAbiertas(respuestasAbiertas);

        assertEquals(respuestasAbiertas, examen.getRespuestasAbiertas());

    }

    @Test

    public void testResponderValido(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Verificar que la respuesta abierta se haya guardado correctamente

        assertEquals("Java es un lenguaje de programación", examen.getRespuestasAbiertas().get(0));

        // Verificar que la respuesta cerrada se haya guardado correctamente

        assertEquals(1, examen.getRespuestasCorrectas());

        // Verificar que el estado del estudiante sea Enviado

        assertEquals(Status.Enviada, examen.getEstadosPorEstudiante().get(estudiante));
    }

    @Test
    public void testResponderInvalidoPorEstudianteNulo(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(null, "B;Java es un lenguaje de programación"));

    }

    @Test
    public void testResponderInvalidoPorRespuestaNula(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, null));

    }


    @Test
    public void testResponderInvalidoPorRespuestaVacia(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, ""));

    }

    @Test
    public void testResponderInvalidoPorYaRespondida(){

        // Set el estado del estudiante a Enviado
        examen.getEstadosPorEstudiante().put(estudiante, Status.Enviada);

        // Responder la pregunta cerrada y abierta
        assertThrows(UnsupportedOperationException.class, () -> examen.responder(estudiante, "B;Java es un lenguaje de programación"));

    }

    @Test
    public void testResponderInvalidoPorYaCompleta(){

        // Set el estado del estudiante a Completada
        examen.getEstadosPorEstudiante().put(estudiante, Status.Completado);

        // Responder la pregunta cerrada y abierta
        assertThrows(UnsupportedOperationException.class, () -> examen.responder(estudiante, "B;Java es un lenguaje de programación"));

    }

    @Test

    public void testResponderInvalidoPorTamanioDeRespuestaIncorrecto(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, "B"));
    }






}


//        // Responder la pregunta cerrada y abierta
//examen.responder(estudiante, "B;Java es un lenguaje de programación");

// Profesor preguntas abiertas del examen

//for (Pregunta pregunta : examen.getListaPreguntas()) {
  //  if (pregunta instanceof PreguntaAbierta) {
   //     PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
        // Evaluar la pregunta abierta

    //    preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");


// examen.evaluar(profesor, estudiante, learningPath, 100, true);


// Verificar la calificación obtenida
// assertEquals(100, examen.getCalificacionObtenida());