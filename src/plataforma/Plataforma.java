package plataforma;
import actividad.Actividad;
import actividad.Nivel;
import usuario.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

    private void cargarEstudiantes() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(RUTA_ESTUDIANTES))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ESTUDIANTE")) {
                    String[] partes = line.split(",");
                    if (partes.length >= 4) {
                        String nombre = partes[1];
                        String contrasenia = partes[2];
                        String correo = partes[3];
                        estudiantes.put(correo, new Estudiante(nombre, contrasenia, correo));
                    } else {
                        System.out.println("Error en el formato de la línea: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar estudiantes: " + e.getMessage());
        }
    }
    

    private void cargarProfesores() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(RUTA_PROFESORES))) {
            String line;
            Profesor profesorActual = null;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Elimina espacios en blanco adicionales
                if (line.isEmpty()) continue; // Ignora líneas vacías
                System.out.println("Procesando línea: " + line);
                if (line.startsWith("PROFESOR")) {
                    String[] partes = line.split(",");
                    if (partes.length >= 4) {
                        String nombre = partes[1];
                        String contrasenia = partes[2];
                        String correo = partes[3];
                        profesorActual = new Profesor(nombre, contrasenia, correo, new ArrayList<>(), new ArrayList<>());
                        profesores.put(correo, profesorActual);
                    } else {
                        System.out.println("Formato inválido en la línea: " + line);
                    }
                } else if (line.startsWith("LEARNING_PATH")) {
                    if (profesorActual != null) {
                        String[] partes = line.split(",");
                        if (partes.length >= 2) {
                            String titulo = partes[1];
                            LearningPath lp = learningPaths.get(titulo);
                            if (lp != null) {
                                profesorActual.getLearningPathCreado().add(lp);
                            } else {
                                System.out.println("Learning Path no encontrado: " + titulo);
                            }
                        } else {
                            System.out.println("Formato inválido en la línea: " + line);
                        }
                    } else {
                        System.out.println("No se encontró un profesor asociado para el Learning Path: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar profesores: " + e.getMessage());
        }
    }    
    

    private void cargarLearningPaths() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(RUTA_LEARNING_PATHS))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Learning Path")) {
                    String[] partes = line.split(",");
                    if (partes.length >= 5) {
                        String titulo = partes[0];
                        Nivel nivel = Nivel.valueOf(partes[1]);
                        String descripcion = partes[2];
                        String objetivos = partes[3];
                        int duracion = Integer.parseInt(partes[4]);
                        LearningPath lp = new LearningPath(titulo, nivel, descripcion, objetivos, duracion, null, 0, new ArrayList<>());
                        learningPaths.put(titulo, lp);
                    } else {
                        System.out.println("Error en el formato de la línea: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar Learning Paths: " + e.getMessage());
        }
    }
    

    public void guardarDatos() {
        guardarEstudiantes();
        guardarProfesores();
        guardarLearningPaths();
    }

    private void guardarEstudiantes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("estudiantes.txt"))) {
            for (Map.Entry<String, Estudiante> entry : estudiantes.entrySet()) {
                Estudiante estudiante = entry.getValue();
                writer.write("ESTUDIANTE," + estudiante.getNombre() + "," + estudiante.getContrasenia() + "," + estudiante.getCorreo());
                if (estudiante.tieneLearningPathAsignado()) {
                    writer.write("\nLEARNING_PATH_ACTUAL:" + estudiante.getLearningPathActual().getTitulo());
                }
    
                // Guardar Learning Paths completados
                List<String> completados = estudiante.listarLearningPathsCompletados();
                writer.write("\nLEARNING_PATHS_COMPLETADOS:" + String.join(",", completados));
    
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }

    private void guardarProfesores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("profesores.txt"))) {
            for (Map.Entry<String, Profesor> entry : profesores.entrySet()) {
                Profesor profesor = entry.getValue();
                writer.write("PROFESOR," + profesor.getNombre() + "," + profesor.getContrasenia() + "," + profesor.getCorreo());
                writer.write("\nLEARNING_PATHS:");
                writer.write(String.join(",", profesor.getLearningPathCreado().stream()
                        .map(LearningPath::getTitulo)
                        .toList()));
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar profesores: " + e.getMessage());
        }
    }
    

    private void guardarLearningPaths() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("learningPaths.txt"))) {
            for (Map.Entry<String, LearningPath> entry : learningPaths.entrySet()) {
                LearningPath lp = entry.getValue();
                writer.write(lp.toString()); // Suponiendo que el método toString devuelve una representación adecuada
                writer.write("\n");
            }
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
        return learningPaths;
    }


    
}
