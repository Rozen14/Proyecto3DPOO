package plataforma;
import actividad.Actividad;
import actividad.Nivel;
import usuario.*;
import java.time.*;
import java.util.*;
import LPRS.LearningPath;

// ESTA CLASE NO IMPORTA PARA LA ENTREGA 2, ESTA LA TENEMOS DE BASE PARA LA PROXIMA ENTREGA, IGNORAR!

public class Plataforma {

    private static Plataforma plataforma = new Plataforma();
    private Administrador administrador;
    private Map<String, Estudiante> estudiantes;
    private Map<String, Profesor> profesores;
    private Map<String, LearningPath> learningPaths;

    public Plataforma(){

        if (plataforma == null){
            plataforma = this;
        }

        this.administrador = Administrador.getAdmin();
        this.estudiantes = new HashMap<>();
        this.profesores = new HashMap<>();

    }
    public static Plataforma getPlataforma() {

        if ((plataforma).equals(null)){

            plataforma = new Plataforma();

        }
        return plataforma;
    }

    public Administrador getAdministrador() {
        return administrador;
    }
    public Map<String, Estudiante> getEstudiantes() {
        return estudiantes;
    }

    

    public boolean agregarEstudiante(Estudiante estudiante){

        String correo = estudiante.getCorreo();
        if (estudiantes.containsKey(correo)){
            return false;
        }
        else{
            estudiantes.put(correo, estudiante);
            return true;
        }

    }

    public boolean agregarProfesor(Profesor profesor){

        String correo = profesor.getCorreo();
        if (profesores.containsKey(correo)){
            return false;
        }
        else{
            profesores.put(correo, profesor);
            return true;
        }

    }

    public boolean agregarLearningPath(LearningPath learningPath){

        String titulo = learningPath.getTitulo();
        if (learningPaths.containsKey(titulo)){
            return false;
        }
        else{
            learningPaths.put(titulo, learningPath);
            return true;
        }

    } 


    /**
     
   public void crearActividad(){

       Actividad actividad = new Actividad("A", Nivel.Principiante, "hola", 300, 2.5, LocalDateTime.plusHours(2));


    }
    */

    public Map<String, Profesor> getProfesores() {
        return profesores;
    }
    public Map<String, LearningPath> getLearningPaths() {
        return learningPaths;
    }


    
}
