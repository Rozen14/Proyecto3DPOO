package actividad;

import pregunta.PreguntaAbierta;
import usuario.Estudiante;
import usuario.Profesor;
import LPRS.LearningPath;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Encuesta extends Actividad {

    private ArrayList<PreguntaAbierta> listaPreguntas; // Lista de preguntas abiertas de la encuesta

    public Encuesta(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, 
                    double version, LocalDateTime fechaLimite, Status status, Obligatoria obligatoria, List<Actividad>actividadesPreviasSugeridas, List<Actividad>actividadesSeguimientoRecomendadas,
                    Profesor creador, ArrayList<PreguntaAbierta> listaPreguntas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, 
              fechaLimite, status, obligatoria, "encuesta", creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        this.listaPreguntas = listaPreguntas;
    }

    // Getters

    public ArrayList<PreguntaAbierta> getListaPreguntas() {
        return listaPreguntas;
    }

    // Método para responder la encuesta por un estudiante
    @Override
    public void responder(Estudiante estudiante, String respuesta) {
        if (estudiante == null) {
            throw new SecurityException("Un estudiante debe responder la encuesta.");
        }

        if (this.status == Status.Completado) {
            throw new UnsupportedOperationException("La encuesta ya ha sido completada.");
        }

        // Simulación de respuestas: se espera una lista de respuestas separadas por comas (una por cada pregunta)
        String[] respuestasEstudiante = respuesta.split(",");
        if (respuestasEstudiante.length != listaPreguntas.size()) {
            throw new IllegalArgumentException("Debe responder a todas las preguntas de la encuesta.");
        }

        // Guardar las respuestas en las preguntas abiertas
        for (int i = 0; i < listaPreguntas.size(); i++) {
            PreguntaAbierta pregunta = listaPreguntas.get(i);
            pregunta.setRespuestaEstudiante(respuestasEstudiante[i]); // Asignar la respuesta del estudiante
        }

        this.status = Status.Completado; // Cambiar el estado de la encuesta a completada
        System.out.println("La encuesta ha sido completada por: " + estudiante.getNombre());
    }

    // Método para verificar si la encuesta es exitosa (si fue completada)
    @Override
    public boolean esExitosa(Estudiante estudiante) {
        if (this.status == Status.Completado) {
            System.out.println("La encuesta ha sido completada exitosamente por: " + estudiante.getNombre());
            return true;
        } else {
            System.out.println("La encuesta no ha sido completada por: " + estudiante.getNombre());
            return false;
        }
    }

    // Método de evaluación (no aplica para Encuesta)
    @Override
    public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        // No se necesita implementación para Encuesta
        throw new UnsupportedOperationException("Las encuestas no requieren evaluación.");
    }

    // Método de reintentar (no aplica para Encuesta)
    @Override
    public void reintentar(Estudiante estudiante) {
        // No se necesita implementación para Encuesta
        throw new UnsupportedOperationException("Las encuestas no se pueden reintentar.");
    }

    // Crear y agregar una pregunta a la encuesta

    public void agregarPregunta(PreguntaAbierta pregunta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }

        listaPreguntas.add(pregunta);
    }

    // Método para eliminar una pregunta de la encuesta

    public void eliminarPregunta(PreguntaAbierta pregunta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }

        // Si la cantidad de preguntas es 0 o 1, no se puede eliminar más porque teiene que haber al menos una pregunta
        if (listaPreguntas.size() <= 1) {
            throw new UnsupportedOperationException("La encuesta debe tener al menos una pregunta.");
        }

        listaPreguntas.remove(pregunta);
    }
}
