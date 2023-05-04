import requests

def main():
    while True:
        mostrar_menu()
        opcion = input('Selecciona una opcion en la wiki: ')
        if opcion == '1':
            mostrar_opciones('character')
            id_personaje = input('Introduce el ID del personaje que deseas conocer: ')
            obtener_personaje(id_personaje)
        elif opcion == '2':
            mostrar_opciones('episode')
            id_episodio = input('Introduce el ID del episodio que deseas conocer: ')
            obtener_episodio(id_episodio)
        elif opcion == '3':
            mostrar_opciones('location')
            id_ubicacion = input('Introduce el ID de la ubicacion que deseas conocer: ')
            obtener_ubicacion(id_ubicacion)
        elif opcion == '4':
            print('Hasta luego!!')
            break
        else:
            print('Opcion invalida. Por favor, selecciona una opcion del menu.')

def mostrar_menu():
    print('\nMenu:\n')
    print('1. Buscar personaje')
    print('2. Buscar episodio')
    print('3. Buscar ubicacion')
    print('4. Salir\n')

def obtener_personaje(id_personaje):
    url = f'https://rickandmortyapi.com/api/character/{id_personaje}'
    response = requests.get(url)

    if response.status_code == 200:
        personaje = response.json()
        print(f'Nombre del personaje: {personaje["name"]}')
        print(f'Especie: {personaje["species"]}')
        print(f'Genero: {personaje["gender"]}')
        print(f'Origen: {personaje["origin"]["name"]}')
    else:
        print(f'Error al obtener el personaje con ID {id_personaje}. Codigo de estado: {response.status_code}')

def obtener_episodio(id_episodio):
    url = f'https://rickandmortyapi.com/api/episode/{id_episodio}'
    response = requests.get(url)

    if response.status_code == 200:
        episodio = response.json()
        print(f'Nombre del episodio: {episodio["name"]}')
        print(f'Temporada: {episodio["episode"][:2]}')
        print(f'Numero de episodio: {episodio["episode"][3:]}')
        print(f'Fecha de emision: {episodio["air_date"]}')
    else:
        print(f'Error al obtener el episodio con ID {id_episodio}. Codigo de estado: {response.status_code}')

def obtener_ubicacion(id_ubicacion):
    url = f'https://rickandmortyapi.com/api/location/{id_ubicacion}'
    response = requests.get(url)

    if response.status_code == 200:
        ubicacion = response.json()
        print(f'Nombre de la ubicacion: {ubicacion["name"]}')
        print(f'Tipo: {ubicacion["type"]}')
        print(f'Dimension: {ubicacion["dimension"]}')
    else:
        print(f'Error al obtener la ubicacion con ID {id_ubicacion}. Codigo de estado: {response.status_code}')
    url = f'https://rickandmortyapi.com/api/location/{id_ubicacion}'
    response = requests.get(url)

    if response.status_code == 200:
        ubicacion = response.json()
        print(f'Nombre de la ubicación: {ubicacion["name"]}')
        print(f'Tipo: {ubicacion["type"]}')
        print(f'Dimensión: {ubicacion["dimension"]}')
    else:
        print(f'Error al obtener la ubicación con ID {id_ubicacion}. Código de estado: {response.status_code}')

def mostrar_opciones(tipo):
    url = f'https://rickandmortyapi.com/api/{tipo}'
    response = requests.get(url)

    if response.status_code == 200:
        resultado = response.json()
        print(f'\nIDs disponibles para {tipo}:')
        if tipo == 'episode':
            for elemento in resultado['results']:
                print(f'ID: {elemento["id"]}, Nombre: {elemento["name"]}, Episodio: {elemento["episode"]}')
        else:
            for elemento in resultado['results']:
                print(f'ID: {elemento["id"]}, Nombre: {elemento["name"]}')
        print()
    else:
        print(f'Error al obtener los IDs disponibles para {tipo}. Cdigo de estado: {response.status_code}')

if __name__ == '__main__':
    main()