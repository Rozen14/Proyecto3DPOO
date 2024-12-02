package plataforma;
import actividad.Actividad;
import actividad.Nivel;
import persistencia.PersistenciaEstudiante;
import persistencia.PersistenciaLearningPath;
import persistencia.PersistenciaProfesor;
import usuario.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private final String RUTA_ESTUDIANTES = "src/persistencia/archivo/estudiantes.txt";
    private final String RUTA_PROFESORES = "src/persistencia/archivo/profesores.txt";
    private final String RUTA_LEARNING_PATHS = "src/persistencia/archivo/learningPaths.txt";


    public Plataforma(){

        if (plataforma == null){
            plataforma = this;
        }

        this.administrador = Administrador.getAdmin();
        this.estudiantes = new HashMap<>();
        this.profesores = new HashMap<>();
        this.learningPaths = new HashMap<>();
        cargarDatos();
    }

    private void cargarDatos(){
        cargarEstudiantes();
        cargarProfesores();
        cargarLearningPaths();
    }

    // Cargar estudiantes usando PersistenciaEstudiante
    private void cargarEstudiantes() {
        File archivoEstudiantes = new File(RUTA_ESTUDIANTES);
        if (!archivoEstudiantes.exists()) {
            System.out.println("Archivo de estudiantes no encontrado.");
            return;
        }

        try {
            List<Estudiante> listaEstudiantes = PersistenciaEstudiante.cargarEstudiantes(archivoEstudiantes, learningPaths);
            for (Estudiante estudiante : listaEstudiantes) {
                estudiantes.put(estudiante.getCorreo(), estudiante);
                System.out.println("Estudiante cargado: " + estudiante.getNombre());
            }
            System.out.println("Estudiantes cargados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar estudiantes: " + e.getMessage());
        }
    }
    

    // Cargar profesores usando PersistenciaProfesor
    private void cargarProfesores() {
        File archivoProfesores = new File(RUTA_PROFESORES);
        if (!archivoProfesores.exists()) {
            System.out.println("Archivo de profesores no encontrado.");
            return;
        }

        try {
            List<Profesor> listaProfesores = PersistenciaProfesor.cargarProfesores(archivoProfesores);
            for (Profesor profesor : listaProfesores) {
                profesores.put(profesor.getCorreo(), profesor);
            }
            System.out.println("Profesores cargados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar profesores: " + e.getMessage());
        }
    }
    

    private void cargarLearningPaths() {
        File archivoLearningPaths = new File(RUTA_LEARNING_PATHS);
    
        if (!archivoLearningPaths.exists()) {
            System.out.println("Archivo de Learning Paths no encontrado.");
            return;
        }
    
        try {
            System.out.println("Cargando Learning Paths...");
            // Iterar sobre cada profesor para cargar sus Learning Paths

            profesores = getProfesores();

            for (Profesor profesor : profesores.values()) {
                System.out.println("Cargando Learning Paths para el profesor: " + profesor.getNombre());
                List<LearningPath> learningPathsProfesor = new ArrayList<>();
    
                // Cargar todos los Learning Paths del archivo para este profesor
                LearningPath learningPath = LearningPath.cargarDeArchivo(archivoLearningPaths, profesor);
    
                System.out.println("Learning Path a cargar: " + learningPath.getTitulo());
                if (learningPath != null) {
                    learningPathsProfesor.add(learningPath);
                    learningPaths.put(learningPath.getTitulo(), learningPath); // Agregar al mapa global
                }
    
                // Asociar los Learning Paths al profesor
                profesor.setLearningPathCreado(learningPathsProfesor);
            }
    
            System.out.println("Learning Paths cargados correctamente.");
    
        } catch (IOException e) {
            System.out.println("Error al cargar Learning Paths: " + e.getMessage());
        }
    }
    
    
    

    public void guardarDatos() {
        guardarLearningPaths();
        guardarProfesores();
        guardarEstudiantes();
        
    }

    // Guardar estudiantes usando PersistenciaEstudiante
    private void guardarEstudiantes() {
        File archivoEstudiantes = new File(RUTA_ESTUDIANTES);
        try {
            for (Estudiante estudiante : estudiantes.values()) {
                PersistenciaEstudiante.guardarEstudiante(estudiante, archivoEstudiantes);
            }
            System.out.println("Estudiantes guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }

    private void guardarProfesores() {
        File archivoProfesores = new File(RUTA_PROFESORES);
        try {
            for (Profesor profesor : profesores.values()) {
                PersistenciaProfesor.guardarProfesor(profesor, archivoProfesores);
            }
            System.out.println("Profesores guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar profesores: " + e.getMessage());
        }
    }
    

    private void guardarLearningPaths() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LEARNING_PATHS, false))) {
            
            for (LearningPath learningPath : learningPaths.values()) {
                

                System.out.println("Guardando Learning Path: " + learningPath.getTitulo());
                // Mostrar datos del Learning Path
                System.out.println("Título: " + learningPath.getTitulo());
                System.out.println("Nivel: " + learningPath.getNivelDificultad());
                System.out.println("Descripción: " + learningPath.getDescripcion());
                System.out.println("Objetivos: " + learningPath.getObjetivos());
                System.out.println("Duración: " + learningPath.getDuracionMinutos());
                System.out.println("Fecha de creación: " + learningPath.getFechaCreacion());
                System.out.println("Creador: " + learningPath.getCreador().getNombre());
                System.out.println("Número de actividades: " + learningPath.getListaActividades().size());
                System.out.println("Número de estudiantes inscritos: " + learningPath.getEstudiantesInscritos().size());
                
                // Verificar que no sea el hp learning path de prueba
                if (!learningPath.getTitulo().equals("Learning Path de prueba")) {
                    learningPath.guardarEnArchivo();
                }
                
            }
            System.out.println("Learning Paths guardados exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar Learning Paths: " + e.getMessage());
        }
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
        System.out.println("LearningPaths disponibles en la plataforma:");
        for (String key : learningPaths.keySet()) {
            System.out.println("- " + key);
        }
        return learningPaths;
    }
    


    
}
