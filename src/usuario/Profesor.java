package usuario;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import LPRS.LearningPath;
import actividad.*;
import pregunta.PreguntaAbierta;
import pregunta.PreguntaCerrada;
import pregunta.Opcion;
import pregunta.Pregunta;

public class Profesor extends Usuario {
    private List<LearningPath> learningPathCreado;
    private List<Estudiante> estudiantes;

    public Profesor(String nombre, String contrasena, String correo, List<LearningPath> learningPathCreado, List<Estudiante> estudiantes) {
        super(nombre, contrasena, "profesor",correo);
        this.learningPathCreado = learningPathCreado;
        this.estudiantes = estudiantes;
    }

    // Getters y Setters

    public List<LearningPath> getLearningPathCreado() {
        return learningPathCreado;
    }

    public void setLearningPathCreado(List<LearningPath> learningPathCreado) {
        // Verificar que la lista de Learning Paths no sea nula

        if (learningPathCreado == null) {
            throw new IllegalArgumentException("La lista de Learning Paths no puede ser nula.");
        }
        this.learningPathCreado = learningPathCreado;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        // Verificar que la lista de estudiantes no sea nula

        if (estudiantes == null) {
            throw new IllegalArgumentException("La lista de estudiantes no puede ser nula.");
        }
        this.estudiantes = estudiantes;
    }

    
    // Método para evaluar la tarea
    public void evaluarTarea(Tarea tarea, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        tarea.evaluar(this, estudiante, learningPath, calificacionObtenida, exitosa);
        System.out.println("La tarea fue evaluada por el profesor: " + this.getNombre());
    }

    // Método para evaluar el examen
    public void evaluarExamen(Examen examen, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        // Metodo para evaluar preguntas abiertas del examen antes de evaluar el resto
        
        examen.evaluar(this, estudiante, learningPath, calificacionObtenida, exitosa);
        System.out.println("El examen fue evaluado por el profesor: " + this.getNombre());
    }

    public void evaluarPreguntasAbiertas(PreguntaAbierta PreguntaAbierta){

        PreguntaAbierta.evaluarPorProfesor(true, "Respuesta correcta");
        System.out.println("Pregunta abierta evaluada por el profesor: " + this.getNombre());

    }

    // Método para evaluar el quiz (aunque no requiere evaluación directa, podemos verificar su estado)
    public void revisarQuiz(Quiz quiz, Estudiante estudiante) {
        if (quiz.esExitosa(estudiante)) {
            System.out.println("El quiz ha sido completado exitosamente.");
        } else {
            System.out.println("El quiz no ha sido aprobado.");
        }
    }

    // Método para revisar la encuesta (aunque no se evalúa directamente)
    public void revisarEncuesta(Encuesta encuesta, Estudiante estudiante) {
        if (encuesta.esExitosa(estudiante)) {
            System.out.println("La encuesta ha sido completada por el estudiante: " + estudiante.getNombre());
        } else {
            System.out.println("La encuesta no ha sido completada.");
        }
    }

