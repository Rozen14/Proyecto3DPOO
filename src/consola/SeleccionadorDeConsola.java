package consola;

public class SeleccionadorDeConsola {
    public static Consola getConsola(String tipo){
        if(tipo.equals("profesor")){
            return new ConsolaProfesor();
        }else if(tipo.equals("estudiante")){
            return new ConsolaEstudiante();
        }else if(tipo.equals("admin")){
            return new ConsolaAdmin();
        }else{
            throw new IllegalArgumentException("Tipo de usuario no reconocido");
        }
    }
}
