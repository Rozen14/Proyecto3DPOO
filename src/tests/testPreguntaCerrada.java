package tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import pregunta.PreguntaCerrada;
import java.util.Dictionary;
import java.util.Hashtable;
import pregunta.Opcion;

public class testPreguntaCerrada {
    
    private PreguntaCerrada pregunta;
    private Dictionary<Opcion, String> opcionA;
    private Dictionary<Opcion, String> opcionB;
    private Dictionary<Opcion, String> opcionC;
    private Dictionary<Opcion, String> opcionD;
    private Dictionary<Opcion, String> respuestaCorrecta;

    @BeforeEach
    public void setUp() {
        opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "Opción A");
        
        opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "Opción B");
        
        opcionC = new Hashtable<>();
        opcionC.put(Opcion.C, "Opción C");
        
        opcionD = new Hashtable<>();
        opcionD.put(Opcion.D, "Opción D");
        
        respuestaCorrecta = new Hashtable<>();
        respuestaCorrecta.put(Opcion.A, "Opción A"); // Suponiendo que la opción A es la correcta

        pregunta = new PreguntaCerrada("¿Cuál es la opción correcta?");
        pregunta.setOpcionA(opcionA);
        pregunta.setOpcionB(opcionB);
        pregunta.setOpcionC(opcionC);
        pregunta.setOpcionD(opcionD);
        pregunta.setRespuesta(respuestaCorrecta);
    }

    @Test
    public void testSetOpcionA() {
        assertEquals(opcionA, pregunta.getOpcionA());   
    }

    @Test
    public void testSetOpcionB() {
        assertEquals(opcionB, pregunta.getOpcionB());
    }

    @Test
    public void testSetOpcionC() {
        assertEquals(opcionC, pregunta.getOpcionC());
    }

    @Test
    public void testSetOpcionD() {
        assertEquals(opcionD, pregunta.getOpcionD());
    }

    @Test
    public void testSetRespuesta() {
        assertEquals(respuestaCorrecta, pregunta.getRespuesta());
    }

    @Test
    public void testElegirRespuestaOpcionA() {
        pregunta.elegirRespuesta("A");
        assertEquals(opcionA, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaOpcionB() {
        pregunta.elegirRespuesta("B");
        assertEquals(opcionB, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaOpcionC() {
        pregunta.elegirRespuesta("C");
        assertEquals(opcionC, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaOpcionD() {
        pregunta.elegirRespuesta("D");
        assertEquals(opcionD, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaIncorrectaB() {
        pregunta.elegirRespuesta("B");
        assertNotEquals(opcionA, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaIncorrectaC() {
        pregunta.elegirRespuesta("C");
        assertNotEquals(opcionA, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaIncorrectaD() {
        pregunta.elegirRespuesta("D");
        assertNotEquals(opcionA, pregunta.getEscogida());
    }

    @Test
    public void testElegirRespuestaInexistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pregunta.elegirRespuesta("E");
        });
        assertEquals("Opción no válida.", exception.getMessage());
    }

    @Test 
    public void testEsCorrecta() {
        pregunta.elegirRespuesta("A");
        assertTrue(pregunta.esCorrecta());
    }

    @Test
    public void testEsIncorrecta() {
        pregunta.elegirRespuesta("B");
        assertFalse(pregunta.esCorrecta());
    }

    @Test
    public void testEsIncorrectaSinSeleccion() {
        assertFalse(pregunta.esCorrecta());
    }

    @Test 
    public void testGetRetroalimentacionCorrecta() {
        pregunta.elegirRespuesta("A");
        assertEquals("Respuesta correcta.", pregunta.getRetroalimentacion());
    }

    @Test
    public void testGetRetroalimentacionIncorrecta() {
        pregunta.elegirRespuesta("B");
        assertEquals("Respuesta incorrecta. La opción correcta era: Opción A", pregunta.getRetroalimentacion());
    }

    @Test
    public void testGetRetroalimentacionSinSeleccion() {
        assertEquals("No se ha seleccionado ninguna opción.", pregunta.getRetroalimentacion());
    }

    @Test
    public void testEsEvaluada() {
        assertFalse(pregunta.esEvaluada());
        pregunta.elegirRespuesta("A");
        assertTrue(pregunta.esEvaluada());
    }
}