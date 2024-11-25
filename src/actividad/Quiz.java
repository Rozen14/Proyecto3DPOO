package actividad;

import pregunta.PreguntaCerrada;
import usuario.Estudiante;
import usuario.Profesor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LPRS.LearningPath;

public class Quiz extends Actividad {

    protected List<PreguntaCerrada> listaPreguntas; // Lista de preguntas cerradas
    protected double calificacionMinima; // Calificación mínima para aprobar
    private Map<Estudiante, Double> calificacionesObtenidas; // Nota final obtenida por el estudiante

    public Quiz(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, 
                double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante, Obligatoria obligatoria, 
                List<PreguntaCerrada> listaPreguntas, double calificacionMinima, Profesor creador, 
                List<Actividad> actividadesPreviasSugeridas, List<Actividad> actividadesSeguimientoRecomendadas) {
        super(descripcion, nivelDificultad, objetivo, duracionEsperada, version, 
              fechaLimite, estadosPorEstudiante, obligatoria, "quiz", creador, 
              actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        this.listaPreguntas = listaPreguntas;
        this.calificacionMinima = calificacionMinima;
        this.calificacionesObtenidas = new HashMap<>(); // Inicializar la calificación obtenida en 0, ya que no se ha completado, cuando se haga la persistencia la idea es que se cargue la calificación obtenida con el setter en vez del constructor 
    }

    // Getters 

    public List<PreguntaCerrada> getListaPreguntas() {
        return listaPreguntas;
    }

    public double getCalificacionMinima() {
        return calificacionMinima;
    }

    public double getCalificacionObtenida(Estudiante estudiante) {
        return calificacionesObtenidas.getOrDefault(estudiante, 0.0);
    }

    public Map<Estudiante, Double> getCalificacionesObtenidas() {
        return calificacionesObtenidas;
    }

    // Setters que solo pueden ser usados por profesores, administradores, o por la persistencia de datos.

    public void setListaPreguntas(List<PreguntaCerrada> listaPreguntas) {
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

    
    // Método para responder al quiz completo
    @Override
    public void responder(Estudiante estudiante, String respuesta) {
        if (estudiante == null) {
            throw new IllegalArgumentException("Se requiere un estudiante para completar el quiz.");
        }

        if (respuesta == null || respuesta.isEmpty()) {
            throw new IllegalArgumentException("Las respuestas no pueden estar vacías.");
        }
    
        Status estadoEstudiante = estadosPorEstudiante.get(estudiante); // Obtener el estado del estudiante
        if (estadoEstudiante == Status.Completado) {
            throw new UnsupportedOperationException("El quiz ya ha sido completado exitosamente y no se puede repetir.");
        }
    
        System.out.println("Respuestas recibidas: " + respuesta); // Mostrar las respuestas recibidas

        int preguntasCorrectas = 0; // Contador de respuestas correctas
    
        // Dividir las respuestas según el formato esperado "1:A,2:B,3:C", esto se le pide al estudiante
        String[] respuestas = respuesta.split(","); // Separar las respuestas por coma con el formato "índice:respuesta", el split devuelve un arreglo de Strings
    
        for (String respuestaEstudiante : respuestas) { // Iterar sobre cada respuesta
            String[] partes = respuestaEstudiante.split(":"); // Separar el índice de la respuesta ya que solo nos interesa la respuesta
            int indicePregunta = Integer.parseInt(partes[0]) - 1; // Convertir índice de pregunta (1-based index) con paresInt que convierte un String a un entero
            String opcionSeleccionada = partes[1].trim(); // Opción seleccionada por el estudiante, el trim lo que hace es eliminar los espacios en blanco al inicio y al final de la cadena
    
            System.out.println("Opcion desglosada:" + opcionSeleccionada); // Mostrar la opción seleccionada por el estudiante
            // Asegurarse de que el índice de la pregunta sea válido
            if (indicePregunta >= 0 && indicePregunta < listaPreguntas.size()) {
                PreguntaCerrada pregunta = listaPreguntas.get(indicePregunta);
    
                // Marcar la opción elegida por el estudiante y verificar si es correcta
                pregunta.elegirRespuesta(opcionSeleccionada); // Marcar la respuesta elegida por el estudiante con el metodo de pregunta
                if (pregunta.esCorrecta()) { // Verificar si la respuesta es correcta
                    preguntasCorrectas++; // Incrementar el contador de respuestas correctas
                }
    
                // Mostrar retroalimentación de la pregunta
                System.out.println("Pregunta " + (indicePregunta + 1) + ": " + pregunta.getRetroalimentacion()); // Mostrar la retroalimentación de la pregunta
            } else {
                System.out.println("Índice de pregunta no válido: " + (indicePregunta + 1)); // Mostrar mensaje de error si el índice de pregunta no es válido
            }
        }
    
        
        // Calcular la nota final obtenida
        double calificacionObtenida = ((double) preguntasCorrectas / listaPreguntas.size()) * 100; // Calcular la calificación obtenida en base a las respuestas correctas
    
        // Guardar la calificación obtenida
        setCalificacionObtenida(estudiante, calificacionObtenida); // Guardar la calificación obtenida por el estudiante

        // Verificar si se alcanzó la calificación mínima para aprobar
        if (calificacionObtenida >= calificacionMinima) {
            System.out.println("El quiz fue completado exitosamente con una calificación de " + calificacionObtenida + "%.");
            setStatusParaEstudiante(estudiante, Status.Completado); // Cambiar el estado del estudiante a completado, ya que aprobó el quiz 
            // Agregar el quiz a la lista de actividades completadas del estudiante falta
        } else {
            estadosPorEstudiante.put(estudiante, Status.noExitosa);
            System.out.println("El quiz no fue aprobado. Calificación obtenida: " + calificacionObtenida + "%.");
        }
    }
    

    // Método para verificar si el quiz es exitoso (cumple la calificación mínima)
    @Override
    public boolean esExitosa(Estudiante estudiante) {

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        Status estadoActual = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante

        if (estadoActual == Status.Exitosa || estadoActual == Status.Completado) { // Si el estado es exitoso o completado, realmente exitoso y completado son lo mismo para nostros a fin de cuentas, el estudiante aprobó el quiz, por lo que consideramos ambos casos para no confundir al usuario de todas maneras
            System.out.println("El quiz fue completado exitosamente por: " + estudiante.getNombre());
            estudiante.agregarActividadCompletada(this); // Agregar el quiz a la lista de actividades completadas del estudiante
            setStatusParaEstudiante(estudiante, Status.Completado); // Cambiar el estado del estudiante a completado
            return true; // El quiz fue exitoso
        } else {
            System.out.println("El quiz no fue aprobado o realizado por: " + estudiante.getNombre()); // Mensaje de confirmación
            return false; // El quiz no fue exitoso
        }
    }

    // Método para reintentar el quiz
    @Override
    public void reintentar(Estudiante estudiante) {

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        
   
        Status estadoActual = getStatusParaEstudiante(estudiante); // Obtener el estado del estudiante
       
        if (estadoActual == Status.Exitosa || estadoActual == Status.Completado) { // Si el estado es exitoso o completado
            throw new UnsupportedOperationException("El quiz ya fue completado exitosamente y no se puede repetir."); // No se puede reintentar si ya se completó
        } else { // Si el estado es incompleto o no exitoso
            System.out.println("El estudiante " + estudiante.getNombre() + " puede iniciar o volver a intentar el quiz."); // Mensaje de confirmación
            // Reiniciar el estado del quiz para reintento
            setCalificacionObtenida(estudiante, 0.0); // Reiniciar la calificación obtenida
            for (PreguntaCerrada pregunta : listaPreguntas) {
                pregunta.setEscogida(null); // Reiniciar la respuesta elegida
            }

            setStatusParaEstudiante(estudiante, Status.Incompleto); // Cambiar el estado del estudiante a incompleto
        }
    }

    // Implementación vacía del método evaluar, ya que no se requiere para Quiz
    @Override
        public void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa) {
        // No se necesita implementación para Quiz
        System.out.println("El profesor " + profesor.getNombre() + " no puede evaluar el quiz.");
    }

    // Método para agregar una pregunta al quiz

    public void agregarPregunta(PreguntaCerrada pregunta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }


        listaPreguntas.add(pregunta);
    }