    // Método para crear un Learning Path
    public void crearLearningPath(String titulo, Nivel nivelDificultad, String descripcion, String objetivos, 
                                  int duracionMinutos, float rating, List<Actividad> listaActividades) throws IllegalStateException {
        if (titulo == null || nivelDificultad == null || descripcion == null || objetivos == null || duracionMinutos <= 0 || rating < 0 || rating > 5) {
            throw new IllegalStateException("Los datos ingresados no son válidos.");
        }

        boolean tieneActividadObligatoria = listaActividades.stream().anyMatch(Actividad::esObligatoria);
        if (!tieneActividadObligatoria) {
            throw new IllegalStateException("El Learning Path debe contener al menos una actividad obligatoria.");
        }

        LearningPath learningPath = new LearningPath(titulo, nivelDificultad, descripcion, objetivos, duracionMinutos, this, rating, listaActividades);
        System.out.println("Learning Path creado exitosamente: " + learningPath.getTitulo());

        // Agregar el Learning Path a la lista de Learning Paths creados por el profesor
        this.learningPathCreado.add(learningPath);
    }

// Método para crear actividades en el Learning Path (Se adapto del que ya se hizo para la persistencia)

public void crearActividad(LearningPath learningPath, String descripcion, Nivel nivelDificultad, String objetivo,
                           int duracionEsperada, double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante,
                           Obligatoria obligatoria, String tipo, List<Actividad> actividadesPreviasSugeridas,
                           List<Actividad> actividadesSeguimientoRecomendadas, ArrayList<PreguntaAbierta> listaPreguntasAbiertas,
                           List<PreguntaCerrada> listaPreguntasCerradas, String submissionMethod,
                           double calificacionMinima, String tipoRecurso, List<Pregunta> listaPreguntas) {

    Actividad nuevaActividad; // Actividad a crear

    Profesor creador = this; // Profesor creador de la actividad

    switch (tipo.toUpperCase()) { // Convertir a mayúsculas para evitar errores de comparación

        // El case se usa para verificar el tipo de actividad y crear la instancia correspondiente, más facil que un pocoton de ifs
        case "TAREA": 
            // Creación de la Tarea
            nuevaActividad = new Tarea(descripcion, nivelDificultad, objetivo, duracionEsperada, version, fechaLimite,
            estadosPorEstudiante, obligatoria, submissionMethod, creador, actividadesPreviasSugeridas,
                                       actividadesSeguimientoRecomendadas);
            break;

        case "ENCUESTA":
            // Verificación de preguntas abiertas para Encuesta
            if (listaPreguntasAbiertas == null || listaPreguntasAbiertas.isEmpty()) {
                throw new IllegalArgumentException("La encuesta debe tener preguntas abiertas.");
            }
            nuevaActividad = new Encuesta(descripcion, nivelDificultad, objetivo, duracionEsperada, version, fechaLimite,
            estadosPorEstudiante, obligatoria, actividadesPreviasSugeridas,
                                          actividadesSeguimientoRecomendadas, creador, listaPreguntasAbiertas);
            break;

        case "EXAMEN":
            // Verificación de preguntas para Examen
            if (listaPreguntas == null || listaPreguntas.isEmpty()) {
                throw new IllegalArgumentException("El examen debe tener al menos una pregunta.");
            }
            nuevaActividad = new Examen(descripcion, nivelDificultad, objetivo, duracionEsperada, version,
                                        fechaLimite, estadosPorEstudiante, obligatoria, listaPreguntas, calificacionMinima,
                                        creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
            break;

        case "QUIZ":
            // Verificación de preguntas cerradas para Quiz
            if (listaPreguntasCerradas == null || listaPreguntasCerradas.isEmpty()) {
                throw new IllegalArgumentException("El quiz debe tener preguntas cerradas.");
            }
            nuevaActividad = new Quiz(descripcion, nivelDificultad, objetivo, duracionEsperada, version, fechaLimite,
            estadosPorEstudiante, obligatoria, listaPreguntasCerradas, calificacionMinima, creador,
                                      actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
            break;

        case "RECURSO EDUCATIVO":
            // Verificación de tipoRecurso para Recurso Educativo
            if (tipoRecurso == null || tipoRecurso.isEmpty()) {
                throw new IllegalArgumentException("El tipo de recurso no puede estar vacío.");
            }
            nuevaActividad = new RecursoEducativo(descripcion, nivelDificultad, objetivo, duracionEsperada, version,
                                                  fechaLimite, estadosPorEstudiante, obligatoria, tipoRecurso, creador,
                                                  actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
            break;

        default:
            throw new IllegalStateException("Tipo de actividad no válido: " + tipo);
    }

    // Agregar la nueva actividad al Learning Path
    learningPath.agregarActividad(nuevaActividad);
    System.out.println("Actividad " + tipo + " agregada al Learning Path: " + learningPath.getTitulo());
}


// Metodo para agregar estudiantes a la lista del profesor si estan inscritos en el learning path creado por el profesor o actividad

public void agregarEstudianteSiCorresponde(LearningPath learningPath, Estudiante estudiante, Profesor profesor) {
    if (learningPath == null) {
        throw new IllegalArgumentException("El Learning Path no puede ser nulo.");
    }
    if (estudiante == null) {
        throw new IllegalArgumentException("El estudiante no puede ser nulo.");
    }
    if (!learningPath.getCreador().equals(profesor)) {
        throw new IllegalStateException("El profesor no es el creador del Learning Path.");
    }
    if (!learningPath.verificarSiInscrito(estudiante)) {
        throw new IllegalStateException("El estudiante no está inscrito en el Learning Path.");
    }
    agregarEstudiante(estudiante);
}

public void agregarEstudiante(Estudiante estudiante) {
    if (estudiante == null) {
        throw new IllegalArgumentException("El estudiante no puede ser nulo.");
    }
    if (estudiantes.contains(estudiante)) {
        throw new IllegalStateException("El estudiante ya está inscrito en la lista.");
    }
    estudiantes.add(estudiante);
}

public void crearPreguntaCerrada(String enunciado) {
        PreguntaCerrada pregunta = new PreguntaCerrada(enunciado);
        System.out.println("Pregunta cerrada creada: " + pregunta.getEnunciado());
    }

    public void agregarOpcionA(Dictionary<Opcion, String> opcionA, PreguntaCerrada pregunta) {
        pregunta.setOpcionA(opcionA);
        System.out.println("Opción agregada a la pregunta cerrada: " + pregunta.getEnunciado());
    }

    public void agregarOpcionB(Dictionary<Opcion, String> opcionB, PreguntaCerrada pregunta) {
        pregunta.setOpcionB(opcionB);
        System.out.println("Opción agregada a la pregunta cerrada: " + pregunta.getEnunciado());
    }

    public void agregarOpcionC(Dictionary<Opcion, String> opcionC, PreguntaCerrada pregunta) {
        pregunta.setOpcionC(opcionC);
        System.out.println("Opción agregada a la pregunta cerrada: " + pregunta.getEnunciado());
    }
    public void agregarOpcionD(Dictionary<Opcion, String> opcionD, PreguntaCerrada pregunta) {
        pregunta.setOpcionD(opcionD);
        System.out.println("Opción agregada a la pregunta cerrada: " + pregunta.getEnunciado());
    }

    public List<String> obtenerEstudiantesEnLearningPath(LearningPath learningPath) {
    if (!learningPathCreado.contains(learningPath)) {
        throw new IllegalStateException("El Learning Path no pertenece a este profesor.");
    }
    return learningPath.getEstudiantesInscritos().stream()
            .map(Estudiante::getNombre)
            .collect(Collectors.toList());
    }

    public Map<String, Float> obtenerProgresoEstudiantes(LearningPath learningPath) {
        if (!learningPathCreado.contains(learningPath)) {
            throw new IllegalStateException("El Learning Path no pertenece a este profesor.");
        }
        Map<String, Float> progresoEstudiantes = new HashMap<>();
        for (Estudiante estudiante : learningPath.getEstudiantesInscritos()) {
            progresoEstudiantes.put(estudiante.getNombre(), learningPath.getProgresoParaEstudiante(estudiante));
        }
        return progresoEstudiantes;
    }

    public void eliminarLearningPath(LearningPath learningPath) {
        if (!learningPathCreado.contains(learningPath)) {
            throw new IllegalStateException("El Learning Path no pertenece a este profesor.");
        }
        if (learningPath.verificarSiHayInscritos()) {
            throw new IllegalStateException("No se puede eliminar un Learning Path con estudiantes inscritos.");
        }
        learningPathCreado.remove(learningPath);
        System.out.println("Learning Path eliminado: " + learningPath.getTitulo());
    }

    public boolean verificarEstudianteCompletoLearningPath(LearningPath learningPath, Estudiante estudiante) {
        if (!learningPathCreado.contains(learningPath)) {
            throw new IllegalStateException("El Learning Path no pertenece a este profesor.");
        }
        return learningPath.getListaActividades().stream()
                .allMatch(actividad -> actividad.esExitosa(estudiante));
    }
    

}
