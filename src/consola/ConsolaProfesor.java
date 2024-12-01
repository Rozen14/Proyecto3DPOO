package consola;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import LPRS.LearningPath;
import actividad.Nivel;
import usuario.Estudiante;
import usuario.Profesor;

public class ConsolaProfesor implements Consola{
    private Profesor profesor;
    private Scanner scanner;

    public ConsolaProfesor(Profesor profesor, Scanner scanner) {
        this.profesor = profesor;
        this.scanner = scanner;
    }

    public ConsolaProfesor() {
        this.scanner = new Scanner(System.in); // Inicializa el scanner para evitar NullPointerException
    }
    
    @Override
    public void iniciar() {
        System.out.println("Consola del profesor iniciada");
        mostrarMenu();
    }

    @Override
    public void mostrarMenu() {
        System.out.println("1. Crear actividad");
        System.out.println("2. Crear learning path");
        System.out.println("3. Ver estudiantes inscritos en Learning Path");
        System.out.println("4. Ver progreso de estudiantes en Learning Path");
        System.out.println("5. Eliminar Learning Path");
        System.out.println("6. Verificar si Estudiante ha completado Learning Path");
        System.out.println("7. Evaluar actividad");
        System.out.println("8. Salir");
    }

    public LearningPath solicitarLearningPath(Profesor profesor, Scanner scanner) {
        // Verificar si el profesor tiene Learning Paths creados
        if (profesor.getLearningPathCreado().isEmpty()) {
            System.out.println("No tienes Learning Paths creados.");
            return null;
        }
    
        // Listar los Learning Paths creados
        System.out.println("Learning Paths creados:");
        List<LearningPath> learningPaths = profesor.getLearningPathCreado();
        for (int i = 0; i < learningPaths.size(); i++) {
            System.out.println((i + 1) + ". " + learningPaths.get(i).getTitulo());
        }
    
        // Solicitar al usuario seleccionar un Learning Path
        System.out.print("Selecciona el número del Learning Path: ");
        int opcionSeleccionada;
        try {
            opcionSeleccionada = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingresa un número.");
            return null;
        }
    
        // Validar opción ingresada
        if (opcionSeleccionada < 1 || opcionSeleccionada > learningPaths.size()) {
            System.out.println("Opción inválida. Selecciona un número válido.");
            return null;
        }
    
        // Retornar el Learning Path seleccionado
        return learningPaths.get(opcionSeleccionada - 1);
    }

    private Estudiante solicitarEstudiante(Profesor profesor, Scanner scanner) {
        // Obtener la lista de estudiantes asociados al profesor
        List<Estudiante> estudiantes = profesor.getEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes asociados a este profesor.");
            return null; // No hay estudiantes disponibles
        }
    
        // Mostrar la lista de estudiantes
        System.out.println("Estudiantes asociados:");
        for (int i = 0; i < estudiantes.size(); i++) {
            System.out.println((i + 1) + ". " + estudiantes.get(i).getNombre() + " (" + estudiantes.get(i).getCorreo() + ")");
        }
    
