package tests;

import pregunta.Pregunta;
import pregunta.Tipo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class testPregunta {
    
     // Subclase anonima par aprobar la clase abstracta
     private static class PreguntaConcreta extends Pregunta {
        public PreguntaConcreta(Tipo tipo, String enunciado) {
            super(tipo, enunciado);
        }

        @Override
        public boolean esEvaluada() {
            return false; // Implementacion ficticia
        }

        @Override
        public String getRetroalimentacion() {
            return "Retroalimentacion";
        }

        @Override
        public boolean esCorrecta() {
            return false;
        }

     }

     @Test
     public void testConstructorValido() {
         Pregunta pregunta = new PreguntaConcreta(Tipo.Abierta, "Enunciado");
         assertEquals(Tipo.Abierta, pregunta.getTipo());
         assertEquals("Enunciado", pregunta.getEnunciado());
     }

    @Test
    public void testConstructorEnunciadoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PreguntaConcreta(Tipo.Abierta, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PreguntaConcreta(Tipo.Abierta, "");
        });
    }

    @Test
    public void testSetEnunciadoValido() {
        Pregunta pregunta = new PreguntaConcreta(Tipo.Abierta, "Enunciado");
        pregunta.setEnunciado("Nuevo enunciado");
        assertEquals("Nuevo enunciado", pregunta.getEnunciado());
    }

    @Test
    public void testSetEnunciadoInvalido() {
        Pregunta pregunta = new PreguntaConcreta(Tipo.Abierta, "Enunciado");
        assertThrows(IllegalArgumentException.class, () -> {
            pregunta.setEnunciado(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            pregunta.setEnunciado("");
        });
    }

    @Test
    public void testSetTipoValido() {
        Pregunta pregunta = new PreguntaConcreta(Tipo.Abierta, "Enunciado");
        pregunta.setTipo(Tipo.Cerrada);
        assertEquals(Tipo.Cerrada, pregunta.getTipo());
    }

    @Test
    public void testSetTipoInvalido() {
        Pregunta pregunta = new PreguntaConcreta(Tipo.Abierta, "Enunciado");
        assertThrows(IllegalArgumentException.class, () -> {
            pregunta.setTipo(null);
        });
    }



}






