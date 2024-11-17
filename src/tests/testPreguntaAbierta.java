package tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pregunta.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testPreguntaAbierta {
    
    private PreguntaAbierta pregunta;

    @BeforeEach
    public void setUp() {
        pregunta = new PreguntaAbierta("¿Cuál es la capital de Francia?");
    }

    @Test
    public void testSetRespuestaEstudiante() {
        pregunta.setRespuestaEstudiante("París");
        assertEquals("París", pregunta.getRespuestaEstudiante());
    }

    @Test
    public void testGetRespuestaEstudiante() {
        pregunta.setRespuestaEstudiante("París");
        assertEquals("París", pregunta.getRespuestaEstudiante());
    }

    @Test
    public void testEvaluarPorProfesor() {
        pregunta.evaluarPorProfesor(true, "¡Correcto!");
        assertEquals(true, pregunta.esEvaluada());
        assertEquals(true, pregunta.esCorrecta());
        assertEquals("Respuesta correcta. ¡Correcto!", pregunta.getRetroalimentacion());
    }

    @Test
    public void testEsEvaluada() {
        assertEquals(false, pregunta.esEvaluada());
        pregunta.evaluarPorProfesor(true, "¡Correcto!");
        assertEquals(true, pregunta.esEvaluada());
    }

    @Test
    public void testGetRetroalimentacion() {
        assertEquals("Pendiente de evaluación por el profesor.", pregunta.getRetroalimentacion());
        pregunta.evaluarPorProfesor(true, "¡Correcto!");
        assertEquals("Respuesta correcta. ¡Correcto!", pregunta.getRetroalimentacion());
    }

    @Test
    public void testGetRetroalimentacionIncorrecta() {
        assertEquals("Pendiente de evaluación por el profesor.", pregunta.getRetroalimentacion());
        pregunta.evaluarPorProfesor(false, "¡Incorrecto!");
        assertEquals("Respuesta incorrecta. ¡Incorrecto!", pregunta.getRetroalimentacion());
    }

    @Test
    public void testGetRetroalimentacionNoEvaluada() {
        assertEquals("Pendiente de evaluación por el profesor.", pregunta.getRetroalimentacion());
    }

    @Test
    public void testEsCorrecta() {
        assertEquals(false, pregunta.esCorrecta());
        pregunta.evaluarPorProfesor(true, "¡Correcto!");
        assertEquals(true, pregunta.esCorrecta());
    } 
    
    @Test
    public void testEsCorrectaIncorrecta() {
        assertEquals(false, pregunta.esCorrecta());
        pregunta.evaluarPorProfesor(false, "¡Incorrecto!");
        assertEquals(false, pregunta.esCorrecta());
    }

    @Test
    public void testEsCorrectaNoEvaluada() {
        assertEquals(false, pregunta.esCorrecta());
    }

  

}
