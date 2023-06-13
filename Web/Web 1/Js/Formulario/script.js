document.addEventListener('DOMContentLoaded', (event) => {

    document.getElementById("validarNombre").addEventListener("click", validarNombre);
    document.getElementById("validarNota").addEventListener("click", validarNota);
    document.getElementById("validarValoracion").addEventListener("click", validarValoracion);
    document.getElementById("validarEdad").addEventListener("click", validarEdad);

    function validarNombre() {
        var nombre = document.getElementById("nombre").value;
        var apellido1 = document.getElementById("apellido1").value;
        var apellido2 = document.getElementById("apellido2").value;
        document.getElementById("nombreCompleto").textContent = nombre + " " + apellido1 + " " + apellido2;
    }

    function validarNota() {
        var nota = document.getElementById("nota").value;
        var notaTexto;
        if (nota < 5) {
            notaTexto = "Insuficiente";
        } else if (nota < 6) {
            notaTexto = "Suficiente";
        } else if (nota < 7) {
            notaTexto = "Bien";
        } else if (nota < 9) {
            notaTexto = "Notable";
        } else {
            notaTexto = "Sobresaliente";
        }
        document.getElementById("notaTexto").textContent = notaTexto;
    }

    function validarValoracion() {
        var valoracion = document.getElementById("valoracion").value;
        var valoracionTexto = "";
        for (var i = 0; i < valoracion; i++) {
            valoracionTexto += "★";
        }
        document.getElementById("valoracionTexto").textContent = valoracionTexto;
    }

    function validarEdad() {
        var fechaNacimiento = new Date(document.getElementById("fechaNacimiento").value);
        var hoy = new Date();
        var edad = hoy.getFullYear() - fechaNacimiento.getFullYear();
        var m = hoy.getMonth() - fechaNacimiento.getMonth();
        if (m < 0 || (m === 0 && hoy.getDate() < fechaNacimiento.getDate())) {
            edad--;
        }
        document.getElementById("edad").textContent = edad + " años";
    }
});
