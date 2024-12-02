package LPRS;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // Importar la clase Collectors para utilizar el método joining, el metodo joining se usara para unir las descripciones de las actividades completadas y asi guardarlas en el archivo de texto

import actividad.*;
import usuario.Estudiante;
import usuario.Profesor;
import persistencia.*;

public class LearningPath {

    protected String titulo; // Titulo del Learning Path
    protected Nivel nivelDificultad; // Nivel de dificultad del Learning Path
    protected String descripcion;   // Descripcion del Learning Path
    protected String objetivos; // Objetivos del Learning Path
    protected int duracionMinutos; // Duracion en minutos del Learning Path
    protected LocalDateTime fechaCreacion; // Fecha de creacion del Learning Path
    protected LocalDateTime fechaModificacion; // Fecha de modificacion del Learning Path
    protected int version; // Version del Learning Path
    protected Map<Estudiante, Status> status; // Status por estudiante
    protected List<Actividad> listaActividades; // Lista de actividades del Learning Path
    protected Profesor creador; // Creador del Learning Path
    protected float rating; // Rating del Learning Path
    protected List<Estudiante> estudiantesInscritos; // Lista de estudiantes inscritos en el Learning Path
    protected Map<Estudiante, Float> progreso; // Progreso por estudiante
    protected Map<Estudiante, List<Actividad>> listaActividadesCompletadasConDup; // Lista de actividades completadas con duplicados
    protected Map<Estudiante, List<Actividad>> listaActividadesCompletadas; // Lista de actividades completadas sin duplicados
    

    public LearningPath (String titulo, Nivel nivelDificultad, String descripcion, String objetivos, int duracionMinutos, Profesor creador, float rating, List<Actividad> listaActividades){

        
        this.titulo=titulo; 
        this.nivelDificultad=nivelDificultad;
        this.descripcion=descripcion;
        this.objetivos= objetivos;
        this.duracionMinutos=duracionMinutos;
        this.fechaCreacion=LocalDateTime.now();
        this.fechaModificacion=LocalDateTime.now();
        this.version=1;
        this.status= new HashMap<>();
        this.listaActividades = listaActividades;
        this.creador=creador;
        this.rating = rating;
        this.estudiantesInscritos = new ArrayList<>();
        this.progreso =  new HashMap<>();
        this.listaActividadesCompletadasConDup = new HashMap<>();
        this.listaActividadesCompletadas = new HashMap<>();

        // Verificar si la lista de actividades no está vacía y si tiene al menos una actividad obligatoria
        //if (!listaActividades.isEmpty() && !tieneActividadObligatoria()) {
        //    throw new IllegalArgumentException("El Learning Path debe tener al menos una actividad obligatoria.");
        //}
    }



    // Verificar que al menos una actividad sea obligatoria

    // Getters y Setters
    public String getTitulo() { // Obtener el titulo del Learning Path
        return titulo;
    }


    public void setTitulo(String titulo) { // Establecer el titulo del Learning Path
        this.titulo = titulo;
    }


    public Nivel getNivelDificultad() { // Obtener el nivel de dificultad del Learning Path
        return nivelDificultad;
    }


    public void setNivelDificultad(Nivel nivelDificultad) { // Establecer el nivel de dificultad del Learning Path
        this.nivelDificultad = nivelDificultad;
    }


    public String getDescripcion() { // Obtener la descripcion del Learning Path
        return descripcion;
    }


    public void setDescripcion(String descripcion) { // Establecer la descripcion del Learning Path
        this.descripcion = descripcion;
    }


    public String getObjetivos() { // Obtener los objetivos del Learning Path
        return objetivos;
    }


    public void setObjetivos(String objetivos) { // Establecer los objetivos del Learning Path
        this.objetivos = objetivos;
    }


    public int getDuracionMinutos() { // Obtener la duracion en minutos del Learning Path
        return duracionMinutos;
    }


    public void setDuracionMinutos(int duracionMinutos) { // Establecer la duracion en minutos del Learning Path
        this.duracionMinutos = duracionMinutos;
    }


    public LocalDateTime getFechaCreacion() { // Obtener la fecha de creacion del Learning Path
        return fechaCreacion;
    }


    public void setFechaCreacion(LocalDateTime fechaCreacion) { // Establecer la fecha de creacion del Learning Path
        this.fechaCreacion = fechaCreacion;
    }


    public LocalDateTime getFechaModificacion() { // Obtener la fecha de modificacion del Learning Path
        return fechaModificacion;
    }


    public void setFechaModificacion(LocalDateTime fechaModificacion) { // Establecer la fecha de modificacion del Learning Path
        this.fechaModificacion = fechaModificacion;
    }


