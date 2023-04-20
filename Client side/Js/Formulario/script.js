function validacion() {
    if (validaTexto(document.getElementById("apellido1").value ) ) {
        var error = document.getElementById("errorapellido1");
        error.parentNode.removeChild(error);
        var label = document.getElementById("apellido1").value;
        var resultado = document.getElementById("apellido1f");
        resultado.value = label;
    }
    if (validaTexto(document.getElementById("apellido2").value )) {
        var error = document.getElementById("errorapellido2");
        error.parentNode.removeChild(error);
        var label= document.getElementById("apellido2").value;
        var resultado = document.getElementById("apellido2f");
        resultado.value = label;
    }
    if (validaTexto(document.getElementById("nombre").value )){
        var error = document.getElementById("errornombre");
        error.parentNode.removeChild(error);
        var label = document.getElementById("nombre").value;
        var resultado = document.getElementById("nombref");
        resultado.value = label;
    }
    if (validaNum(document.getElementById("nota").value )){
        var error = document.getElementById("errornota");
        error.parentNode.removeChild(error);
        var label = document.getElementById("nota").value;
        var resultado = document.getElementById("notaf");
        resultado.value = label;
    }
    if (validaNum(document.getElementById("edad").value )){
        var error = document.getElementById("erroredad");
        error.parentNode.removeChild(error);
        var label = document.getElementById("edad").value;
        var resultado = document.getElementById("edadf");
        resultado.value = label;
    }
    if(validaLista(document.getElementById("valoracion").selectedIndex)){
        var error = document.getElementById("errorvaloracion");
        error.parentNode.removeChild(error);
        var label = document.getElementById("valoracion").value;
        var resultado = document.getElementById("valoracionf");
        resultado.value = label;
    }
}


function validaTexto(valor){
if( valor == null || valor.length == 0 || /^\s+$/.test(valor) ) {
    return false;
  }
  else return true;
  } 
function validaNum(valor){
    if( isNaN(valor) ) {
        return false;
      }else return true;
}


function validaLista(indice){
  if( indice == null || indice == 0 ) {
    return false;
  } else return true;
}
