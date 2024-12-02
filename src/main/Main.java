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
    
            }

    // Método para sobrescribir datos de persistencia
    private static void guardarDatos(File archivoProfesores, File archivoEstudiantes, File archivoLearningPaths, Profesor profesor, Estudiante estudiante1, Estudiante estudiante2, LearningPath learningPath) throws IOException {
        PersistenciaProfesor.guardarProfesor(profesor, archivoProfesores); // Guardar el profesor en el archivo
        PersistenciaEstudiante.guardarEstudiante(estudiante1, archivoEstudiantes); // Guardar el estudiante 1 en el archivo
        PersistenciaEstudiante.guardarEstudiante(estudiante2, archivoEstudiantes); // Guardar el estudiante 2 en el archivo
        PersistenciaLearningPath.guardarLearningPath(learningPath); // Guardar el LearningPath en el archivo
        }
            

    
    
}
    