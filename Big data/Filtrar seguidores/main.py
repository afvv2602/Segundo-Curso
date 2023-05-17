import json

def main():
    with open('Filtrar seguidores\\seguidores.json', 'r') as file:
        # Abrimos el archivo y cargamos toda la informacion en data
        data = json.load(file)

    # Creamos una lista para guardar los valores que necesitamos 
    datos_filtrados = []

    for usuario in data:
        usuario_filtrado = {
            'user': usuario['screen_name'],
            'name': usuario['name'],
            'statuses count': usuario['statuses_count']
        }
        datos_filtrados.append(usuario_filtrado)
    with open('Filtrar seguidores\\seguidores_filtrados.json', 'w') as file:
        json.dump(datos_filtrados, file, ensure_ascii=False, indent=4)

if __name__ == "__main__":
    main()