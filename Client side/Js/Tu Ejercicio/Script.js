var Personitas = Array();
var IMCs= Array();
var SWs = Array();
var IMC;
var sw;

window.onload = function(){
    document.getElementById("enviar").onclick = crearP;
    document.getElementById("mostrar").onclick = mostrarP;
}
function crearP(){
    var nombre = prompt("Introduce tu nombre");
    var edad = parseInt(prompt("Introduce tu edad"));
    var DNI = prompt("Introduce tu DNI ");
    var sexo = prompt("Introduce tu sexo ('H' o 'M')");
    var peso = parseInt(prompt("Introduce tu peso"));
    var altura= parseInt(prompt("Introduce tu altura en cm"));
    var PS= new Persona(nombre,edad,DNI,sexo,peso,altura);
    IMC = PS.calcularIMC(peso,altura);
    sw = PS.esMayorDeEdad();
    IMCs.push(IMC);
    SWs.push(sw);
    Personitas.push(PS);
}
function mostrarP(){
    for (var i = 0 ; i<Personitas.length ;i++){
        document.getElementById("resultados").innerHTML+='<tbody><td>'+Personitas[i].nombre+'</td><td>'+Personitas[i].edad+'</td><td>'+Personitas[i].DNI+'</td><td>'
        +Personitas[i].sexo+'</td><td>'+Personitas[i].peso+'</td><td>'+Personitas[i].altura+'</td><td>'+IMCs[i]+'</td><td>'+SWs[i]+'</td></tbody>';
    }
    
}