    public int getVersion() { // Obtener la version del Learning Path
        return version;
    }


    public void setVersion() { // Establecer la version del Learning Path
        this.version +=1;
    }


    public Map<Estudiante, Status> getStatusParaEstudiantes() { // Obtener el status del Learning Path
        return status;
    }

    public Status getStatusParaEstudiante(Estudiante estudiante) { // Obtener el status del Learning Path
        return status.get(estudiante);
    }


    public void setStatusParaEstudiante(Estudiante estudiante, Status status) { // Establecer el status del Learning Path
        this.status.put(estudiante, status);
    }

    public void setStatusParaEstudiantes(Map<Estudiante, Status> status) { // Establecer el status del Learning Path
        this.status = status;
    }

    public Profesor getCreador() { // Obtener el creador del Learning Path
        return creador; 
    }

    public List<Actividad> getListaActividades() { // Obtener la lista de actividades del Learning Path
        return listaActividades;
    }

    public void agregarActividad(Actividad actividad) {
        if (listaActividades.contains(actividad)) {
            throw new IllegalArgumentException("La actividad ya está en la lista de actividades del Learning Path.");
        }
    
        // Verificar si la lista de actividades está vacía y si la actividad que se va a agregar es obligatoria
        if (listaActividades.isEmpty() && !actividad.esObligatoria()) {
            throw new IllegalArgumentException("La primera actividad en el Learning Path debe ser obligatoria.");
        }
    
        listaActividades.add(actividad); // Agregar la actividad a la lista de actividades
        this.fechaModificacion = LocalDateTime.now(); // Actualizar la fecha de modificación
        setVersion(); // Incrementar la versión del Learning Path
    }

    public void eliminarActividad(Actividad actividad) { // Eliminar una actividad de la lista de actividades del Learning Path 
        if (!listaActividades.contains(actividad)) {
            throw new IllegalArgumentException("La actividad no está en la lista de actividades del Learning Path.");
        } 
        else {
            listaActividades.remove(actividad); // Eliminar la actividad de la lista de actividades 
            if (listaActividades.stream().noneMatch(Actividad::esObligatoria)) { // Verificar si el Learning Path tiene al menos una actividad obligatoria, luego el noneMatch verifica si no hay ninguna actividad obligatoria
                listaActividades.add(actividad); // Si no hay ninguna actividad obligatoria, se agrega la actividad eliminada
                throw new IllegalStateException("El Learning Path debe contener al menos una actividad obligatoria."); // Lanzar una excepción
            }
            this.fechaModificacion=LocalDateTime.now(); // Actualizar la fecha de modificacion
            setVersion(); // Incrementar la versión del Learning Path
        }
    }


    public boolean tieneActividadObligatoria() { // Verificar si el Learning Path tiene al menos una actividad obligatoria
        return listaActividades.stream().anyMatch(actividad -> actividad.esObligatoria()); // Verificar si hay alguna actividad obligatoria, nuevamente usamos stream y anyMatch para verificar si hay alguna actividad obligatoria, la flechita es una expresión lambda que se utiliza para verificar si la actividad es obligatoria
    }

    public void validarActividadesObligatorias() {
        if (listaActividades.stream().noneMatch(Actividad::esObligatoria)) { // Verificar si el Learning Path tiene al menos una actividad obligatoria
            throw new IllegalStateException("El Learning Path debe contener al menos una actividad obligatoria.");
        }
    }
    public void registrarLearningPath() { 
        validarActividadesObligatorias(); // Validar que haya al menos una actividad obligatoria
        System.out.println("Learning Path validado y listo para su uso.");
    } // registrarLearningPath se debe utilizar cada vez que se cree un learning path de manera que se garantice que haya al menos una actividad obligatoria
    // EJEMPLO: 
    // LearningPath learningPath = new LearningPath("Título", Nivel.MEDIO, "Descripción", "Objetivos", 120, profesor, 4.5f);
    
    // learningPath.agregarActividad(new Actividad("Descripción Actividad", Nivel.FACIL, "Objetivo", 30, 1.0, LocalDateTime.of(2024, 12, 31, 23, 59), Status.INCOMPLETO, Obligatoria.SI, "Tipo de Actividad"));
    
    // learningPath.registrarLearningPath();

    public void inscripcionEstudiante(Estudiante estudiante) {     
        if (this.estudiantesInscritos.contains(estudiante)) { // Verificar si el estudiante ya está inscrito
            throw new IllegalArgumentException("El estudiante ya está inscrito en este Learning Path.");
        }
        // No permitir que se incriban si ya estan inscritos en otro Learning Path
        if (estudiante.getLearningPathActual() != null) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en otro Learning Path.");
        }   
        this.estudiantesInscritos.add(estudiante);

