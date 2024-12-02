package consola;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import persistencia.PersistenciaEstudiante;
import persistencia.PersistenciaProfesor;
import usuario.Administrador;
import usuario.Estudiante;
import usuario.Profesor;
import usuario.Usuario;

public class Autenticacion {
    private static final String RUTA_ESTUDIANTES = "src/persistencia/archivo/estudiantes.txt";
    private static final String RUTA_PROFESORES = "src/persistencia/archivo/profesores.txt";

    private Map<String, Usuario> usuarios;

    public Autenticacion() {
        this.usuarios = new HashMap<>();
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        // Cargar usuarios persistidos
        cargarUsuariosPersistidos();

        // Agregar usuarios manuales si no existen
        agregarUsuarioSiNoExiste("admin1", Administrador.getAdmin());
        agregarUsuarioSiNoExiste("teacher1", new Profesor("teacher1", "abcd", "teacher1@prof.com", new ArrayList<>(), new ArrayList<>()));
        agregarUsuarioSiNoExiste("student1", new Estudiante("student1", "5678", "student1@student.com"));
    }

    private void cargarUsuariosPersistidos() {
        // Cargar Administrador
        usuarios.put("admin1", Administrador.getAdmin());

        // Cargar Profesores
        File archivoProfesores = new File(RUTA_PROFESORES);
        if (archivoProfesores.exists()) {
            try {
                for (Profesor profesor : PersistenciaProfesor.cargarProfesores(archivoProfesores)) {
                    usuarios.put(profesor.getNombre(), profesor);
                }
            } catch (IOException e) {
                System.out.println("Error al cargar profesores: " + e.getMessage());
            }
        } else {
            System.out.println("Archivo de profesores no encontrado.");
        }

        // Cargar Estudiantes
        File archivoEstudiantes = new File(RUTA_ESTUDIANTES);
        if (archivoEstudiantes.exists()) {
            try {
                for (Estudiante estudiante : PersistenciaEstudiante.cargarEstudiantes(archivoEstudiantes, new HashMap<>())) {
                    usuarios.put(estudiante.getNombre(), estudiante);
                }
            } catch (IOException e) {
                System.out.println("Error al cargar estudiantes: " + e.getMessage());
            }
        } else {
            System.out.println("Archivo de estudiantes no encontrado.");
        }
    }

    private void agregarUsuarioSiNoExiste(String nombreUsuario, Usuario usuario) {
        if (!usuarios.containsKey(nombreUsuario)) {
            usuarios.put(nombreUsuario, usuario);
            guardarUsuario(usuario); // Persistir inmediatamente
            System.out.println("Usuario agregado manualmente: " + usuario.getNombre());
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia) {
        Usuario usuario = usuarios.get(nombreUsuario);
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        }
        return null;
    }

    public void agregarUsuario(String nombreUsuario, Usuario usuario) {
        if (!usuarios.containsKey(nombreUsuario)) {
            usuarios.put(nombreUsuario, usuario);
            guardarUsuario(usuario); // Guardar al persistencia
            System.out.println("Usuario agregado exitosamente: " + usuario.getNombre());
        } else {
            System.out.println("El usuario ya existe: " + nombreUsuario);
        }
    }

    public void eliminarUsuario(Usuario usuario) {
        String nombreUsuario = null;

        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            if (entry.getValue().equals(usuario)) {
                nombreUsuario = entry.getKey();
                break;
            }
        }

        if (nombreUsuario != null) {
            usuarios.remove(nombreUsuario);
            guardarDatos(); // Guardar cambios a persistencia
            System.out.println("Usuario eliminado: " + usuario.getNombre());
        } else {
            System.out.println("Usuario no encontrado para eliminar: " + usuario.getNombre());
        }
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCorreo().equals(correo)) {
                return usuario;
            }
        }
        return null;
    }

    public void mostrarUsuarios() {
        System.out.println("Usuarios en el sistema:");
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            System.out.println("Usuario: " + entry.getKey() + " | Nombre: " + entry.getValue().getNombre() +
                    " | Correo: " + entry.getValue().getCorreo() + " | Tipo: " + entry.getValue().getTipo());
        }
    }

    private void guardarUsuario(Usuario usuario) {
        try {
            if (usuario instanceof Profesor) {
                PersistenciaProfesor.guardarProfesor((Profesor) usuario, new File(RUTA_PROFESORES));
            } else if (usuario instanceof Estudiante) {
                PersistenciaEstudiante.guardarEstudiante((Estudiante) usuario, new File(RUTA_ESTUDIANTES));
            }
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    private void guardarDatos() {
        try {
            // Guardar todos los profesores
            for (Usuario usuario : usuarios.values()) {
                if (usuario instanceof Profesor) {
                    PersistenciaProfesor.guardarProfesor((Profesor) usuario, new File(RUTA_PROFESORES));
                }
            }

            // Guardar todos los estudiantes
            for (Usuario usuario : usuarios.values()) {
                if (usuario instanceof Estudiante) {
                    PersistenciaEstudiante.guardarEstudiante((Estudiante) usuario, new File(RUTA_ESTUDIANTES));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }
}
