package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.*;
import actividad.*;
import pregunta.Opcion;
import pregunta.Pregunta;
import pregunta.PreguntaAbierta;
import pregunta.PreguntaCerrada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import LPRS.LearningPath;

import static org.junit.jupiter.api.Assertions.*;


public class testExamen {
 
    private Examen examen; // Examen inicial que será usado en todos los tests
    private Profesor profesor;
    private Estudiante estudiante;
    private Map<Estudiante, Status> estadosPorEstudiante;
    private List<Pregunta> listaPreguntas;
    private LearningPath learningPath;

    @BeforeEach
    public void setUp() {
    profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
    estudiante = new Estudiante("Maria", "password", "maria@example.com");

        listaPreguntas = new ArrayList<>();

        // Configurar PreguntaCerrada
        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Configurar PreguntaAbierta
        PreguntaAbierta preguntaAbierta = new PreguntaAbierta("¿Qué es Java?");
        
        listaPreguntas.add(preguntaCerrada);
        listaPreguntas.add(preguntaAbierta);

        estadosPorEstudiante = new HashMap<>();
        examen = new Examen(
            "Examen Inicial",
            Nivel.Intermedio,
            "Probar conocimientos básicos",
            60,
            1.0,
            LocalDateTime.now().plusDays(5),
            estadosPorEstudiante,
            Obligatoria.SI,
            listaPreguntas,
            50.0,
            profesor,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // Crear LearningPath y asociarlo con el examen y estudiante
    List<Actividad> actividades = new ArrayList<>();
    actividades.add(examen);
    
    learningPath = new LearningPath(
                "Learning Path Inicial",
                Nivel.Intermedio,
                "Camino de aprendizaje de prueba",
                "Objetivo general",
                120,
                 profesor,
                4.5f,
                actividades
        );
    
    learningPath.inscripcionEstudiante(estudiante);
    // Asociar el examen al estudiante
    examen.inscripcionEstudiante(estudiante);
    }

    @Test
    public void testGetListaPreguntas() {
        assertEquals(listaPreguntas, examen.getListaPreguntas());
    }

    @Test
    public void testGetCalificacionMinima(){
        assertEquals(50.0, examen.getCalificacionMinima());
    }

    @Test
    public void testGetCalificacionObtenida(){

        // Deberia ser 0 y ya al inicio

        assertEquals(0, examen.getCalificacionObtenida(estudiante));

    }

    @Test
    public void testGetRespuestasCorrectas(){

        // Deberia ser un 0 al inicio

        assertEquals(0, examen.getRespuestasCorrectas());

    }

    @Test
    public void testGetRespuestasAbiertas(){

        // Deberia ser un arraylist vacio al inicio

        assertEquals(new ArrayList<>(), examen.getRespuestasAbiertas());

    }   

    @Test

    public void testSetListaPreguntas(){

        // Crear una nueva lista de preguntas

        List<Pregunta> listaPreguntasNueva = new ArrayList<>();

        // Configurar PreguntaCerrada
        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Configurar PreguntaAbierta
        PreguntaAbierta preguntaAbierta = new PreguntaAbierta("¿Qué es Java?");
        
        listaPreguntasNueva.add(preguntaCerrada);
        listaPreguntasNueva.add(preguntaAbierta);

        examen.setListaPreguntas(listaPreguntasNueva);

        assertEquals(listaPreguntasNueva, examen.getListaPreguntas());

    }

    @Test
    public void testSetCalificacionMinima(){

        // Cambiar la calificacion minima

        examen.setCalificacionMinima(60.0);

        assertEquals(60.0, examen.getCalificacionMinima());

    }
    
    @Test

    public void testSetCalificacionObtenida(){

        // Cambiar la calificacion obtenida

        examen.setCalificacionObtenida(estudiante, 100);

        assertEquals(100, examen.getCalificacionObtenida(estudiante));

    }

    @Test

    public void testSetRespuestasCorrectas(){

        // Cambiar la cantidad de respuestas correctas

        examen.setRespuestasCorrectas(1);

        assertEquals(1, examen.getRespuestasCorrectas());

    }

    @Test

    public void testSetRespuestasAbiertas(){

        // Crear una nueva lista de respuestas abiertas

        List<String> respuestasAbiertas = new ArrayList<>();

        respuestasAbiertas.add("Java es un lenguaje de programación");

        examen.setRespuestasAbiertas(respuestasAbiertas);

        assertEquals(respuestasAbiertas, examen.getRespuestasAbiertas());

    }

    @Test

    public void testResponderValido(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Verificar que la respuesta abierta se haya guardado correctamente

        assertEquals("Java es un lenguaje de programación", examen.getRespuestasAbiertas().get(0));

        // Verificar que la respuesta cerrada se haya guardado correctamente

        assertEquals(1, examen.getRespuestasCorrectas());

        // Verificar que el estado del estudiante sea Enviado

        assertEquals(Status.Enviada, examen.getEstadosPorEstudiante().get(estudiante));
    }

    @Test
    public void testResponderInvalidoPorEstudianteNulo(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(null, "B;Java es un lenguaje de programación"));

    }

