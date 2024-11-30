package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.Profesor;
import usuario.Estudiante;
import usuario.Administrador;
import actividad.Actividad;
import actividad.Nivel;
import LPRS.LearningPath;
import actividad.Status;
import actividad.Obligatoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class testActividad {
    private Actividad actividad;
    private Profesor profesor;
    private Estudiante estudiante;
    private LearningPath learningPath;
    private Administrador administrador;
    private LocalDateTime fechaLimiteEsperada;

    @BeforeEach
    public void setUp() {
       profesor = new Profesor("Juan", "1234", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante = new Estudiante("Maria", "1234", "maria@example.com");
        administrador = Administrador.getAdmin();
        learningPath = new LearningPath(
                "Learning Path Test",
                Nivel.Intermedio,
                "Descripción de prueba",
                "Objetivos de prueba",
                60,
                profesor,
                4.5f,
                new ArrayList<>());
            

            Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>();
            estadosPorEstudiante.put(estudiante, Status.Incompleto);
    
            fechaLimiteEsperada = LocalDateTime.now().plusDays(5);

            actividad = new ActividadConcreta(
                    "Descripción de prueba",
                    Nivel.Intermedio,
                    "Objetivo de prueba",
                    60,
                    1.0,
                    fechaLimiteEsperada,
                    estadosPorEstudiante,
                    Obligatoria.SI,
                    "Tipo de prueba",
                    profesor,
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            
        }
    // Clase concreta ficticia para probar la abstracta
    private static class ActividadConcreta extends Actividad {

    public ActividadConcreta(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada,
                             double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante,
                             Obligatoria obligatoria, String tipo, Profesor creador, List<Actividad> actividadesPreviasSugeridas,
                             List<Actividad> actividadesSeguimientoRecomendadas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, fechaLimite, estadosPorEstudiante,
                obligatoria, tipo, creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
    }

    @Override
    public void responder(Estudiante estudiante, String respuesta) {
        // Implementación ficticia para pruebas
    }

    @Override
    public boolean esExitosa(Estudiante estudiante) {
        return true; // Implementación ficticia para pruebas
    }

    @Override
    public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        // Implementación ficticia para pruebas
    }

    @Override
    public void reintentar(Estudiante estudiante) {
        // Implementación ficticia para pruebas
        }
    }

    @Test
    public void testConstructorValido(){
        assertEquals("Descripción de prueba", actividad.getDescripcion());
        assertEquals(Nivel.Intermedio, actividad.getNivelDificultad());
        assertEquals("Objetivo de prueba", actividad.getObjetivo());
        assertEquals(60, actividad.getDuracionEsperada());
        assertEquals(1.0, actividad.getVersion());
        assertEquals(fechaLimiteEsperada, actividad.getFechaLimite());
        assertEquals(Status.Incompleto, actividad.getEstadosPorEstudiante().get(estudiante));
        assertEquals(Obligatoria.SI, actividad.getObligatoria());
        assertEquals("Tipo de prueba", actividad.getTipo());
        assertEquals(profesor, actividad.getCreador());
        assertEquals(0, actividad.getActividadesPreviasSugeridas().size());
        assertEquals(0, actividad.getActividadesSeguimientoRecomendadas().size());
    }

    

    @Test
    public void testConstructorFechaLimitesValidas() {
        assertEquals(fechaLimiteEsperada, actividad.getFechaLimite());
    }

    @Test
    public void testConstructorFechaLimiteInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ActividadConcreta(
                    "Descripción de prueba",
                    Nivel.Intermedio,
                    "Objetivo de prueba",
                    60,
                    1.0,
                    LocalDateTime.now().minusDays(5),
                    new HashMap<>(),
                    Obligatoria.SI,
                    "Tipo de prueba",
                    profesor,
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        });
    }

    @Test
    public void testGetDescripcion() {
        assertEquals("Descripción de prueba", actividad.getDescripcion());
    }

    @Test
    public void testGetNivelDificultad() {
        assertEquals(Nivel.Intermedio, actividad.getNivelDificultad());
    }

    @Test
    public void testGetObjetivo() {
        assertEquals("Objetivo de prueba", actividad.getObjetivo());
    }

    @Test
    public void testGetDuracionEsperada() {
        assertEquals(60, actividad.getDuracionEsperada());
    }

    @Test
    public void testGetVersion() {
        assertEquals(1.0, actividad.getVersion());
    }

    @Test
    public void testGetFechaLimite() {
        assertEquals(fechaLimiteEsperada, actividad.getFechaLimite());
    }

    @Test
    public void testGetFechaInicio() {
        // Capturar el LocalDateTime esperado
        LocalDateTime fechaInicioEsperada = LocalDateTime.now();
    
        // Crear la actividad con la fecha de inicio esperada
        actividad = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                fechaLimiteEsperada,
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );
    
        // Verificar que la fecha de inicio es la esperada
        assertTrue(actividad.getFechaInicio().isEqual(fechaInicioEsperada) || actividad.getFechaInicio().isAfter(fechaInicioEsperada));
    }

    @Test
    public void testSetStatusParaEstudiante() {
        actividad.setStatusParaEstudiante(estudiante, Status.Completado);
        assertEquals(Status.Completado, actividad.getEstadosPorEstudiante().get(estudiante));
    }

    @Test
    public void testGetStatusParaEstudiante() {
        assertEquals(Status.Incompleto, actividad.getEstadosPorEstudiante().get(estudiante));
    }

    @Test
    public void testGetEstadosPorEstudiante() {
        assertEquals(Status.Incompleto, actividad.getEstadosPorEstudiante().get(estudiante));
    }

    @Test
    public void testGetObligatoria() {
        assertEquals(Obligatoria.SI, actividad.getObligatoria());
    }

    @Test
    public void testGetTipo() {
        assertEquals("Tipo de prueba", actividad.getTipo());
    }

    @Test
    public void testGetCreador() {
        assertEquals(profesor, actividad.getCreador());
    }

    @Test
    public void testGetActividadesPreviasSugeridas() {
        assertEquals(0, actividad.getActividadesPreviasSugeridas().size());
    }

    @Test
    public void testGetActividadesSeguimientoRecomendadas() {
        assertEquals(0, actividad.getActividadesSeguimientoRecomendadas().size());
    }

    @Test
    public void testTienePermisoModificarValido() {
        assertTrue(actividad.tienePermisoModificar(profesor));
    }

    @Test
    public void testTienePermisoModificarInvalido() {
        // Nuevo profesor sin permisos

        Profesor nuevoProfesor = new Profesor("Camilo", "4321", "camilo@example.com", new ArrayList<>(), new ArrayList<>());
        assertFalse(actividad.tienePermisoModificar(nuevoProfesor));
    }

    @Test
    public void testTienePermisoModificarValidoAdmin() {
        assertTrue(actividad.tienePermisoModificar(administrador));
    }

    @Test
    public void testAgregarActividadPreviaSugeridaValida() {
        Actividad actividadPrevia = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );



        actividad.agregarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);
        assertEquals(1, actividad.getActividadesPreviasSugeridas().size());
        assertEquals(actividadPrevia, actividad.getActividadesPreviasSugeridas().get(0));
    }

    @Test
    public void testAgregarActividadPreviaSugeridaPorAdmin() {
        Actividad actividadPrevia = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        actividad.agregarActividadPreviaSugerida(actividadPrevia, administrador, learningPath);
        assertEquals(1, actividad.getActividadesPreviasSugeridas().size());
        assertEquals(actividadPrevia, actividad.getActividadesPreviasSugeridas().get(0));
    }  

    @Test
    public void testAgregarActividadPreviaSugeridaPorInscritos(){
        // Verificar que no se pueden agregar actividades si hay estudiantes inscritos al learning path

        Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        LearningPath learningPathInscrito = new LearningPath(
                "Learning Path Test",
                Nivel.Intermedio,
                "Descripción de prueba",
                "Objetivos de prueba",
                60,
                profesor,
                4.5f,
                new ArrayList<>());

        // Meter estudiantes al learning path
        learningPathInscrito.inscripcionEstudiante(estudiante);


        Actividad actividadInscrita = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                estadosPorEstudiante,
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThrows(UnsupportedOperationException.class, () -> {actividadInscrita.agregarActividadPreviaSugerida(actividadInscrita, profesor, learningPathInscrito);});
    }

    @Test
    public void testAgregarActividadPreviaSugeridaInvalidaPorNula(){
        assertThrows(IllegalArgumentException.class, () -> { actividad.agregarActividadPreviaSugerida(null, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadPreviaSugeridaInvalidaPorSerLaMisma(){
        assertThrows(IllegalArgumentException.class, () -> { actividad.agregarActividadPreviaSugerida(actividad, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadPreviaSugeridaInvalidaPorRepetida(){
        Actividad actividadPrevia = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        actividad.agregarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);
        assertThrows(IllegalArgumentException.class, () -> {actividad.agregarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadPreviaSugeridaInvalidaPorPermiso() {
        // Nuevo profesor sin permisos

        Profesor nuevoProfesor = new Profesor("Camilo", "4321", "camilo@example.com", new ArrayList<>(), new ArrayList<>());
        Actividad actividadPrevia = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                nuevoProfesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThrows(SecurityException.class, () -> {actividad.agregarActividadPreviaSugerida(actividadPrevia, nuevoProfesor, learningPath);});
    }
    
    @Test
    public void testEliminarActividadSugeridaValida(){

        Actividad actividadPrevia = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        actividad.agregarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);
        assertEquals(1, actividad.getActividadesPreviasSugeridas().size());
        assertEquals(actividadPrevia, actividad.getActividadesPreviasSugeridas().get(0));

        actividad.eliminarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);
        assertEquals(0, actividad.getActividadesPreviasSugeridas().size());

    }

    @Test
    public void testEliminarActividadPreviaSugeridaInvalidaPorInscritos(){
        // Verificar que no se pueden eliminar actividades si hay estudiantes inscritos al learning path

        Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        LearningPath learningPathInscrito = new LearningPath(
                "Learning Path Test",
                Nivel.Intermedio,
                "Descripción de prueba",
                "Objetivos de prueba",
                60,
                profesor,
                4.5f,
                new ArrayList<>());

        // Meter estudiantes al learning path
        learningPathInscrito.inscripcionEstudiante(estudiante);
    }

    @Test
    public void testEliminarActividadSugeridaInvalidaPorNula(){
        assertThrows(IllegalArgumentException.class, () -> { actividad.eliminarActividadPreviaSugerida(null, profesor, learningPath);});
    }

    @Test
    public void testEliminarActividadPreviaSugeridaPorNoExistir(){
        Actividad actividadPrevia = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        assertThrows(IllegalArgumentException.class, () -> { actividad.eliminarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);});
    }

    @Test
    public void testEliminarActividadPreviaSugeridaInvalidaPorPermiso(){
        // Nuevo profesor sin permisos

        Profesor nuevoProfesor = new Profesor("Camilo", "4321", "camilo@example.com", new ArrayList<>(), new ArrayList<>());
        
        Actividad actividadPrevia = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            nuevoProfesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        // Agregar actividad previa
        actividad.agregarActividadPreviaSugerida(actividadPrevia, profesor, learningPath);

        assertThrows(SecurityException.class, () -> { actividad.eliminarActividadPreviaSugerida(actividadPrevia, nuevoProfesor, learningPath);});
    
     }

     @Test
        public void testAgregarActividadSeguimientoRecomendadaValida(){
            Actividad actividadSeguimiento = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                new HashMap<>(),
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
            );
    
            actividad.agregarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);
            assertEquals(1, actividad.getActividadesSeguimientoRecomendadas().size());
            assertEquals(actividadSeguimiento, actividad.getActividadesSeguimientoRecomendadas().get(0));
    }

    @Test
    public void testAgregarActividadSeguimientoRecomendadaInvaldiaPorInscritos(){

        Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        LearningPath learningPathInscrito = new LearningPath(
                "Learning Path Test",
                Nivel.Intermedio,
                "Descripción de prueba",
                "Objetivos de prueba",
                60,
                profesor,
                4.5f,
                new ArrayList<>());

        // Meter estudiantes al learning path
        learningPathInscrito.inscripcionEstudiante(estudiante);

        Actividad actividadInscrita = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                estadosPorEstudiante,
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThrows(UnsupportedOperationException.class, () -> {actividadInscrita.agregarActividadSeguimiento(actividadInscrita, profesor, learningPathInscrito);});



    }

    @Test
    public void testAgregarActividadSeguimientoRecomendadaInvalidaPorNula(){
        assertThrows(IllegalArgumentException.class, () -> {actividad.agregarActividadSeguimiento(null, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadSeguimientoRecomendadaInvalidaPorSerLaMisma(){
        assertThrows(IllegalArgumentException.class, () -> {actividad.agregarActividadSeguimiento(actividad, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadSeguimientoRecomendadaInvalidaPorRepetida(){
        Actividad actividadSeguimiento = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        actividad.agregarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);
        assertThrows(IllegalArgumentException.class, () -> {actividad.agregarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);});
    }

    @Test
    public void testAgregarActividadSeguimientoRecomendadaInvalidaPorPermiso(){
        // Nuevo profesor sin permisos

        Profesor nuevoProfesor = new Profesor("Camilo", "4321", "camilo@example.com", new ArrayList<>(), new ArrayList<>());

        Actividad actividadSeguimiento = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            nuevoProfesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        assertThrows(SecurityException.class, () -> {actividad.agregarActividadSeguimiento(actividadSeguimiento, nuevoProfesor, learningPath);});

    }

    @Test
    public void testEliminarActividadSeguimientoRecomendadaValida(){
        Actividad actividadSeguimiento = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        actividad.agregarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);
        assertEquals(1, actividad.getActividadesSeguimientoRecomendadas().size());
        assertEquals(actividadSeguimiento, actividad.getActividadesSeguimientoRecomendadas().get(0));

        actividad.eliminarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);
        assertEquals(0, actividad.getActividadesSeguimientoRecomendadas().size());
    }

    @Test
    public void testEliminarActividadSeguimientoRecomendadaInvalidaPorInscritos(){
        // Verificar que no se pueden eliminar actividades si hay estudiantes inscritos al learning path

        Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>();
        estadosPorEstudiante.put(estudiante, Status.Incompleto);

        LearningPath learningPathInscrito = new LearningPath(
                "Learning Path Test",
                Nivel.Intermedio,
                "Descripción de prueba",
                "Objetivos de prueba",
                60,
                profesor,
                4.5f,
                new ArrayList<>());

        // Meter estudiantes al learning path
        learningPathInscrito.inscripcionEstudiante(estudiante);

        Actividad actividadInscrita = new ActividadConcreta(
                "Descripción de prueba",
                Nivel.Intermedio,
                "Objetivo de prueba",
                60,
                1.0,
                LocalDateTime.now().plusDays(5),
                estadosPorEstudiante,
                Obligatoria.SI,
                "Tipo de prueba",
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThrows(UnsupportedOperationException.class, () -> {actividadInscrita.eliminarActividadSeguimiento(actividadInscrita, profesor, learningPathInscrito);});
    }

    @Test
    public void testEliminarActividadSeguimientoRecomendadaInvalidaPorNula(){
        assertThrows(IllegalArgumentException.class, () -> {actividad.eliminarActividadSeguimiento(null, profesor, learningPath);});
    }

    @Test
    public void testEliminarActividadSeguimientoRecomendadaPorNoExistir(){
        Actividad actividadSeguimiento = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        assertThrows(IllegalArgumentException.class, () -> { actividad.eliminarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);});
    }

    @Test
    public void testEliminarActividadSeguimientoRecomendadaInvalidaPorPermiso(){
        // Nuevo profesor sin permisos

        Profesor nuevoProfesor = new Profesor("Camilo", "4321", "camilo@example.com", new ArrayList<>(), new ArrayList<>());

        Actividad actividadSeguimiento = new ActividadConcreta(
            "Descripción de prueba",
            Nivel.Intermedio,
            "Objetivo de prueba",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            new HashMap<>(),
            Obligatoria.SI,
            "Tipo de prueba",
            nuevoProfesor,
            new ArrayList<>(),
            new ArrayList<>()
        );

        // Agregar actividad seguimiento
        actividad.agregarActividadSeguimiento(actividadSeguimiento, profesor, learningPath);

        assertThrows(SecurityException.class, () -> {
            actividad.eliminarActividadSeguimiento(actividadSeguimiento, nuevoProfesor, learningPath);
        });

    }

    @Test
    public void testSetDescripcionValida(){
        actividad.setDescripcion("Nueva descripción", learningPath);
        assertEquals("Nueva descripción", actividad.getDescripcion());
    }

    @Test
    public void setNivelDificultadValido(){
        actividad.setNivelDificultad(Nivel.Avanzado, learningPath);
        assertEquals(Nivel.Avanzado, actividad.getNivelDificultad());
    }

    @Test
    public void testSetObjetivoValido(){
        actividad.setObjetivo("Nuevo objetivo", learningPath);
        assertEquals("Nuevo objetivo", actividad.getObjetivo());
    }

    @Test
    public void testSetDuracionEsperadaValida(){
        actividad.setDuracionEsperada(120, learningPath);
        assertEquals(120, actividad.getDuracionEsperada());
    }

    @Test
    public void testSetVersionValida(){
        actividad.setVersion(2.0, learningPath);
        assertEquals(2.0, actividad.getVersion());
    }

    @Test
    public void testSetObligatoria(){
        actividad.setObligatoria(Obligatoria.NO, learningPath);
        assertEquals(Obligatoria.NO, actividad.getObligatoria());
    }

    @Test
    public void testSetFechaInicioValida(){
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(10);
        actividad.setFechaLimite(nuevaFecha, learningPath);
        assertEquals(nuevaFecha, actividad.getFechaLimite());
    }

    @Test
    public void testSetTipoValido(){
        actividad.setTipo("Nuevo tipo", learningPath);
        assertEquals("Nuevo tipo", actividad.getTipo());
    }

    @Test
    public void testEsObligatoria(){
        assertEquals(Obligatoria.SI, actividad.getObligatoria());
    }
}


