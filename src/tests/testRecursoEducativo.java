package tests;

import actividad.RecursoEducativo;
import actividad.Nivel;
import actividad.Status;
import actividad.Obligatoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.Estudiante;
import usuario.Profesor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class testRecursoEducativo {

    private RecursoEducativo recursoEducativo;
    private Estudiante estudiante;
    private Profesor profesor;
    private Map<Estudiante, Status> estadosPorEstudiante;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante = new Estudiante("Maria", "password", "maria@example.com");
        estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        recursoEducativo = new RecursoEducativo(
                "Introducción a Java",
                Nivel.Intermedio,
                "Aprender los fundamentos del lenguaje Java",
                30,
                1.0,
                LocalDateTime.now().plusDays(7),
                estadosPorEstudiante,
                Obligatoria.NO,
                "video",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Test
    public void testGetTipoRecurso() {
        assertEquals("video", recursoEducativo.getTipoRecurso());
    }

    @Test
    public void testResponderValido() {
        recursoEducativo.responder(estudiante, "visto");
        assertEquals(Status.Completado, estadosPorEstudiante.get(estudiante));
    }

    @Test
    public void testResponderInvalidoPorRespuestaIncorrecta() {
        recursoEducativo.responder(estudiante, "no visto");
        assertEquals(Status.Incompleto, estadosPorEstudiante.get(estudiante));
    }

    @Test
    public void testResponderInvalidoPorEstudianteNulo() {
        assertThrows(SecurityException.class, () -> recursoEducativo.responder(null, "visto"));
    }

    @Test
    public void testResponderInvalidoPorYaCompletado() {
        estadosPorEstudiante.put(estudiante, Status.Completado);
        assertThrows(UnsupportedOperationException.class, () -> recursoEducativo.responder(estudiante, "visto"));
    }

    @Test
    public void testEsExitosaValido() {
        recursoEducativo.responder(estudiante, "visto");
        assertTrue(recursoEducativo.esExitosa(estudiante));
    }

    @Test
    public void testEsExitosaInvalidoPorNoCompletado() {
        assertFalse(recursoEducativo.esExitosa(estudiante));
    }

    @Test
    public void testEsExitosaInvalidoPorEstudianteNulo() {
        assertThrows(SecurityException.class, () -> recursoEducativo.esExitosa(null));
    }

    @Test
    public void testEvaluarNoImplementado() {
        recursoEducativo.evaluar(profesor, estudiante, null, 0.0, false); // No hace nada
    }

    @Test
    public void testReintentarNoImplementado() {
        recursoEducativo.reintentar(estudiante); // No hace nada
    }
}
