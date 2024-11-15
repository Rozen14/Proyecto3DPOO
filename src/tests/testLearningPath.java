package LPRS;

import actividad.*;
import actividad.Nivel;
import actividad.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.Estudiante;
import usuario.Profesor;
import LPRS.LearningPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LearningPathTest {

    private LearningPath learningPath;
    private Profesor profesor;
    private Estudiante estudiante1, estudiante2;
    private Tarea actividadObligatoria;
    private Quiz actividadOpcional;

    @BeforeEach
    void setUp() {
        // Configuración inicial antes de cada prueba
        profesor = new Profesor("Juan", "1234", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        learningPath = new LearningPath("Test Path", Nivel.Intermedio, "Descripción", "Objetivos", 60, profesor, 4.5f, new ArrayList<>());
        estudiante1 = new Estudiante("Maria", "1234", "maria@example.com");
        estudiante2 = new Estudiante("Carlos", "5678", "carlos@example.com");

        // Crear actividades de ejemplo
        actividadObligatoria = new Tarea(
            "Tarea Obligatoria",
            Nivel.Principiante,
            "Resolver problemas básicos",
            30,
            1.0,
            LocalDateTime.now().plusDays(5),
            Map <estudiante1, Status> estadosPorEstudiante,
            Status.Incompleto,
            true,
            "LMS",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        actividadOpcional = new Quiz(
            "Quiz Opcional",
            Nivel.Principiante,
            "Prueba básica de lógica",
            20,
            1.0,
            LocalDateTime.now().plusDays(3),
            Status.Incompleto,
            false,
            new ArrayList<>(),
            60.0,
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Test
    void testAgregarActividad() {
        // Prueba agregar actividad válida
        learningPath.agregarActividad(actividadObligatoria);
        assertTrue(learningPath.getListaActividades().contains(actividadObligatoria));

        // Prueba agregar actividad duplicada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            learningPath.agregarActividad(actividadObligatoria);
        });
        assertEquals("La actividad ya está en la lista de actividades del Learning Path.", exception.getMessage());
    }

    @Test
    void testEliminarActividad() {
        // Configurar estado inicial
        learningPath.agregarActividad(actividadObligatoria);

        // Prueba eliminar actividad válida
        learningPath.eliminarActividad(actividadObligatoria);
        assertFalse(learningPath.getListaActividades().contains(actividadObligatoria));

        // Prueba eliminar actividad inexistente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            learningPath.eliminarActividad(actividadObligatoria);
        });
        assertEquals("La actividad no está en la lista de actividades del Learning Path.", exception.getMessage());
    }

    @Test
    void testValidarActividadesObligatorias() {
        // Sin actividades obligatorias
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            learningPath.validarActividadesObligatorias();
        });
        assertEquals("El Learning Path debe contener al menos una actividad obligatoria.", exception.getMessage());

        // Con actividad obligatoria
        learningPath.agregarActividad(actividadObligatoria);
        assertDoesNotThrow(() -> learningPath.validarActividadesObligatorias());
    }

    @Test
    void testInscripcionEstudiante() {
        // Prueba inscripción válida
        learningPath.inscripcionEstudiante(estudiante1);
        assertTrue(learningPath.verificarSiInscrito(estudiante1));

        // Prueba inscripción duplicada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            learningPath.inscripcionEstudiante(estudiante1);
        });
        assertEquals("El estudiante ya está inscrito en este Learning Path.", exception.getMessage());
    }

    @Test
    void testCalcularProgreso() {
        // Configurar actividades y progreso del estudiante
        learningPath.agregarActividad(actividadObligatoria);
        learningPath.actividadObligatoriaCompletada(actividadObligatoria, estudiante1);

        float progreso = learningPath.calcularProgreso(estudiante1);
        assertEquals(100.0f, progreso, 0.01);

        // Estudiante sin progreso
        assertEquals(0.0f, learningPath.calcularProgreso(estudiante2), 0.01);
    }
}
