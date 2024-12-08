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

