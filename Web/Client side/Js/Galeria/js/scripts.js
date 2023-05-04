document.addEventListener('DOMContentLoaded', () => {
    const exposicionesSelect = document.getElementById('exposiciones');
    const galeria = document.getElementById('galeria');
    const formulario = document.getElementById('formulario');

    // Guardamos las referencias de las imagenes
    const obras = {
        exposicion1: [
            { src: 'imagenes/exposicion1/obra1.jpg', nombre: 'Obra 1' },
            { src: 'imagenes/exposicion1/obra2.jpg', nombre: 'Obra 2' },
            { src: 'imagenes/exposicion1/obra3.jpg', nombre: 'Obra 3' },
            { src: 'imagenes/exposicion1/obra4.jpg', nombre: 'Obra 4' },
            { src: 'imagenes/exposicion1/obra5.jpg', nombre: 'Obra 5' },
        ],
        exposicion2: [
            { src: 'imagenes/exposicion2/obra1.jpg', nombre: 'Obra1' },
            { src: 'imagenes/exposicion2/obra2.jpg', nombre: 'Obra 2' },
            { src: 'imagenes/exposicion2/obra3.jpg', nombre: 'Obra 3' },
            { src: 'imagenes/exposicion2/obra4.jpg', nombre: 'Obra 4' },
            { src: 'imagenes/exposicion2/obra5.jpg', nombre: 'Obra 5' },

            ],
            exposicion3: [
            { src: 'imagenes/exposicion3/obra1.jpg', nombre: 'Obra 1' },
            { src: 'imagenes/exposicion3/obra2.jpg', nombre: 'Obra 2' },
            { src: 'imagenes/exposicion3/obra3.jpg', nombre: 'Obra 3' },
            { src: 'imagenes/exposicion3/obra4.jpg', nombre: 'Obra 4' },
            { src: 'imagenes/exposicion3/obra5.jpg', nombre: 'Obra 5' },

         ],
     };

     // Funcion de creacion del carrousel
     exposicionesSelect.addEventListener('change', () => {
        const exposicion = obras[exposicionesSelect.value];
        galeria.innerHTML = '';
    
        // Crear carrusel con la clase carrusel
        const carrusel = document.createElement('div');
        carrusel.classList.add('carrusel');
        
        //Por cada obra se crea una imagen
        exposicion.forEach(obra => {
            const imagen = document.createElement('figure');
            imagen.classList.add('galeria-imagen');
            imagen.innerHTML = `
                <img src="${obra.src}" alt="${obra.nombre}">
                <figcaption>${obra.nombre}</figcaption>
            `;
            carrusel.appendChild(imagen);
            
            //Cada imagen se le pone un listener para que se pueda crear un popup para cada imagen
            imagen.addEventListener('click', () => {
                const popUp = crearPopUp(obra.src);
                document.body.appendChild(popUp);
                popUp.style.display = 'flex';
            });
        });
    
        galeria.appendChild(carrusel);
    });
    
    // Funcion para crear el popup cuando se selecciona una imagen
    function crearPopUp(src) {
        const popUp = document.createElement('div');
        popUp.classList.add('popup');
        popUp.innerHTML = `
            <img class="popup-img" src="${src}" alt="">
        `;
        popUp.addEventListener('click', () => {
            popUp.remove();
        });
    
        return popUp;
    }
    
    //Se envia a otra pagina cuando se pulsa el boton
    formulario.addEventListener('submit', (e) => {
        e.preventDefault();
        window.location.href = "compra.html";
    });
});
