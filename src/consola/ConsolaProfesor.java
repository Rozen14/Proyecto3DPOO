package consola;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import LPRS.LearningPath;
import actividad.Actividad;
import actividad.Encuesta;
import actividad.Examen;
import actividad.Nivel;
import actividad.Obligatoria;
import actividad.Quiz;
import actividad.Status;
import actividad.Tarea;
import pregunta.Opcion;
import pregunta.Pregunta;
import pregunta.PreguntaAbierta;
import pregunta.PreguntaCerrada;
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
        System.out.println("8. Modificar Actividad");
        System.out.println("9. Modificar Learning Path");
        System.out.println("10. Salir");
    }

    private LearningPath solicitarLearningPath(Profesor profesor, Scanner scanner) {
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

    private double solicitarCalificacionMinima(Scanner scanner) {
        System.out.print("Ingrese la calificación mínima para aprobar la actividad: ");
        double calificacionMinima = Double.parseDouble(scanner.nextLine());
        return calificacionMinima;
    }

    private PreguntaCerrada crearPreguntaCerradaInteractiva(Profesor profesor, Scanner scanner) {
        System.out.print("Ingrese el enunciado de la pregunta cerrada: ");
        String enunciado = scanner.nextLine();
    
        PreguntaCerrada pregunta = profesor.crearPreguntaCerrada(enunciado);

        Dictionary<Opcion, String> opcionA = new Hashtable<>();
        System.out.print("Ingrese la opción A: ");
        opcionA.put(Opcion.A, scanner.nextLine());
        profesor.agregarOpcionA(opcionA, pregunta);

        Dictionary<Opcion, String> opcionB = new Hashtable<>();
        System.out.print("Ingrese la opción B: ");
        opcionB.put(Opcion.B, scanner.nextLine());
        profesor.agregarOpcionB(opcionB, pregunta);

        Dictionary<Opcion, String> opcionC = new Hashtable<>();
        System.out.print("Ingrese la opción C: ");
        opcionC.put(Opcion.C, scanner.nextLine());
        profesor.agregarOpcionC(opcionC, pregunta);

        Dictionary<Opcion, String> opcionD = new Hashtable<>();
        System.out.print("Ingrese la opción D: ");
        opcionD.put(Opcion.D, scanner.nextLine());
        profesor.agregarOpcionD(opcionD, pregunta);

        // Elegir la respuesta correcta
        System.out.print("Seleccione la opción correcta (A, B, C, D): ");
        String opcionCorrecta = scanner.nextLine().toUpperCase();
        Dictionary<Opcion, String> respuestaCorrecta = switch (opcionCorrecta) {
            case "A" -> opcionA;
            case "B" -> opcionB;
            case "C" -> opcionC;
            case "D" -> opcionD;
            default -> {
                System.out.println("Opción incorrecta. No se establece una respuesta correcta.");
                yield null;
            }
        };
        if (respuestaCorrecta != null) {
            profesor.agregarRespuesta(respuestaCorrecta, pregunta);
        }

        System.out.println("Pregunta cerrada creada exitosamente.");
        return pregunta;
    }

    private PreguntaAbierta crearPreguntaAbiertaInteractiva(Profesor profesor, Scanner scanner) {
        System.out.print("Ingrese el enunciado de la pregunta abierta: ");
        String enunciado = scanner.nextLine();
    
        // Crear la pregunta abierta
        PreguntaAbierta preguntaAbierta = new PreguntaAbierta(enunciado);
        System.out.println("Pregunta abierta creada con enunciado: " + preguntaAbierta.getEnunciado());
        return preguntaAbierta;
    }
    
    private Actividad crearActividadInteractiva(Profesor profesor, Scanner scanner) {
        
        try {
            System.out.println("Creando una nueva actividad...");
    
            // Solicitar el Learning Path
            LearningPath lpActividad = solicitarLearningPath(profesor, scanner);
    
            // Solicitar detalles básicos de la actividad
            System.out.print("Ingrese el tipo de actividad (TAREA/QUIZ/EXAMEN/ENCUESTA/RECURSO EDUCATIVO): ");
            String tipo = scanner.nextLine().toUpperCase();
            System.out.print("Ingrese la descripción de la actividad: ");
            String descripcion = scanner.nextLine();
            System.out.print("Ingrese la dificultad de la actividad (PRINCIPIANTE, INTERMEDIO, AVANZADO): ");
            Nivel nivel = Nivel.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Ingrese el objetivo de la actividad: ");
            String objetivo = scanner.nextLine();
            System.out.print("Ingrese la duración esperada de la actividad en minutos: ");
            int duracion = Integer.parseInt(scanner.nextLine());
    
            // Fecha límite
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            System.out.print("Ingrese la fecha límite en formato (dd-MM-yyyy HH:mm:ss): ");
            String fechaLimiteInput = scanner.nextLine();
            LocalDateTime fechaLimite = LocalDateTime.parse(fechaLimiteInput, formatter);
    
            // Obligatoria
            System.out.print("¿La actividad es obligatoria? (si/no): ");
            Obligatoria obligatoria = scanner.nextLine().equalsIgnoreCase("si") ? Obligatoria.SI : Obligatoria.NO;
    
            // Actividades previas sugeridas
            List<Actividad> actividadesPrevias = new ArrayList<>();
            System.out.print("¿Desea agregar actividades previas sugeridas? (si/no): ");
            if (scanner.nextLine().equalsIgnoreCase("si")) {
                System.out.print("Ingrese la cantidad de actividades previas: ");
                int cantidadPrevias = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < cantidadPrevias; i++) {
                    System.out.print("Ingrese la descripción de la actividad previa sugerida: ");
                    String descPrevia = scanner.nextLine();
                    Actividad actividadPrevia = lpActividad.getListaActividades().stream()
                            .filter(a -> a.getDescripcion().equals(descPrevia))
                            .findFirst()
                            .orElse(null);
                    if (actividadPrevia != null) {
                        actividadesPrevias.add(actividadPrevia);
                    } else {
                        System.out.println("Actividad previa no encontrada.");
                    }
                }
            }
    
            // Actividades de seguimiento
            List<Actividad> actividadesSeguimiento = new ArrayList<>();
            System.out.print("¿Desea agregar actividades de seguimiento recomendadas? (si/no): ");
            if (scanner.nextLine().equalsIgnoreCase("si")) {
                System.out.print("Ingrese la cantidad de actividades de seguimiento: ");
                int cantidadSeguimiento = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < cantidadSeguimiento; i++) {
                    System.out.print("Ingrese la descripción de la actividad de seguimiento: ");
                    String descSeguimiento = scanner.nextLine();
                    Actividad actividadSeguimiento = lpActividad.getListaActividades().stream()
                            .filter(a -> a.getDescripcion().equals(descSeguimiento))
                            .findFirst()
                            .orElse(null);
                    if (actividadSeguimiento != null) {
                        actividadesSeguimiento.add(actividadSeguimiento);
                    } else {
                        System.out.println("Actividad de seguimiento no encontrada.");
                    }
                }
            }
    
            // Dependiendo del tipo de actividad, completar detalles específicos
            String tipoRecurso = null;
            List<PreguntaCerrada> listaPreguntasCerradas = new ArrayList<>();
            ArrayList<PreguntaAbierta> preguntasEncuesta = new ArrayList<>();
            List<Pregunta> listaPreguntasExamen = new ArrayList<>();
            String submissionMethod = null;
            double calificacionMinima = 0;
    
            switch (tipo) {
                case "ENCUESTA" -> {
                    System.out.print("Ingrese la cantidad de preguntas abiertas para la encuesta: ");
                    int cantidadPreguntas = Integer.parseInt(scanner.nextLine());
                    for (int i = 0; i < cantidadPreguntas; i++) {
                        System.out.println("Creando pregunta abierta " + (i + 1) + ":");
                        PreguntaAbierta preguntaAbierta = crearPreguntaAbiertaInteractiva(profesor, scanner);
                        preguntasEncuesta.add(preguntaAbierta);
                    }
                }
                case "QUIZ" -> {
                    System.out.print("Ingrese la cantidad de preguntas cerradas para el quiz: ");
                    int cantidadPreguntas = Integer.parseInt(scanner.nextLine());
                    for (int i = 0; i < cantidadPreguntas; i++) {
                        System.out.println("Creando pregunta cerrada " + (i + 1) + ":");
                        PreguntaCerrada preguntaCerrada = crearPreguntaCerradaInteractiva(profesor, scanner);
                        listaPreguntasCerradas.add(preguntaCerrada);
                    }
                    calificacionMinima = solicitarCalificacionMinima(scanner);
                }
                case "EXAMEN" -> {
                    System.out.print("Ingrese la cantidad de preguntas cerradas para el examen: ");
                    int cantidadPreguntas = Integer.parseInt(scanner.nextLine());
                    for (int i = 0; i < cantidadPreguntas; i++) {
                        PreguntaCerrada preguntaCerrada = crearPreguntaCerradaInteractiva(profesor, scanner);
                        listaPreguntasExamen.add(preguntaCerrada);
                    }
                    System.out.print("Ingrese la cantidad de preguntas abiertas para el examen: ");
                    cantidadPreguntas = Integer.parseInt(scanner.nextLine());
                    for (int i = 0; i < cantidadPreguntas; i++) {
                        PreguntaAbierta preguntaAbierta = crearPreguntaAbiertaInteractiva(profesor, scanner);
                        listaPreguntasExamen.add(preguntaAbierta);
                    }
                    calificacionMinima = solicitarCalificacionMinima(scanner);
                }
                case "TAREA" -> {
                    System.out.print("Ingrese el método de entrega (submission method): ");
                    submissionMethod = scanner.nextLine();
                }
                case "RECURSO EDUCATIVO" -> {
                    System.out.print("Ingrese el tipo de recurso educativo: ");
                    tipoRecurso = scanner.nextLine();
                }
                default -> {
                    System.out.println("Tipo de actividad no reconocido.");
                }

            }
    
            // Crear estados iniciales vacíos
            Map<Estudiante, Status> estadosPorEstudiante = new Hashtable<>();
    
            // Crear la actividad
            return profesor.crearActividad(lpActividad, descripcion, nivel, objetivo, duracion, 0, fechaLimite,
                    estadosPorEstudiante, obligatoria, tipo, actividadesPrevias, actividadesSeguimiento,
                    preguntasEncuesta, listaPreguntasCerradas, submissionMethod, calificacionMinima, tipoRecurso, listaPreguntasExamen);
    
        } catch (Exception e) {
            System.out.println("Error al crear la actividad: " + e.getMessage());
            return null;
        }

    }
    

    @Override
    public void ejecutarComando(String comando) {
        switch(comando){
            case "1":
                crearActividadInteractiva(profesor, scanner);
                System.out.println("Actividad creada exitosamente.");
                break;
            case "2":
                System.out.println("Creando learning path...");
                System.out.print("Ingrese el título del Learning Path: ");
                String titulo = scanner.nextLine();
                System.out.print("Ingrese la descripción del Learning Path: ");
                String descripcionLP = scanner.nextLine();
                System.out.print("Ingrese la duración estimada del Learning Path: ");
                int duracionLP = Integer.parseInt(scanner.nextLine());
                System.out.print("Ingrese el nivel del Learning Path (Principiante/Intermedio/Avanzado): ");
                Nivel nivelLP = Nivel.valueOf(scanner.nextLine());
                System.out.print("Ingrese el objetivo del Learning Path: ");
                String objetivoLP = scanner.nextLine();
                System.out.print("Ingrese la cantidad de actividades que desea agregar al Learning Path: ");
                int cantidadActividades = Integer.parseInt(scanner.nextLine());
                List<Actividad> listaActividades = new ArrayList<>();
                for (int i = 0; i < cantidadActividades; i++) {
                    System.out.println("\nCreando actividad " + (i + 1) + ":");
                    Actividad actividad = crearActividadInteractiva(profesor, scanner); // Llama a la función creada anteriormente
                    listaActividades.add(actividad);
                }
                profesor.crearLearningPath(titulo, nivelLP, descripcionLP, objetivoLP, duracionLP, 0, listaActividades);
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
                System.out.print("Ingrese el correo del estudiante: ");
                String correoEstudiante = scanner.nextLine();
                Estudiante estudiante = profesor.getEstudiantes().stream()
                        .filter(e -> e.getCorreo().equals(correoEstudiante))
                        .findFirst()
                        .orElse(null);
                if (estudiante == null) {
                    System.out.println("Estudiante no encontrado.");
                    break;
                }
                System.out.print("Ingrese el título del Learning Path: ");
                LearningPath learningPathSeleccionadoEvaluar = solicitarLearningPath(profesor, scanner);
                System.out.print("Ingrese la descripcion de la actividad: ");
                String descripcionActividad = scanner.nextLine();
                Actividad actividad = learningPathSeleccionadoEvaluar.getListaActividades().stream()
                        .filter(a -> a.getDescripcion().equals(descripcionActividad))
                        .findFirst()
                        .orElse(null);
                if (actividad == null) {
                    System.out.println("Actividad no encontrada.");
                    break;
                }
                if (actividad instanceof Tarea) {
                    Tarea tarea = (Tarea) actividad;
                    System.out.print("Ingrese la calificación obtenida: ");
                    double calificacion = Double.parseDouble(scanner.nextLine());
                    System.out.print("¿La tarea fue exitosa? (si/no): ");
                    boolean exitosa = scanner.nextLine().equalsIgnoreCase("si");
                    profesor.evaluarTarea(tarea, estudiante, learningPathSeleccionadoEvaluar, calificacion, exitosa);
                } else if (actividad instanceof Examen) {
                    Examen examen = (Examen) actividad;
                    System.out.print("Ingrese la calificación obtenida: ");
                    double calificacion = Double.parseDouble(scanner.nextLine());
                    System.out.print("¿El examen fue exitoso? (si/no): ");
                    boolean exitosa = scanner.nextLine().equalsIgnoreCase("si");
                    profesor.evaluarExamen(examen, estudiante, learningPathSeleccionadoEvaluar, calificacion, exitosa);
                } else if (actividad instanceof Quiz) {
                    Quiz quiz = (Quiz) actividad;
                    profesor.revisarQuiz(quiz, estudiante);
                } else if (actividad instanceof Encuesta) {
                    Encuesta encuesta = (Encuesta) actividad;
                    profesor.revisarEncuesta(encuesta, estudiante);
                } else {
                    System.out.println("Tipo de actividad no reconocido.");
                }
                break;
                case "8":
                System.out.println("Modificando actividad...");
                try {
                    // Solicitar el Learning Path que contiene la actividad
                    LearningPath lpModificarActividad = solicitarLearningPath(profesor, scanner);
            
                    // Seleccionar la actividad a modificar
                    System.out.print("Ingrese la descripción de la actividad que desea modificar: ");
                    String descripcionActividadMod = scanner.nextLine();
                    Actividad actividadModificar = lpModificarActividad.getListaActividades().stream()
                            .filter(a -> a.getDescripcion().equals(descripcionActividadMod))
                            .findFirst()
                            .orElse(null);
            
                    if (actividadModificar == null) {
                        System.out.println("Actividad no encontrada.");
                        break;
                    }
            
                    // Modificar los detalles de la actividad
                    System.out.println("Detalles actuales de la actividad:");
                    System.out.println("Descripción: " + actividadModificar.getDescripcion());
                    System.out.println("Objetivo: " + actividadModificar.getObjetivo());
                    System.out.println("Duración esperada: " + actividadModificar.getDuracionEsperada() + " minutos");
            
                    System.out.print("Ingrese la nueva descripción (deje en blanco para no cambiar): ");
                    String nuevaDescripcion = scanner.nextLine();
                    if (!nuevaDescripcion.isEmpty()) {
                        actividadModificar.setDescripcion(nuevaDescripcion, lpModificarActividad);
                    }
            
                    System.out.print("Ingrese el nuevo objetivo (deje en blanco para no cambiar): ");
                    String nuevoObjetivo = scanner.nextLine();
                    if (!nuevoObjetivo.isEmpty()) {
                        actividadModificar.setObjetivo(nuevoObjetivo, lpModificarActividad);
                    }
            
                    System.out.print("Ingrese la nueva duración esperada (deje en blanco para no cambiar): ");
                    String nuevaDuracionInput = scanner.nextLine();
                    if (!nuevaDuracionInput.isEmpty()) {
                        int nuevaDuracion = Integer.parseInt(nuevaDuracionInput);
                        actividadModificar.setDuracionEsperada(nuevaDuracion, lpModificarActividad);
                    }
            
                    System.out.println("Actividad modificada exitosamente.");
                } catch (Exception e) {
                    System.out.println("Error al modificar la actividad: " + e.getMessage());
                }
                break;
            
                case "9":
                System.out.println("Modificando Learning Path...");
                try {
                    // Solicitar el Learning Path a modificar
                    LearningPath lpModificar = solicitarLearningPath(profesor, scanner);
            
                    // Mostrar detalles actuales
                    System.out.println("Detalles actuales del Learning Path:");
                    System.out.println("Título: " + lpModificar.getTitulo());
                    System.out.println("Descripción: " + lpModificar.getDescripcion());
                    System.out.println("Objetivo: " + lpModificar.getObjetivos());
                    System.out.println("Duración estimada: " + lpModificar.getDuracionMinutos() + " minutos");
            
                    // Modificar los detalles
                    System.out.print("Ingrese el nuevo título (deje en blanco para no cambiar): ");
                    String nuevoTitulo = scanner.nextLine();
                    if (!nuevoTitulo.isEmpty()) {
                        lpModificar.setTitulo(nuevoTitulo);
                    }
            
                    System.out.print("Ingrese la nueva descripción (deje en blanco para no cambiar): ");
                    String nuevaDescripcion = scanner.nextLine();
                    if (!nuevaDescripcion.isEmpty()) {
                        lpModificar.setDescripcion(nuevaDescripcion);
                    }
            
                    System.out.print("Ingrese el nuevo objetivo (deje en blanco para no cambiar): ");
                    String nuevoObjetivo = scanner.nextLine();
                    if (!nuevoObjetivo.isEmpty()) {
                        lpModificar.setObjetivos(nuevoObjetivo);
                    }
            
                    System.out.print("Ingrese la nueva duración estimada (deje en blanco para no cambiar): ");
                    String nuevaDuracionInput = scanner.nextLine();
                    if (!nuevaDuracionInput.isEmpty()) {
                        int nuevaDuracion = Integer.parseInt(nuevaDuracionInput);
                        lpModificar.setDuracionMinutos(nuevaDuracion);
                    }
            
                    System.out.println("Learning Path modificado exitosamente.");
                } catch (Exception e) {
                    System.out.println("Error al modificar el Learning Path: " + e.getMessage());
                }
                break;
            case "10":
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }

}
