package consola;

import java.util.Map;

import usuario.Estudiante;
import usuario.Profesor;
import usuario.Administrador;
import usuario.Usuario;
import java.util.HashMap;

public class Autenticacion {
    private Map<String, Usuario> usuarios;

    public Autenticacion(){
        this.usuarios = new HashMap<>();
        usuarios.put("admin1", Administrador.getAdmin());
        usuarios.put("teacher1", new Profesor("teacher1", "abcd", "teacher1@prof.com", null,  null));
        usuarios.put("student1", new Estudiante("student1", "5678", "student1@student.com"));
    }
    
    public Usuario iniciarSesion(String nombreUsuario, String contrasenia){
        Usuario usuario = usuarios.get(nombreUsuario);
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)){
            return usuario;
        }
        return null;
    }

    public void agregarUsuario(String nombreUsuario, Usuario usuario) {
        usuarios.put(nombreUsuario, usuario);
        System.out.println("Usuario agregado: " + usuario.getNombre());
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
        } 
    }
    
    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    } 
    

    // Buscar usuario por correo
    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCorreo().equals(correo)) {
                return usuario;
            }
        }
        return null;
    }

    // Ver lista de usuarios
    public void mostrarUsuarios() {
        System.out.println("Usuarios en el sistema:");
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            System.out.println("Usuario: " + entry.getKey() + " | Nombre: " + entry.getValue().getNombre() +
                    " | Correo: " + entry.getValue().getCorreo() + " | Tipo: " + entry.getValue().getTipo());
        }
    }
}
