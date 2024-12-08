// Simulación de Base de Datos
const usuarios = [];
const learningPaths = [];

let usuarioActual = null;

// Clases
class Usuario {
    constructor(nombre, contrasena, tipo, correo) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.correo = correo;
    }
}

class Profesor extends Usuario {
    constructor(nombre, contrasena, correo) {
        super(nombre, contrasena, "profesor", correo);
        this.learningPathsCreados = [];
    }

    crearLearningPath(titulo, descripcion, nivel, objetivos, duracion) {
        const nuevoLP = new LearningPath(titulo, descripcion, nivel, objetivos, duracion, this);
        this.learningPathsCreados.push(nuevoLP);
        learningPaths.push(nuevoLP); // Agregar a la lista global
    }
}


class Estudiante extends Usuario {
    constructor(nombre, contrasena, correo) {
        super(nombre, contrasena, "estudiante", correo);
    }
}

class LearningPath {
    constructor(titulo, descripcion, nivel, objetivos, duracion, creador) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.objetivos = objetivos;
        this.duracion = duracion;
        this.creador = creador;
        this.actividades = []; // Lista de actividades
        this.estudiantes = []; // Lista de estudiantes inscritos
    }

    // Agregar estudiante
    inscribirEstudiante(estudiante) {
        if (!this.estudiantes.includes(estudiante)) {
            this.estudiantes.push(estudiante);
        } else {
            throw new Error("El estudiante ya está inscrito en este Learning Path.");
        }
    }

    // Mostrar detalles del Learning Path
    obtenerDetalles() {
        return `
            Título: ${this.titulo}
            Descripción: ${this.descripcion}
            Nivel: ${this.nivel}
            Objetivos: ${this.objetivos}
            Duración: ${this.duracion} minutos
            Actividades: ${this.actividades.length}
            Estudiantes inscritos: ${this.estudiantes.length}
        `;
    }
}


// Manejo de Login y Registro
document.getElementById("loginForm").addEventListener("submit", (e) => {
    e.preventDefault();
    const correo = document.getElementById("loginEmail").value;
    const contrasena = document.getElementById("loginPassword").value;

    const usuario = usuarios.find((user) => user.correo === correo && user.contrasena === contrasena);
    if (usuario) {
        usuarioActual = usuario;
        mostrarVista(usuario.tipo);
    } else {
        alert("Credenciales incorrectas.");
    }
});

document.getElementById("registerForm").addEventListener("submit", (e) => {
    e.preventDefault();
    const nombre = document.getElementById("registerName").value;
    const correo = document.getElementById("registerEmail").value;
    const contrasena = document.getElementById("registerPassword").value;
    const rol = document.getElementById("registerRole").value;

    if (usuarios.find((user) => user.correo === correo)) {
        alert("El correo ya está registrado.");
        return;
    }

    const nuevoUsuario = rol === "profesor"
        ? new Profesor(nombre, contrasena, correo)
        : new Estudiante(nombre, contrasena, correo);

    usuarios.push(nuevoUsuario);
    alert("Registro exitoso. Ahora puedes iniciar sesión.");
    mostrarLogin();
});

// Funciones de Vista
function mostrarVista(rol) {
    document.getElementById("authContainer").classList.add("hidden");
    document.getElementById("roleContainer").classList.remove("hidden");

    if (rol === "profesor") {
        document.getElementById("profesorView").classList.remove("hidden");
    } else if (rol === "estudiante") {
        document.getElementById("estudianteView").classList.remove("hidden");
    }
}

document.getElementById("viewLearningPaths").addEventListener("click", () => {
    const studentPathsList = document.getElementById("studentPathsList");
    studentPathsList.innerHTML = ""; // Limpiar lista

    if (learningPaths.length === 0) {
        studentPathsList.innerHTML = "<p>No hay Learning Paths disponibles.</p>";
        return;
    }

    // Mostrar Learning Paths disponibles
    learningPaths.forEach((lp, index) => {
        const lpElement = document.createElement("div");
        lpElement.innerHTML = `
            <h3>${lp.titulo}</h3>
            <p>${lp.descripcion}</p>
            <p><strong>Nivel:</strong> ${lp.nivel}</p>
            <p><strong>Duración:</strong> ${lp.duracion} minutos</p>
            <button onclick="inscribirseEnLearningPath(${index})">Inscribirse</button>
        `;
        studentPathsList.appendChild(lpElement);
    });
});

// Inscribir estudiante en un Learning Path
function inscribirseEnLearningPath(index) {
    const selectedLP = learningPaths[index];
    try {
        selectedLP.inscribirEstudiante(usuarioActual);
        alert(`Te has inscrito en el Learning Path "${selectedLP.titulo}".`);
    } catch (error) {
        alert(error.message);
    }
}


function mostrarLogin() {
    document.getElementById("authContainer").classList.remove("hidden");
    document.getElementById("roleContainer").classList.add("hidden");
    document.getElementById("profesorView").classList.add("hidden");
    document.getElementById("estudianteView").classList.add("hidden");
}

