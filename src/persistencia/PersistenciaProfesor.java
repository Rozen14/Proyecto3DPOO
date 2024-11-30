package persistencia;
import java.io.*;
import java.util.*;
import usuario.*;
import LPRS.LearningPath;

public class PersistenciaProfesor {

    // Método para guardar un profesor en el archivo especificado
    public static void guardarProfesor(Profesor profesor, File archivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            // Guardar datos básicos del profesor
            writer.write("PROFESOR," + profesor.getNombre() + "," + profesor.getContrasenia() + "," + profesor.getCorreo());
            writer.newLine();

            // Guardar los títulos de cada LearningPath creado por el profesor
            writer.write("LEARNING_PATHS:");
            writer.newLine();
            for (LearningPath learningPath : profesor.getLearningPathCreado()) {
                writer.write("LEARNING_PATH," + learningPath.getTitulo());  // Solo el título
                writer.newLine();
            }

            // Guardar lista de estudiantes asociados al profesor
            writer.write("ESTUDIANTES:");
            writer.newLine();
            for (Estudiante estudiante : profesor.getEstudiantes()) { // Solo el nombre y correo
                writer.write("ESTUDIANTE," + estudiante.getNombre() + "," + estudiante.getCorreo()); // Solo el nombre y correo
                writer.newLine(); // Salto de línea
            }

            writer.write("FIN_PROFESOR");
            writer.newLine();
        }
    }

    // Método para cargar los profesores desde el archivo especificado
    public static List<Profesor> cargarProfesores(File archivo) throws IOException {
        List<Profesor> profesores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) { // Crear un BufferedReader para leer el archivo
            String linea; // Línea actual
            Profesor profesorActual = null; // Profesor actual
            List<LearningPath> learningPaths = new ArrayList<>();  // Solo almacenaremos títulos
            List<Estudiante> estudiantes = new ArrayList<>();

            while ((linea = reader.readLine()) != null) { // Leer una línea del archivo
                if (linea.startsWith("FIN_PROFESOR")) { // Si la línea contiene la marca de fin de profesor
                    if (profesorActual != null) { // Si el profesor actual no es nulo
                        profesorActual.setLearningPathCreado(learningPaths);  // Asignar los títulos de los LearningPaths
                        profesorActual.setEstudiantes(estudiantes); // Asignar la lista de estudiantes
                        profesores.add(profesorActual); // Agregar el profesor actual a la lista de profesores
                    }
                    profesorActual = null; // Asignar nulo al profesor actual
                    learningPaths = new ArrayList<>(); // Crear una nueva lista de títulos
                    estudiantes = new ArrayList<>(); // Crear una nueva lista de estudiantes
                } else if (linea.startsWith("LEARNING_PATH,")) { // Si la línea contiene un LearningPath
                    // Extraer solo el título del LearningPath
                    String titulo = linea.split(",")[1]; 
                    LearningPath lp = new LearningPath(titulo, null, "", "", 0, profesorActual, 0, new ArrayList<>());  // Crear LearningPath con solo el título
                    learningPaths.add(lp); // Agregar el LearningPath a la lista
                } else if (linea.startsWith("ESTUDIANTE,")) {
                    String[] datosEstudiante = linea.split(","); // Separar los datos del estudiante
                    String nombre = datosEstudiante[1]; // Extraer el nombre
                    String correo = datosEstudiante[2]; // Extraer el correo
                    Estudiante estudiante = new Estudiante(nombre, "", correo);  // Constructor de ejemplo
                    estudiantes.add(estudiante); // Agregar el estudiante a la lista
                } else if (linea.startsWith("PROFESOR,")) {
                    String[] datos = linea.split(","); // Separar los datos del profesor
                    String nombre = datos[1]; // Extraer el nombre
                    String contrasena = datos[2]; // Extraer la contraseña
                    String correo = datos[3]; // Extraer el correo
                    profesorActual = new Profesor(nombre, contrasena, correo, new ArrayList<>(), new ArrayList<>()); // Constructor de ejemplo
                }
            }
        }

        return profesores; // Retornar la lista de profesores
    }
}