    // Método para eliminar una pregunta del quiz

    public void eliminarPregunta(PreguntaCerrada pregunta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("La pregunta no puede ser nula.");
        }

        // Si la cantidad de preguntas es 0 o 1, no se puede eliminar más porque teiene que haber al menos una pregunta
        if (listaPreguntas.size() <= 1) {
            throw new UnsupportedOperationException("El quiz debe tener al menos una pregunta.");
        }

        if (!listaPreguntas.contains(pregunta)) {
            throw new UnsupportedOperationException("La pregunta no está en el quiz.");
        }

        listaPreguntas.remove(pregunta);
    }

    // Inscripcion estudiante a quiz

    public void inscripcionEstudiante(Estudiante estudiante) {

        if (estudiante == null) { // Verificar si el estudiante es nulo
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
    
        if (estadosPorEstudiante.containsKey(estudiante)) { // Verificar si el estudiante ya está inscrito
            throw new UnsupportedOperationException("El estudiante ya está inscrito en el quiz.");
        }
    
        if (fechaLimite.isBefore(LocalDateTime.now())) { // Verificar si la fecha límite ya pasó
            throw new UnsupportedOperationException("La fecha límite para inscribirse en el quiz ha pasado.");
        }
    
        LearningPath learningPath = estudiante.getLearningPathActual(); // Obtener el Learning Path actual del estudiante
    
        if (learningPath == null) { // Verificar si el estudiante no tiene un Learning Path
            throw new UnsupportedOperationException("El estudiante no tiene un Learning Path.");
        }

        if (!learningPath.verificarSiInscrito(estudiante)) { // Verificar si el estudiante está inscrito en un Learning Path
            throw new UnsupportedOperationException("El estudiante no está inscrito en un Learning Path.");
        }

        if (learningPath.getLearningPathDeUnaActividad(this) == false) { // Verificar si el estudiante no está inscrito en el Learning Path de la actividad
            throw new UnsupportedOperationException("El estudiante no está inscrito en el Learning Path de la actividad.");
        }
    
        estadosPorEstudiante.put(estudiante, Status.Incompleto); // Agregar el estudiante al quiz con estado Incompleto
        calificacionesObtenidas.put(estudiante, 0.0); // Agregar el estudiante al quiz con calificación 0
        System.out.println("El estudiante " + estudiante.getNombre() + " se ha inscrito en la actividad."); // Mensaje de confirmación
    }
}
