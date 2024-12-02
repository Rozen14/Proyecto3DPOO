package persistencia;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import actividad.Actividad;
import actividad.Encuesta;
import actividad.Examen;
import actividad.Nivel;
import actividad.Obligatoria;
import actividad.Quiz;
import actividad.RecursoEducativo;
import actividad.Status;
import actividad.Tarea;
import pregunta.Pregunta;
import pregunta.PreguntaAbierta;
import pregunta.PreguntaCerrada;
import usuario.Estudiante;
import usuario.Profesor;

public class PersistenciaActividad {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]");

    // Metodo auxiliar guardar actividades 

    public static void guardarActividad(Actividad actividad, BufferedWriter writer) throws IOException {
        String estadosPorEstudianteStr = actividad.getEstadosPorEstudiante().entrySet().stream() // Se sacan los estados por estudiante, el entry set es para sacar los valores del mapa y el stream para hacer el recorrido de los valores
            .map(entry -> entry.getKey().getCorreo() + ":" + entry.getValue().name()) // Se mapea el correo y el estado del estudiante, el entry es para sacar el valor del mapa y el getKey para sacar el correo y el getValue para sacar el estado. Resumiendo esta linea se saca el correo y el estado del estudiante
            .collect(Collectors.joining(";"));  // Se juntan los valores con un ; para separarlos
    if (actividad instanceof Tarea)
    {
        Tarea tarea = (Tarea) actividad;
        writer.write("Tarea,"+ actividad.getDescripcion()+","+ 
        tarea.getNivelDificultad()+","+ 
        tarea.getObjetivo()+","+ 
        tarea.getDuracionEsperada()+","+ 
        tarea.getVersion()+","+ 
        (tarea.getFechaLimite() != null ? actividad.getFechaLimite().format(formatter):"")+","+ 
        (tarea.getFechaInicio() != null ? actividad.getFechaInicio().format(formatter):"")+","+
        estadosPorEstudianteStr+","+ 
        (tarea.esObligatoria()? "SI" : "NO")+","+ 
        tarea.getActividadesPreviasSugeridas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+ // Se sacan las actividades previas sugeridas y se juntan con una coma
        tarea.getActividadesSeguimientoRecomendadas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+ // Se sacan las actividades de seguimiento recomendadas y se juntan con una coma
        tarea.getSubmissionMethod()+","+ 
        tarea.getCreador().getNombre());
    } 
    else if (actividad instanceof Quiz) {
        Quiz quiz = (Quiz) actividad;
    
        String calificacionesObtenidasStr = quiz.getCalificacionesObtenidas().entrySet().stream()
            .map(entry -> entry.getKey().getCorreo() + ":" + entry.getValue())
            .collect(Collectors.joining(";"));
    
        writer.write("Quiz," + actividad.getDescripcion() + "," +
                quiz.getNivelDificultad() + "," +
                quiz.getObjetivo() + "," +
                quiz.getDuracionEsperada() + "," +
                quiz.getVersion() + "," +
                (quiz.getFechaLimite() != null ? actividad.getFechaLimite().format(formatter) : "") + "," +
                (quiz.getFechaInicio() != null ? actividad.getFechaInicio().format(formatter) : "") + "," +
                estadosPorEstudianteStr + "," +
                (quiz.esObligatoria() ? "SI" : "NO") + "," +
                quiz.getActividadesPreviasSugeridas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(",")) + "," +
                quiz.getActividadesSeguimientoRecomendadas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(",")) + "," +
                quiz.getListaPreguntas().stream().map(Pregunta::getEnunciado).collect(Collectors.joining(",")) + "," +
                quiz.getCalificacionMinima() + "," +
                calificacionesObtenidasStr + "," +
                quiz.getCreador().getNombre());
    
        writer.newLine();
    }
    else if (actividad instanceof RecursoEducativo)
    {
        RecursoEducativo recurso = (RecursoEducativo) actividad;
        writer.write("Recurso,"+ actividad.getDescripcion()+","+
        recurso.getNivelDificultad()+","+
        recurso.getObjetivo()+","+
        recurso.getDuracionEsperada()+","+
        recurso.getVersion()+","+
        (recurso.getFechaLimite() != null ? actividad.getFechaLimite().format(formatter):"")+","+
        (recurso.getFechaInicio() != null ? actividad.getFechaInicio().format(formatter):"")+","+
        estadosPorEstudianteStr+","+
        (recurso.esObligatoria()? "SI" : "NO")+","+
        recurso.getActividadesPreviasSugeridas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+
        recurso.getActividadesSeguimientoRecomendadas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+
        recurso.getTipoRecurso()+","+
        recurso.getCreador().getNombre());
    } 
    else if (actividad instanceof Examen) {
        Examen examen = (Examen) actividad;

        String calificacionesObtenidasStr = examen.getCalificacionesObtenidas().entrySet().stream()
        .map(entry -> entry.getKey().getCorreo() + ":" + entry.getValue())
        .collect(Collectors.joining(";"));

        writer.write("Examen," + actividad.getDescripcion() + "," +
                examen.getNivelDificultad() + "," +
                examen.getObjetivo() + "," +
                examen.getDuracionEsperada() + "," +
                examen.getVersion() + "," +
                (examen.getFechaLimite() != null ? actividad.getFechaLimite().format(formatter) : "") + "," +
                (examen.getFechaInicio() != null ? actividad.getFechaInicio().format(formatter) : "") + "," +
                estadosPorEstudianteStr + "," +
                (examen.esObligatoria() ? "SI" : "NO") + "," +
                examen.getActividadesPreviasSugeridas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(",")) + "," +
                examen.getActividadesSeguimientoRecomendadas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(",")) + "," +
                examen.getListaPreguntas().stream() // Se sacan las preguntas y se juntan con una coma
                        .map(pregunta -> { // Se mapea la pregunta
                            if (pregunta instanceof PreguntaCerrada) { // Si la pregunta es cerrada
                                return "Cerrada|" + pregunta.getEnunciado(); // Se retorna cerrada y el enunciado de la pregunta
                            } else if (pregunta instanceof PreguntaAbierta) { // Si la pregunta es abierta
                                return "Abierta|" + pregunta.getEnunciado(); // Se retorna abierta y el enunciado de la pregunta
                            }
                            return "";
                        })
                        .collect(Collectors.joining(";")) + "," + // Se juntan las preguntas con ;
                examen.getCalificacionMinima() + "," +
                examen.getRespuestasCorrectas() + "," +
                calificacionesObtenidasStr + "," +
                examen.getRespuestasAbiertas().stream().collect(Collectors.joining(",")) + "," +
                examen.getCreador().getNombre());
    
        writer.newLine();
    }
    
    
    else if (actividad instanceof Encuesta)
    {
        Encuesta encuesta = (Encuesta) actividad;
        writer.write("Encuesta,"+ actividad.getDescripcion()+","+
        encuesta.getNivelDificultad()+","+
        encuesta.getObjetivo()+","+
        encuesta.getDuracionEsperada()+","+
        encuesta.getVersion()+","+
        (encuesta.getFechaLimite() != null ? actividad.getFechaLimite().format(formatter):"")+","+
        (encuesta.getFechaInicio() != null ? actividad.getFechaInicio().format(formatter):"")+","+
        estadosPorEstudianteStr+","+
        (encuesta.esObligatoria()? "SI" : "NO")+","+
        encuesta.getTipo()+","+
        encuesta.getActividadesPreviasSugeridas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+
        encuesta.getActividadesSeguimientoRecomendadas().stream().map(Actividad::getDescripcion).collect(Collectors.joining(","))+","+
        encuesta.getListaPreguntas().stream().map(PreguntaAbierta::getEnunciado).collect(Collectors.joining(","))+","+
        encuesta.getCreador().getNombre());
    }

    writer.newLine();
}

