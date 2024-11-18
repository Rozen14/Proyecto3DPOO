
// FALTA ASEGURAR LO DE QUE LOS PROFESORES NO PUEDEN MODIFICAR SI YA HAY ESTUDIANTE SINSCRITOS AL LEARNIGN PATH ASOCIADO AL LEARNING PATH DONDE ESTA LA ACTIVIDAD


package actividad; 

import java.time.*; // Importar todo lo relacionado con la fecha y hora

import java.util.List; // Importar la lista de actividades previas sugeridas y de seguimiento recomendadas
import java.util.Map;
import LPRS.LearningPath; // Importar la clase LearningPath
import usuario.Usuario; // Importar la clase Usuario
import usuario.Administrador; // Importar la clase Administrador
import usuario.Estudiante; // Importar la clase Estudiante
import usuario.Profesor; // Importar la clase Profesor

public abstract class Actividad { // Clase abstracta Actividad

    // Atributos de la clase Actividad, son protected para que puedan ser accedidos por las subclases de Actividad
    protected String descripcion; 
    protected Nivel nivelDificultad;
    protected String objetivo;
    //actividades previas
    protected int duracionEsperada;
    protected double version;
    protected LocalDateTime fechaLimite;
    protected LocalDateTime fechaInicio;
    protected Map<Estudiante, Status> estadosPorEstudiante;
    protected Obligatoria obligatoria;
    protected String tipo;
    // Atributo de creador de la actividad
    protected Profesor creador;
    // Atributo para lista de actividades previas
    protected List<Actividad> actividadesPreviasSugeridas;

    // Lista de actividades de seguimiento recomendas
    protected List<Actividad> actividadesSeguimientoRecomendadas;

