
package pregunta;
import java.util.*;

public class PreguntaCerrada extends Pregunta {

    private Dictionary<Opcion, String> opcionA;
    private Dictionary<Opcion, String> opcionB;
    private Dictionary<Opcion, String> opcionC;
    private Dictionary<Opcion, String> opcionD;
    private Dictionary<Opcion, String> respuesta; // La opción correcta
    private Dictionary<Opcion, String> escogida; // La opción elegida por el estudiante

    // Constructor
    public PreguntaCerrada(String enunciado) {
        super(Tipo.Cerrada, enunciado);
    }

    // Métodos para establecer cada opción
    public void setOpcionA(Dictionary<Opcion, String> opcionA) {
        this.opcionA = opcionA;
    }

    public void setOpcionB(Dictionary<Opcion, String> opcionB) {
        this.opcionB = opcionB;
    }

    public void setOpcionC(Dictionary<Opcion, String> opcionC) {
        this.opcionC = opcionC;
    }

    public void setOpcionD(Dictionary<Opcion, String> opcionD) {
        this.opcionD = opcionD;
    }

    // Método para definir la respuesta correcta
    public void setRespuesta(Dictionary<Opcion, String> respuesta) {
        this.respuesta = respuesta;
    }

    // Getters

    public Dictionary<Opcion, String> getOpcionA() {
        return opcionA;
    }

    public Dictionary<Opcion, String> getOpcionB() {
        return opcionB;
    }

    public Dictionary<Opcion, String> getOpcionC() {
        return opcionC;
    }

    public Dictionary<Opcion, String> getOpcionD() {
        return opcionD;
    }

    public Dictionary<Opcion, String> getRespuesta() {
        return respuesta;
    }
    
    public Dictionary<Opcion, String> getEscogida() {
        return escogida;
    }


    // Método para seleccionar la respuesta del estudiante basado en una opción ("A", "B", "C", "D")
    public void elegirRespuesta(String opcion) {
        switch (opcion) {
            case "A":
                setEscogida(this.opcionA);
                break;
            case "B":
                setEscogida(this.opcionB);
                break;
            case "C":
                setEscogida(this.opcionC);
                break;
            case "D":
                setEscogida(this.opcionD);
                break;
            default:
                throw new IllegalArgumentException("Opción no válida.");
        }
    }

    // Método para establecer la opción seleccionada
    public void setEscogida(Dictionary<Opcion, String> escogida) {
        this.escogida = escogida;
    }

    // Verificar si la respuesta es correcta
    @Override
    public boolean esCorrecta() {
        return escogida != null && escogida.equals(respuesta);
    }

    // Proporcionar retroalimentación según la respuesta seleccionada
    @Override
    public String getRetroalimentacion() {
        if (escogida == null) {
            return "No se ha seleccionado ninguna opción.";
        }
        return esCorrecta() ? "Respuesta correcta." : "Respuesta incorrecta. La opción correcta era: " + respuesta.get(Opcion.valueOf(respuesta.keys().nextElement().name()));
    }

    @Override
    public boolean esEvaluada() {
    return escogida != null; // Verifica si se ha escogido una opción
    }

}

