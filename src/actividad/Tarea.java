package actividad;

import LPRS.LearningPath;
import usuario.Profesor;
import usuario.Estudiante;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Tarea extends Actividad {

    protected String submissionMethod; // Método de entrega (e.g., LMS, correo electrónico, etc.)

    public Tarea(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, 
                 double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante, Obligatoria obligatoria, 
                 String submissionMethod, Profesor creador, List<Actividad> actividadesPreviasSugeridas, 
                 List<Actividad> actividadesSeguimientoRecomendadas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, 
              fechaLimite, estadosPorEstudiante, obligatoria, "tarea", creador, 
              actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        this.submissionMethod = submissionMethod;
    }

    public String getSubmissionMethod() {
        return submissionMethod;
    }

    // Método para que el estudiante marque la tarea como enviada
    @Override
    public void responder(Estudiante estudiante, String respuesta) {

        Status estadoEstudiante = estadosPorEstudiante.get(estudiante); // Obtener el estado del estudiante

        if (estudiante == null) {
            throw new SecurityException("Se requiere un estudiante para enviar la tarea."); // Verificar que el estudiante no sea nulo
        }

        if (estadoEstudiante == Status.Enviada) {
            throw new UnsupportedOperationException("La tarea ya ha sido enviada y no se puede reenviar."); // Verificar que la tarea no haya sido enviada
        }

        if (estadoEstudiante == Status.Completado) {
            throw new UnsupportedOperationException("La tarea ya ha sido completada y no se puede enviar."); // Verificar que la tarea no haya sido completada
        }

        // La "respuesta" en este caso es el método de entrega (e.g., LMS, correo)
        this.submissionMethod = respuesta;
        setStatusParaEstudiante(estudiante, Status.Enviada);
        System.out.println("La tarea fue enviada por: " + estudiante.getNombre() + " usando el método: " + submissionMethod); // Mostrar mensaje de envío
    }

    // Verificar si la tarea es exitosa
    @Override
    public boolean esExitosa(Estudiante estudiante) {
        Status estadoEstudiante = estadosPorEstudiante.get(estudiante);

        if ( estadoEstudiante == Status.Completado) { // Verificar si la tarea fue completada
            System.out.println("La tarea fue completada exitosamente por: " + estudiante.getNombre()); // Mostrar mensaje de éxito
            estudiante.agregarActividadCompletada(this); // Agregar a actividades completadas pro el estudiante
            return true; // La tarea fue exitosa
        } else {
            System.out.println("La tarea no ha sido completada exitosamente por: " + estudiante.getNombre()); // Mostrar mensaje de falla
            return false; // La tarea no fue exitosa   
        }
    }

    // **Método para que el profesor evalúe la tarea
    @Override
    public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, 
                        double calificacionObtenida, boolean exitosa) {

        // Verificar si el estudiante está inscrito en el LearningPath
        if (!learningPath.verificarSiInscrito(estudiante)) {
            throw new SecurityException("El estudiante no está inscrito en el Learning Path asociado a esta tarea.");
        }

        // Verificar si la tarea está asociada al LearningPath
        if (!learningPath.verificarActividad(this)) {
            throw new IllegalArgumentException("La tarea no está asociada al Learning Path actual.");
        }

        // Verificar si el profesor es el creador de la tarea
        if (profesor == null || !profesor.equals(creador)) {
            throw new SecurityException("Solo el profesor creador puede evaluar la tarea.");
        }


        // Asignar la calificación y el estado según la evaluación
        estadosPorEstudiante.put(estudiante, exitosa ? Status.Completado : Status.noExitosa); // Marcar la tarea como exitosa o fallida segun el criterio del profesor
        System.out.println("La tarea fue marcada como " + (exitosa ? "exitosa" : "fallida") + " por el profesor: " + profesor.getNombre()); // Mostrar mensaje de evaluación
    }

    // Método para reintentar la tarea
    @Override
    public void reintentar(Estudiante estudiante) {

        Status estadoEstudiante = estadosPorEstudiante.get(estudiante); // Obtener el estado del estudiante

        if (estadoEstudiante == Status.Completado) { // Verificar que la tarea no haya sido completada
            throw new UnsupportedOperationException("No se puede reintentar una tarea que ya ha sido completada exitosamente.");
        }

        System.out.println("El estudiante " + estudiante.getNombre() + " está reintentando la tarea."); // Mostrar mensaje de reintentar
        estadosPorEstudiante.put(estudiante, Status.Incompleto); // Se reinicia el estado de la tarea 
    }

    // Método para inscribri al estudiante en la tarea

    public void inscripcionEstudiante(Estudiante estudiante) {
        if (estadosPorEstudiante.containsKey(estudiante)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en la tarea.");
        }
        estadosPorEstudiante.put(estudiante, Status.Incompleto);
    }

}
