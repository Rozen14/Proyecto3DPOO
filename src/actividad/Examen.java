package actividad;

import pregunta.Pregunta;
import pregunta.PreguntaCerrada;
import pregunta.PreguntaAbierta;
import usuario.Estudiante;
import usuario.Profesor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LPRS.LearningPath;

public class Examen extends Actividad {

    protected List<Pregunta> listaPreguntas;
    protected double calificacionMinima;
    private List<String> respuestasAbiertas;
    private int respuestasCorrectas;
    private Map <Estudiante, Double> calificacionesObtenidas;

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
        this.calificacionesObtenidas = new HashMap<>();
        this.respuestasAbiertas = new ArrayList<>();
    }

    // Getters
    public List<Pregunta> getListaPreguntas() { 
        return listaPreguntas;
    }

    public double getCalificacionMinima() {
        return calificacionMinima;
    }

    public double getCalificacionObtenida(Estudiante estudiante) {
        return calificacionesObtenidas.getOrDefault(estudiante, 0.0);
    }

    public int getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public List<String> getRespuestasAbiertas() {
        return respuestasAbiertas;
    }

    public Map<Estudiante, Double> getCalificacionesObtenidas() {
        return calificacionesObtenidas;
    }
 

    // Setters
    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public void setCalificacionMinima(double calificacionMinima) {
        this.calificacionMinima = calificacionMinima;
    }

    public void setCalificacionObtenida(Estudiante estudiante, double calificacionObtenida) {
        this.calificacionesObtenidas.put(estudiante, calificacionObtenida);
    }

    public void setCalificacionesObtenidas(Map<Estudiante, Double> calificacionesObtenidas) {
        this.calificacionesObtenidas = calificacionesObtenidas;
    }
    

    public void setRespuestasCorrectas(int respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
    }

    public void setRespuestasAbiertas(List<String> respuestasAbiertas) {
        this.respuestasAbiertas = respuestasAbiertas;
    }

    public void inscripcionEstudiante(Estudiante estudiante)
    {

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        if (estadosPorEstudiante.containsKey(estudiante)) { // Verificar si el estudiante ya está inscrito
            throw new UnsupportedOperationException("El estudiante ya está inscrito en el examen.");
        }

        if (fechaLimite.isBefore(LocalDateTime.now())) { // Verificar si la fecha límite ya pasó
            throw new UnsupportedOperationException("La fecha límite para inscribirse en el examen ha pasado.");
        }

        LearningPath learningPath = estudiante.getLearningPathActual(); // Obtener el Learning Path actual del estudiante
    
        if (learningPath == null) { // Verificar si el Learning Path es nulo
            throw new UnsupportedOperationException("El estudiante no tiene un Learning Path.");
        }

        if (!learningPath.verificarSiInscrito(estudiante)) { // Verificar si el estudiante está inscrito en un Learning Path
            throw new UnsupportedOperationException("El examen es obligatorio y el estudiante no está inscrito en un Learning Path.");
        }

        if (learningPath.verificarInscripcionYActividad(estudiante, this) == false) { // Verificar si el Learning Path tiene la actividad actual
            throw new UnsupportedOperationException("El examen no está en el Learning Path del estudiante.");
        } // Mato dos pajaros de un tiro verificando si el estudiante está inscrito en el learning path y si la actividad está en el learning path
        

        estadosPorEstudiante.put(estudiante, Status.Incompleto); // Agregar el estudiante al examen con estado Incompleto
        calificacionesObtenidas.put(estudiante, 0.0); // Agregar el estudiante al examen con calificación 0
        System.out.println("El estudiante " + estudiante.getNombre() + " se ha inscrito en la actividad."); // Mensaje de confirmación

    }

    // Método para que el estudiante responda el examen
    @Override
    public void responder(Estudiante estudiante, String respuestas) {

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        
        if (respuestas == null || respuestas.isEmpty()) {
            throw new IllegalArgumentException("Las respuestas no pueden estar vacías.");
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
            }
            
            if (pregunta instanceof PreguntaAbierta) { // Verificar si la pregunta es abierta
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

        if (learningPath == null) { // Verificar si el learningPath es nulo
            throw new IllegalArgumentException("El Learning Path no puede ser nulo.");
        }

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        if (!learningPath.verificarSiInscrito(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en el Learning Path para este examen."); // Lanzar excepción
        }


        System.out.println("Advertencia: la calificación proporcionada (" + calificacionObtenida + ") se ignorará en el examen."); // Advertencia

        for (Pregunta pregunta : listaPreguntas) { // Iterar sobre las preguntas del examen
            if (pregunta instanceof PreguntaAbierta) { // Verificar si la pregunta es abierta
                PreguntaAbierta preguntaAbierta = (PreguntaAbierta) pregunta; // Castear la pregunta a PreguntaAbierta
                
                // Evaluar la pregunta abierta, esto el profesor lo hace manualmente en la vida real, cuando se haga interfaz gráfica se puede hacer de forma automática con el metodo evaluarPorProfesor en PreguntaAbierta.
                
                // Mandar mensaje que a continuación se evaluará la pregunta abierta
                System.out.println("Evaluando la pregunta abierta: " + preguntaAbierta.getEnunciado());
                
                if (preguntaAbierta.esCorrecta()) {  // Verificar si la respuesta es correcta
                    respuestasCorrectas++; // Aumentar el contador de respuestas correctas

                }
            }
        }

        calcularCalificacionFinal(estudiante); // Calcular la calificación final del examen con el método auxiliar
        if (this.calificacionesObtenidas.get(estudiante) >= this.calificacionMinima) { // Verificar si la calificación obtenida es mayor o igual a la mínima
            setStatusParaEstudiante(estudiante, Status.Completado); // Cambiar el estado del estudiante a Exitosa
            System.out.println("El examen ha sido aprobado por: " + estudiante.getNombre() + " con una nota de " + calificacionObtenida + "%."); // Mensaje de confirmación
        } else { // Si la calificación no es suficiente
            setStatusParaEstudiante(estudiante, Status.noExitosa); // Cambiar el estado del estudiante a noExitosa
            System.out.println("El examen no ha sido aprobado por: " + estudiante.getNombre() + ". Nota obtenida: " + calificacionObtenida + "%."); // Mensaje de confirmación
        }
    }

    // Método para calcular la calificación final del examen
    public void calcularCalificacionFinal(Estudiante estudiante) { 
        int totalPreguntas = listaPreguntas.size(); // Obtener la cantidad total de preguntas
        double calificacionObtenida = ((double) respuestasCorrectas / totalPreguntas) * 100; // Calcular la calificación obtenida en base a las respuestas correctas
        calificacionesObtenidas.put(estudiante, calificacionObtenida); // Guardar la calificación obtenida para el estudiante
    }

    // Método para verificar si el examen es exitoso para un estudiante específico
    @Override
    public boolean esExitosa(Estudiante estudiante) { 

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        Status estadoEstudiante = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante
        if (estadoEstudiante == Status.Completado) { // Si el estado es completado
            System.out.println("El examen fue completado exitosamente por: " + estudiante.getNombre() + " con una nota de " + calificacionesObtenidas.get(estudiante) + "%."); // Mensaje de confirmación
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

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        Status estadoEstudiante = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante
        if (estadoEstudiante == Status.Enviada || estadoEstudiante == Status.Completado) { // Si el estado es enviado o exitoso     
            throw new UnsupportedOperationException("El examen ya ha sido enviado o aprobado y no se puede reintentar."); // No se puede reintentar
        }

        respuestasCorrectas = 0; // Reiniciar el contador de respuestas correctas
        calificacionesObtenidas.put(estudiante, 0.0); // Reiniciar la calificación obtenida para el estudiante
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

        if (!listaPreguntas.contains(pregunta)) { // Verificar si la pregunta no está en la lista de preguntas
            throw new IllegalArgumentException("La pregunta no está en el examen.");  
        }

        listaPreguntas.remove(pregunta); // Eliminar la pregunta de la lista de preguntas
    }
}
