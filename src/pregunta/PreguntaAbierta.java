package pregunta;

import java.util.List;
import java.util.ArrayList;
import usuario.Profesor;

public class PreguntaAbierta extends Pregunta {

    private String respuestaEstudiante; // Respuesta del estudiante
    private boolean evaluada; // Indica si ha sido evaluada por el profesor
    private boolean esCorrecta; // Indica si la respuesta es correcta
    private String comentarioProfesor; // Comentario del profesor

    // Constructor modificado para aceptar el enunciado
    public PreguntaAbierta(String enunciado) {
        super(Tipo.Abierta, enunciado); // Llamar al constructor de Pregunta con enunciado
        this.evaluada = false;
    }

    // Método para que el estudiante escriba su respuesta
    public void setRespuestaEstudiante(String respuestaEstudiante) {
        this.respuestaEstudiante = respuestaEstudiante;
    }

    public String getRespuestaEstudiante() {
        return respuestaEstudiante;
    }

    // Método para que el profesor marque la pregunta como evaluada
    public void evaluarPorProfesor(boolean esCorrecta, String comentario) { 
    

        if (comentario == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo.");
        }

        if (comentario.isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío.");
        }

        this.evaluada = true;
        this.esCorrecta = esCorrecta;
        this.comentarioProfesor = comentario;

        // Mandar notificación al estudiante

        System.out.println("La pregunta ha sido evaluada por el profesor.");
    }


    // Indica si la pregunta ha sido evaluada por el profesor
    @Override
    public boolean esEvaluada() {
        return evaluada;
    }

    @Override
    public String getRetroalimentacion() {
        if (!evaluada) {
            return "Pendiente de evaluación por el profesor.";
        }
        return esCorrecta ? "Respuesta correcta. " + comentarioProfesor : "Respuesta incorrecta. " + comentarioProfesor;
    }

    @Override
    public boolean esCorrecta() {
        return evaluada && esCorrecta;
    }


}
