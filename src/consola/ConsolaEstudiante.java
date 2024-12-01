package consola;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import LPRS.LearningPath;
import actividad.Actividad;
import plataforma.Plataforma;
import usuario.Estudiante;

public class ConsolaEstudiante implements Consola {
    private Estudiante estudiante;
    private Plataforma plataforma;
    private Scanner scanner;

    public ConsolaEstudiante(Estudiante estudiante, Plataforma plataforma, Scanner scanner) {
        this.estudiante = estudiante;
        this.plataforma = plataforma;
        this.scanner = scanner;
    }

    public ConsolaEstudiante() {
        this.scanner = new Scanner(System.in); // Inicializa el scanner para evitar NullPointerException
    }

    @Override
    public void iniciar() {
        System.out.println("Hola, estudiante");
        while (true) {
            mostrarMenu();
            System.out.print("Selecciona una opción: ");
            String comando = scanner.nextLine();
            if ("8".equals(comando)) {
                break; // Salir del bucle y finalizar la consola
            }
            ejecutarComando(comando);
        }
    }

    @Override
    public void mostrarMenu() {
        System.out.println("1. Ver Learning Paths disponibles");
        System.out.println("2. Comenzar Learning Path");
        System.out.println("3. Ver actividades pendientes");
        System.out.println("4. Comenzar nueva actividad");
        System.out.println("5. Responder actividad");
        System.out.println("6. Ver progreso en el Learning Path actual");
        System.out.println("7. Ver Learning Paths completados");
        System.out.println("8. Salir");
    }

    private Actividad seleccionarActividad(Estudiante estudiante, Scanner scanner) {
        if (!estudiante.tieneLearningPathAsignado()) {
            System.out.println("No tienes un Learning Path asignado.");
            return null;
        }

        List<Actividad> actividadesPendientes = estudiante.getListaActividadesPorCompletar();
        if (actividadesPendientes == null || actividadesPendientes.isEmpty()) {
            System.out.println("No hay actividades pendientes en tu Learning Path actual.");
            return null;
        }

        System.out.println("Actividades pendientes:");
        for (int i = 0; i < actividadesPendientes.size(); i++) {
            System.out.println((i + 1) + ". " + actividadesPendientes.get(i).getDescripcion());
        }

        System.out.print("Selecciona el número de la actividad: ");
        try {
            int seleccion = Integer.parseInt(scanner.nextLine());
            if (seleccion < 1 || seleccion > actividadesPendientes.size()) {
                System.out.println("Selección inválida.");
                return null;
            }
            return actividadesPendientes.get(seleccion - 1);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor ingresa un número.");
            return null;
        }
    }

    @Override
    public void ejecutarComando(String comando) {
        switch (comando) {
            case "1":
                System.out.println("Lista de Learning Paths disponibles:");
                Map<String, LearningPath> learningPaths = plataforma.getLearningPaths();
                if (learningPaths.isEmpty()) {
                    System.out.println("No hay Learning Paths disponibles.");
                } else {
                    int index = 1;
                    for (Map.Entry<String, LearningPath> entry : learningPaths.entrySet()) {
                        System.out.println(index++ + ". " + entry.getValue().getTitulo());
                    }
                }
                break;

            case "2":
                if (estudiante.tieneLearningPathAsignado()) {
                    System.out.println("No puedes comenzar un nuevo Learning Path hasta completar el actual.");
                } else {
                    System.out.println("Selecciona un Learning Path para comenzar:");
                    Map<String, LearningPath> learningPathsDisponibles = plataforma.getLearningPaths();
                    if (learningPathsDisponibles.isEmpty()) {
                        System.out.println("No hay Learning Paths disponibles.");
                    } else {
                        int index = 1;
                        List<LearningPath> listaLearningPaths = new ArrayList<>(learningPathsDisponibles.values());
                        for (LearningPath lp : listaLearningPaths) {
                            System.out.println(index++ + ". " + lp.getTitulo());
                        }
                        System.out.print("Ingresa el número del Learning Path: ");
                        int seleccionLP = Integer.parseInt(scanner.nextLine());
                        if (seleccionLP < 1 || seleccionLP > listaLearningPaths.size()) {
                            System.out.println("Opción inválida.");
                        } else {
                            LearningPath learningPathSeleccionado = listaLearningPaths.get(seleccionLP - 1);
                            estudiante.comenzarLearningPath(learningPathSeleccionado);
                            System.out.println("Has comenzado el Learning Path: " + learningPathSeleccionado.getTitulo());
                        }
                    }
                }
                break;

            case "3":
                System.out.println("Actividades pendientes en el Learning Path actual:");
                if (!estudiante.tieneLearningPathAsignado()) {
                    System.out.println("No tienes un Learning Path asignado.");
                } else {
                    try {
                        List<String> actividadesPendientes = estudiante.listarActividadesPendientes();
                        if (actividadesPendientes.isEmpty()) {
                            System.out.println("No hay actividades pendientes en el Learning Path actual.");
                        } else {
                            System.out.println("Actividades pendientes:");
                            actividadesPendientes.forEach(System.out::println);
                        }
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                break;

            case "4":
                System.out.println("Comenzando una nueva actividad...");
                try {
                    Actividad actividadActual = seleccionarActividad(estudiante, scanner);
                    if (actividadActual != null) {
                        estudiante.setActividadActual(actividadActual);
                        System.out.println("Has comenzado la actividad: " + actividadActual.getDescripcion());
                    }
                } catch (IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            case "5":
                System.out.println("Respondiendo actividad...");
                try {
                    Actividad actividadSeleccionada = seleccionarActividad(estudiante, scanner);
                    if (actividadSeleccionada != null) {
                        System.out.print("Ingresa tu respuesta: ");
                        String respuesta = scanner.nextLine();
                        estudiante.responderActividad(actividadSeleccionada, respuesta);
                        System.out.println("¡Actividad respondida exitosamente!");
                    }
                } catch (Exception e) {
                    System.out.println("Error al responder la actividad: " + e.getMessage());
                }
                break;

            case "6":
                System.out.println("Viendo progreso en el Learning Path actual...");
                if (!estudiante.tieneLearningPathAsignado()) {
                    System.out.println("No tienes un Learning Path en curso.");
                } else {
                    float progreso = estudiante.obtenerProgresoLearningPathActual();
                    System.out.printf("Has completado %.2f%% del Learning Path actual.\n", progreso);
                }
                break;

            case "7":
                System.out.println("Lista de Learning Paths completados:");
                List<String> learningPathsCompletados = estudiante.listarLearningPathsCompletados();
                if (learningPathsCompletados.isEmpty()) {
                    System.out.println("No has completado ningún Learning Path.");
                } else {
                    learningPathsCompletados.forEach(System.out::println);
                }
                break;

            case "8":
                System.out.println("Saliendo...");
                break;

            default:
                System.out.println("Comando no reconocido.");
                break;
        }
    }
}
