var doc;
doc = $(document);
doc.ready(iniciarWeb);

var nombre;
var apellidos;
var usuario;
var ciudad;
var estado;
var codigoP;
var color;
var cantidad;
var precioI;
var precioA;
var sw = true;
var swCompra = true;
var compradores = [];

$(".cantidad").change(function () {
    cantidad = $("#cantidad").val();
    cambiarPrecio(cantidad);
});
$("#enviar").click(function () {
    revisarForm();
});
$("#mostrarC").click(function () {
    mostrarCompras();
});
(function() {
    'use strict';
    window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
    form.addEventListener('submit', function(event) {
    if (form.checkValidity() === false) {
    event.preventDefault();
    event.stopPropagation();
    }
    form.classList.add('was-validated');
    }, false);
    });
    }, false);
    })();
function iniciarWeb() {
    nombre = $("#validationCustom01").val();
    apellidos = $("#validationCustom02").val();
    usuario = $("#validationCustomUsername").val();
    ciudad = $("#validationCustom03").val();
    estado = $("#validationCustom04").val();
    codigoP = $("#validationCustom05").val();
    color = $("#color").val();
    cantidad = $("#cantidad").val();
    precioA = $("#precio").text();
    if (sw) {
        rellenarSelect();
        precioI = precioA;
        sw = false;
    }


}
function cambiarPrecio(cantidad) {
    precioA = precioI * cantidad;
    $("#precio").text(precioA);
}

function rellenarSelect() {
    for (var i = 0; i < 100; i++) {
        $(".cantidad").append('<option>' + (i + 1) + '</option>');
    }
}

function mostrarCompras() {
    var lista = "";
    for (var i = 0; i < compradores.length; i++) {
        lista += "<li>" + compradores[i].comprador.nombre + " ha comprado "
            + compradores[i].cantidad + " " + compradores[i].modelo + " de color " + compradores[i].color +
            " por un precio total de " + compradores[i].precio + "$" + "</li>";
    }
    $(".compradores").html(lista);
}

function revisarForm() {
    swCompra=true;
    nombre = $("#validationCustom01").val();
    apellidos = $("#validationCustom02").val();
    usuario = $("#validationCustomUsername").val();
    ciudad = $("#validationCustom03").val();
    estado = $("#validationCustom04").val();
    codigoP = $("#validationCustom05").val();
    if (nombre.trim() == "") {
        alert("Introduce nombre");
        swCompra=false;
    }
    if (apellidos.trim() == "") {
        alert("introduce apellidos");
        swCompra=false;
    }
    if (usuario.trim() == "") {
        alert("Introduce tu nombre de usuario");
        swCompra=false;
    }
    if (ciudad.trim() == ""){
        alert("Introduce una ciudad");
        swCompra=false;
    }
    if(estado.trim() == ""){
        alert("Introduce un estado");
        swCompra=false;
    }
    if(codigoP.trim() == ""){
        alert("Introduce un codigo");
        swCompra=false;
    }
    if(swCompra){
    comprar();
    }
}

function comprar() {

    nombre = $("#validationCustom01").val();
    apellidos = $("#validationCustom02").val();
    usuario = $("#validationCustomUsername").val();
    ciudad = $("#validationCustom03").val();
    estado = $("#validationCustom04").val();
    codigoP = $("#validationCustom05").val();
    modelo = $("#modelo").text();
    color = $("#color").val();
    cantidad = $("#cantidad").val();
    precioA = $("#precio").text();

    var cliente = new comprador(nombre, apellidos, usuario, ciudad, estado, codigoP);
    var compra = new venta(cliente, color, cantidad, precioA, modelo);
    compradores.push(compra);
}