        // Asignar el Learning Path al estudiante

        estudiante.setLearningPathActual(this);

        // Asignar el estado inicial del estudiante al Learning Path y el progreso

        this.status.put(estudiante, Status.Incompleto);
        this.progreso.put(estudiante, 0.0f);

    } // inscripcionEstudiante se debe utilizar cada vez que un estudiante se inscriba a un learning path
    // si esta variable es verdadera, no se puede crear, modificar, ni eliminar actividades del learning path

    public boolean verificarSiInscrito(Estudiante estudiante) {
        return this.estudiantesInscritos.contains(estudiante);
    } // verificarSiInscrito se debe utilizar para verificar si un estudiante ya está inscrito en un learning path

    public boolean verificarInscripcionYActividad(Estudiante estudiante, Actividad actividad) {
        if (!verificarSiInscrito(estudiante)) { // Método ya existente que verifica si el estudiante está inscrito
            return false; // Estudiante no está inscrito
        }
    
        // Verificar si la actividad pertenece a este LearningPath
        return listaActividades.contains(actividad); // `listActividades` es la lista de actividades del LearningPath
    }
    
    
    public boolean verificarSiHayInscritos() {
        return !this.estudiantesInscritos.isEmpty();
    } // verificarSiHayInscritos se debe utilizar para verificar si hay estudiantes inscritos en un learning path

    public boolean verificarActividad(Actividad actividad) {
        return this.listaActividades.contains(actividad); 
        
    } // verificarActividad se debe utilizar para verificar si una actividad está asociada a un learning path
    public void eliminarInscripcion(Estudiante estudiante) {
        if (!this.estudiantesInscritos.contains(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en este Learning Path.");
        }
        this.estudiantesInscritos.remove(estudiante);
    } // eliminarInscripcion se debe utilizar para eliminar la inscripción de un estudiante en un learning path
    // se debe utilizar cada vez que un estudiante finalice el learning path 

    // Método para marcar una actividad obligatoria como completada para un estudiante específico
    public void actividadObligatoriaCompletada(Actividad actividad, Estudiante estudiante) {
        // Asegurarse de que las listas existen para el estudiante
        listaActividadesCompletadasConDup.computeIfAbsent(estudiante, k -> new ArrayList<>()); // Si no existe la lista, se crea una nueva, el metodo computeIfAbsent se utiliza para obtener el valor de una clave especifica para tal setudiante, si no existe, se crea una nueva instancia de la lista
        listaActividadesCompletadas.computeIfAbsent(estudiante, k -> new ArrayList<>()); 

        // Agregar la actividad a la lista con duplicados si el estado es completado
        if (actividad.esExitosa(estudiante)) {
        listaActividadesCompletadasConDup.get(estudiante).add(actividad);
        estudiante.setActividadActual(null); // Establecer la actividad actual del estudiante como nula porque ya completó la actividad
        }

        // Agregar la actividad a la lista sin duplicados solo si aún no está presente
        if (!listaActividadesCompletadas.get(estudiante).contains(actividad)) {
            listaActividadesCompletadas.get(estudiante).add(actividad);
        }
    }

    // Método para calcular el progreso de un estudiante
    public float calcularProgreso(Estudiante estudiante) {
        int totalObligatorias = (int) listaActividades.stream()
            .filter(Actividad::esObligatoria)
            .count();
    
        if (totalObligatorias == 0) {
            System.out.println("No hay actividades obligatorias en el Learning Path.");
            return 0;
        }
    
        List<Actividad> actividadesCompletadas = listaActividadesCompletadas.get(estudiante);
    
        int completadasObligatorias = (int) actividadesCompletadas.stream()
            .filter(a -> a.esObligatoria() && a.esExitosa(estudiante))
            .count();
    
        float p = (float) completadasObligatorias / totalObligatorias * 100;
        progreso.put(estudiante, p);
    
        if (p == 100.00) {
            setStatusParaEstudiante(estudiante, Status.Completado);
            // Agregar el Learning Path a la lista de Learning Paths completados del estudiante
            estudiante.agregarLearningPathCompletado(this);
            // Quitar el Learning Path actual del estudiante
            estudiante.setLearningPathActual(null);
        }
    
        return p;
    }

    public void actualizarProgreso(Estudiante estudiante, float progreso) {

        this.progreso.put(estudiante, progreso);

        if (progreso == 100.00) {
            setStatusParaEstudiante(estudiante, Status.Completado);
            // Agregar el Learning Path a la lista de Learning Paths completados del estudiante
            estudiante.agregarLearningPathCompletado(this);
            // Quitar el Learning Path actual del estudiante
    
        }
    }
    
    public float getProgresoParaEstudiante(Estudiante estudiante) {
        return progreso.getOrDefault(estudiante, 0.0f);
    }



    public float getRating(){ // Obtener el rating del Learning Path
        return rating;
    }

    public void setRating(float rating){ // Establecer el rating del Learning Path
        this.rating = rating;
    }
    
    public List<Estudiante> getEstudiantesInscritos(){ // Obtener la lista de estudiantes inscritos en el Learning Path
        return estudiantesInscritos;
    }


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]"); // Formato de fecha para persistencia en archivos de texto

    // Persistencia de LearningPath en archivos de texto plano, dejamos esta parte del codigo aqui porque era más facil sacar los datos de los atributos de la clase LearningPath, la creacion de archivos en si se maneja en la clase PersistenciaLearningPath

    // Método para guardar el LearningPath en un archivo de texto plano
    public void guardarEnArchivo() throws IOException {
        File archivo = new File("src/persistencia/archivo/learningPaths.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) { // BufferedWriter se utiliza para escribir texto en un archivo, FileWriter se utiliza para escribir caracteres en un archivo, y append = true para agregar texto al final del archivo
            // Guardar atributos básicos del LearningPath
            // Aqui el write esta guardando los atributos del LearningPath en el archivo de texto, se utiliza el metodo write para escribir texto en el archivo, y el metodo newLine para escribir una nueva linea en el archivo
            writer.write(this.titulo + "," + 
                         this.nivelDificultad + "," +
                         this.descripcion + "," +
                         this.objetivos + "," +
                         this.duracionMinutos + "," +
                         (this.fechaCreacion != null ? this.fechaCreacion.format(formatter) : "") + "," +
                         (this.fechaModificacion != null ? this.fechaModificacion.format(formatter) : "") + "," +
                         this.version + "," +
                         this.rating + "," +
                         this.creador.getNombre()); 
            writer.newLine();
    

            
        // Guardar información de los estudiantes (incluye nombre y contraseña)
        writer.write("ESTUDIANTES:");
        writer.newLine();
        for (Estudiante estudiante : estudiantesInscritos) {
            String correo = estudiante.getCorreo();
            String nombre = estudiante.getNombre();
            String contrasena = estudiante.getContrasenia(); // Agregar contraseña
            Status estado = getStatusParaEstudiante(estudiante);
            float progresoEstudiante = getProgresoParaEstudiante(estudiante);

            // Formato: correo:nombre:contraseña:estado,progreso
            writer.write(correo + ":" + nombre + ":" + contrasena + ":" + estado + "," + progresoEstudiante);
            writer.newLine();
        }


            writer.write("ACTIVIDADES:"); // Etiqueta para indicar que se guardará la lista de actividades
            writer.newLine();
    
            // Guardar cada actividad en la lista de actividades del LearningPath
            for (Actividad actividad : listaActividades) {
                PersistenciaActividad.guardarActividad(actividad, writer);
            }
    
            // Guardar lista de actividades completadas con duplicados para cada estudiante
            writer.write("COMPLETADAS_CON_DUP:");
            writer.newLine();
            for (Map.Entry<Estudiante, List<Actividad>> entry : listaActividadesCompletadasConDup.entrySet()) {
                Estudiante estudiante = entry.getKey();
                writer.write(estudiante.getCorreo() + ":");
                writer.write(entry.getValue().stream()
                              .map(Actividad::getDescripcion) // Cambiar esto según los datos necesarios para cargar
                              .collect(Collectors.joining(",")));
                writer.newLine();
            }
    
            // Guardar actividades completadas sin duplicados
            writer.write("COMPLETADAS_SIN_DUP:");
            writer.newLine();
            for (Map.Entry<Estudiante, List<Actividad>> entry : listaActividadesCompletadas.entrySet()) {
                Estudiante estudiante = entry.getKey();
                writer.write(estudiante.getCorreo() + ":");
                writer.write(entry.getValue().stream()
                            .map(Actividad::getDescripcion) // Cambiar esto según los datos necesarios para cargar
                            .collect(Collectors.joining(",")));
                writer.newLine();
            }
        }
    }

    // Método principal para cargar el LearningPath
    public static LearningPath cargarDeArchivo(File archivo, Profesor creador) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();
            if (linea != null) {
                // Leer atributos básicos del LearningPath
                String[] datos = linea.split(",");
                String titulo = datos[0];
                Nivel nivelDificultad = Nivel.valueOf(datos[1]);
                String descripcion = datos[2];
                String objetivos = datos[3];
                int duracionMinutos = Integer.parseInt(datos[4]);
                LocalDateTime fechaCreacion = LocalDateTime.parse(datos[5], formatter);
                LocalDateTime fechaModificacion = datos[6].isEmpty() ? null : LocalDateTime.parse(datos[6], formatter);
                int version = Integer.parseInt(datos[7]);
                float rating = Float.parseFloat(datos[8]);
    
                LearningPath learningPath = new LearningPath(titulo, nivelDificultad, descripcion, objetivos, duracionMinutos, creador, rating, new ArrayList<>());
                learningPath.fechaCreacion = fechaCreacion;
                learningPath.fechaModificacion = fechaModificacion;
                learningPath.version = version;
    
                // Leer secciones
                while ((linea = reader.readLine()) != null) {
                    if (linea.equals("ESTUDIANTES:")) {
                        while ((linea = reader.readLine()) != null && !linea.equals("ACTIVIDADES:")) {
                            String[] partes = linea.split(":");
                            String correo = partes[0];
                            String nombre = partes[1];
                            String contrasena = partes[2]; // Leer contraseña
                            String[] estadoYProgreso = partes[3].split(",");
                            Status estado = Status.valueOf(estadoYProgreso[0]);

                            float progresoEstudiante = Float.parseFloat(estadoYProgreso[1]);
                    
                            // Crear el estudiante con todos los datos
                            Estudiante estudiante = new Estudiante(nombre, correo, contrasena);
                            learningPath.estudiantesInscritos.add(estudiante);
                            learningPath.status.put(estudiante, estado);
                            learningPath.progreso.put(estudiante, progresoEstudiante);

                        }
                    } 
                    
                    if (linea.equals("ACTIVIDADES:")) {
                        while ((linea = reader.readLine()) != null && !linea.equals("COMPLETADAS_CON_DUP:")) {
                            System.out.println("actividades");
                            Actividad actividad = PersistenciaActividad.cargarActividad(linea, creador, formatter);
                            if (actividad != null) {
                                learningPath.listaActividades.add(actividad);
                            }
                        }
                    } 
                    
                    if (linea.equals("COMPLETADAS_CON_DUP:")) {
                        while ((linea = reader.readLine()) != null && !linea.equals("COMPLETADAS_SIN_DUP:")) {
                            System.out.println("completadascondup");
                            String[] partes = linea.split(":");
                            String correo = partes[0];
                            Estudiante estudiante = new Estudiante("", "", correo); // Crear solo con el correo
                    
                            if (partes.length > 1) {
                                System.out.println("partes mayora  1");
                                List<Actividad> actividades = Arrays.stream(partes[1].split(","))
                                    .map(desc -> PersistenciaActividad.cargarActividad(desc, creador, formatter))
                                    .collect(Collectors.toList());
                                learningPath.listaActividadesCompletadasConDup.put(estudiante, actividades);
                            }
                        }
                    }
                    
                    if (linea.equals("COMPLETADAS_SIN_DUP:")) {
                        while ((linea = reader.readLine()) != null) {
                            String[] partes = linea.split(":");
                            String correo = partes[0];
                            Estudiante estudiante = new Estudiante("", "", correo); // Crear solo con el correo
                    
                            if (partes.length > 1) {
                                List<Actividad> actividades = Arrays.stream(partes[1].split(","))
                                
                                    .map(desc -> PersistenciaActividad.cargarActividad(desc, creador, formatter))
                                    .distinct()
                                    .collect(Collectors.toList());
                                    System.out.println("completadas con dup");
                                learningPath.listaActividadesCompletadas.put(estudiante, actividades);
                            }
                        }
                    }
                    
                }
    
                return learningPath;
            }
        }
        return null;
    }
    

    
    // Método auxiliar para buscar estudiantes por correo en la lista de inscritos
    private static Estudiante buscarEstudiantePorCorreo(List<Estudiante> estudiantes, String correo) { 
        return estudiantes.stream() // Se utiliza stream para obtener un flujo de datos de la lista de estudiantes
                          .filter(e -> e.getCorreo().equals(correo)) // Se utiliza filter para filtrar los estudiantes por correo
                          .findFirst() // Se utiliza findFirst para obtener el primer estudiante que cumpla con el filtro
                          .orElse(null); // Se utiliza orElse para devolver null si no se encontró ningún estudiante
    }

}
    

