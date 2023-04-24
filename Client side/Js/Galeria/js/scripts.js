document.addEventListener('DOMContentLoaded', () => {
    const exposicionesSelect = document.getElementById('exposiciones');
    const galeria = document.getElementById('galeria');
    const formulario = document.getElementById('formulario');

    const obras = {
        exposicion1: [
            { src: 'imagenes/exposicion1/obra1.jpg', nombre: 'Obra 1' },
            { src: 'imagenes/exposicion1/obra2.jpg', nombre: 'Obra 2' },
        ],
        exposicion2: [
            { src: 'imagenes/exposicion2/obra1.jpg', nombre: 'Obra 1' },
            { src: 'imagenes/exposicion2/obra2.jpg', nombre: 'Obra 2' },
        ],
        exposicion3: [
            { src: 'imagenes/exposicion3/obra1.jpg', nombre: 'Obra 1' },
            { src: 'imagenes/exposicion3/obra2.jpg', nombre: 'Obra 2' },
        ],
    };

    exposicionesSelect.addEventListener('change', () => {
        const exposicion = obras[exposicionesSelect.value];
        galeria.innerHTML = '';

        exposicion.forEach(obra => {
            const imagen = document.createElement('figure');
            imagen.classList.add('galeria-imagen');
            imagen.innerHTML = `
                <img src="${obra.src}" alt="${obra.nombre}">
                <figcaption>${obra.nombre}</figcaption>
            `;
            galeria.appendChild(imagen);
        });
    });

    formulario.addEventListener('submit', (e) => {
        e.preventDefault();
        const nombre = formulario.nombre.value;
        const exposicionFavorita = formulario.exposicionFavorita.value;
        const enlace = `${nombre}-${exposicionFavorita}-entradas.pdf`;

        alert(`Gracias por tu compra, ${nombre}! Descarga tus entradas aquí: ${enlace}`);
    });
});