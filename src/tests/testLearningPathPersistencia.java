package tests;

import actividad.*;
import LPRS.LearningPath;
import pregunta.*;
import usuario.*;
import persistencia.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;



public class testLearningPathPersistencia {
    public static void main(String[] args) throws IOException {
        final String RUTA_ARCHIVO = "src/persistencia/archivo/learningPaths.txt"; // Path del archivo de texto donde se guardan los LearningPaths con sus actividades y preguntas
        File archivo = new File(RUTA_ARCHIVO);
        Profesor creador = new Profesor("Juan", "juan@example.com", "12345", new ArrayList<>(), new ArrayList<>());

        LearningPath lp = LearningPath.cargarDeArchivo(archivo, creador);

        

        if (lp != null) {
            System.out.println("LearningPath cargado correctamente.");
            System.out.println("Título: " + lp.getTitulo());
            System.out.println("Descripción: " + lp.getDescripcion());
            System.out.println("Número de actividades: " + lp.getListaActividades());
            // Nombres de los estudiantes inscritos
            System.out.println("Estudiantes inscritos:");
            for (Estudiante estudiante : lp.getEstudiantesInscritos()) {
                System.out.println(estudiante.getNombre());
            }
            // Estados de learning path para cada estudiante
            System.out.println("Estados de LearningPath para cada estudiante:");
            for (Estudiante estudiante : lp.getEstudiantesInscritos()) {
                System.out.println("Estudiante: " + estudiante.getNombre());
                System.out.println("Estado: " + lp.getStatusParaEstudiante(estudiante));
                System.out.println("Verificar si inscrito: " + lp.verificarSiInscrito(estudiante));
            }


        } else {
            System.out.println("Error al cargar el LearningPath.");
        }
    }
}
