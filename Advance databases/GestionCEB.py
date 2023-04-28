from pymongo import MongoClient
from datetime import datetime

#Conexiones con la base de datos
client = MongoClient('mongodb://localhost:27017/')
db = client['ceb']
alumnos = db['alumnos']
modulos = db['modulos']

def main():
    while True:
        menu()
        opcion = int(input("\nIngrese el numero de la opcion deseada: "))
        if opcion == 1:
            listar_alumnos()
        elif opcion == 2:
            buscar_fecha_nacimiento()
        elif opcion == 3:
            listar_modulos()
        elif opcion == 4:
            listar_modulos_semestre()
        elif opcion == 5:
            consultar_nota_modulo()
        elif opcion == 6:
            actualizar_datos()
        elif opcion == 7:
            print("Saliendo del programa.")
            break
        else:
            print("Opcion no valida. Por favor, elija una opcion valida.")

def menu():
    print("\nMenu de opciones:")
    print("1. Consultar el listado de companeros de clase.")
    print("2. Buscar un companero y obtener su fecha de nacimiento.")
    print("3. Consultar todos los modulos del curso.")
    print("4. Consultar los modulos de un semestre concreto.")
    print("5. Consultar la nota de un modulo concreto.")
    print("6. Actualizar datos.")
    print("7. Salir.")

# En esta funcion se muestran todos los alumnos
def listar_alumnos():
    print("\nLista de companeros de clase:")
    for alumno in alumnos.find():
        print(f"{alumno['nombre']} {alumno['apellidos']['apellido1']} {alumno['apellidos']['apellido2']}")

# Se muestran todos los modulos
def listar_modulos():
    print("\nTodos los modulos del curso:")
    for modulo in modulos.find():
        print(modulo['nombre'])

# Dependiendo del semetre se enumeran los distintos modulos
def listar_modulos_semestre():
    semestre = input("Ingrese el semestre (ejemplo: '1er semestre'): ")
    print(f"\nModulos del {semestre}:")
    for modulo in modulos.find({"semestre": semestre}):
        print(modulo['nombre'])

# 
def consultar_nota_modulo():
    nombre_modulo = input("Ingrese el nombre del módulo: ")
    modulo = modulos.find_one({"nombre": nombre_modulo})
    if modulo:
        print(f"La nota del módulo {nombre_modulo} es {modulo['nota']}.")
    else:
        print(f"No se encontró el módulo con nombre {nombre_modulo}.")

# 
def buscar_fecha_nacimiento():
    nombre = input("Ingrese el nombre del compañero: ")
    alumno = alumnos.find_one({"nombre": nombre.capitalize()})
    if alumno:
        fecha_nacimiento = alumno['fecha_nacimiento'].strftime("%d/%m/%Y")
        print(f"La fecha de nacimiento de {nombre.capitalize()} es {fecha_nacimiento}.")
    else:
        print(f"No se encontro al compañero con nombre {nombre.capitalize()}.")

def actualizar_datos():
    print("\n¿Que dato quieres actualizar?")
    print("1 Fecha de nacimiento de compañero.")
    print("2. Nota de modulo.")
    print("3. Semestre de modulo.")
    opcion = int(input("Ingrese el numero de la opcion deseada: "))
    if opcion == 1:
        nombre = input("Ingrese el nombre del companero: ")
        fecha_nueva = input("Ingrese la nueva fecha de nacimiento (formato: 'YYYY-MM-DD'): ")
        fecha_nueva = datetime.strptime(fecha_nueva, "%Y-%m-%d")
        alumnos.update_one({"nombre": nombre.capitalize()}, {"$set": {"fecha_nacimiento": fecha_nueva}})
        print("Fecha de nacimiento actualizada.")
    elif opcion == 2:
        nombre_modulo = input("Ingrese el nombre del modulo: ")
        nota_nueva = float(input("Ingrese la nueva nota del modulo: "))
        modulos.update_one({"nombre": nombre_modulo}, {"$set": {"nota": nota_nueva}})
        print("Nota del modulo actualizada.")
    elif opcion == 3:
        nombre_modulo = input("Ingrese el nombre del modulo: ")
        semestre_nuevo = input("Ingrese el nuevo semestre (ejemplo: '1er semestre'): ")
        modulos.update_one({"nombre": nombre_modulo}, {"$set": {"semestre": semestre_nuevo}})
        print("Semestre del modulo actualizado.")
    else:
        print("Opcion no valida. Por favor, elija una opcion valida.")

if __name__ == "__main__":
    main()

