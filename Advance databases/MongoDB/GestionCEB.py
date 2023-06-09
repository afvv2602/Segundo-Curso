from c import MongoClient
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
            menu_aggregate()
        elif opcion == 8:
            retos_mongo()
        elif opcion == 9:
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
    print("7. Funciones aggregate.")
    print("8. Salir.")

def menu_aggregate():
    while True:
        print("\nMenu de funciones aggregate")
        print("1. Mostrar los alumnos agrupados por fecha de nacimiento.")
        print("2. Promedio de notas por semestre.")
        print("3. Ver que modulos superan un promedio.")
        print("4. Volver al menu.")
        opcion = int(input("\nIngrese el numero de la opcion deseada: "))
        if opcion == 1:
            mostrar_alumnos_por_fecha()
        elif opcion == 2:
            promedio_notas_semetre()
        elif opcion == 3:
            nota = int(input("\nIntroduce la nota para buscar."))
            buscar_modulos_por_promedio(nota)
        elif opcion == 4:
            print("Volviendo al menu principal.")
            break
    main()
           
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

def consultar_nota_modulo():
    nombre_modulo = input("Ingrese el nombre del modulo: ")
    modulo = modulos.find_one({"nombre": nombre_modulo})
    if modulo:
        print(f"La nota del módulo {nombre_modulo} es {modulo['nota']}.")
    else:
        print(f"No se encontro el modulo con nombre {nombre_modulo}.")

def buscar_fecha_nacimiento():
    nombre = input("Ingrese el nombre del alumno: ")
    alumno = alumnos.find_one({"nombre": nombre.capitalize()})
    if alumno:
        fecha_nacimiento = alumno['fecha_nacimiento'].strftime("%d/%m/%Y")
        print(f"La fecha de nacimiento de {nombre.capitalize()} es {fecha_nacimiento}.")
    else:
        print(f"No se encontro al alumno con nombre {nombre.capitalize()}.")

def actualizar_datos():
    print("\n¿Que dato quieres actualizar?")
    print("1 Fecha de nacimiento de alumno.")
    print("2. Nota de modulo.")
    print("3. Semestre de modulo.")
    opcion = int(input("Ingrese el numero de la opcion deseada: "))
    if opcion == 1:
        nombre = input("Ingrese el nombre del alumno: ")
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

# Aggregate
# Se muestran todos los alumnos agrupados por fecha de nacimiento
def mostrar_alumnos_por_fecha():
    print("Numero de alumnos por año de nacimiento:")
    query = db.alumnos.aggregate([
        {
            "$group": {
                "_id": {"$year": "$fecha_nacimiento"},
                "total": {"$sum": 1}
            }
        }
    ])
    for actual in query:
        print(f"Año {actual['_id']}: {actual['total']} alumnos")
    main()
    
# Se muestra el promedio de notas por semestre
def promedio_notas_semetre():
    print("Promedio de notas de los modulos por semestre:")
    query = db.modulos.aggregate([
        {
            "$group": {
                "_id": "$semestre",
                "nota_promedio": {"$avg": "$nota"}
            }
        }
    ])
    for actual in query:
        print(f"{actual['_id']}: nota promedio {round(actual['nota_promedio'], 2)}")
    main()

# Se muestran los modulos que superar una nota promedio
def buscar_modulos_por_promedio(nota):
    print(f"Numero de modulos con nota igual o superior a {nota} por semestre:")
    query = db.modulos.aggregate([
        {
            "$match": {
                "nota": {"$gte": nota}
            }
        },
        {
            "$group": {
                "_id": "$semestre",
                "total_modulos": {"$sum": 1},
                "modulos": {"$push": "$nombre"}
            }
        }
    ])
    for actual in query:
        print(f"{actual['_id']}: {actual['total_modulos']} modulos")
        for modulo in actual['modulos']:
            print(f"  - {modulo}")
    main()

def retos_mongo():
    mod = db.modulos.find()

    for modulo in mod:



if __name__ == "__main__":
    main()

