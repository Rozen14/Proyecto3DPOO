package usuario;
import plataforma.*;
import actividad.*;
import LPRS.LearningPath;
import java.util.*;
import java.util.stream.Collectors;

public class Estudiante extends Usuario{

    protected Actividad actividadActual;
    protected LearningPath learningPathActual;
    public List<LearningPath> listaLearningPathsCompletados;
    protected List<Actividad> listaActividadesCompletadas;
    protected List<Actividad> listaActividadesPorCompletar;
    protected List<Actividad> listaActividadesPreviasSugeridas;

    public Estudiante(String nombre, String contrasenia, String correo){

        super(nombre, contrasenia, "estudiante", correo);
        this.actividadActual=null;
        this.learningPathActual=null;
        this.listaActividadesCompletadas = new ArrayList<>();
        this.listaLearningPathsCompletados = new ArrayList<>();
        this.listaActividadesPorCompletar = new ArrayList<>();
        this.listaActividadesPreviasSugeridas = new ArrayList<>();
        
    }

    public void actualizarActividadesPorCompletar(){

        if (learningPathActual!=null){
            listaActividadesPorCompletar = new ArrayList<>();
            listaActividadesPorCompletar.addAll(learningPathActual.getListaActividades());
        }
        else {
            throw new IllegalStateException("No hay un Learning Path que se está siguiendo actualmente; escoja uno y vuelva a intentar.");
        }
        for (int i=0; i<listaActividadesCompletadas.size(); i++){
            listaActividadesPorCompletar.remove(listaActividadesCompletadas.get(i));
        }



    }

    public void actualizarActividadesPreviasSugeridas(){

        if (actividadActual!=null){
            listaActividadesPreviasSugeridas = new ArrayList<>();
            listaActividadesPreviasSugeridas.addAll(actividadActual.getActividadesPreviasSugeridas());
        }
        else {
            throw new IllegalStateException("No hay una actividad que se está siguiendo actualmente; escoja una y vuelva a intentar.");
        }
        for (int i=0; i<listaActividadesCompletadas.size(); i++){
            listaActividadesPreviasSugeridas.remove(listaActividadesCompletadas.get(i));
        }

    }

    public Actividad getActividadActual() {
        return actividadActual;
    }

    public List<Actividad> getListaActividadesPorCompletar() {
        return listaActividadesPorCompletar;
    }

    public List<Actividad> getActividadesPreviasSugeridas(){
        return listaActividadesPreviasSugeridas;
    }

    public boolean actividadesPreviasSugeridasRealizadas(List<Actividad> listaActividadesPreviasSugeridas){
        for (int i=0; i<listaActividadesPreviasSugeridas.size(); i++){
            if (!listaActividadesCompletadas.contains(listaActividadesPreviasSugeridas.get(i))){
                return false;
            }
        }
        return true;
    }

    public void setActividadActual(Actividad actividadActual) {
        this.actividadActual = actividadActual;
    }

    public List<Actividad> getListaActividadesCompletadas() {
        return listaActividadesCompletadas;
    }
    

    public LearningPath getLearningPathActual() {
        return learningPathActual;
    }

    public void setLearningPathActual(LearningPath learningPathActual) {
        this.learningPathActual = learningPathActual;
    }

    public static void registrarEstudiante(String nombre, String correo, String contrasenia) {
		
        Estudiante estudiante = new Estudiante(nombre, correo, contrasenia);
		
	}


    public void marcarTareaCompletada(Tarea tarea, String submissionMethod){ // Este metodo en si esta mal solo se deberia marcar la tarea como completada si se ha enviado la tarea, esto lo uso es para los tests y ya

        tarea.setStatusParaEstudiante(this, Status.Enviada);
        listaActividadesCompletadas.add(this.actividadActual);
        listaActividadesPorCompletar.removeFirst();
        this.actividadActual=null;

        if (listaActividadesPorCompletar.isEmpty()){
            agregarLearningPathCompletado(this.learningPathActual);
        }
        else{
            new IllegalStateException("No se han completado todas las actividades para acabar el Learning Path.");
        }


    }

