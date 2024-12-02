package persistencia;

import java.io.*;
import java.util.*;
import LPRS.LearningPath;
import usuario.*;

public class PersistenciaEstudiante {

    // Método para guardar un estudiante en el archivo especificado
    public static void guardarEstudiante(Estudiante estudiante, File archivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) { // Se crea un BufferedWriter para escribir en el archivo
            // Guardar datos básicos del estudiante
            writer.write("ESTUDIANTE," + estudiante.getNombre() + "," + estudiante.getContrasenia() + "," + estudiante.getCorreo()); // Se escribe en el archivo el nombre, contraseña y correo del estudiante
            writer.newLine(); // Se escribe un salto de línea

            // Guardar el título del LearningPath actual si está siguiendo uno
            if (estudiante.getLearningPathActual() != null) {
                writer.write("LEARNING_PATH_ACTUAL:" + estudiante.getLearningPathActual().getTitulo()); // Se escribe en el archivo el título del LearningPath actual
                writer.newLine();
            }

            // Guardar títulos de LearningPaths completados
            writer.write("LEARNING_PATHS_COMPLETADOS:");
            for (LearningPath learningPath : estudiante.listaLearningPathsCompletados) { // Se recorre la lista de LearningPaths completados por el estudiante
                writer.write(learningPath.getTitulo() + ";"); // Se escribe en el archivo el título del LearningPath completado
            }
            writer.newLine(); // Se escribe un salto de línea

            writer.write("FIN_ESTUDIANTE"); // Se escribe en el archivo la marca de fin de estudiante
            writer.newLine(); // Se escribe un salto de línea
        }
    }

    public static List<Estudiante> cargarEstudiantes(File archivo, Map<String, LearningPath> learningPathsDisponibles) throws IOException {
        List<Estudiante> estudiantes = new ArrayList<>(); // Se crea una lista de estudiantes
    
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) { // Se crea un BufferedReader para leer el archivo
            String linea;
            Estudiante estudianteActual = null; 
            List<LearningPath> learningPathsCompletados = new ArrayList<>(); 
    
            while ((linea = reader.readLine()) != null) { // Se lee una línea del archivo
                if (linea.startsWith("FIN_ESTUDIANTE")) { // Si la línea contiene la marca de fin de estudiante
                    if (estudianteActual != null) { // Si el estudiante actual no es nulo
                        estudianteActual.listaLearningPathsCompletados = learningPathsCompletados; // Se asigna la lista de LearningPaths completados al estudiante actual
                        estudiantes.add(estudianteActual); // Se agrega el estudiante actual a la lista de estudiantes
                    }
                    estudianteActual = null; // Se asigna nulo al estudiante actual
                    learningPathsCompletados = new ArrayList<>(); // Se crea una nueva lista de LearningPaths completados
                } else if (linea.startsWith("LEARNING_PATH_ACTUAL:")) { // Si la línea contiene el título del LearningPath actual
                    if (estudianteActual != null) { // Si el estudiante actual no es nulo
                        String[] partes = linea.split(":");
                        if (partes.length > 1) { // Validar que haya contenido después de los dos puntos
                            String tituloLP = partes[1]; // Se obtiene el título del LearningPath actual
                            LearningPath lpActual = learningPathsDisponibles.get(tituloLP); // Se obtiene el LearningPath actual
                            if (lpActual != null) { // Si el LearningPath actual no es nulo
                                estudianteActual.setLearningPathActual(lpActual); // Se asigna el LearningPath actual al estudiante actual
                            }
                        }
                    }
                } else if (linea.startsWith("LEARNING_PATHS_COMPLETADOS:")) { // Si la línea contiene los títulos de LearningPaths completados
                    if (estudianteActual != null) { // Si el estudiante actual no es nulo
                        String[] partes = linea.split(":");
                        if (partes.length > 1 && !partes[1].isEmpty()) { // Validar que haya contenido después de los dos puntos y no esté vacío
                            String[] titulosCompletados = partes[1].split(";"); // Se obtienen los títulos de LearningPaths completados
                            for (String titulo : titulosCompletados) { // Se recorren los títulos de LearningPaths completados
                                LearningPath lpCompletado = learningPathsDisponibles.get(titulo); // Se obtiene el LearningPath completado
                                if (lpCompletado != null) { // Si el LearningPath completado no es nulo
                                    learningPathsCompletados.add(lpCompletado); // Se agrega el LearningPath completado a la lista de LearningPaths completados
                                }
                            }
                        }
                    }
                } else if (linea.startsWith("ESTUDIANTE,")) { // Si la línea contiene los datos de un estudiante
                    String[] datos = linea.split(","); // Se obtienen los datos del estudiante
                    if (datos.length >= 4) { // Validar que la línea tenga los datos necesarios
                        String nombre = datos[1]; // Se obtiene el nombre del estudiante 
                        String contrasenia = datos[2]; // Se obtiene la contraseña del estudiante
                        String correo = datos[3]; // Se obtiene el correo del estudiante
                        estudianteActual = new Estudiante(nombre, contrasenia, correo); // Se crea un nuevo estudiante con los datos obtenidos
                    } else {
                        System.out.println("Línea mal formateada: " + linea); // Mensaje de advertencia para líneas inválidas
                    }
                }
            }
        }
    
        return estudiantes; // Se retorna la lista de estudiantes
    }
}
    
