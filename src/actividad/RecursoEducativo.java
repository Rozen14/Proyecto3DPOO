package actividad;

import usuario.Estudiante;
import usuario.Profesor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import LPRS.LearningPath;

public class RecursoEducativo extends Actividad {

    protected String tipoRecurso; // Tipo de recurso educativo (e.g., video, documento, etc.)

    public RecursoEducativo(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, 
                            double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante, Obligatoria obligatoria, 
                            String tipoRecurso, Profesor creador, List<Actividad> actividadesPreviasSugeridas, 
                            List<Actividad> actividadesSeguimientoRecomendadas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, 
              fechaLimite, estadosPorEstudiante, obligatoria, "recurso educativo", creador, 
              actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        this.tipoRecurso = tipoRecurso;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    // Método para responder al recurso educativo (marcarlo como revisado)
    @Override
    public void responder(Estudiante estudiante, String respuesta) {


        Status estadoEstudiante = estadosPorEstudiante.get(estudiante);
        if (estudiante == null) {
            throw new SecurityException("Se requiere un estudiante para marcar el recurso educativo como revisado.");
        }
        
        if (estadoEstudiante == Status.Completado) {
            throw new UnsupportedOperationException("El recurso educativo ya ha sido completado exitosamente.");
        }

        // La respuesta válida es "visto" para marcar el recurso como revisado
        if ("visto".equalsIgnoreCase(respuesta)) {
            setStatusParaEstudiante(estudiante, Status.Completado);
            System.out.println("El recurso educativo fue marcado como revisado por: " + estudiante.getNombre());
        } else {
            System.out.println("Respuesta no válida para Recurso Educativo. Para marcar como revisado, responde 'visto'.");
        }
    }

    // Método para verificar si el recurso educativo es exitoso (revisado)
    @Override
    public boolean esExitosa(Estudiante estudiante) {
        Status estadoEstudiante = estadosPorEstudiante.get(estudiante);
        if (estadoEstudiante == Status.Exitosa || estadoEstudiante == Status.Completado) {
            System.out.println("El recurso educativo fue completado exitosamente por: " + estudiante.getNombre());
            estudiante.agregarActividadCompletada(this);
            return true;
            // Agregar a actividades completadas pro el estudiante
        } else {
            System.out.println("El recurso educativo no ha sido completado por: " + estudiante.getNombre());
            return false;
        }
    }

    @Override
        public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        // No se necesita implementación para Recurso Educativo
    }

    @Override
    public void reintentar(Estudiante estudiante) {
        // No se necesita implementación para Recurso Educativo
    }


}
