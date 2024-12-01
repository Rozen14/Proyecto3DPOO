package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import LPRS.LearningPath;
import actividad.*;

import java.time.LocalDateTime;
import java.util.*;
import usuario.*;
import pregunta.*;


public class testProfesor {

    private Profesor profesor;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private LearningPath learningPath;
    private Tarea tarea;
    private PreguntaCerrada preguntaCerrada;

    @BeforeEach
    void setUp() {
        profesor = new Profesor("Carlos", "5678", "carlos@example.com", new ArrayList<>(), new ArrayList<>());

        estudiante1 = new Estudiante("Maria", "1234", "maria@example.com");
        estudiante2 = new Estudiante("Juan", "abcd", "juan@example.com");

        // Creación de actividades con valores válidos
        List<Actividad> actividades = new ArrayList<>();
        actividades.add(new Tarea(
            "Tarea 1",
            Nivel.Principiante,
            "Resolver problemas básicos",
            30,
            1.0,
            LocalDateTime.now().plusDays(5), // Fecha límite en 5 días
            new HashMap<>(),
            Obligatoria.SI,
            "Online",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        ));

        // Creación del Learning Path
        learningPath = new LearningPath(
            "Learning Path 1",
            Nivel.Intermedio,
            "Un camino",
            "Objetivo del camino",
            50,
            profesor,
            4.5f,
            actividades
        );
        profesor.getLearningPathCreado().add(learningPath);
    }

    @Test
    public void testCrearLearningPath() {
        // Actividad obligatoria

        
        tarea = (new Tarea(
            "Tarea 2",
            Nivel.Intermedio,
            "Resolver problemas intermedios",
            60,
            1.0,
            LocalDateTime.now().plusDays(10), // Fecha límite en 10 días
            new HashMap<>(),
            Obligatoria.SI,
            "Online",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        ));

        ArrayList<Actividad> actividades = new ArrayList<>();
        actividades.add(tarea);
        profesor.crearLearningPath("Learning Path 2", Nivel.Avanzado, "Un camino", "Objetivo del camino", 60, 4.5f, actividades);
        assertEquals(2, profesor.getLearningPathCreado().size());
    
    }

    @Test
    public void testCrearLearningPathInvalidoPorFaltaDeActividades() {
        // Actividad obligatoria
        assertThrows(IllegalStateException.class, () -> {
            profesor.crearLearningPath("Learning Path 2", Nivel.Avanzado, "Un camino", "Objetivo del camino", 60, 4.5f, new ArrayList<>());
        });
    }



    @Test
    public void testGetLearningPathCreado() {
        assertEquals(1, profesor.getLearningPathCreado().size());
        assertEquals("Learning Path 1", profesor.getLearningPathCreado().get(0).getTitulo());
    }

    @Test
    public void testSetLearningPathCreado(){
        List<LearningPath> learningPaths = new ArrayList<>();
        learningPaths.add(learningPath);
        profesor.setLearningPathCreado(learningPaths);
        assertEquals(1, profesor.getLearningPathCreado().size());
        assertEquals("Learning Path 1", profesor.getLearningPathCreado().get(0).getTitulo());
    }

    @Test
    public void testGetEstudiantes() {
        assertEquals(0, profesor.getEstudiantes().size());
    }

    @Test
    public void testSetEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante1);
        profesor.setEstudiantes(estudiantes);
        assertEquals(1, profesor.getEstudiantes().size());
        assertEquals(estudiante1, profesor.getEstudiantes().get(0));
    }

    @Test
    public void testAgregarEstudiante() {
        profesor.agregarEstudiante(estudiante1);
        assertEquals(1, profesor.getEstudiantes().size());
        assertEquals(estudiante1, profesor.getEstudiantes().get(0));
    }




    @Test
    public void testAgregarEstudianteSiCorresponde() {
        learningPath.inscripcionEstudiante(estudiante1);
        profesor.agregarEstudianteSiCorresponde(learningPath, estudiante1, profesor);
        assertEquals(1, profesor.getEstudiantes().size());
        assertEquals(estudiante1, profesor.getEstudiantes().get(0));
    }


    @Test
    void testEliminarLearningPath() {

        // Inscribir al estudiante al learning path
        learningPath.inscripcionEstudiante(estudiante1);
        assertThrows(IllegalStateException.class, () -> profesor.eliminarLearningPath(learningPath));

        learningPath.getEstudiantesInscritos().clear(); // Vaciar lista de estudiantes
        profesor.eliminarLearningPath(learningPath);
        assertEquals(0, profesor.getLearningPathCreado().size());
    }

    @Test
    void testObtenerProgresoEstudiantes() {
        learningPath.inscripcionEstudiante(estudiante1);
        Map<String, Float> progreso = profesor.obtenerProgresoEstudiantes(learningPath);
        assertEquals(1, progreso.size());

    }

    @Test
    void testVerificarEstudianteCompletoLearningPath() {
        learningPath.inscripcionEstudiante(estudiante1);
        boolean completo = profesor.verificarEstudianteCompletoLearningPath(learningPath, estudiante1);
        assertFalse(completo);
    }
}

