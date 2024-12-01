package consola;

import java.util.Scanner;

import usuario.Administrador;
import usuario.Profesor;
import usuario.Usuario;

import LPRS.LearningPath;
import actividad.Actividad;

public class ConsolaAdmin implements Consola {
    private Autenticacion autenticacion = new Autenticacion();
    private Administrador admin;
    private Scanner scanner;

    public ConsolaAdmin(Scanner scanner) {
        this.admin = Administrador.getAdmin();
        this.scanner = scanner;
    }

    public ConsolaAdmin() {
        this.admin = Administrador.getAdmin();
        this.scanner = new Scanner(System.in); // Inicializa el scanner para evitar NullPointerException
    }
    

    @Override
    public void iniciar() {
        System.out.println("Hola, administrador");
        mostrarMenu();
    }

    @Override
    public void mostrarMenu() {
        System.out.println("1. Crear usuario");
        System.out.println("2. Eliminar usuario");
        System.out.println("3. Modificar usuario");
        System.out.println("4. Ver usuarios");
        System.out.println("5. Eliminar Learning Path");
        System.out.println("6. Eliminar Actividad");
        System.out.println("7. Salir");
    }

    @Override
    public void ejecutarComando(String comando){

            switch(comando){
            case "1":
                System.out.print("Ingrese el tipo de usuario (profesor/estudiante): ");
                String tipoUsuario = scanner.nextLine().toLowerCase();
                System.out.print("Ingrese el nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("Ingrese la contraseña: ");
                String contrasenia = scanner.nextLine();
                System.out.print("Ingrese el correo: ");
                String correo = scanner.nextLine();

                if (tipoUsuario.equals("profesor")) {
                    admin.crearProfesor(nombre, contrasenia, correo, autenticacion);
                    System.out.println("Profesor creado exitosamente.");
                } else if (tipoUsuario.equals("estudiante")) {
                    admin.crearEstudiante(nombre, contrasenia, correo, autenticacion);
                    System.out.println("Estudiante creado exitosamente.");
                } else {
                    System.out.println("Tipo de usuario no reconocido.");
                }
                break;
            case "2":
            System.out.print("Ingrese el correo del usuario a eliminar: ");
            String correoEliminar = scanner.nextLine();
            Usuario usuarioEliminar = autenticacion.buscarUsuarioPorCorreo(correoEliminar);
            if (usuarioEliminar != null) {
                admin.eliminarUsuario(usuarioEliminar, autenticacion);
                System.out.println("Usuario eliminado exitosamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
                break;
            case "3":
            System.out.print("Ingrese el correo del usuario a modificar: ");
            String correoModificar = scanner.nextLine();
            Usuario usuarioModificar = autenticacion.buscarUsuarioPorCorreo(correoModificar);

            if (usuarioModificar != null) {
                System.out.print("Ingrese el nuevo nombre completo (deje en blanco para no cambiar): ");
                String nuevoNombre = scanner.nextLine();
                System.out.print("Ingrese la nueva contraseña (deje en blanco para no cambiar): ");
                String nuevaContrasenia = scanner.nextLine();
                System.out.print("Ingrese el nuevo correo (deje en blanco para no cambiar): ");
                String nuevoCorreo = scanner.nextLine();

                if (!nuevoNombre.isEmpty()) usuarioModificar.setNombre(nuevoNombre);
                if (!nuevaContrasenia.isEmpty()) usuarioModificar.setContrasenia(nuevaContrasenia);
                if (!nuevoCorreo.isEmpty()) {
                    if (autenticacion.buscarUsuarioPorCorreo(nuevoCorreo) == null) {
                        usuarioModificar.setCorreo(nuevoCorreo);
                    } else {
                        System.out.println("Ya existe un usuario con este nuevo correo. No se cambiará.");
                    }
                }

                System.out.println("Usuario modificado exitosamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }
                break;
            case "4":
                System.out.println("Viendo usuarios...");
                autenticacion.mostrarUsuarios();
                break;
            case "5":
                System.out.print("Ingrese el título del Learning Path que desea eliminar: ");
                String tituloLP = scanner.nextLine();
                LearningPath learningPath = null;
                for (Usuario usuario : autenticacion.getUsuarios().values()) {
                    if (usuario instanceof Profesor) {
                        for (LearningPath lp : ((Profesor) usuario).getLearningPathCreado()) {
                            if (lp.getTitulo().equalsIgnoreCase(tituloLP)) {
                                learningPath = lp;
                                break;
                            }
                        }
                    }
                }
                if (learningPath != null) {
                    admin.eliminarLearningPath(learningPath);
                    System.out.println("Learning Path eliminado exitosamente.");
                } else {
                    System.out.println("Learning Path no encontrado.");
                }
                break;
            case "6":
                System.out.print("Ingrese la descripción de la actividad que desea eliminar: ");
                String descripcionActividad = scanner.nextLine();

                // Buscar la actividad por descripción
                Actividad actividad = null;
                for (Usuario usuario : autenticacion.getUsuarios().values()) {
                    if (usuario instanceof Profesor) {
                        for (LearningPath lp : ((Profesor) usuario).getLearningPathCreado()) {
                            for (Actividad act : lp.getListaActividades()) {
                                if (act.getDescripcion().equalsIgnoreCase(descripcionActividad)) {
                                    actividad = act;
                                    break;
                                }
                            }
                            if (actividad != null) break; // Rompe el bucle si ya se encontró la actividad
                        }
                    }
                }

                if (actividad != null) {
                    admin.eliminarActividad(actividad);
                    System.out.println("Actividad eliminada exitosamente.");
                } else {
                    System.out.println("Actividad no encontrada.");
                }
                break;
            case "7":
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }

}