    @Test
    public void testResponderInvalidoPorRespuestaNula(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, null));

    }


    @Test
    public void testResponderInvalidoPorRespuestaVacia(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, ""));

    }

    @Test
    public void testResponderInvalidoPorYaRespondida(){

        // Set el estado del estudiante a Enviado
        examen.getEstadosPorEstudiante().put(estudiante, Status.Enviada);

        // Responder la pregunta cerrada y abierta
        assertThrows(UnsupportedOperationException.class, () -> examen.responder(estudiante, "B;Java es un lenguaje de programación"));

    }

    @Test
    public void testResponderInvalidoPorYaCompleta(){

        // Set el estado del estudiante a Completada
        examen.getEstadosPorEstudiante().put(estudiante, Status.Completado);

        // Responder la pregunta cerrada y abierta
        assertThrows(UnsupportedOperationException.class, () -> examen.responder(estudiante, "B;Java es un lenguaje de programación"));

    }

    @Test

    public void testResponderInvalidoPorTamanioDeRespuestaIncorrecto(){

        // Responder la pregunta cerrada y abierta
        assertThrows(IllegalArgumentException.class, () -> examen.responder(estudiante, "B"));
    }

    @Test
    public void testEvaluarValido(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Verificar la calificación obtenida
        assertEquals(100, examen.getCalificacionObtenida(estudiante));


    }

    @Test
    public void testEvaluarInvalidoPorProfesorNulo(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        // Evaluar el examen con un profesor nulo
        assertThrows(SecurityException.class, () -> examen.evaluar(null, estudiante, learningPath, 100, true));

    }

    @Test
    public void testEvaluarInvalidoPorProfesorNoCreador(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        // Evaluar el examen con un profesor no creador
        Profesor profesor2 = new Profesor("Pedro", "password", "pedro@example.com", new ArrayList<>(), new ArrayList<>());

        assertThrows(SecurityException.class, () -> examen.evaluar(profesor2, estudiante, learningPath, 100, true));



    }

    @Test
    public void testEvaluarInvalidoPorEstudianteNulo(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        // Evaluar el examen con un estudiante nulo
        assertThrows(IllegalArgumentException.class, () -> examen.evaluar(profesor, null, learningPath, 100, true));

    }

    @Test
    public void testEvaluarInvalidoPorLearningPathNulo(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        // Evaluar el examen con un LearningPath nulo
        assertThrows(IllegalArgumentException.class, () -> examen.evaluar(profesor, estudiante, null, 100, true));

    }

    @Test
    public void testEvaluarInvalidoPorEstudiantesInscritosEnLearningPath(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        // Evaluar el examen con un estudiante no inscrito en el LearningPath
        Estudiante estudiante2 = new Estudiante("Pedro", "password", "pedro@example.com");

        assertThrows(IllegalArgumentException.class, () -> examen.evaluar(profesor, estudiante2, learningPath, 100, true));


    }

    @Test
    public void calcularCalificacionFinalValido(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        // Verificar que la calificacion final sea 100

        assertEquals(100, examen.getCalificacionObtenida(estudiante));


    }

    @Test

    public void testEsExitosaValida(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        // Verificar que la calificacion final sea 100

        assertTrue(examen.esExitosa(estudiante));

    }

    @Test
    public void testEsExitosaInvalidaPorNoExitoso(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "A;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen, el estudiante no responde correctamente

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(false, "Respuesta incorrecta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        assertFalse(examen.esExitosa(estudiante));

    }

    @Test
    public void testEsExitosaInvalidoPorEstudianteNulo(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "A;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen, el estudiante no responde correctamente

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(false, "Respuesta incorrecta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        // Verificar que la calificacion final sea 100

        assertThrows(IllegalArgumentException.class, () -> examen.esExitosa(null));

    }   

    @Test
    public void testReintentarValido(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "A;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen, el estudiante no responde correctamente

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(false, "Respuesta incorrecta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

       // Verificar que el estudiante no paso el examen

        assertFalse(examen.esExitosa(estudiante));

        // Verificar que el estado del estudiante sea NoExitosa ya que no paso

        assertEquals(Status.noExitosa, examen.getEstadosPorEstudiante().get(estudiante));

        // Reintentar el examen

        examen.reintentar(estudiante);

        // Estudiante vuelve a responder el examen

        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen, el estudiante responde correctamente

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        // Verificar que el estudiante paso el examen

        assertTrue(examen.esExitosa(estudiante));

        // Verificar que el estado del estudiante sea Completado

        assertEquals(Status.Completado, examen.getEstadosPorEstudiante().get(estudiante));

        // Verificar que la calificacion final sea 100

        assertEquals(100, examen.getCalificacionObtenida(estudiante));

    }

    @Test
    public void testReintentarInvalidoPorEstudianteNulo(){

        // Reintentar el examen

        assertThrows(IllegalArgumentException.class, () -> examen.reintentar(null));

    }


    @Test

    public void testReintentarInvalidoPorEstudianteQueYaCompleto(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }

        examen.evaluar(profesor, estudiante, learningPath, 100, true);

        // Calcular la calificacion final

        examen.calcularCalificacionFinal(estudiante);

        // Verificar que el estudiante paso el examen

        assertTrue(examen.esExitosa(estudiante));

        // Verificar que el estado del estudiante sea Completado

        assertEquals(Status.Completado, examen.getEstadosPorEstudiante().get(estudiante));

        // Reintentar el examen

        assertThrows(UnsupportedOperationException.class, () -> examen.reintentar(estudiante));


    }

    @Test

    public void testReintentarInvalidoPorEstudianteQueYaEnvio(){

        // Responder la pregunta cerrada y abierta
        examen.responder(estudiante, "B;Java es un lenguaje de programación");

        // Profesor preguntas abiertas del examen

        for (Pregunta pregunta : examen.getListaPreguntas()) {
            if (pregunta instanceof PreguntaAbierta) {
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
                // Evaluar la pregunta abierta

                preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
            }
        }


        // Verificar que el estado del estudiante sea Enviado

        assertEquals(Status.Enviada, examen.getEstadosPorEstudiante().get(estudiante));

        // Reintentar el examen

        assertThrows(UnsupportedOperationException.class, () -> examen.reintentar(estudiante));



    }

    @Test
    public void testAgregarPreguntaValido(){

        // Crear una nueva pregunta cerrada

        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Agregar la pregunta al examen

        examen.agregarPregunta(preguntaCerrada);

        // Verificar que la pregunta se haya agregado correctamente

        assertEquals(preguntaCerrada, examen.getListaPreguntas().get(2));

    }

    @Test
    public void testAgregarPreguntaInvalidoPorPreguntaNula(){

        // Agregar la pregunta al examen

        assertThrows(IllegalArgumentException.class, () -> examen.agregarPregunta(null));

    }

    @Test
    public void testEliminarPreguntaValida(){

        // Crear una nueva pregunta cerrada

        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // Agregar la pregunta al examen

        examen.agregarPregunta(preguntaCerrada);

        // Verificar que la pregunta se haya agregado correctamente

        assertEquals(preguntaCerrada, examen.getListaPreguntas().get(2));

        // Eliminar la pregunta del examen

        examen.eliminarPregunta(preguntaCerrada);

        // Verificar que la pregunta se haya eliminado correctamente

        assertEquals(2, examen.getListaPreguntas().size());

    }

    @Test
    public void testEliminarPreguntaInvalidoPorPreguntaNula(){

        // Eliminar la pregunta del examen

        assertThrows(IllegalArgumentException.class, () -> examen.eliminarPregunta(null));

    }

    @Test
    public void testEliminarPreguntaInvalidoPorPreguntaNoExistente(){

        // Crear una nueva pregunta cerrada

        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");
        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        opcionA.put(Opcion.A, "3");
        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        opcionB.put(Opcion.B, "4"); // Respuesta correcta
        preguntaCerrada.setOpcionA(opcionA);
        preguntaCerrada.setOpcionB(opcionB);
        preguntaCerrada.setRespuesta(opcionB);

        // No la agregamos al examen

        // Eliminar la pregunta del examen

        assertThrows(IllegalArgumentException.class, () -> examen.eliminarPregunta(preguntaCerrada));

    }

    @Test
    public void testEvaluarInvalidoPorTamanioDePreguntas(){

        // No se puede eliminar si actualmente solo hay uno o menos preguntas

        // Eliminar las preguntas actuales

        examen.getListaPreguntas().clear();

        // Crear una nueva pregunta cerrada

        PreguntaCerrada preguntaCerrada = new PreguntaCerrada("¿2 + 2?");

        Dictionary<Opcion, String> opcionA = new Hashtable<>();

        opcionA.put(Opcion.A, "3");

        Dictionary<Opcion, String> opcionB = new Hashtable<>();

        opcionB.put(Opcion.B, "4"); // Respuesta correcta

        preguntaCerrada.setOpcionA(opcionA);

        preguntaCerrada.setOpcionB(opcionB);

        preguntaCerrada.setRespuesta(opcionB);

        // Agregar la pregunta al examen

        examen.agregarPregunta(preguntaCerrada);

        // Verificar que la pregunta se haya agregado correctamente

        assertEquals(preguntaCerrada, examen.getListaPreguntas().get(0));

        // Intentar eliminar la pregunta

        assertThrows(UnsupportedOperationException.class, () -> examen.eliminarPregunta(preguntaCerrada));

    
    }











}


//        // Responder la pregunta cerrada y abierta
//examen.responder(estudiante, "B;Java es un lenguaje de programación");

// Profesor preguntas abiertas del examen

//for (Pregunta pregunta : examen.getListaPreguntas()) {
  //  if (pregunta instanceof PreguntaAbierta) {
   //     PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta;
        // Evaluar la pregunta abierta

    //    preguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");


// examen.evaluar(profesor, estudiante, learningPath, 100, true);


// Verificar la calificación obtenida
// assertEquals(100, examen.getCalificacionObtenida());