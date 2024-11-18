package tests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import actividad.Encuesta;
import actividad.Nivel;
import actividad.Obligatoria;
import pregunta.PreguntaAbierta;
import usuario.Estudiante;
import usuario.Profesor;
import actividad.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.*;
import actividad.*;
import pregunta.PreguntaAbierta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class testEncuesta {

    private Encuesta encuesta; // Encuesta inicial que será usada en todos los tests
    private Profesor profesor;
    private Estudiante estudiante;
    private Map<Estudiante, Status> estadosPorEstudiante;
    private ArrayList<PreguntaAbierta> preguntas;

    @BeforeEach

    void setUp() {
        profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante = new Estudiante("Maria", "password", "maria@example.com");
        estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        preguntas = new ArrayList<>();
        preguntas.add(new PreguntaAbierta("¿Cuál es tu objetivo principal?"));
        preguntas.add(new PreguntaAbierta("¿Qué habilidades esperas mejorar?"));

        encuesta = new Encuesta(
                "Encuesta Inicial",
                Nivel.Principiante,
                "Recolectar información inicial",
                10,
                1.0,
                LocalDateTime.now().plusDays(5),
                estadosPorEstudiante,
                Obligatoria.NO,
                new ArrayList<>(),
                new ArrayList<>(),
                profesor,
                preguntas
        );
    }
    
    @Test
    public void testGetListaPreguntas() {
        assertEquals(2, encuesta.getListaPreguntas().size());
        assertEquals("¿Cuál es tu objetivo principal?", encuesta.getListaPreguntas().get(0).getEnunciado());
    }

    @Test
    public void testCrearEncuesta() {
        assertNotNull(encuesta);
        assertEquals("Encuesta Inicial", encuesta.getDescripcion());
        assertEquals(2, encuesta.getListaPreguntas().size());
        assertEquals("¿Cuál es tu objetivo principal?", encuesta.getListaPreguntas().get(0).getEnunciado());
    }

    @Test
    public void testResponderEncuesta() {
        encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación");
        assertEquals(Status.Completado, encuesta.getEstadosPorEstudiante().get(estudiante));
        assertEquals("Aprender a programar", encuesta.getListaPreguntas().get(0).getRespuestaEstudiante());
        assertEquals("Mejorar mis habilidades de programación", encuesta.getListaPreguntas().get(1).getRespuestaEstudiante());
    }

    @Test
    public void testResponderEncuestaInvalidaPorEstudianteNulo() {
        assertThrows(SecurityException.class, () -> encuesta.responder(null, "Aprender a programar,Mejorar mis habilidades de programación"));
    }

    @Test
    public void testResponderEncuestaInvaldiaPorCompletadoYa() {
        encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación");
        assertThrows(UnsupportedOperationException.class, () -> encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación"));
    }

    @Test
    public void testResponderEncuestaInvalidaPorRespuestasIncompletas() {
        assertThrows(IllegalArgumentException.class, () -> encuesta.responder(estudiante, "Aprender a programar"));
    }

    @Test
    public void testResponderEncuestaInvalidaPorRespuestasExcesivas() {
        assertThrows(IllegalArgumentException.class, () -> encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación,Mejorar mis habilidades de programación"));
    }

    @Test
    public void testResponderEncuestaInvalidaPorRespuestasVacias() {
        assertThrows(IllegalArgumentException.class, () -> encuesta.responder(estudiante, ""));
    }

    @Test
    public void testEsExitosa() {
        encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación");
        assertTrue(encuesta.esExitosa(estudiante));
    }

    @Test
    public void testEsExitosaInvalidaPorNoCompletada() {
        assertFalse(encuesta.esExitosa(estudiante));
    }

    @Test
    public void testEsExitosaInvalidaPorEstudianteNulo() {
        assertThrows(SecurityException.class, () -> encuesta.esExitosa(null));
    }

    @Test
    public void testEsExitosaInvalidaPorEstadoIncompleto() {
        encuesta.responder(estudiante, "Aprender a programar,Mejorar mis habilidades de programación");
        encuesta.getEstadosPorEstudiante().put(estudiante, Status.Incompleto);
        assertFalse(encuesta.esExitosa(estudiante));
    }

    @Test
    public void testEvaluar() {
        assertThrows(UnsupportedOperationException.class, () -> encuesta.evaluar(profesor, estudiante, null, 0, false));
    }

    @Test
    public void testReintentar() {
        assertThrows(UnsupportedOperationException.class, () -> encuesta.reintentar(estudiante));
    }

    @Test
    public void testAgregarPregunta() {
        encuesta.agregarPregunta(new PreguntaAbierta("¿Cuál es tu lenguaje de programación favorito?"));
        assertEquals(3, encuesta.getListaPreguntas().size());
        assertEquals("¿Cuál es tu lenguaje de programación favorito?", encuesta.getListaPreguntas().get(2).getEnunciado());
    }

    @Test
    public void testAgregarPreguntaInvalidaPorPreguntaNula() {
        assertThrows(IllegalArgumentException.class, () -> encuesta.agregarPregunta(null));
    }

    @Test
    public void testEliminarPregunta() {
        encuesta.eliminarPregunta(encuesta.getListaPreguntas().get(0));
        assertEquals(1, encuesta.getListaPreguntas().size());
        assertEquals("¿Qué habilidades esperas mejorar?", encuesta.getListaPreguntas().get(0).getEnunciado());
    }

    @Test
    public void testEliminarPreguntaInvalidaPorPreguntaNula() {
        // Agregar una pregunta para poder eliminarla
        encuesta.agregarPregunta(new PreguntaAbierta("¿Cuál es tu lenguaje de programación favorito?"));
        assertThrows(IllegalArgumentException.class, () -> encuesta.eliminarPregunta(null));
    }

    @Test
    public void testEliminarPreguntaInvalidaPorCantidadMinima() {

        // Eliminar todas las preguntas
        encuesta.eliminarPregunta(encuesta.getListaPreguntas().get(0));

        // Intentar eliminar la última pregunta
        assertThrows(UnsupportedOperationException.class, () -> encuesta.eliminarPregunta(encuesta.getListaPreguntas().get(0)));
    }

    @Test
    public void testEliminarPreguntaInvalidaPorPreguntaInexistente() {
        assertThrows(IllegalArgumentException.class, () -> encuesta.eliminarPregunta(new PreguntaAbierta("¿Cuál es tu lenguaje de programación favorito?")));
    }



}