        // Solicitar al usuario que seleccione uno
        System.out.print("Selecciona el número del estudiante (o escribe 0 para cancelar): ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
    
            // Validar la opción ingresada
            if (opcion == 0) {
                System.out.println("Operación cancelada.");
                return null; // Usuario decidió cancelar
            } else if (opcion < 1 || opcion > estudiantes.size()) {
                System.out.println("Opción inválida.");
                return null; // Opción fuera de rango
            }
    
            // Retornar el estudiante seleccionado
            return estudiantes.get(opcion - 1);
    
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida. Debes ingresar un número.");
            return null; // Manejo de entrada no numérica
        }
    }

    @Override
    public void ejecutarComando(String comando) {
        switch(comando){
            case "1":
                System.out.println("Creando una nueva actividad...");
                // Solicitar detalles de la actividad
                System.out.print("Ingrese el título del Learning Path: ");
                String tituloLP = scanner.nextLine();
                LearningPath lpActividad = profesor.getLearningPathCreado().stream()
                        .filter(lp -> lp.getTitulo().equals(tituloLP))
                        .findFirst()
                        .orElse(null);

                if (lpActividad == null) {
                    System.out.println("Learning Path no encontrado.");
                    break;
                }

                System.out.print("Ingrese el tipo de actividad (TAREA/QUIZ/EXAMEN/ENCUESTA/RECURSO EDUCATIVO): ");
                String tipo = scanner.nextLine();
                System.out.print("Ingrese la descripcion de la actividad: ");
                String descripcion = scanner.nextLine();
                System.out.print("Ingrese la dificultad de la actividad: ");
                Nivel nivel = Nivel.valueOf(scanner.nextLine().toUpperCase());
                System.out.print("Ingrese el objetivo de la actividad: ");
                String objetivo = scanner.nextLine();
                System.out.print("Ingrese la duracion esperada de la actividad: ");
                int duracion = Integer.parseInt(scanner.nextLine());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                System.out.print("Ingrese la fecha límite en formato (dd-MM-yyyy HH:mm:ss): ");
                String fechaLimiteInput = scanner.nextLine();
                LocalDateTime fechaLimite = LocalDateTime.parse(fechaLimiteInput, formatter);
                //TODO terminar
                profesor.crearActividad(lpActividad, descripcion, nivel, objetivo, duracion, 0, fechaLimite, null, null, tipo, null, null, null, null, comando, 0, tipo, null);
                
                System.out.println("Actividad creada exitosamente.");
                break;
            case "2":
                System.out.println("Creando learning path...");
                break;
            case "3":
                System.out.println("Viendo estudiantes inscritos en Learning Path...");
                LearningPath learningPathSeleccionado = solicitarLearningPath(profesor, scanner);
                
                List<Estudiante> estudiantesInscritos = learningPathSeleccionado.getEstudiantesInscritos();
                if (estudiantesInscritos.isEmpty()) {
                    System.out.println("No hay estudiantes inscritos en este Learning Path.");
                } else {
                    System.out.println("Estudiantes inscritos en " + learningPathSeleccionado.getTitulo() + ":");
                for (Estudiante estudiante : estudiantesInscritos) {
                    System.out.println("- " + estudiante.getNombre() + " (" + estudiante.getCorreo() + ")");
                }
                }
                break;
            case "4":
                System.out.println("Viendo progreso de estudiantes en Learning Path...");
                LearningPath learningPathSeleccionadoProgreso = solicitarLearningPath(profesor, scanner);
                
                Map<String, Float> progresoEstudiantes = profesor.obtenerProgresoEstudiantes(learningPathSeleccionadoProgreso);
                if (progresoEstudiantes.isEmpty()) {
                    System.out.println("No hay estudiantes inscritos en este Learning Path.");
                } else {
                    System.out.println("Progreso de los estudiantes en " + learningPathSeleccionadoProgreso.getTitulo() + ":");
                    for (Map.Entry<String, Float> entry : progresoEstudiantes.entrySet()) {
                        System.out.println("- " + entry.getKey() + ": " + entry.getValue() + "%");
                    }
                }
                break;
            case "5":
                System.out.println("Eliminando Learning Path...");
                LearningPath learningPathSeleccionadoEliminar = solicitarLearningPath(profesor, scanner);

                // Confirmar la eliminación
                System.out.print("¿Estás seguro de que deseas eliminar el Learning Path '" 
                     + learningPathSeleccionadoEliminar.getTitulo() + "'? (si/no): ");
                String confirmacion = scanner.nextLine().toLowerCase();
                if (confirmacion.equals("si")) {
                    // Eliminar el Learning Path
                    profesor.eliminarLearningPath(learningPathSeleccionadoEliminar);
                    System.out.println("Learning Path eliminado exitosamente.");
                } else {
                    System.out.println("Eliminación cancelada.");
                }
                break;
            case "6":
                System.out.println("Verificando si Estudiante ha completado Learning Path...");
                LearningPath learningPathSeleccionadoVerificar = solicitarLearningPath(profesor, scanner);
                Estudiante estudianteVerificar = solicitarEstudiante(profesor, scanner);
                boolean completo = profesor.verificarEstudianteCompletoLearningPath(learningPathSeleccionadoVerificar, estudianteVerificar);
                if (completo) {
                    System.out.println("El estudiante " + estudianteVerificar.getNombre() + " ha completado el Learning Path " + learningPathSeleccionadoVerificar.getTitulo() + ".");
                } else {
                    System.out.println("El estudiante " + estudianteVerificar.getNombre() + " no ha completado el Learning Path " + learningPathSeleccionadoVerificar.getTitulo() + ".");
                }
                break;
            case "7":
                System.out.println("Evaluando actividad...");
                break;
            case "8":
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }

}