    public Actividad(String descripcion, Nivel nivelDificultad, String objetivo, int duracionEsperada, double version, LocalDateTime fechaLimite, Map<Estudiante, Status> estadosPorEstudiante, Obligatoria obligatoria, String tipo, Profesor creador, List<Actividad> actividadesPreviasSugeridas, List<Actividad> actividadesSeguimientoRecomendadas) throws IllegalArgumentException{

        this.fechaInicio=LocalDateTime.now(); // La fecha de inicio de la actividad sera la fecha y hora actual en la que se crea la actividad
        this.descripcion=descripcion;
        this.nivelDificultad=nivelDificultad;
        this.objetivo=objetivo;
        this.duracionEsperada = duracionEsperada;
        this.version = version;

        if (fechaLimite.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a la fecha de inicio.");
        } // La fecha límite no puede ser anterior a la fecha de inicio

        this.fechaLimite=fechaLimite;
        this.estadosPorEstudiante= estadosPorEstudiante; // Inicializamos el mapa de estados por estudiante vacio al principio, al igual que las listas de actividades previas y de seguimiento
        this.obligatoria = obligatoria;
        this.tipo=tipo;
        this.creador = creador;

        this.actividadesPreviasSugeridas = (actividadesPreviasSugeridas != null) ? actividadesPreviasSugeridas : actividadesPreviasSugeridas;
        this.actividadesSeguimientoRecomendadas = (actividadesSeguimientoRecomendadas != null) ? actividadesSeguimientoRecomendadas : actividadesSeguimientoRecomendadas;


    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public Nivel getNivelDificultad() {
        return nivelDificultad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public int getDuracionEsperada() {
        return duracionEsperada;
    }

    public double getVersion() {
        return version;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    // Establecer el estado para un estudiante específico
    public void setStatusParaEstudiante(Estudiante estudiante, Status status) {
        estadosPorEstudiante.put(estudiante, status);
    }

    // Obtener el estado de la actividad para un estudiante específico
    public Status getStatusParaEstudiante(Estudiante estudiante) {
        return estadosPorEstudiante.getOrDefault(estudiante, Status.Incompleto);
    }

    // Obtener todos los estados por estudiante (para revisión general del profesor)
    public Map<Estudiante, Status> getEstadosPorEstudiante() {
        return estadosPorEstudiante;
    }

    public Obligatoria getObligatoria() {
        return obligatoria;
    }

    public String getTipo() {
        return tipo;
    }


    public List<Actividad> getActividadesSeguimientoRecomendadas() {
        return actividadesSeguimientoRecomendadas;
    }

    public List<Actividad> getActividadesPreviasSugeridas() {
        return actividadesPreviasSugeridas;
    }

    public Profesor getCreador() {
        return creador;
    }
   
    // Metodos


    // Metodo de verificacion para todos los metodos relacionados con la modifiacion de actividades

    public boolean tienePermisoModificar(Usuario usuario) { // Vamos a hacerlo publico para que se pueda acceder desde las subclases tmb
        return (usuario instanceof Profesor && usuario.equals(creador)) || usuario instanceof Administrador; // Solo profesores y administradores pueden modificar actividades
    }


    // public abstract Actividad clonarActividad();


    // Metodos para el sistema de completar y respuesta de estudiantes con respecto a las actividades

    // METODO 1 - RESPONDER (SOLO PARA ESTUDIANTES), la idea es que cada subclase de actividad tenga su propio metodo responder, a partir de esto obtendremoos las respuestas que se necesiten para las subclases, y estas se mandaran al siguiente metodo:

    // Declaramos el metodo responder, que sera implementado en las subclases de actividad

    public abstract void responder(Estudiante estudiante, String respuesta); // Habra un atributo respuesta que sera String pero se adaptara a las necesidades de cada subclase de actividad

    // METODO 2 - EXITOSA (SOLO PARA ESTUDIANTES), verificar si la actividad fue exitosa o no, a partir de la respuesta obtenida en el metodo responder, y se mandara al siguiente metodo en el caso de que no se pueda automatizar, como en el caso de las actividades que tengan preguntas de tipo abierta
    public abstract boolean esExitosa(Estudiante estudiante);
    // METODO 3 - EVALUAR (SOLO PARA PROFESORES), la  idea es que cada subclase de actividad tenga su propio metodo evaluar, a partir de esto se obtendra la calificacion final de la actividad, y se marcara como exitosa o no dependiendo de la calificacion obtenida. En caso de que la actividad no se completa exitosamente, siguiente metodo:
    public abstract void evaluar(Profesor profesor, Estudiante estudiante, LearningPath learningPath, double calificacionObtenida, boolean exitosa);
    // METODO 4 - REINTENTAR (SOLO PARA ESTUDIANTES), la idea es que cada subclase de actividad tenga su propio metodo reintentar, a partir de esto se podra reintentar la actividad, y se podra completar nuevamente, en caso de que se complete exitosamente, se podra evaluar nuevamente, para que haya un ciclo de reintentos hasta que se complete exitosamente para la misma actividad, antes de continuar con otra
    public abstract void reintentar(Estudiante estudiante);

    

    // Metodos para modificacion de las actividades en si
    // Método para agregar una actividad previa sugerida en la clase Actividad
    public void agregarActividadPreviaSugerida(Actividad actividad, Usuario usuario, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se pueden modificar actividades previas si hay estudiantes inscritos en el Learning Path.");
        } // No se pueden modificar actividades previas si hay estudiantes inscritos en el Learning Path, per las reglas de dominio

        if (actividad == null){
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }

        if (this.equals(actividad)){
            throw new IllegalArgumentException("Una actividad no puede ser previa de sí misma.");
        }

        if (actividadesPreviasSugeridas.contains(actividad)){
            throw new IllegalArgumentException("La actividad ya está en la lista de actividades previas sugeridas.");
        }
        if (tienePermisoModificar(usuario)) { // Solo el creador o un administrador pueden modificar actividades previas sugeridas
             // Si la actividad no es nula y no esta en la lista de actividades previas sugeridas
                actividadesPreviasSugeridas.add(actividad); // La agregamos
                System.out.println("Actividad previa sugerida agregada por: " + usuario.getNombre()); // Mensaje de confirmacion
        } else {
            throw new SecurityException("Solo el creador o un administrador pueden modificar actividades previas sugeridas."); // Mensaje de error si no se tiene permiso
        }
    }


    public void eliminarActividadPreviaSugerida(Actividad actividad, Usuario usuario, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se pueden modificar actividades previas si hay estudiantes inscritos en el Learning Path.");
        }
    
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }
    
        if (!actividadesPreviasSugeridas.contains(actividad)) {
            throw new IllegalArgumentException("La actividad no está en la lista de actividades previas sugeridas.");
        }
    
        if (!tienePermisoModificar(usuario)) {
            throw new SecurityException("Solo el creador o un administrador pueden eliminar actividades previas sugeridas.");
        }
    
        actividadesPreviasSugeridas.remove(actividad);
        System.out.println("Actividad previa sugerida eliminada por: " + usuario.getNombre());
    }