document.getElementById("switchToRegister").addEventListener("click", () => {
    document.getElementById("loginView").classList.add("hidden");
    document.getElementById("registerView").classList.remove("hidden");
});

document.getElementById("switchToLogin").addEventListener("click", () => {
    document.getElementById("registerView").classList.add("hidden");
    document.getElementById("loginView").classList.remove("hidden");
});

document.querySelectorAll("#logout").forEach((button) => {
    button.addEventListener("click", () => {
        usuarioActual = null;
        mostrarLogin();
    });
});

// Crear Learning Path (Profesor)
document.getElementById("createLearningPath").addEventListener("click", () => {
    const titulo = prompt("Título del Learning Path:");
    const descripcion = prompt("Descripción del Learning Path:");
    const nivel = prompt("Nivel del Learning Path (Básico, Intermedio, Avanzado):");
    const objetivos = prompt("Objetivos del Learning Path:");
    const duracion = parseInt(prompt("Duración estimada (en minutos):"));

    if (!titulo || !descripcion || !nivel || !objetivos || isNaN(duracion)) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    // Crear y asociar el Learning Path con el profesor actual
    usuarioActual.crearLearningPath(titulo, descripcion, nivel, objetivos, duracion);

    alert(`Learning Path "${titulo}" creado exitosamente.`);


});

// Manejar la selección del tipo de actividad
document.getElementById("tipoActividad").addEventListener("change", (e) => {
    const tipo = e.target.value;
    document.querySelectorAll(".activity-fields").forEach((field) => (field.style.display = "none"));
    if (tipo === "QUIZ") {
        document.getElementById("quiz-fields").style.display = "block";
    } else if (tipo === "ENCUESTA") {
        document.getElementById("encuesta-fields").style.display = "block";
    } else if (tipo === "RECURSO_EDUCATIVO") {
        document.getElementById("recurso-fields").style.display = "block";
    }
});

// Manejar la adición de preguntas para Quiz
function addQuizQuestion() {
    const container = document.getElementById("quiz-questions");
    const questionHTML = `
        <div class="quiz-question">
            <input type="text" placeholder="Enunciado de la pregunta" required />
            <input type="text" placeholder="Opción A" required />
            <input type="text" placeholder="Opción B" required />
            <input type="text" placeholder="Opción C" required />
            <input type="text" placeholder="Opción D" required />
            <select>
                <option value="" disabled selected>Respuesta correcta</option>
                <option value="A">A</option>
                <option value="B">B</option>
                <option value="C">C</option>
                <option value="D">D</option>
            </select>
        </div>`;
    container.insertAdjacentHTML("beforeend", questionHTML);
}

// Manejar la adición de preguntas abiertas para Encuesta
function addEncuestaQuestion() {
    const container = document.getElementById("encuesta-questions");
    const questionHTML = `
        <div class="encuesta-question">
            <input type="text" placeholder="Enunciado de la pregunta abierta" required />
        </div>`;
    container.insertAdjacentHTML("beforeend", questionHTML);
}

// Manejar el guardado de actividades
document.getElementById("create-activity-form").addEventListener("submit", (e) => {
    e.preventDefault();

    const nombre = document.getElementById("nombreActividad").value;
    const descripcion = document.getElementById("descripcionActividad").value;
    const tipo = document.getElementById("tipoActividad").value;
    const duracion = parseInt(document.getElementById("duracionActividad").value);

    let actividad;

    if (tipo === "QUIZ") {
        const preguntas = Array.from(document.querySelectorAll("#quiz-questions .quiz-question")).map((q) => ({
            enunciado: q.querySelector("input:nth-child(1)").value,
            opciones: [
                q.querySelector("input:nth-child(2)").value,
                q.querySelector("input:nth-child(3)").value,
                q.querySelector("input:nth-child(4)").value,
                q.querySelector("input:nth-child(5)").value,
            ],
            respuesta: q.querySelector("select").value,
        }));
        actividad = { nombre, descripcion, tipo, duracion, preguntas };
    } else if (tipo === "ENCUESTA") {
        const preguntasAbiertas = Array.from(
            document.querySelectorAll("#encuesta-questions .encuesta-question input")
        ).map((q) => q.value);
        actividad = { nombre, descripcion, tipo, duracion, preguntasAbiertas };
    } else if (tipo === "RECURSO_EDUCATIVO") {
        const tipoRecurso = document.getElementById("tipoRecurso").value;
        actividad = { nombre, descripcion, tipo, duracion, tipoRecurso };
    } else {
        actividad = { nombre, descripcion, tipo, duracion };
    }

    console.log("Nueva actividad creada:", actividad);
    alert(`Actividad "${nombre}" creada exitosamente.`);
    document.getElementById("create-activity-form").reset();
    document.querySelectorAll(".activity-fields").forEach((field) => (field.style.display = "none"));
});

