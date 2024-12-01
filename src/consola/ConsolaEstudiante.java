package consola;

public class ConsolaEstudiante implements Consola {
    @Override
    public void iniciar() {
        System.out.println("Hola, estudiante");
        mostrarMenu();
    }

    @Override
    public void mostrarMenu() {
        System.out.println("1. Comenzar Actividad");
        System.out.println("2. Comenzar Learning Path");
        System.out.println("3. Ver progreso");
        System.out.println("4. Salir");
    }

    @Override
    public void ejecutarComando(String comando) {
        switch(comando){
            case "1":
                System.out.println("Comenzando actividad...");
                break;
            case "2":
                System.out.println("Comenzando learning path...");
                break;
            case "3":
                System.out.println("Viendo progreso...");
                break;
            case "4":
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }
}
