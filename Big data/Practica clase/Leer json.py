import json

def main():
    archivo_json()

def archivo_json():
    with open("Practica clase\\fichero.json") as file:
        datos = json.load(file)

    with open("Practica clase\\data.json","w") as file:
        json.dump(datos["direcciones"],file,indent=4)

def json_test():
    json_data = '''
    {
    "nombre": "Ejemplo",
    "edad": 25,
    "direcciones": [
        {
        "calle": "Calle Principal",
        "ciudad": "Ciudad Ejemplo",
        "codigo_postal": "12345"
        },
        {
        "calle": "Calle Secundaria",
        "ciudad": "Otra Ciudad",
        "codigo_postal": "67890"
        }
    ]
    }
    '''
    data = json.loads(json_data)

    # Accediendo a los campos
    nombre = data['nombre']
    edad = data['edad']
    direcciones = data['direcciones']

    # Imprimiendo los valores
    print("Nombre:", nombre)
    print("Edad:", edad)
    print("Direcciones:")
    for direccion in direcciones:
        calle = direccion['calle']
        ciudad = direccion['ciudad']
        codigo_postal = direccion['codigo_postal']
        print("  Calle:", calle)
        print("  Ciudad:", ciudad)
        print("  Código Postal:", codigo_postal)
        print()

if __name__ == "__main__":
    main()