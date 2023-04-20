class Persona {
constructor(nombre,edad,DNI,sexo,peso,altura){
    this.nombre = nombre;
    this.edad = edad;
    this.DNI = DNI;
    this.sexo = comprobarSexo(sexo);
    this.peso = peso;
    this.altura = altura;
}
calcularIMC(peso,altura){
    var calculado = peso / (altura/100)^2;
    if (calculado < 20){
        return -1;
    }else if (calculado >20 && calculado <25 ){
        return 0;
    }else if (calculado >25){
        return 1;
    }
}
esMayorDeEdad(){
    if (this.edad > 17){
        return true;
    } else {
        return false;
    }
}

toString(){
    return "Bienvenido "+this.nombre +" con edad de "+this.edad+" DNI "+this.DNI+" sexo "+this.sexo;
}
}
function comprobarSexo(sexo){
    if (sexo == "H"){
        return sexo;
    }else if(sexo == "M"){
        return sexo;
    }else {
        sexo = "H";
        return sexo;
    }
}