    public Actividad comenzarActividad() throws IllegalStateException{ // Ahora mismo esta no nos interesa pero servira para la interfaz de la proxima entrega
        
        if (actividadActual==null){
            if (learningPathActual!=null){
                if (!listaActividadesPorCompletar.isEmpty()){
                    if(actividadesPreviasSugeridasRealizadas(listaActividadesPreviasSugeridas)){
                        System.out.println("No se han completado todas las actividades previas sugeridas para comenzar la actividad. Seguro que quieres continuar con esta actividad?");
                        java.util.Scanner scanner = new java.util.Scanner(System.in);
                        String respuesta = scanner.nextLine();
                        if(respuesta.toUpperCase().equals("NO")){
                            scanner.close();
                            throw new IllegalStateException("Has decidido no comenzar la actividad.");
                        }
                        else if(respuesta.toUpperCase().equals("SI")){
                            scanner.close();
                            setActividadActual(listaActividadesPorCompletar.getFirst());
                            return actividadActual;
                        }        
                        else{
                            scanner.close();
                            throw new IllegalStateException("Respuesta inválida.");
                        }                
                    }
                    else{
                        setActividadActual(listaActividadesPorCompletar.getFirst());
                        return actividadActual;
                    }
                        
                    }
                else{
                    throw new IllegalStateException("No hay actividades que quedan por completar.");
                }
            }
            else{
                throw new IllegalStateException("No hay un Learning Path que se está siguiendo actualmente; escoja uno y vuelva a intentar.");
            }
        }

        else{
            throw new IllegalStateException("No se puede comenzar una actividad nueva porque hay una en progreso.");
        }
        
    }
    
    
    public void comenzarLearningPath(LearningPath learningPath){
        if (learningPathActual==null && !listaLearningPathsCompletados.contains(learningPathActual) ){
            setLearningPathActual(learningPath);
            this.listaActividadesPorCompletar = new ArrayList<>();
            listaActividadesPorCompletar.addAll(learningPath.getListaActividades());

        }
        else if (listaLearningPathsCompletados.contains(learningPathActual)){
            throw new IllegalStateException("No se puede comenzar un Learning Path que ya ha sido completado.");
        }
        else{
            throw new IllegalStateException("No se puede comenzar un Learning Path cuando ya hay uno en curso.");
        }

    }

    public void agregarLearningPathCompletado(LearningPath learningPathActual){

        if (listaActividadesPorCompletar.isEmpty()){
            listaLearningPathsCompletados.add(learningPathActual);
            this.listaActividadesPorCompletar=null;
            this.learningPathActual=null;
        }
        else{
            throw new IllegalStateException("No se puede completar un Learning Path que aún tiene actividades por completar.");
        }

    }
    

    // Agregar actividades completadas

        public void agregarActividadCompletada(Actividad actividad){
            listaActividadesCompletadas.add(actividad);
        }

        public List<String> listarActividadesPendientes() {
        if (listaActividadesPorCompletar == null || listaActividadesPorCompletar.isEmpty()) {
            throw new IllegalStateException("No hay actividades pendientes.");
        }
        return listaActividadesPorCompletar.stream()
                .map(Actividad::getDescripcion)
                .collect(Collectors.toList());
    }

    public float obtenerProgresoLearningPathActual() {
        if (learningPathActual == null) {
            throw new IllegalStateException("No hay un Learning Path en curso.");
        }
        int totalActividades = learningPathActual.getListaActividades().size();
        int completadas = listaActividadesCompletadas.size();
        float progreso = (float) completadas / totalActividades * 100;

        learningPathActual.actualizarProgreso(this, progreso);  

        return progreso;

        // Invocar
    }

    public boolean tieneLearningPathAsignado() {
        return learningPathActual != null;
    }

    public List<String> listarLearningPathsCompletados() {
        return listaLearningPathsCompletados.stream()
                .map(LearningPath::getTitulo)
                .collect(Collectors.toList());
    }
    
    public void responderActividad(Actividad actividad, String respuesta) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula.");
        }
    
        if (actividadActual == null || !actividadActual.equals(actividad)) {
            throw new IllegalStateException("No puedes responder una actividad que no está en progreso.");
        }
    
        if (actividad instanceof Tarea) {
            // Responder a una tarea
            ((Tarea) actividad).responder(this, respuesta);
        } else if (actividad instanceof Quiz) {
            // Responder a un quiz
            ((Quiz) actividad).responder(this, respuesta);
        } else if (actividad instanceof Examen) {
            // Responder a un examen
            ((Examen) actividad).responder(this, respuesta);
        } else if (actividad instanceof Encuesta) {
            // Responder a una encuesta
            ((Encuesta) actividad).responder(this, respuesta);
        } else if (actividad instanceof RecursoEducativo) {
            System.out.println("Los recursos educativos no requieren una respuesta directa.");
        } else {
            throw new IllegalArgumentException("Tipo de actividad no reconocido.");
        }
    
        // Actualizar el estado de la actividad
        actividad.setStatusParaEstudiante(this, Status.Enviada);
        listaActividadesCompletadas.add(actividad);
        listaActividadesPorCompletar.remove(actividad);
        actividadActual = null;
    
        System.out.println("Has completado la actividad: " + actividad.getDescripcion());
    }
    

}
