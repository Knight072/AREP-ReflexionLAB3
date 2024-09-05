document.getElementById('userForm').onsubmit = function(event) {
    event.preventDefault(); // Prevenir la recarga de la página

    const nombre = document.getElementById('nombre').value;
    const edad = parseInt(document.getElementById('edad').value);
    const email = document.getElementById('email').value;

    const usuario = { nombre: nombre, edad: edad, email: email };

    crearUsuario(usuario);
};

function crearUsuario(usuario) {
    fetch('/crearUsuario', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Usuario creado:', data);
        agregarUsuarioALista(data);
    })
    .catch(error => {
        console.error('Error al crear el usuario:', error.message);
    });
}

function agregarUsuarioALista(usuario) {
    const usuariosList = document.getElementById('usuariosList');
    const li = document.createElement('li');
    li.textContent = `Nombre: ${usuario.nombre}, Edad: ${usuario.edad}, Email: ${usuario.email}`;
    usuariosList.appendChild(li);
}

function fetchUsuarios() {
    fetch('/App/usuarios')
        .then(response => response.json())
        .then(data => {
            const usuariosList = document.getElementById('usuariosList');
            usuariosList.innerHTML = '';

            data.usuarios.forEach(usuario => {
                const li = document.createElement('li');
                li.textContent = `Nombre: ${usuario.nombre}, Edad: ${usuario.edad}, Email: ${usuario.email}`;
                usuariosList.appendChild(li);
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));
}

function mostrarArchivo(event) {
    event.preventDefault();
    const archivo = document.getElementById('archivo').value;

    fetch(`/App/leerArchivos?nombre=${archivo}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Archivo no encontrado');
            }
            return response.text();
        })
        .then(data => {
            document.getElementById('contenidoArchivo').textContent = data;
        })
        .catch(error => {
            document.getElementById('contenidoArchivo').textContent = 'Error: ' + error.message;
        });
}

