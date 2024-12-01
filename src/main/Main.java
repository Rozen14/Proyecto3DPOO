package main;

import actividad.*;
import consola.Autenticacion;
import consola.Consola;
import consola.ConsolaAdmin;
import consola.ConsolaEstudiante;
import consola.ConsolaProfesor;
import LPRS.LearningPath;
import pregunta.*;
import usuario.*;
import persistencia.*;
import plataforma.Plataforma;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;

public class Main {
 // El proposito de esta clase es simular el flujo de un estudiante en un Learning Path, completando actividades y evaluaciones para mostrar evidencia que la implementacion de las clases y metodos es correcta.
    public static void main(String[] args) {
        // Definir archivos de persistencia para profesores, estudiantes y learning paths
    
        Autenticacion autenticacion = new Autenticacion();
        Plataforma plataforma = Plataforma.getPlataforma();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bienvenido al sistema ===");
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        // Autenticación
        Usuario usuario = autenticacion.iniciarSesion(nombreUsuario, contrasenia);

        if (usuario == null) {
            System.out.println("Credenciales incorrectas. Saliendo...");
            scanner.close();
            return;
        }

        // Identificar el tipo de usuario y delegar a la consola correspondiente
        Consola consola;
        switch (usuario.getTipo()) {
        case "profesor":
            Profesor profesor = (Profesor) usuario;
            ConsolaProfesor consolaProfesor = new ConsolaProfesor(profesor, plataforma, scanner);
            consola = consolaProfesor;
            break;

        case "estudiante":
            Estudiante estudiante = (Estudiante) usuario;
            ConsolaEstudiante consolaEstudiante = new ConsolaEstudiante(estudiante, plataforma, scanner);
            consola = consolaEstudiante;
            break;

        case "admin":
            ConsolaAdmin consolaAdmin = new ConsolaAdmin(plataforma, scanner);
            consola = consolaAdmin;
            break;

        default:
            System.out.println("Tipo de usuario no reconocido.");
            consola = null;
            break;
        }

        consola.iniciar();

        // Interacción en bucle con la consola
        while (true) {
            System.out.print("Ingrese un comando: ");
            String comando = scanner.nextLine();
            if ((comando.equals("10") && (consola instanceof ConsolaProfesor)) || (comando.equals("8") && consola instanceof ConsolaEstudiante) || (comando.equals("7") && consola instanceof ConsolaAdmin)) { // Comando genérico de "Salir"
                consola.ejecutarComando(comando);
                break;
            }
            consola.ejecutarComando(comando);
        }

        scanner.close();
        System.out.println("Sesión finalizada.");
        }
    
