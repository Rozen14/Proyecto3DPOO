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

import actividad.Actividad;

import actividad.Nivel;

import actividad.Status;

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
    protected Status status; // Status del Learning Path
    protected List<Actividad> listaActividades; // Lista de actividades del Learning Path
    protected Profesor creador; // Creador del Learning Path
    protected float rating; // Rating del Learning Path
    protected List<Estudiante> estudiantesInscritos; // Lista de estudiantes inscritos en el Learning Path
    protected float progreso; // Progreso del Learning Path
    protected Map<Estudiante, List<Actividad>> listaActividadesCompletadasConDup; // Lista de actividades completadas con duplicados
    protected Map<Estudiante, List<Actividad>> listaActividadesCompletadas; // Lista de actividades completadas sin duplicados
    

    public LearningPath (String titulo, Nivel nivelDificultad, String descripcion, String objetivos, int duracionMinutos, Profesor creador, float rating, List<Actividad> listaActividades){

        this.titulo=titulo; 
        this.nivelDificultad=nivelDificultad;
        this.descripcion=descripcion;
        this.objetivos= objetivos;
        this.duracionMinutos=duracionMinutos;
        this.fechaCreacion=LocalDateTime.now();
        this.fechaModificacion=null;
        this.version=1;
        this.status=Status.Incompleto;
        this.listaActividades = listaActividades;
        this.creador=creador;
        this.rating = rating;
        this.estudiantesInscritos = new ArrayList<>();
        this.progreso = 0;
        this.listaActividadesCompletadasConDup = new HashMap<>();
        this.listaActividadesCompletadas = new HashMap<>();
    }

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


    public Status getStatus() { // Obtener el status del Learning Path
        return status;
    }


    public void setStatus(Status status) { // Establecer el status del Learning Path
        this.status = status; 
    }

    public Profesor getCreador() { // Obtener el creador del Learning Path
        return creador; 
    }

    public List<Actividad> getListaActividades() { // Obtener la lista de actividades del Learning Path
        return listaActividades;
    }

    public void agregarActividad(Actividad actividad) { // Agregar una actividad a la lista de actividades del Learning Path
        if (listaActividades.contains(actividad)) {
            throw new IllegalArgumentException("La actividad ya está en la lista de actividades del Learning Path.");
        } else {
            listaActividades.add(actividad); // Agregar la actividad a la lista de actividades
            this.fechaModificacion=LocalDateTime.now(); // Actualizar la fecha de modificacion
            setVersion(); // Incrementar la versión del Learning Path
        }
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

        // Agregar la actividad a la lista con duplicados
        listaActividadesCompletadasConDup.get(estudiante).add(actividad); 

        // Agregar la actividad a la lista sin duplicados solo si aún no está presente
        if (!listaActividadesCompletadas.get(estudiante).contains(actividad)) {
            listaActividadesCompletadas.get(estudiante).add(actividad);
        }
    }

    // Método para calcular el progreso de un estudiante
    public float calcularProgreso(Estudiante estudiante) {
        // Contar todas las actividades obligatorias en el Learning Path
        int totalObligatorias = (int) listaActividades.stream() // Se utiliza stream para obtener un flujo de datos de la lista de actividades
            .filter(Actividad::esObligatoria) // Se utiliza filter para filtrar las actividades que son obligatorias
            .count(); // Se utiliza count para contar cuántas actividades obligatorias hay

        // Verificar que existen actividades obligatorias para evitar divisiones por cero
        if (totalObligatorias == 0) {
            System.out.println("No hay actividades obligatorias en el Learning Path.");
            return 0;
        }

        // Obtener la lista de actividades completadas por el estudiante
        List<Actividad> actividadesCompletadasPorEstudiante = listaActividadesCompletadas.get(estudiante);

        // Contar cuántas actividades obligatorias ha completado exitosamente el estudiante
        int completadasObligatorias = (int) actividadesCompletadasPorEstudiante.stream()
            .filter(a -> a.esObligatoria() && a.esExitosa(estudiante)) // Se utiliza filter para filtrar las actividades obligatorias que han sido completadas exitosamente
            .count(); // Se utiliza count para contar cuántas actividades obligatorias han sido completadas exitosamente
            System.out.println("Actividades obligatorias completadas por el estudiante: " + completadasObligatorias);

        // Calcular el progreso como un porcentaje
        float progreso = (float) completadasObligatorias / totalObligatorias * 100;

        // Si el progreso alcanza el 100%, marcar todas las actividades del Learning Path como completadas para el estudiante
        if (progreso == 100) {
            listaActividades.forEach(a -> a.setStatusParaEstudiante(estudiante, Status.Completado));
        } else {
            System.out.println("El progreso no ha alcanzado el 100% para el estudiante: " + estudiante.getNombre());
        }

        System.out.println("Progreso calculado para " + estudiante.getNombre() + ": " + progreso + "%");
        return progreso;
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
    public void guardarEnArchivo(File archivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) { // BufferedWriter se utiliza para escribir texto en un archivo, FileWriter se utiliza para escribir caracteres en un archivo, y append = true para agregar texto al final del archivo
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
                         this.status + "," +
                         this.rating + "," +
                         this.creador.getNombre() + "," +
                         this.progreso);
            writer.newLine();
    
            writer.write("ACTIVIDADES:"); // Etiqueta para indicar que se guardará la lista de actividades
            writer.newLine();
    
            // Guardar cada actividad en la lista de actividades del LearningPath
            for (Actividad actividad : listaActividades) {
                PersistenciaActividad.guardarActividad(actividad, writer);
            }
    
            // Guardar lista de actividades completadas con duplicados para cada estudiante
            writer.write("COMPLETADAS_CON_DUP:");
            writer.newLine();
            for (Map.Entry<Estudiante, List<Actividad>> entry : listaActividadesCompletadasConDup.entrySet()) { // Se utiliza entrySet para obtener un conjunto de pares clave-valor, en este caso se obtiene un conjunto de estudiantes y actividades completadas con duplicados
                Estudiante estudiante = entry.getKey(); // Se obtiene la clave del par
                writer.write(estudiante.getCorreo() + ":"); // Identificación del estudiante
                for (Actividad actividad : entry.getValue()) { // Se obtiene el valor del par
                    PersistenciaActividad.guardarActividad(actividad, writer); // Se guardan las actividades completadas con duplicados
                }
                writer.newLine(); // Nueva línea entre estudiantes
            }
    
            // Guardar lista de actividades completadas sin duplicados para cada estudiante
            writer.write("COMPLETADAS_SIN_DUP:"); // Etiqueta para indicar que se guardará la lista de actividades completadas sin duplicados
            writer.newLine();
            for (Map.Entry<Estudiante, List<Actividad>> entry : listaActividadesCompletadas.entrySet()) { // Se utiliza entrySet para obtener un conjunto de pares clave-valor, en este caso se obtiene un conjunto de estudiantes y actividades completadas sin duplicados
                Estudiante estudiante = entry.getKey(); // Se obtiene la clave del par
                writer.write(estudiante.getCorreo() + ":"); // Identificación del estudiante
                for (Actividad actividad : entry.getValue()) { // Se obtiene el valor del par
                    PersistenciaActividad.guardarActividad(actividad, writer); // Se guardan las actividades completadas sin duplicados
                }
                writer.newLine(); // Nueva línea entre estudiantes
            }
        }
    }

    // Método principal para cargar el LearningPath
    public static LearningPath cargarDeArchivo(File archivo, Profesor creador) throws IOException { 
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) { // BufferedReader se utiliza para leer texto de un archivo, FileReader se utiliza para leer caracteres de un archivo
            String linea = reader.readLine(); // Leer la primera línea del archivo
            if (linea != null) { // Verificar si la línea no está vacía
                String[] datos = linea.split(","); // Separar los datos por comas
                
                // Leer atributos en el mismo orden en que se guardaron
                String titulo = datos[0]; 
                Nivel nivelDificultad = Nivel.valueOf(datos[1]);
                String descripcion = datos[2];
                String objetivos = datos[3];
                int duracionMinutos = Integer.parseInt(datos[4]);
                LocalDateTime fechaCreacion = LocalDateTime.parse(datos[5], formatter); // Convertir la fecha de creación al formato correcto
                LocalDateTime fechaModificacion = datos[6].isEmpty() ? null : LocalDateTime.parse(datos[6], formatter); // Convertir la fecha de modificación al formato correcto
                int version = Integer.parseInt(datos[7]); 
                Status status = Status.valueOf(datos[8]);
                float rating = Float.parseFloat(datos[9]);
                String nombreCreador = datos[10]; // Nombre del creador, este no se usa en si solo lo dejamos para tener la referencia
                float progreso = Float.parseFloat(datos[11]);
    
                // Crear el LearningPath con los valores cargados
                LearningPath learningPath = new LearningPath(titulo, nivelDificultad, descripcion, objetivos, duracionMinutos, creador, rating, new ArrayList<>());
                
                // Establecer los atributos cargados que no se pasan al constructor
                learningPath.fechaCreacion = fechaCreacion;
                learningPath.fechaModificacion = fechaModificacion;
                learningPath.version = version;
                learningPath.status = status;
                learningPath.progreso = progreso;
    
                System.out.println("Learning Path cargado correctamente. Título: " + titulo); // Mensaje de éxito
    
                // Leer y cargar las listas adicionales
                while ((linea = reader.readLine()) != null) { // Leer las siguientes líneas del archivo
                    if (linea.equals("ACTIVIDADES:")) { // Verificar si se llegó a la lista de actividades
                        System.out.println("Cargando ACTIVIDADES..."); // Mensaje de éxito
                        while ((linea = reader.readLine()) != null && !linea.equals("COMPLETADAS_CON_DUP:")) { // Leer las actividades hasta llegar a la siguiente sección
                            Actividad actividad = PersistenciaActividad.cargarActividad(linea, creador, formatter); // Cargar la actividad
                            if (actividad != null) { // Verificar si la actividad se cargó correctamente
                                learningPath.listaActividades.add(actividad); // Agregar la actividad a la lista de actividades
                                System.out.println("Actividad cargada: " + actividad.getDescripcion()); // Mensaje de éxito
                            }
                        }
                    } else if (linea.equals("COMPLETADAS_CON_DUP:")) { // Verificar si se llegó a la lista de actividades completadas con duplicados
                        System.out.println("Cargando COMPLETADAS_CON_DUP..."); // Mensaje de éxito
                        while ((linea = reader.readLine()) != null && !linea.equals("COMPLETADAS_SIN_DUP:")) { // Leer las actividades completadas con duplicados hasta llegar a la siguiente sección
                            String[] partes = linea.split(":"); // Separar los datos por dos puntos
                            String correoEstudiante = partes[0]; // Identificador del estudiante
                            Estudiante estudiante = buscarEstudiantePorCorreo(learningPath.getEstudiantesInscritos(), correoEstudiante);  // Buscar el estudiante por correo, esto se hace para verificar si el estudiante se encuentra en la lista de inscritos y el identificador decidimos es su correo
                            
                            if (estudiante != null && partes.length > 1) { // Verificar si el estudiante se encontró y si hay actividades completadas
                                List<Actividad> actividades = Arrays.stream(partes[1].split(",")) // Convertir las descripciones de actividades a una lista
                                    .map(desc -> PersistenciaActividad.cargarActividad(desc, creador, formatter)) // Mapear las descripciones a actividades, esto significa que se convierten las descripciones de las actividades a objetos de tipo Actividad
                                    .collect(Collectors.toList()); // Convertir el flujo de datos a una lista, esa vaina se hace porque ya lo queremos en forma de lista y Collectors es una clase que proporciona operaciones para secuencias de elementos y deja hacer eso
                                learningPath.listaActividadesCompletadasConDup.put(estudiante, actividades);  // Agregar las actividades completadas con duplicados al mapa
                                System.out.println("Actividades completadas (con duplicados) cargadas para: " + estudiante.getNombre()); // Mensaje de éxito
                            }
                        }
                    } else if (linea.equals("COMPLETADAS_SIN_DUP:")) { // Verificar si se llegó a la lista de actividades completadas sin duplicados
                        System.out.println("Cargando COMPLETADAS_SIN_DUP..."); // Mensaje de éxito
                        while ((linea = reader.readLine()) != null) { // Leer las actividades completadas sin duplicados
                            String[] partes = linea.split(":"); // Separar los datos por dos puntos
                            String correoEstudiante = partes[0]; // Identificador del estudiante
                            Estudiante estudiante = buscarEstudiantePorCorreo(learningPath.getEstudiantesInscritos(), correoEstudiante); // Buscar el estudiante por correo
                            
                            if (estudiante != null && partes.length > 1) { // Verificar si el estudiante se encontró y si hay actividades completadas
                                List<Actividad> actividades = Arrays.stream(partes[1].split(",")) // Convertir las descripciones de actividades a una lista
                                    .map(desc -> PersistenciaActividad.cargarActividad(desc, creador, formatter)) // Mapear las descripciones a actividades
                                    .distinct() // Eliminar duplicados 
                                    .collect(Collectors.toList()); // Convertir el flujo de datos a una lista
                                learningPath.listaActividadesCompletadas.put(estudiante, actividades); // Agregar las actividades completadas sin duplicados al mapa
                                System.out.println("Actividades completadas (sin duplicados) cargadas para: " + estudiante.getNombre()); // Mensaje de éxito
                            }
                        }
                    }
                }
    
                return learningPath; // Devolver el LearningPath cargado
            }
        } 
        return null; // Devolver null si no se pudo cargar el LearningPath
    }
    
    // Método auxiliar para buscar estudiantes por correo en la lista de inscritos
    private static Estudiante buscarEstudiantePorCorreo(List<Estudiante> estudiantes, String correo) { 
        return estudiantes.stream() // Se utiliza stream para obtener un flujo de datos de la lista de estudiantes
                          .filter(e -> e.getCorreo().equals(correo)) // Se utiliza filter para filtrar los estudiantes por correo
                          .findFirst() // Se utiliza findFirst para obtener el primer estudiante que cumpla con el filtro
                          .orElse(null); // Se utiliza orElse para devolver null si no se encontró ningún estudiante
    }

}
    

