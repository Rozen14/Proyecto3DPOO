package actividad;

import pregunta.Pregunta;
import pregunta.PreguntaCerrada;
import pregunta.PreguntaAbierta;
import usuario.Estudiante;
import usuario.Profesor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import LPRS.LearningPath;

public class Examen extends Actividad {

    protected List<Pregunta> listaPreguntas;
    protected double calificacionMinima;
    private List<String> respuestasAbiertas;
    private int respuestasCorrectas;
    private double calificacionObtenida;

    public Examen(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, 
                  double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante, Obligatoria obligatoria, 
                  List<Pregunta> listaPreguntas, double calificacionMinima, Profesor creador, 
                  List<Actividad> actividadesPreviasSugeridas, List<Actividad> actividadesSeguimientoRecomendadas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, 
              fechaLimite, estadosPorEstudiante, obligatoria, "examen", creador, 
              actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        this.listaPreguntas = listaPreguntas;
        this.calificacionMinima = calificacionMinima;
        this.respuestasCorrectas = 0;
        this.calificacionObtenida = 0.0;
        this.respuestasAbiertas = new ArrayList<>();
    }

    // Getters
    public List<Pregunta> getListaPreguntas() { 
        return listaPreguntas;
    }

    public double getCalificacionMinima() {
        return calificacionMinima;
    }

    public double getCalificacionObtenida() {
        return calificacionObtenida;
    }

    public int getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public List<String> getRespuestasAbiertas() {
        return respuestasAbiertas;
    }
 

    // Setters
    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public void setCalificacionMinima(double calificacionMinima) {
        this.calificacionMinima = calificacionMinima;
    }

    public void setCalificacionObtenida(double calificacionObtenida) {
        this.calificacionObtenida = calificacionObtenida;
    }

    public void setRespuestasCorrectas(int respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
    }

    public void setRespuestasAbiertas(List<String> respuestasAbiertas) {
        this.respuestasAbiertas = respuestasAbiertas;
    }

    // Método para que el estudiante responda el examen
    @Override
    public void responder(Estudiante estudiante, String respuestas) {
        if (estudiante == null) {
            throw new SecurityException("Se requiere un estudiante para completar el examen.");
        }

        Status estadoActual = getStatusParaEstudiante(estudiante);
        if (estadoActual == Status.Completado || estadoActual == Status.Enviada) {
            throw new UnsupportedOperationException("El examen ya ha sido completado o enviado por este estudiante.");
        }

        String[] respuestasEstudiante = respuestas.split(";");
        if (respuestasEstudiante.length != listaPreguntas.size()) {
            throw new IllegalArgumentException("La cantidad de respuestas no coincide con la cantidad de preguntas en el examen.");
        }

        int respuestasCorrectas = 0;
        for (int i = 0; i < respuestasEstudiante.length; i++) { // Iterar sobre las respuestas del estudiante
            Pregunta pregunta = listaPreguntas.get(i); // Obtener la pregunta correspondiente
            String respuestaEstudiante = respuestasEstudiante[i]; // Obtener la respuesta del estudiante

            if (pregunta instanceof PreguntaCerrada) { // Verificar si la pregunta es cerrada
                PreguntaCerrada preguntaCerrada = (PreguntaCerrada) pregunta; // Castear la pregunta a PreguntaCerrada
                preguntaCerrada.elegirRespuesta(respuestaEstudiante); // Elegir la respuesta del estudiante con el metodo de esa clase
                if (preguntaCerrada.esCorrecta()) {  // Verificar si la respuesta es correcta
                    respuestasCorrectas++; // Aumentar el contador de respuestas correctas
                }
            } else if (pregunta instanceof PreguntaAbierta) { // Verificar si la pregunta es abierta
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta; // Castear la pregunta a PreguntaAbierta, esto no se usa pero lo hago para guiarme en el futuro
                respuestasAbiertas.add(respuestaEstudiante);
            }
        }

        this.respuestasCorrectas = respuestasCorrectas; // Guardar la cantidad de respuestas correctas
        setStatusParaEstudiante(estudiante, Status.Enviada); // Cambiar el estado del estudiante a Enviada
        System.out.println("El examen ha sido enviado por: " + estudiante.getNombre()); // Mensaje de confirmación
    }

    // Método para que el profesor evalúe las preguntas abiertas del examen
    @Override
    public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        if (profesor == null || !profesor.equals(creador)) { // Verificar si el profesor es nulo o no es el creador
            throw new SecurityException("Solo el profesor creador puede evaluar el examen."); // Lanzar excepción
        }