        static {
            // Esta sección conserva el flujo anterior de datos iniciales para simulación
            // Datos iniciales y persistencia
            File archivoProfesores = new File("src/persistencia/archivo/profesores.txt");
            File archivoEstudiantes = new File("src/persistencia/archivo/estudiantes.txt");
            File archivoLearningPaths = new File("src/persistencia/archivo/learningPaths.txt");
    

        try {
            // Crear datos iniciales y guardarlos en persistencia
            System.out.println("=== Creando y guardando datos iniciales ===");

            // Crear un profesor
            Profesor profesor = new Profesor("Juan", "1234", "juan@example.com", new ArrayList<>(), new ArrayList<>());
            
            // Crear un LearningPath y agregarlo al profesor
            LearningPath learningPath = new LearningPath(
                "Learning Path de Prueba", 
                Nivel.Intermedio, 
                "Un camino de aprendizaje completo", 
                "Mejorar habilidades de programación", 
                120, 
                profesor, 
                4.5f, 
                new ArrayList<>()
            );
            profesor.getLearningPathCreado().add(learningPath);

            // Crear estudiantes y agregarlos al profesor
            Estudiante estudiante1 = new Estudiante("Maria", "1234", "maria@example.com");
            Estudiante estudiante2 = new Estudiante("Carlos", "5678", "carlos@example.com");
            profesor.agregarEstudiante(estudiante1);
            profesor.agregarEstudiante(estudiante2);

            // Inscribir estudiantes en el LearningPath
            learningPath.inscripcionEstudiante(estudiante1);
            learningPath.inscripcionEstudiante(estudiante2);

            // Crear el mapa de estados inicial para los estudiantes
            HashMap<Estudiante, Status> estadosTarea = new HashMap<>();
            estadosTarea.put(estudiante1, Status.Incompleto);
            estadosTarea.put(estudiante2, Status.Incompleto);

            // Crear y agregar una Tarea al LearningPath
            Tarea tarea = new Tarea(
                "Tarea de Ejemplo", 
                Nivel.Intermedio, 
                "Resolver problemas de lógica", 
                60, 
                1.0, 
                LocalDateTime.now().plusDays(7), 
                estadosTarea, 
                Obligatoria.SI, 
                "LMS", 
                profesor, 
                new ArrayList<>(), 
                new ArrayList<>()
            );
            learningPath.agregarActividad(tarea);

            // Crear y agregar un Quiz al LearningPath
            List<PreguntaCerrada> preguntasQuiz = new ArrayList<>();
            PreguntaCerrada preguntaQuiz = new PreguntaCerrada("¿Cuál es el resultado de 2+2?");

            Dictionary<Opcion, String> opcionA = new Hashtable<>();
            opcionA.put(Opcion.A, "3");
            preguntaQuiz.setOpcionA(opcionA);

            Dictionary<Opcion, String> opcionB = new Hashtable<>();
            opcionB.put(Opcion.B, "4"); // Respuesta correcta
            preguntaQuiz.setOpcionB(opcionB);

            Dictionary<Opcion, String> opcionC = new Hashtable<>();
            opcionC.put(Opcion.C, "5");
            preguntaQuiz.setOpcionC(opcionC);

            Dictionary<Opcion, String> opcionD = new Hashtable<>();
            opcionD.put(Opcion.D, "6");
            preguntaQuiz.setOpcionD(opcionD);

            preguntaQuiz.setRespuesta(opcionB); // Marca la opción B como la respuesta correcta
            preguntasQuiz.add(preguntaQuiz);

            HashMap<Estudiante, Status> estadosQuiz = new HashMap<>();
            estadosQuiz.put(estudiante1, Status.Incompleto);
            estadosQuiz.put(estudiante2, Status.Incompleto);

            Quiz quiz = new Quiz(
                "Quiz de Matemáticas Básicas", 
                Nivel.Principiante, 
                "Verificar habilidades básicas de matemáticas", 
                30, 
                1.0, 
                LocalDateTime.now().plusDays(5), 
                estadosQuiz, 
                Obligatoria.NO, 
                preguntasQuiz, 
                60.0, 
                profesor, 
                new ArrayList<>(), 
                new ArrayList<>()
            );
            learningPath.agregarActividad(quiz);

            HashMap<Estudiante, Status> estadosRecurso = new HashMap<>(); // Crear el mapa de estados inicial para los estudiantes
            estadosRecurso.put(estudiante1, Status.Incompleto); // Establecer el estado inicial de la actividad para cada estudiante
            estadosRecurso.put(estudiante2, Status.Incompleto); // Establecer el estado inicial de la actividad para cada estudiante

            RecursoEducativo recurso = new RecursoEducativo(
                "Video de Introducción a Java", 
                Nivel.Intermedio, 
                "Introducción a la programación en Java", 
                20, 
                1.0, 
                LocalDateTime.now().plusDays(10), 
                estadosRecurso, 
                Obligatoria.NO, 
                "video", 
                profesor, 
                new ArrayList<>(), 
                new ArrayList<>()
            );
            learningPath.agregarActividad(recurso);

            guardarDatos(archivoProfesores, archivoEstudiantes, archivoLearningPaths, profesor, estudiante1, estudiante2, learningPath); // Guardar los datos iniciales en persistencia

            // Simulación de respuestas por ambos estudiantes 
            System.out.println("\n== Simulación de Respuestas de Estudiantes =="); 

            // Estudiante 1 completa todas las actividades exitosamente
            tarea.responder(estudiante1, "LMS");
            learningPath.actividadObligatoriaCompletada(tarea, estudiante1);
            quiz.responder(estudiante1, "1:B"); // Respuesta correcta
            if (quiz.esExitosa(estudiante1)) learningPath.actividadObligatoriaCompletada(quiz, estudiante1);
            recurso.responder(estudiante1, "visto");
            if (recurso.esExitosa(estudiante1)) learningPath.actividadObligatoriaCompletada(recurso, estudiante1);
            profesor.evaluarTarea(tarea, estudiante1, learningPath, 80.0, true);

            float progreso1 = learningPath.calcularProgreso(estudiante1);
            if (progreso1 == 100.0f) {
                estudiante1.listaLearningPathsCompletados.add(learningPath);
                estudiante1.setLearningPathActual(null);
                System.out.println("El estudiante 1 ha completado el Learning Path.");
            }

            // Estudiante 2 completa parcialmente y falla en el quiz
            tarea.responder(estudiante2, "Correo");
            learningPath.actividadObligatoriaCompletada(tarea, estudiante2);
            quiz.responder(estudiante2, "1:A"); // Respuesta incorrecta
            recurso.responder(estudiante2, "visto");
            if (recurso.esExitosa(estudiante2)) learningPath.actividadObligatoriaCompletada(recurso, estudiante2);
            profesor.evaluarTarea(tarea, estudiante2, learningPath, 60.0, false);

            float progreso2 = learningPath.calcularProgreso(estudiante2);
            System.out.println("Progreso del Estudiante 1: " + progreso1 + "%");
            System.out.println("Progreso del Estudiante 2: " + progreso2 + "%");

            guardarDatos(archivoProfesores, archivoEstudiantes, archivoLearningPaths, profesor, estudiante1, estudiante2, learningPath); // Guardar los datos actualizados en persistencia

        } catch (Exception e) {
            System.out.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Prueba para ver si sirve el método de cargarLearningPaths

    // Método para sobrescribir datos de persistencia
    private static void guardarDatos(File archivoProfesores, File archivoEstudiantes, File archivoLearningPaths, Profesor profesor, Estudiante estudiante1, Estudiante estudiante2, LearningPath learningPath) throws IOException {
        PersistenciaProfesor.guardarProfesor(profesor, archivoProfesores); // Guardar el profesor en el archivo
        PersistenciaEstudiante.guardarEstudiante(estudiante1, archivoEstudiantes); // Guardar el estudiante 1 en el archivo
        PersistenciaEstudiante.guardarEstudiante(estudiante2, archivoEstudiantes); // Guardar el estudiante 2 en el archivo
        PersistenciaLearningPath.guardarLearningPath(learningPath); // Guardar el LearningPath en el archivo
    }
}
