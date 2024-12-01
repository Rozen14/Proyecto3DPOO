package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuario.Usuario;

public class testUsuario {

    private Usuario usuario;

    // Clase concreta para probar la clase abstracta
    static class UsuarioConcreto extends Usuario {
        public UsuarioConcreto(String nombre, String contrasenia, String tipo, String correo) {
            super(nombre, contrasenia, tipo, correo);
        }
    }

    @BeforeEach
    void setUp() {
        usuario = new UsuarioConcreto("Juan", "1234", "Admin", "juan@example.com");
    }

    @Test
    void testInstanciacion() {
        assertEquals("Juan", usuario.getNombre());
        assertEquals("1234", usuario.getContrasenia());
        assertEquals("Admin", usuario.getTipo());
        assertEquals("juan@example.com", usuario.getCorreo());
    }

    @Test
    void testValidarCredenciales() {
        assertTrue(usuario.validarCredenciales("juan@example.com", "1234"));
        assertFalse(usuario.validarCredenciales("juan@example.com", "incorrecta"));
        assertFalse(usuario.validarCredenciales("otro@example.com", "1234"));
    }

    @Test
    void testActualizarContrasenia() {
        assertTrue(usuario.actualizarContrasenia("1234", "nueva1234"));
        assertEquals("nueva1234", usuario.getContrasenia());

        assertFalse(usuario.actualizarContrasenia("incorrecta", "otraNueva"));
        assertEquals("nueva1234", usuario.getContrasenia()); // Asegurarse que no se actualizó
    }

    @Test
    void testSettersAndGetters() {
        usuario.setNombre("Carlos");
        assertEquals("Carlos", usuario.getNombre());

        usuario.setContrasenia("5678");
        assertEquals("5678", usuario.getContrasenia());

        usuario.setId("Usuario");
        assertEquals("Usuario", usuario.getTipo());

        usuario.setCorreo("carlos@example.com");
        assertEquals("carlos@example.com", usuario.getCorreo());
    }
}
