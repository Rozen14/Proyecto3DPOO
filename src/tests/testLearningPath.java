package tests;

import actividad.*;
import usuario.Estudiante;
import usuario.Profesor;
import LPRS.LearningPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class testLearningPath {

    private LearningPath learningPath;
    private Profesor profesor;
    private Estudiante estudiante1, estudiante2;
    private List<Actividad> actividades;
    private Tarea actividad1;
    private Quiz actividad2;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante1 = new Estudiante("Maria", "password", "maria@example.com");
        estudiante2 = new Estudiante("Pedro", "password", "pedro@example.com");

        // Crear actividades
        actividades = new ArrayList<>();
        actividad1 = new Tarea("Tarea 1", Nivel.Principiante, "Resolver problemas básicos", 60, 1.0,
                LocalDateTime.now().plusDays(7), new HashMap<>(), Obligatoria.SI, "LMS", profesor,
                new ArrayList<>(), new ArrayList<>());
        actividad2 = new Quiz("Quiz 1", Nivel.Intermedio, "Evaluar conceptos", 30, 1.0,
                LocalDateTime.now().plusDays(7), new HashMap<>(), Obligatoria.NO, new ArrayList<>(),
                50, profesor, new ArrayList<>(), new ArrayList<>());

        actividades.add(actividad1);
        actividades.add(actividad2);
        



        // Crear LearningPath
        learningPath = new LearningPath("Path Matemáticas", Nivel.Intermedio, "Descripción del path",
                "Aprender álgebra", 120, profesor, 4.5f, actividades);

        // Inscribir estudiantes

        learningPath.inscripcionEstudiante(estudiante1);
    }

    

    // Test para stters y getters

    @Test
    public void testGetTitulo() {
        assertEquals("Path Matemáticas", learningPath.getTitulo());
    }

    @Test
    public void testGetNivelDificultad() {
        assertEquals(Nivel.Intermedio, learningPath.getNivelDificultad());
    }

    @Test
    public void testGetDescripcion() {
        assertEquals("Descripción del path", learningPath.getDescripcion());
    }

    @Test
    public void testGetObjetivo() {
        assertEquals("Aprender álgebra", learningPath.getObjetivos());
    }

    @Test
    public void testGetDuracion() {
        assertEquals(120, learningPath.getDuracionMinutos());
    }

    @Test
    public void testGetFechaCreacion() {
        assertNotNull(learningPath.getFechaCreacion());
    }   

    @Test
    public void testGetFechaModificacion() {
        assertNotNull(learningPath.getFechaModificacion());
    }

    @Test
    public void testGetVersion(){
        assertEquals(1.0f, learningPath.getVersion());
    }

    @Test
    public void testGetStatusParaEstudiante() {
        assertEquals(Status.Incompleto, learningPath.getStatusParaEstudiante(estudiante1));
    }

    @Test
    public void testSetTitulo() {
        learningPath.setTitulo("Path Física");
        assertEquals("Path Física", learningPath.getTitulo());
    }

    @Test
    public void testSetNivelDificultad() {
        learningPath.setNivelDificultad(Nivel.Avanzado);
        assertEquals(Nivel.Avanzado, learningPath.getNivelDificultad());
    }

    @Test
    public void testSetDescripcion() {
        learningPath.setDescripcion("Descripción de Física");
        assertEquals("Descripción de Física", learningPath.getDescripcion());
    }

    @Test
    public void testSetObjetivo() {
        learningPath.setObjetivos("Aprender mecánica");
        assertEquals("Aprender mecánica", learningPath.getObjetivos());
    }

    @Test
    public void testSetDuracion() {
        learningPath.setDuracionMinutos(180);
        assertEquals(180, learningPath.getDuracionMinutos());
    }   

    @Test
    public void testSetFechaModificacion() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        learningPath.setFechaModificacion(nuevaFecha);
        assertEquals(nuevaFecha, learningPath.getFechaModificacion());
    }

    @Test
    public void testSetVersion(){
        learningPath.setVersion();
        assertEquals(2.0f, learningPath.getVersion());
    }

    @Test
    public void testSetStatusParaEstudiante() {
        learningPath.setStatusParaEstudiante(estudiante1, Status.Completado);
        assertEquals(Status.Completado, learningPath.getStatusParaEstudiante(estudiante1));
    }

    @Test
    public void setStatusParaEstudiantes(){
        // Para todo un hasmap de estudiantes

        HashMap<Estudiante, Status> statusEstudiantes = new HashMap<>();
        statusEstudiantes.put(estudiante1, Status.Completado);
        statusEstudiantes.put(estudiante2, Status.Incompleto);

        learningPath.setStatusParaEstudiantes(statusEstudiantes);

        assertEquals(Status.Completado, learningPath.getStatusParaEstudiante(estudiante1));
        assertEquals(Status.Incompleto, learningPath.getStatusParaEstudiante(estudiante2));
    }

    @Test
    public void testAgregarActividad() {
        Actividad nuevaActividad = new Tarea("Tarea 2", Nivel.Principiante, "Nueva tarea", 40, 1.0,
                LocalDateTime.now().plusDays(5), new HashMap<>(), Obligatoria.SI, "LMS", profesor,
                new ArrayList<>(), new ArrayList<>());

        learningPath.agregarActividad(nuevaActividad);

        assertTrue(learningPath.getListaActividades().contains(nuevaActividad));
        assertEquals(3, learningPath.getListaActividades().size());
    }

    @Test
    public void testAgregarActividadDuplicada() {
        assertThrows(IllegalArgumentException.class, () -> learningPath.agregarActividad(actividades.get(0)));
    }

    @Test
    public void testEliminarActividadValida() {
        Actividad actividadAEliminar = actividades.get(1);
        learningPath.eliminarActividad(actividadAEliminar);

        assertFalse(learningPath.getListaActividades().contains(actividadAEliminar));
    }

    @Test
    public void testEliminarActividadInvalidaPorNoExistente() {
        Actividad actividadInexistente = new Tarea("Tarea inexistente", Nivel.Avanzado, "No existe", 60, 1.0,
                LocalDateTime.now().plusDays(5), new HashMap<>(), Obligatoria.NO, "Correo", profesor,
                new ArrayList<>(), new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> learningPath.eliminarActividad(actividadInexistente));
    }

    @Test
    public void testEliminarActividadInvalidaPorSinObligatoria() {
        Actividad actividadObligatoria = actividades.get(0);
        assertThrows(IllegalStateException.class, () -> learningPath.eliminarActividad(actividadObligatoria));
        
    }

    @Test
    public void testTieneActividadObligatoria() {
        assertTrue(learningPath.tieneActividadObligatoria());
    }

    @Test
    public void testRegistrarLearningPathSinObligatoria() {
        // Crear LearningPath sin actividad obligatoria

        Actividad actividadSinObligatoria = new Quiz("Quiz 2", Nivel.Intermedio, "Evaluar conceptos", 30, 1.0,
                LocalDateTime.now().plusDays(7), new HashMap<>(), Obligatoria.NO, new ArrayList<>(),
                50, profesor, new ArrayList<>(), new ArrayList<>());

        // Crear LearningPath sin actividad obligatoria

        assertThrows(IllegalArgumentException.class, () -> new LearningPath("Path Matemáticas", Nivel.Intermedio, "Descripción del path",
                "Aprender álgebra", 120, profesor, 4.5f, List.of(actividadSinObligatoria)));
    }

    @Test
    public void testInscripcionEstudianteValida() {
        assertTrue(learningPath.verificarSiInscrito(estudiante1));
        assertEquals(learningPath, estudiante1.getLearningPathActual());
    }

    @Test
    public void testInscripcionEstudianteInvalidaPorYaInscrito() {
    
        assertThrows(IllegalArgumentException.class, () -> learningPath.inscripcionEstudiante(estudiante1));
    }

    @Test
    public void testInscripcionEstudianteInvalidaPorOtroPath() {
        LearningPath otroLearningPath = new LearningPath("Otro Path", Nivel.Avanzado, "Otra descripción",
                "Otro objetivo", 150, profesor, 4.0f, new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> otroLearningPath.inscripcionEstudiante(estudiante1));
    }

    @Test
    public void testEliminarInscripcionEstudianteValida() {
    
        learningPath.eliminarInscripcion(estudiante1);

        assertFalse(learningPath.verificarSiInscrito(estudiante1));
    }

    @Test
    public void testVerificarInscripcionYActividadValida() {
        
        assertTrue(learningPath.verificarInscripcionYActividad(estudiante1, actividades.get(0)));
    }

    @Test
    public void testVerificarInscripcionYActividadInvalidaPorNoInscrito() {
        assertFalse(learningPath.verificarInscripcionYActividad(estudiante2, actividades.get(0)));
    }

    @Test
    public void testCalcularProgreso() {
    
        // Meter a al estudiante a la actividad 1

        actividad1.inscripcionEstudiante(estudiante1);
        actividad1.responder(estudiante1, "Correo");
        actividad1.evaluar(profesor, estudiante1, learningPath, 100, true);

        // Verificar progreso

        learningPath.actividadObligatoriaCompletada(actividad1, estudiante1);

        float progreso = learningPath.calcularProgreso(estudiante1);

        assertEquals(100.0f, progreso, 0.01);
        
    }

    @Test
    public void testCalcularProgresoCompleto() {
    
        actividad1.inscripcionEstudiante(estudiante1);
        actividad1.responder(estudiante1, "Correo");
        actividad1.evaluar(profesor, estudiante1, learningPath, 100, true);
        learningPath.actividadObligatoriaCompletada(actividad1, estudiante1);

        actividad2.inscripcionEstudiante(estudiante1);
        actividad2.responder(estudiante1, "1:B");
        actividad2.evaluar(profesor, estudiante1, learningPath, 100, true);
        learningPath.actividadObligatoriaCompletada(actividad2, estudiante1);

        float progreso = learningPath.calcularProgreso(estudiante1);

        assertEquals(100.0f, progreso, 0.01);
    }

    @Test
    public void testGuardarEnArchivo() throws IOException {
        File archivo = File.createTempFile("learningPathTest", ".txt");
        learningPath.guardarEnArchivo(archivo);

        assertTrue(archivo.exists());
    }

    @Test
    public void testCargarDeArchivo() throws IOException {

        // Meter estudiante a learning path y actividades

        actividad1.inscripcionEstudiante(estudiante1);
        actividad2.inscripcionEstudiante(estudiante1);

        File archivo = new File("src/persistencia/archivo/PRUEBA_LP.txt"); // Se crea un archivo de texto para guardar los profesores
        learningPath.guardarEnArchivo(archivo);

        LearningPath cargado = LearningPath.cargarDeArchivo(archivo, profesor);

        assertEquals(learningPath.getTitulo(), cargado.getTitulo());
        assertEquals(learningPath.getNivelDificultad(), cargado.getNivelDificultad());
        assertEquals(learningPath.getDescripcion(), cargado.getDescripcion());
        assertEquals(learningPath.getObjetivos(), cargado.getObjetivos());
        assertEquals(learningPath.getDuracionMinutos(), cargado.getDuracionMinutos());
        assertEquals(learningPath.getVersion(), cargado.getVersion());
        assertEquals(learningPath.getListaActividades().size(), cargado.getListaActividades().size());
        assertEquals(learningPath.getFechaCreacion(), cargado.getFechaCreacion());
        assertEquals(learningPath.getFechaModificacion(), cargado.getFechaModificacion());

        

    }
}
