package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import actividad.*;
import LPRS.LearningPath;

import java.time.LocalDateTime;
import java.util.*;

import usuario.*;

public class testEstudiante {

    private Estudiante estudiante;
    private LearningPath learningPath;
    private Profesor profesor;
    private Tarea tarea;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante("Maria", "1234", "maria@example.com");
        profesor = new Profesor("Juan", "3212", "juan@example.com", new ArrayList<>(), new ArrayList<>());

        tarea = new Tarea("Tarea 1", Nivel.Principiante, "Resolver problemas básicos", 60, 1.0,
                LocalDateTime.now().plusDays(7), new HashMap<>(), Obligatoria.SI, "LMS", profesor,
                new ArrayList<>(), new ArrayList<>());
        quiz = new Quiz("Quiz 1", Nivel.Intermedio, "Evaluar conceptos", 30, 1.0,
                LocalDateTime.now().plusDays(7), new HashMap<>(), Obligatoria.NO, new ArrayList<>(),
                50, profesor, new ArrayList<>(), new ArrayList<>());
        List<Actividad> actividades = Arrays.asList(tarea, quiz);
        learningPath = new LearningPath("Learning Path 1", Nivel.Intermedio, "Un camino", "Objetivo", 50, null, 4.5f, actividades);
    }

    @Test
    void testCreacionEstudiante() {
        assertEquals("Maria", estudiante.getNombre());
        assertEquals("1234", estudiante.getContrasenia());
        assertEquals("estudiante", estudiante.getTipo());
        assertEquals("maria@example.com", estudiante.getCorreo());
        assertNull(estudiante.getActividadActual());
        assertNull(estudiante.getLearningPathActual());
    }

    @Test
    void testComenzarLearningPath() {
        estudiante.comenzarLearningPath(learningPath);
        assertEquals(learningPath, estudiante.getLearningPathActual());
        assertEquals(2, estudiante.listarActividadesPendientes().size());
    }

    @Test
    void testMarcarTareaCompletada() {
        estudiante.comenzarLearningPath(learningPath);
        estudiante.setActividadActual(tarea);

        estudiante.marcarTareaCompletada(tarea, "Online");
        assertTrue(estudiante.getListaActividadesCompletadas().contains(tarea));
    }

    @Test
    void testListarLearningPathsCompletados() {
        learningPath.inscripcionEstudiante(estudiante);
        tarea.inscripcionEstudiante(estudiante);
        tarea.responder(estudiante, "SMS");
        tarea.evaluar(profesor, estudiante, learningPath, 100, true);
        learningPath.actividadObligatoriaCompletada(tarea, estudiante);
        quiz.inscripcionEstudiante(estudiante);
        estudiante.setActividadActual(quiz);
        quiz.setStatusParaEstudiante(estudiante, Status.Completado);
        learningPath.actividadObligatoriaCompletada(quiz, estudiante);
        estudiante.actualizarActividadesPorCompletar();

        // hacer la verificaicon de que se ha completado el learning path

        learningPath.calcularProgreso(estudiante);




        assertEquals(1, estudiante.listarLearningPathsCompletados().size());
    }

    @Test
    void testObtenerProgresoLearningPathActual() {
        estudiante.comenzarLearningPath(learningPath);
        estudiante.setActividadActual(tarea);

        estudiante.marcarTareaCompletada(tarea, "Online");
        assertEquals(50.0, estudiante.obtenerProgresoLearningPathActual());
    }

    @Test

    public void getListaActividadesPorCompletar() {
        estudiante.comenzarLearningPath(learningPath);
        estudiante.actualizarActividadesPorCompletar();
        assertEquals(2, estudiante.getListaActividadesPorCompletar().size());
    }

    @Test
    public void getActividadesPreviasSugeridas() {

        estudiante.comenzarLearningPath(learningPath);
        estudiante.actualizarActividadesPorCompletar();
        // Inscribirme a un quiz
        estudiante.setActividadActual(quiz);
        estudiante.actualizarActividadesPreviasSugeridas();
        assertEquals(0, estudiante.getActividadesPreviasSugeridas().size());
    }

    @Test
    public void actividadesPreviasSugeridasRealizadas() {
        estudiante.comenzarLearningPath(learningPath);
        estudiante.actualizarActividadesPorCompletar();
        assertTrue(estudiante.actividadesPreviasSugeridasRealizadas(estudiante.getActividadesPreviasSugeridas()));
    }
}