// Método auxiliar para determinar el tipo de actividad y cargarla
    public static Actividad cargarActividad(String linea, Profesor creador, DateTimeFormatter formatter) {

        System.out.println("Cargando actividad: " + linea); // Se imprime la actividad que se esta cargando
        String[] datos = linea.split(","); // Se separan los datos por coma, ya que los datos que se guardaron los separamos con la coma

        
        for (int i = 0; i < datos.length; i++) { // Se recorre los datos
            System.out.println("datos[" + i + "]: " + datos[i]); // Se imprime los datos
        }
        String tipoActividad = datos[0]; // Se obtiene el tipo de actividad
        // Sacar el tipo en si de la actividad, "Tarea de ..."
        
        
        switch (tipoActividad) { // Se hace un switch para determinar el tipo de actividad y dependiendo de eso cargarla
            case "Tarea":
                return cargarTarea(datos, creador, formatter);
            case "Quiz":
                return cargarQuiz(datos, creador, formatter);
            case "Recurso":
                return cargarRecurso(datos, creador, formatter);
            case "Examen":
                return cargarExamen(datos, creador, formatter);
            case "Encuesta":
                return cargarEncuesta(datos, creador, formatter);
            default:
                System.out.println("Tipo de actividad desconocido: " + tipoActividad);
                return null;
        }
    }

    // Métodos auxiliares específicos para cada tipo de actividad (incluyen todos los atributos)

    public static Tarea cargarTarea(String[] datos, Profesor creador, DateTimeFormatter formatter) {
        String descripcion = datos[1];
        Nivel nivel = Nivel.valueOf(datos[2]);
        String objetivo = datos[3];
        int duracion = Integer.parseInt(datos[4]);
        double version = Double.parseDouble(datos[5]);
        LocalDateTime fechaLimite = LocalDateTime.parse(datos[6], formatter);
        LocalDateTime fechaInicio = LocalDateTime.parse(datos[7], formatter);
            Map<Estudiante, Status> estadosPorEstudiante = Arrays.stream(datos[8].split(";")) // Se sacan los estados por estudiante, el stream es para hacer el recorrido de los valores
            .map(par -> par.split(":")) // Se mapea el par y se separa por :
            .collect(Collectors.toMap( // Se juntan los valores en un mapa
                    par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                    par -> Status.valueOf(par[1]) // Se obtiene el estado del estudiante
            ));
        Obligatoria obligatoria = datos[9].equals("SI") ? Obligatoria.SI : Obligatoria.NO;
        List<Actividad> actividadesPreviasSugeridas = cargarActividades(datos[10], creador, formatter);
        List<Actividad> actividadesSeguimientoRecomendadas = cargarActividades(datos[11], creador, formatter);
        String submissionMethod = datos[12];
        String nombreCreador = datos[13]; // No se usa en la creación de la tarea porque ya se tiene el profesor

       // Crear la tarea con los datos cargados
        Tarea tarea = new Tarea(descripcion, nivel, objetivo, duracion, version, fechaLimite, estadosPorEstudiante, obligatoria, submissionMethod, creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        
        // Asignar los valores que no entran al constructor directamente como fecha de inicio
        tarea.setFechaInicio(fechaInicio);

        return tarea;
    }

    public static Quiz cargarQuiz(String[] datos, Profesor creador, DateTimeFormatter formatter) {
        String descripcion = datos[1];
        Nivel nivel = Nivel.valueOf(datos[2]);
        String objetivo = datos[3];
        int duracion = Integer.parseInt(datos[4]);
        double version = Double.parseDouble(datos[5]);
        LocalDateTime fechaLimite = LocalDateTime.parse(datos[6], formatter);
        LocalDateTime fechaInicio = LocalDateTime.parse(datos[7], formatter);
        Map<Estudiante, Status> estadosPorEstudiante = new HashMap<>(); // Inicializa un mapa vacío

        if (!datos[8].isEmpty()) { // Verifica si datos[8] no está vacío
            estadosPorEstudiante = Arrays.stream(datos[8].split(";"))
                .map(par -> par.split(":"))
                .collect(Collectors.toMap(
                    par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                    par -> Status.valueOf(par[1])          // Convierte el estado a Status
                ));
}

        Obligatoria obligatoria = datos[9].equals("SI") ? Obligatoria.SI : Obligatoria.NO;
        List<Actividad> actividadesPreviasSugeridas = cargarActividades(datos[10], creador, formatter);
        List<Actividad> actividadesSeguimientoRecomendadas = cargarActividades(datos[11], creador, formatter);
        List<PreguntaCerrada> listaPreguntas = PersistenciaPregunta.cargarPreguntasCerradas(datos[12]);
        double calificacionMinima = Double.parseDouble(datos[13]);
        Map<Estudiante, Double> calificacionesObtenidas = new HashMap<>(); // Inicializa un mapa vacío

        if (!datos[14].isEmpty()) { // Verifica si datos[14] no está vacío
            calificacionesObtenidas = Arrays.stream(datos[14].split(";"))
                .map(par -> par.split(":"))
                .collect(Collectors.toMap(
                    par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                    par -> Double.parseDouble(par[1])      // Convierte la calificación a Double
                ));
        }
        
        String nombreCreador = datos[15]; // No se usa en la creación del quiz porque ya se tiene el profesor

        Quiz quiz= new Quiz(descripcion, nivel, objetivo, duracion, version, fechaLimite, estadosPorEstudiante, obligatoria, listaPreguntas, calificacionMinima, creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
        
        // Asignar los valores que no entran al constructor directamente como calificacion obtenida y fehca de inicio
        
        quiz.setFechaInicio(fechaInicio);
        quiz.setCalificacionesObtenidas(calificacionesObtenidas);
        quiz.setListaPreguntas(listaPreguntas);


        return quiz;
    }

    public static RecursoEducativo cargarRecurso(String[] datos, Profesor creador, DateTimeFormatter formatter) {
        String descripcion = datos[1];
        Nivel nivel = Nivel.valueOf(datos[2]);
        String objetivo = datos[3];
        int duracion = Integer.parseInt(datos[4]);
        double version = Double.parseDouble(datos[5]);
        LocalDateTime fechaLimite = LocalDateTime.parse(datos[6], formatter);
        LocalDateTime fechaInicio = LocalDateTime.parse(datos[7], formatter);
        Map<Estudiante, Status> estadosPorEstudiante = Arrays.stream(datos[8].split(";"))
        .map(par -> par.split(":"))
        .collect(Collectors.toMap(
                par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                par -> Status.valueOf(par[1])
        ));
        Obligatoria obligatoria = datos[9].equals("SI") ? Obligatoria.SI : Obligatoria.NO;
        List<Actividad> actividadesPreviasSugeridas = cargarActividades(datos[10], creador, formatter);
        List<Actividad> actividadesSeguimientoRecomendadas = cargarActividades(datos[11], creador, formatter);
        String tipoRecurso = datos[12];
        String nombreCreador = datos[13]; // No se usa en la creación del recurso porque ya se tiene el profesor
        // Crear el recurso educativo con los datos cargados

        RecursoEducativo recurso = new RecursoEducativo(descripcion, nivel, objetivo, duracion, version, fechaLimite, estadosPorEstudiante, obligatoria, tipoRecurso, creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);

        // Asignar los valores que no entran al constructor directamente como fecha de inicio

        recurso.setFechaInicio(fechaInicio);

        return recurso;

    }

    public static Examen cargarExamen(String[] datos, Profesor creador, DateTimeFormatter formatter) {
        String descripcion = datos[1];
        Nivel nivel = Nivel.valueOf(datos[2]);
        String objetivo = datos[3];
        int duracion = Integer.parseInt(datos[4]);
        double version = Double.parseDouble(datos[5]);
        LocalDateTime fechaLimite = LocalDateTime.parse(datos[6], formatter);
        LocalDateTime fechaInicio = LocalDateTime.parse(datos[7], formatter);
        Map<Estudiante, Status> estadosPorEstudiante = Arrays.stream(datos[8].split(";"))
            .map(par -> par.split(":"))
            .collect(Collectors.toMap(
                    par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                    par -> Status.valueOf(par[1])
            ));
        Obligatoria obligatoria = datos[9].equals("SI") ? Obligatoria.SI : Obligatoria.NO;
        List<Actividad> actividadesPreviasSugeridas = cargarActividades(datos[10], creador, formatter);
        List<Actividad> actividadesSeguimientoRecomendadas = cargarActividades(datos[11], creador, formatter);
    
        // Cargar lista de preguntas de distintos tipos
        List<Pregunta> listaPreguntas = new ArrayList<>();
        String[] preguntasData = datos[12].split(";");
        for (String preguntaData : preguntasData) {
            if (preguntaData.startsWith("Cerrada|")) {
                String preguntaCerradaStr = preguntaData.substring("Cerrada|".length());
                List<PreguntaCerrada> preguntasCerradas = PersistenciaPregunta.cargarPreguntasCerradas(preguntaCerradaStr);
                listaPreguntas.addAll(preguntasCerradas);
            } else if (preguntaData.startsWith("Abierta|")) {
                String preguntaAbiertaStr = preguntaData.substring("Abierta|".length());
                List<PreguntaAbierta> preguntasAbiertas = PersistenciaPregunta.cargarPreguntasAbiertas(preguntaAbiertaStr);
                listaPreguntas.addAll(preguntasAbiertas);
            }
        }
    
        double calificacionMinima = Double.parseDouble(datos[13]);
        int respuestasCorrectas = Integer.parseInt(datos[14]);
        Map<Estudiante, Double> calificacionesObtenidas = Arrays.stream(datos[15].split(";"))
            .map(par -> par.split(":"))
            .collect(Collectors.toMap(
                    par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                    par -> Double.parseDouble(par[1])
            ));
        List<String> respuestasAbiertas = Arrays.asList(datos[16].split(","));
    
        Examen examen = new Examen(descripcion, nivel, objetivo, duracion, version, fechaLimite, estadosPorEstudiante, obligatoria, listaPreguntas, calificacionMinima, creador, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas);
    
        // Asignar valores adicionales
        examen.setFechaInicio(fechaInicio);
        examen.setRespuestasCorrectas(respuestasCorrectas);
        examen.setCalificacionesObtenidas(calificacionesObtenidas);
        examen.setRespuestasAbiertas(respuestasAbiertas);
    
        return examen;
    }
    

    public static Encuesta cargarEncuesta(String[] datos, Profesor creador, DateTimeFormatter formatter) {
        String descripcion = datos[1];
        Nivel nivel = Nivel.valueOf(datos[2]);
        String objetivo = datos[3];
        int duracion = Integer.parseInt(datos[4]);
        double version = Double.parseDouble(datos[5]);
        LocalDateTime fechaLimite = LocalDateTime.parse(datos[6], formatter);
        LocalDateTime fechaInicio = LocalDateTime.parse(datos[7], formatter);
        Map<Estudiante, Status> estadosPorEstudiante = Arrays.stream(datos[8].split(";"))
        .map(par -> par.split(":"))
        .collect(Collectors.toMap(
                par -> new Estudiante("", "", par[0]),  // Crea Estudiante con solo correo
                par -> Status.valueOf(par[1])
        ));
        Obligatoria obligatoria = datos[9].equals("SI") ? Obligatoria.SI : Obligatoria.NO;
        List<Actividad> actividadesPreviasSugeridas = cargarActividades(datos[10], creador, formatter);
        List<Actividad> actividadesSeguimientoRecomendadas = cargarActividades(datos[11], creador, formatter);
        ArrayList<PreguntaAbierta> listaPreguntas = new ArrayList<>();
       
       
        Encuesta encuesta = new Encuesta(descripcion, nivel, objetivo, duracion, version, fechaLimite, estadosPorEstudiante, obligatoria, actividadesPreviasSugeridas, actividadesSeguimientoRecomendadas, creador, listaPreguntas);
   
        // Asignar los valores que no entran al constructor directamente como fecha de inicio

        encuesta.setFechaInicio(fechaInicio);

        return encuesta;
   
    }

    // Método auxiliar para cargar las actividades previas y de seguimiento
    public static List<Actividad> cargarActividades(String actividadesStr, Profesor creador, DateTimeFormatter formatter) {
        List<Actividad> actividades = new ArrayList<>();
        String[] actividadesData = actividadesStr.split(";");
        for (String actividadData : actividadesData) {
            String[] datos = actividadData.split(",");
            String tipo = datos[0];
            
            // Validación adicional basada en el tipo de actividad
            if (tipo.equals("Tarea") || tipo.equals("Quiz") || tipo.equals("Recurso") || tipo.equals("Examen") || tipo.equals("Encuesta")) {
                Actividad actividad = cargarActividad(actividadData, creador, formatter);
                if (actividad != null) {
                    actividades.add(actividad);
                }
            } else {
                System.out.println("Tipo de actividad desconocido: " + tipo);
            }
        }
        return actividades;
    }




}



