package usuario;

import java.util.List;

import LPRS.LearningPath;
import actividad.Actividad;

import consola.Autenticacion;

public class Administrador extends Usuario{

    private static Administrador admin;

    private Administrador(){

        super("Valentina", "vale123", "admin", "administrador@lprs.com");

    }

    public static Administrador getAdmin() {

        if (admin == null){

            admin = new Administrador();

        }
        return admin;
    }

    public void crearProfesor(String nombre, String contrasenia, String correo, Autenticacion autenticacion){

        Profesor profesor = new Profesor(nombre, contrasenia, correo, null, null); // Se crea un profesor con los datos ingresados
        autenticacion.agregarUsuario(nombre, profesor);
    }

    public void crearEstudiante(String nombre, String contrasenia, String correo, Autenticacion autenticacion){

        Estudiante estudiante = new Estudiante(nombre, contrasenia, correo); // Se crea un estudiante con los datos ingresados
        autenticacion.agregarUsuario(nombre, estudiante);
    }

    public void eliminarUsuario(Usuario usuario, Autenticacion autenticacion){
        autenticacion.eliminarUsuario(usuario);
    }

    public void eliminarLearningPath(LearningPath learningPath){

        learningPath.getCreador().getLearningPathCreado().remove(learningPath);

        for (Estudiante estudiante : learningPath.getEstudiantesInscritos()) {
            if (estudiante.getLearningPathActual() != null && estudiante.getLearningPathActual().equals(learningPath)) {
                estudiante.setLearningPathActual(null); // Desasociar el Learning Path
            }
        }
    }
    
    public void eliminarActividad(Actividad actividad){

        List<LearningPath> learningPaths = actividad.getCreador().getLearningPathCreado();
        for (LearningPath learningPath : learningPaths) {
            if (learningPath.getListaActividades().contains(actividad)) {
                learningPath.getListaActividades().remove(actividad);

                // Desasociar la actividad de los estudiantes inscritos
                for (Estudiante estudiante : learningPath.getEstudiantesInscritos()) {
                    List<Actividad> completadas = estudiante.getListaActividadesCompletadas();
                    if (completadas != null && completadas.contains(actividad)) {
                        completadas.remove(actividad);
                    }

                    if (estudiante.getActividadActual() != null && estudiante.getActividadActual().equals(actividad)) {
                        estudiante.setActividadActual(null);
                    }
                }
            }

        if (actividad.getActividadesPreviasSugeridas() != null) {
            actividad.getActividadesPreviasSugeridas().clear();
        }
        if (actividad.getActividadesSeguimientoRecomendadas() != null) {
            actividad.getActividadesSeguimientoRecomendadas().clear();
        }
    }
}

}