    public void agregarActividadSeguimiento(Actividad actividad, Usuario usuario, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se pueden modificar actividades de seguimiento si hay estudiantes inscritos en el Learning Path.");
        }
    
    
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }

        if (this.equals(actividad)){
            throw new IllegalArgumentException("Una actividad no puede ser de seguimiento de sí misma.");
        }

        if (actividadesSeguimientoRecomendadas.contains(actividad)) {
            throw new IllegalArgumentException("La actividad ya está en la lista de actividades de seguimiento recomendadas.");
        }

        if (!tienePermisoModificar(usuario)) {
            throw new SecurityException("Solo el creador o un administrador pueden modificar actividades de seguimiento.");
        }

        actividadesSeguimientoRecomendadas.add(actividad);
        System.out.println("Actividad de seguimiento recomendada agregada por: " + usuario.getNombre());
    }

    public void eliminarActividadSeguimiento(Actividad actividad, Usuario usuario, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se pueden modificar actividades de seguimiento si hay estudiantes inscritos en el Learning Path.");
        }
    
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }
    
        if (!actividadesSeguimientoRecomendadas.contains(actividad)) {
            throw new IllegalArgumentException("La actividad no está en la lista de actividades de seguimiento recomendadas.");
        }
    
        if (!tienePermisoModificar(usuario)) {
            throw new SecurityException("Solo el creador o un administrador pueden eliminar actividades de seguimiento.");
        }
    
        actividadesSeguimientoRecomendadas.remove(actividad);
        System.out.println("Actividad de seguimiento recomendada eliminada por: " + usuario.getNombre());
    }

    // Métodos de modificación de atributos clave (Setters con verificación de inscripciones en Learning Path)
    public void setDescripcion(String descripcion, LearningPath learningPath) { // Metodo para modificar la descripcion de la actividad
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar la descripción si hay estudiantes inscritos en el Learning Path."); // No se puede modificar la descripcion si hay estudiantes inscritos en el Learning Path
        }
        
        this.descripcion = descripcion;
    }

    public void setNivelDificultad(Nivel nivelDificultad, LearningPath learningPath) { // Metodo para modificar el nivel de dificultad de la actividad
        if (learningPath.verificarSiHayInscritos()) { // No se puede modificar el nivel de dificultad si hay estudiantes inscritos en el Learning Path
            throw new UnsupportedOperationException("No se puede modificar el nivel de dificultad si hay estudiantes inscritos en el Learning Path."); // Mensaje de error
        }
        this.nivelDificultad = nivelDificultad;
    }

    public void setObjetivo(String objetivo, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar el objetivo si hay estudiantes inscritos en el Learning Path.");
        }
        this.objetivo = objetivo;
    }

    public void setDuracionEsperada(int duracionEsperada, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar la duración esperada si hay estudiantes inscritos en el Learning Path.");
        }
        this.duracionEsperada = duracionEsperada;
    }


    public void setFechaLimite(LocalDateTime fechaLimite, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar la fecha límite si hay estudiantes inscritos en el Learning Path.");
        }
        if (fechaLimite.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("La nueva fecha límite no puede ser anterior a la fecha de inicio.");
        }
        this.fechaLimite = fechaLimite;
    }

    public void setVersion(double version, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar la versión si hay estudiantes inscritos en el Learning Path.");
        }
        this.version = version;
    }

    public void setObligatoria(Obligatoria obligatoria, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar si la actividad es obligatoria u opcional si hay estudiantes inscritos en el Learning Path.");
        }
        this.obligatoria = obligatoria;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        // Este método no verifica si hay estudiantes inscritos, ya que la fecha de inicio no debería cambiar una vez que se ha creado la actividad. Solo se usara por la persistencia de datos y no por los profesores o administradores.
        this.fechaInicio = fechaInicio;
    }

    public void setTipo(String tipo, LearningPath learningPath) {
        if (learningPath.verificarSiHayInscritos()) {
            throw new UnsupportedOperationException("No se puede modificar el tipo de la actividad si hay estudiantes inscritos en el Learning Path.");
        }
        this.tipo = tipo;
    }

    public boolean esObligatoria(){ 
        // Retornar "SI" si la actividad es obligatoria, "NO" si es opcional
        return obligatoria.equals(Obligatoria.SI);
    }


}