        if (!learningPath.verificarSiInscrito(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en el Learning Path para este examen."); // Lanzar excepción
        }

        System.out.println("Advertencia: la calificación proporcionada (" + calificacionObtenida + ") se ignorará en el examen."); // Advertencia

        for (Pregunta pregunta : listaPreguntas) { // Iterar sobre las preguntas del examen
            if (pregunta instanceof PreguntaAbierta) { // Verificar si la pregunta es abierta
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta; // Castear la pregunta a PreguntaAbierta

                // Evaluar la pregunta abierta, esto el profesor lo hace manualmente en la vida real, cuando se haga interfaz gráfica se puede hacer de forma automática con el metodo evaluarPorProfesor en PreguntaAbierta.
                if (preguntaAbierta.esCorrecta()) {  // Verificar si la respuesta es correcta
                    respuestasCorrectas++; // Aumentar el contador de respuestas correctas

                }
            }
        }

        calcularCalificacionFinal(); // Calcular la calificación final del examen con el método auxiliar
        if (this.calificacionObtenida >= this.calificacionMinima) { // Verificar si la calificación obtenida es mayor o igual a la mínima
            setStatusParaEstudiante(estudiante, Status.Exitosa); // Cambiar el estado del estudiante a Exitosa
            System.out.println("El examen ha sido aprobado por: " + estudiante.getNombre() + " con una nota de " + calificacionObtenida + "%."); // Mensaje de confirmación
        } else { // Si la calificación no es suficiente
            setStatusParaEstudiante(estudiante, Status.noExitosa); // Cambiar el estado del estudiante a noExitosa
            System.out.println("El examen no ha sido aprobado por: " + estudiante.getNombre() + ". Nota obtenida: " + calificacionObtenida + "%."); // Mensaje de confirmación
        }
    }

    // Método para calcular la calificación final del examen
    private void calcularCalificacionFinal() { 
        int totalPreguntas = listaPreguntas.size(); // Obtener la cantidad total de preguntas
        calificacionObtenida = ((double) respuestasCorrectas / totalPreguntas) * 100; // Calcular la calificación obtenida
    }

    // Método para verificar si el examen es exitoso para un estudiante específico
    @Override
    public boolean esExitosa(Estudiante estudiante) { 
        Status estadoEstudiante = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante
        if (estadoEstudiante == Status.Exitosa || estadoEstudiante == Status.Completado) { // Si el estado es exitoso o completado
            System.out.println("El examen fue completado exitosamente por: " + estudiante.getNombre() + " con una nota de " + calificacionObtenida + "%."); // Mensaje de confirmación
            estudiante.agregarActividadCompletada(this); // Agregar el examen a la lista de actividades completadas del estudiante
            return true; // Retornar verdadero
        } else { // Si el examen no fue completado
            System.out.println("El examen no ha sido completado exitosamente por: " + estudiante.getNombre()); // Mensaje de confirmación
            return false; // Retornar falso
        }
    }

    // Método para reintentar el examen por un estudiante específico, esto se usara más que todo en la interfaz gráfica
    @Override
    public void reintentar(Estudiante estudiante) {
        Status estadoEstudiante = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante
        if (estadoEstudiante == Status.Enviada || estadoEstudiante == Status.Exitosa) { // Si el estado es enviado o exitoso     
            throw new UnsupportedOperationException("El examen ya ha sido enviado o aprobado y no se puede reintentar."); // No se puede reintentar
        }

        respuestasCorrectas = 0; // Reiniciar el contador de respuestas correctas
        calificacionObtenida = 0.0; // Reiniciar la calificación obtenida
        respuestasAbiertas.clear(); // Limpiar las respuestas abiertas
        setStatusParaEstudiante(estudiante, Status.Incompleto); // Cambiar el estado del estudiante a Incompleto
        System.out.println("El examen ha sido reiniciado por: " + estudiante.getNombre()); // Mensaje de confirmación
    }

    // Método para agregar una pregunta al examen
    public void agregarPregunta(Pregunta pregunta) {
        if (pregunta == null) { // Verificar si la pregunta es nula
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }

        listaPreguntas.add(pregunta); // Agregar la pregunta a la lista de preguntas
    }

    // Método para eliminar una pregunta del examen
    public void eliminarPregunta(Pregunta pregunta) {
        if (pregunta == null) { // Verificar si la pregunta es nula
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }

        if (listaPreguntas.size() <= 1) {
            throw new UnsupportedOperationException("El examen debe tener al menos una pregunta.");
        }

        listaPreguntas.remove(pregunta); // Eliminar la pregunta de la lista de preguntas
    }
}
