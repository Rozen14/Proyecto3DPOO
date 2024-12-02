package persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import usuario.Profesor;
import LPRS.LearningPath;

public class PersistenciaLearningPath // Esta clase se encarga de la persistencia de los LearningPaths, guardándolos en un archivo de texto, aqui tambien se hace el manejo de las actividades y preguntas ya que estas dependen de los LearningPaths.

{
    
    private static final String RUTA_ARCHIVO = "src/persistencia/archivo/learningPaths.txt"; // Path del archivo de texto donde se guardan los LearningPaths con sus actividades y preguntas
    
    // Método para guardar un LearningPath en el archivo
    public static void guardarLearningPath(LearningPath learningPath) throws IOException {
        File archivo = new File(RUTA_ARCHIVO);
        archivo.getParentFile().mkdirs(); // Crear directorio "data" si no existe

        // Llamar directamente a `guardarEnArchivo` en LearningPath, pasándole el archivo
        learningPath.guardarEnArchivo();
    }

    // Método para cargar todos los LearningPaths del archivo
    public static List<LearningPath> cargarLearningPaths(Profesor creador) throws IOException {
        File archivo = new File(RUTA_ARCHIVO); // Se crea un archivo con la ruta especificada
        List<LearningPath> learningPaths = new ArrayList<>(); // Lista de LearningPaths a retornar

        if (!archivo.exists()) { // Si el archivo no existe
            System.out.println("El archivo de learning paths no existe."); // Se imprime un mensaje
            return learningPaths; // Se retorna la lista vacía
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) { // Se crea un BufferedReader para leer el archivo
            LearningPath learningPath = LearningPath.cargarDeArchivo(archivo, creador); // Se carga el primer LearningPath del archivo
            if (learningPath != null) { // Si el LearningPath no es nulo
                learningPaths.add(learningPath); // Se agrega el LearningPath a la lista
            }
        }

        return learningPaths; // Se retorna la lista de LearningPaths
    }

    // Método para borrar el archivo (por ejemplo, para reiniciar la persistencia)
    public static void borrarArchivo() throws IOException {
        File archivo = new File(RUTA_ARCHIVO); // Se crea un archivo con la ruta especificada
        if (archivo.exists()) { // Si el archivo existe
            archivo.delete(); // Se elimina el archivo
        }
    }

}
