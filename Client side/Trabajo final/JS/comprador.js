function comprador (nombre,apellidos,usuario,ciudad,estado,codigoP){
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.usuario = usuario;
    this.ciudad = ciudad;
    this.estado = estado;
    this.codigoP = codigoP;
}

function venta(comprador, color, cantidad, precio,modelo) {
    this.comprador = comprador;
    this.color = color;
    this.cantidad = cantidad;
    this.precio = precio;
    if (cantidad > 1){
        this.modelo = modelo+"s";
    }else{
        this.modelo = modelo;
    }
    
}