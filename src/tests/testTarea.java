package tests;

import actividad.*;
import usuario.Estudiante;
import usuario.Profesor;
import LPRS.LearningPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class testTarea {

    private Tarea tarea;
    private Estudiante estudiante;
    private Profesor profesor;
    private LearningPath learningPath;
    private Map<Estudiante, Status> estadosPorEstudiante;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante = new Estudiante("Maria", "password", "maria@example.com");
        estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        // Crear la tarea
        tarea = new Tarea(
                "Tarea de Matemáticas",
                Nivel.Intermedio,
                "Resolver ejercicios algebraicos",
                120,
                1.0,
                LocalDateTime.now().plusDays(7),
                estadosPorEstudiante,
                Obligatoria.SI,
                "LMS",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        // Crear LearningPath asociado
        ArrayList<Actividad> actividades = new ArrayList<>();
        actividades.add(tarea);

        learningPath = new LearningPath(
                "Math Learning Path",
                Nivel.Intermedio,
                "Aprender fundamentos de álgebra",
                "Resolución de problemas algebraicos",
                150,
                profesor,
                4.5f,
                actividades
        );

        learningPath.inscripcionEstudiante(estudiante);
    }

    @Test
    public void testGetSubmissionMethod() {
        assertEquals("LMS", tarea.getSubmissionMethod());
    }

    @Test
    public void testResponderValido() {
        tarea.responder(estudiante, "Correo Electrónico");
        assertEquals("Correo Electrónico", tarea.getSubmissionMethod());
        assertEquals(Status.Enviada, estadosPorEstudiante.get(estudiante));
    }

    @Test
    public void testResponderInvalidoPorEstudianteNulo() {
        assertThrows(SecurityException.class, () -> tarea.responder(null, "Correo Electrónico"));
    }

    @Test
    public void testResponderInvalidoPorYaEnviado() {
        tarea.responder(estudiante, "Correo Electrónico");
        assertThrows(UnsupportedOperationException.class, () -> tarea.responder(estudiante, "LMS"));
    }

    @Test
    public void testResponderInvalidoPorYaExitoso() {
        tarea.responder(estudiante, "Correo Electrónico");
        estadosPorEstudiante.put(estudiante, Status.Completado);
        assertThrows(UnsupportedOperationException.class, () -> tarea.responder(estudiante, "LMS"));
    }

    @Test
    public void testEsExitosaValido() {
        estadosPorEstudiante.put(estudiante, Status.Completado);
        assertTrue(tarea.esExitosa(estudiante));
    }

    @Test
    public void testEsExitosaInvalidoPorNoCompletado() {
        assertFalse(tarea.esExitosa(estudiante));
    }



    @Test
    public void testEvaluarValido() {
        tarea.responder(estudiante, "Correo Electrónico");
        tarea.evaluar(profesor, estudiante, learningPath, 100, true);
        assertEquals(Status.Completado, estadosPorEstudiante.get(estudiante));
    }

    @Test
    public void testEvaluarInvalidoPorProfesorNoCreador() {
        Profesor otroProfesor = new Profesor("Pedro", "password", "pedro@example.com", new ArrayList<>(), new ArrayList<>());
        assertThrows(SecurityException.class, () -> tarea.evaluar(otroProfesor, estudiante, learningPath, 100, true));
    }

    @Test
    public void testEvaluarInvalidoPorProfesorNulo() {
        assertThrows(SecurityException.class, () -> tarea.evaluar(null, estudiante, learningPath, 100, true));
    }

    @Test
    public void testEvaluarInvalidoPorEstudianteNoInscrito() {
        Estudiante otroEstudiante = new Estudiante("Carlos", "password", "carlos@example.com");
        assertThrows(SecurityException.class, () -> tarea.evaluar(profesor, otroEstudiante, learningPath, 100, true));
    }

    @Test
    public void testEvaluarInvalidoPorActividadNoEnLearningPath() {
        Tarea tareaNoAsociada = new Tarea(
                "Tarea de Geometría",
                Nivel.Avanzado,
                "Resolver problemas de geometría avanzada",
                90,
                1.0,
                LocalDateTime.now().plusDays(10),
                estadosPorEstudiante,
                Obligatoria.NO,
                "Correo Electrónico",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );
        assertThrows(IllegalArgumentException.class, () -> tareaNoAsociada.evaluar(profesor, estudiante, learningPath, 100, true));
    }

    @Test
    public void testReintentarValido() {
        tarea.reintentar(estudiante);
        assertEquals(Status.Incompleto, estadosPorEstudiante.get(estudiante));
    }

    @Test
    public void testReintentarInvalidoPorYaExitosa() {
        estadosPorEstudiante.put(estudiante, Status.Completado);
        assertThrows(UnsupportedOperationException.class, () -> tarea.reintentar(estudiante));
    }

    @Test
    public void testReintentarInvalidoPorYaCompletada() {
        estadosPorEstudiante.put(estudiante, Status.Completado);
        assertThrows(UnsupportedOperationException.class, () -> tarea.reintentar(estudiante));
    }
}
