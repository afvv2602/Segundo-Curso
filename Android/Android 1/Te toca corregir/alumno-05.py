# Proyecto Final _  Software Development II ----------------------------------------#
#     Realizado por: Alumno xx
'''
DESCRIPCIÓN DEL PROGRAMA:
Se pide realizar una calculadora con una interfaz de inicio en la cual se pide una
contraseña para poder acceder a ella.

Una vez dentro, deben estar las opciones de calculos aritméticos, cálculos 
matriciales y por último una sección de búsqueda de máximos, mínimos y media.

'''
# VARIABLES: -----------------------------------------------------------------------# 

from math import cos, sin
from time import sleep  # Mi librería favorita para evitar confusión al usuario

menu_inicial = int(999)    # Primer menú  [1.0]
limite = int(3)
limite_cambio = int(3)
key = str('immune')
contraseña = str('')

menu_principal = int(0)    # Segundo menú  [2.0]

menu_aritmetic = int(999)    # Menú [2.1]
cantidad = int(0)    # Decide con cuantos numeros se va a operar.
num1 = float(0)
num2 = float(0)
resultado = float(0)

# Menú [2.2] Matrices
dimensiones = int(0)    # Dimensiones de las matrices
opcion = int(0)
m1 = [[0,0,0], [0,0,0], [0,0,0]]    # Matriz 1
m2 = [[0,0,0], [0,0,0], [0,0,0]]    # Matriz 2
mr = [[0,0,0], [0,0,0], [0,0,0]]    # Matriz resultado
n1 = int(0)    # Variable de ayuda para la multiplicación.
eos = bool(True)    # Entrada o Salida del programa, para que el usuario
#                     decida cuando salir

# Menú [2.3] Búsqueda
numeros = list()
media = float(0)
ans = int(0)

# DEFINICIONES: ______________________________________________________________#

def tan(x):
    return ( sin(x) / cos(x) ) 

def arctan(x):
    return 1 / tan(x)

def arcsin(x):
    return 1 / sin(x)

def arcos(x):
    return 1/ cos(x)
        
def log(x, y):    # Utilizo las propiedades logarítmicas en la función.
    return pow((x/y), -1)

def root(x):
    return pow(x, (1/2) ) # Uso las propiedades de una raíz y la defino.

# ACCESO USUARIO: ------------------------------------------------------------------#

while menu_inicial !=0 :

    print ('''
|-----------------------------------------------------|
|                CALCULADORA CIENTÍFICA               |
|-----------------------------------------------------|
|                   ACCESO USUARIO                    |
|-----------------------------------------------------|
|                [0] Salir Del Programa               |
|----                                             ----|
|  [1] Modificar Clave    [2] Calculadora Científica  |
|                                                     |
|-----------------------------------------------------|
''')

    menu_inicial = int(input('\t Elija una opción: '))

    if menu_inicial == 1:
        print ('  Estas en la opción:   [1] Modificar Clave \n ')
        while limite_cambio > 0:
            contraseña = str(input('Introduzca la contraseña: '))
            if contraseña == key:
                key = str(input('\n Introduce la NUEVA contraseña: '))
                print('Volviendo al menú inicial...')
                menu_inicial = int(999)    # Regresa al menú principal
                sleep (2)
                break    # Sale del bucle " limite_cambio "

            else:    # Resta un intento.
                print ('\n', 'contraseña incorrecta, vuelva a intentar.')
                limite_cambio -= 1
                print ('numero de intentos: ', limite_cambio)

        if limite_cambio <= 0:    # El límite de intentos es excedido...
            print ('\t limite de intentos excedido... \n ')
            sleep (1)
            print('Saliendo del programa...')
            exit()    # Sale del programa.

# ----------------

    if menu_inicial == 2:
        print ('  Estas en la opción:   [2] Calculadora Científica \n ')

        while limite > 0:    # Toma en cuenta el límite de intentos.
            contraseña = str(input('Introduzca la contraseña: '))
            if contraseña == key:
                print ('\n Has accedido al programa... \n')
                sleep(2)    # Evita mostrar el siguiente menú de golpe
                break

            else:    # Informa al Usuario del error y muestra los intentos restantes.
                print ('\n Contraseña incorrecta, vuelva a intentar.')
                limite -= 1
                print ('Numero de intentos: ', limite, '\n')

        if limite <= 0:    # El límite de intentos es excedido...
            print ('\t limite de intentos excedido... \n ')
            sleep (1)
            print('Saliendo del programa...')
            exit()    # Sale del programa.

        break    # Pasa al siguiente menú después de introducir la contraseña.

    if menu_inicial == 0:
        print('Saliendo del programa...')
        exit()    # Sale del programa.

# CALCULADORA CIENTÍFICA: ----------------------------------------------------------#
        
while menu_principal != 4:
    print ('''
|-----------------------------------------------------|
|                CALCULADORA CIENTÍFICA               |
|-----------------------------------------------------|
|                   MENU PRINCIPAL                    |
|-----------------------------------------------------|
|                                                     |
|    [1] Opciones Aritméticas                         |
|    [2] Calculadora Con Matrices                     |
|    [3] Búsqueda                                     |
|    [4] Apagar CC                                    |
|                                                     |
|-----------------------------------------------------|
''')
    menu_principal = int(input('Elija una opción: '))
    sleep(1)

    if menu_principal == 4:
        print('Saliendo del programa...')

    if menu_principal == 1:
        menu_aritmetic = int(999)
        while menu_aritmetic != 0:
            print ('''
|-----------------------------------------------------|
|                CALCULADORA CIENTÍFICA               |
|-----------------------------------------------------|
|               OPERACIONES ARITMÉTICAS               |
|-----------------------------------------------------|
|                     [0] Salir                       |
|----                                             ----|
|    [1] Sumar             [9] Arcoseno               |
|    [2] Restar            [10] Arcocoseno            |
|    [3] Multiplicar       [11] Arcotangente          |
|    [4] Dividir           [12] Logaritmo en base 10  |
|    [5] Valor absoluto    [13] Logaritmo neperiano   |
|    [6] Seno              [14] e elevado a x         |
|    [7] Coseno            [15] raíz cuadrada         |
|    [8] Tangente                                     |
|-----------------------------------------------------|
''')
            menu_aritmetic = int(input('Elija una opción: ')); print('')

        # [2] SUMA: ________________________________________________________________#

            if menu_aritmetic == 1:
                print('Estás en la opción [1] Sumar. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                cantidad = int(input('Con cuantos números vas a operar?: '))
                print('')

                for i in range (cantidad):
                    num1 = float(input('Introduce un número: ')) 
                    resultado += num1
                print ('\n', 'El resultado es: ', resultado)

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))


        # RESTA: ___________________________________________________________________#

            if menu_aritmetic == 2:
                print('Estás en la opción [2] Restar. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                num2 = float(input('Introduce cuanto vas a restar: '))
                resultado = num1 - num2

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # MULTIPLICACIÓN: __________________________________________________________#

            if menu_aritmetic == 3:
                print('Estás en la opción [3] Multiplicar. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                num2 = float(input('Introduce el múltiplo: '))
                resultado = num1 * num2

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # DIVISIÓN: ________________________________________________________________#

            if menu_aritmetic == 4:
                print('Estás en la opción [4] Dividir. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce el divisor: '))
                num2 = float(input('Introduce el dividendo: '))
                resultado = num1 / num2

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # VALOR ABSOLUTO: __________________________________________________________#

            if menu_aritmetic == 5:
                print('Estás en la opción [5] Valor Absoluto. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                print('* Puedes introducir números negativos.')
                num1 = float(input('Introduce un número: '))
                resultado = abs(num1)    # Esta es una función que ya está en python.

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # SENO: ____________________________________________________________________#

            if menu_aritmetic == 6:
                print('Estás en la opción [6] Seno. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = sin(num1)    # Esta es una función que ya está en python.

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # COSENO: __________________________________________________________________#

            if menu_aritmetic == 7:
                print('Estás en la opción [7] Coseno. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = cos(num1)    # Esta es una función que ya está en python.

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # TANGENTE: ________________________________________________________________#

            if menu_aritmetic == 8:
                print('Estás en la opción [8] Tangente. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = tan(num1)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # ARCOSENO: ________________________________________________________________#

            if menu_aritmetic == 9:
                print('Estás en la opción [9] Arcoseno. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = arcsin(num1)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # ARCOCOSENO: ______________________________________________________________#

            if menu_aritmetic == 10:
                print('Estás en la opción [10] Arcocoseno. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = arcos(num1)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))


        # ARCOTANGENTE: ____________________________________________________________#

            if menu_aritmetic == 11:
                print('Estás en la opción [11] Arcotangente. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número: '))
                resultado = arctan(num1)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # LOGARITMO BASE 10: _______________________________________________________#

            if menu_aritmetic == 12:
                print('Estás en la opción [12] Logaritmo Base 10. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número positivo: '))
                resultado = log(num1, 10)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # LOGARITMO NEPERIANO: _____________________________________________________#

            if menu_aritmetic == 13:
                print('Estás en la opción [13] Logaritmo Neperiano. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce un número positivo: '))
                resultado = log(num1, 2.71828)    # Introduzco en número de euler

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # E ELEVADO A X: ___________________________________________________________#

            if menu_aritmetic == 14:
                print('Estás en la opción [14] "e" elevado a "x". \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                num1 = float(input('Introduce el exponente: '))
                resultado = pow(2.71828, num1) # Número de euler elevado por un exponente

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

        # RAÍZ CUADRADA: ___________________________________________________________#

            if menu_aritmetic == 15:
                print('Estás en la opción [15] Raíz Cuadrada. \n')

                resultado = float(0)    # Reinicia el valor del resultado.
                print('(El radical es = 2)')   # aunque se podría cambiar por otro valor
                num1 = float(input('Introduce el valor del radicando: '))
                resultado = root(num1)    # (ver la definición)

                print (f'El resultado es {resultado}.')

                # Pregunta si quiere volver al menu
                menu_aritmetic = int(999)
                while (menu_aritmetic != 1) and (menu_aritmetic != 0):
                    print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                    menu_aritmetic = int(input('Elige una opción:  '))

            # [0] SALIR DEL PROGRAMA: ______________________________________________#
            '''
            Si el usuario decide salir del programa desde el inicio o una vez
            terminada una operación, se va a mostrar este mensaje en pantalla.
            '''
            if menu_aritmetic == 0:
                # Agrego color amarillo
                print('\033[0;33m', 'Has decidido salir del programa...') 
                sleep (1)
                print ('\033[0;0m', 'Que tengas un buen día.    :)')

# CALCULADORA DE MATRICES: ---------------------------------------------------------#

    if menu_principal == 2:
        print('Estás en la opción [2] Calculadora de Matrices. \n')
        print('Nota: Las matrices son cuadradas.')

        while True:    # Bucle para preguntar las matrices
            dimensiones = int(input('Introduce las dimensiones de tus matrices: '))

            # Muestra error y vuelve a preguntar.
            if dimensiones < 2 or dimensiones > 3:
                print('Dimensiones incorrectas, vuelve a intentarlo. \n ')
            
            # Pregunta las matrices.
            else:
                print ('\n Introduce los datos de la primera matriz: ')
                for i in range (dimensiones):
                    for j in range (dimensiones):
                        m1[i][j] = int(input((f'Introduce el elemento {i}, {j}: ')))
                
                print ('\n Introduce los datos de la segunda matriz: ')
                for i in range (dimensiones):
                    for j in range (dimensiones):
                        m2[i][j] = int(input((f'Introduce el elemento {i}, {j}: ')))
                print('')

                for i in (m1):    # Imprime la primera matriz
                    print ('[', *i, end=' ]')
                    print('')

                print('')    # Deja un espacio entre las matrices.

                for i in (m2):    # Imprime la segunda matriz
                    print ('[', *i, end=' ]')
                    print('')

                break    # Sale del bucle de preguntar matrices

# MENU DE MATRICES: ________________________________________________________________#

        while True:
            print('''
|-----------------------------------------------------|
|                CALCULADORA CIENTÍFICA               |
|-----------------------------------------------------|
|               OPERACIONES CON MATRICES              |
|-----------------------------------------------------|
|                    [0] Salir                        |
|                                                     |
|    [1] Suma de Matrices                             |
|    [2] Resta de matrices                            |
|    [3] Multiplicación de Matrices                   |
|-----------------------------------------------------|
''')
            opcion = int(input('Elije una opción: '))

# SUMA DE MATRICES: ________________________________________________________________#

            if opcion == 1:
                print('Has elegido Sumar. \n')
                print('Calculando suma... \n')
                sleep(2)

                for i in range (len(m1)):    # Realiza el cálculo de la suma
                    for j in range (len(m1)):
                        mr [i][j] = m1[i][j] + m2[i][j]
            
                for i in (mr):    # Imprime el resultado
                    print ('[', *i, end=' ]')
                    print('')

                # Pregunta al usuario si quiere salir o continuar
                print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                while True:
                    eos = int(input('Elija una opción: '))
                    if eos == 0:
                        sleep(1)
                        opcion = 0
                        break
                    if eos == 1:
                        print('')
                        break
                    else:
                        print('\n Vuelve a intentar. \n')

# RESTA DE MATRICES: _______________________________________________________________#

            if opcion == 2:
                print('Has elegido Restar. \n')
                print('Calculando Resta... \n')
                sleep(2)

                for i in range (len(m1)):    # Realiza el cálculo de la resta
                    for j in range (len(m1)):
                        mr [i][j] = m1[i][j] - m2[i][j]
            
                for i in (mr):    # Imprime el resultado
                    print ('[', *i, end=' ]')
                    print('')

                # Pregunta al usuario si quiere salir o continuar
                print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                while True:
                    eos = int(input('Elija una opción: '))
                    if eos == 0:
                        sleep(1)
                        opcion = 0
                        break
                    if eos == 1:
                        print('')
                        break
                    else:
                        print('\n Vuelve a intentar. \n')

# MULTIPLICACIÓN DE MATRICES: ______________________________________________________#

            if opcion == 3:
                print('Has elegido Multiplicar. \n')
                print('Calculando Multiplicación... \n')
                sleep(2)

                for i in range(len (m2[0])):    # Calcula la Multiplicación.
                    for j in range (len(m1)):
                        n1 = 0
                        for k in range (len(m1[0])):
                            n1 = n1 + m1[j][k] * m2[k][i]
                        mr [j][i] = n1
                
                for i in (mr):    # Imprime el resultado
                    print ('[', *i, end=' ]')
                    print('')

                # Pregunta al usuario si quiere salir o continuar
                print('''
___________________________________
|  [0] Salir  [1] Volver al menú  |
|_________________________________| ''')
                while True:
                    eos = int(input('Elija una opción: '))
                    if eos == 0:
                        sleep(1)
                        opcion = 0
                        break
                    if eos == 1:
                        print('')
                        break
                    else:
                        print('\n Vuelve a intentar. \n')

# Salir de matrices: _______________________________________________________________#
            if opcion == 0:
                print ('Saliendo del programa...')
                sleep(2)
                break

# OPCIÓN 3, BÚSQUEDA: --------------------------------------------------------------#

    if menu_principal == 3:
        print('\n Estás en la oppción de Búsqueda. \n')

        print('Dime 10 números:')
        for i in range (10):
            numeros.append(int(input()))

        numeros.sort()    # Ordeno los números

# Media:
        for i in range (len(numeros)):
            ans = ans + numeros[i]
            media = ans / len(numeros)
        
        print (f'''
|-----------------------------------------------------|
|                CALCULADORA CIENTÍFICA               |
|-----------------------------------------------------|
|                      BÚSQUEDA                       |
|-----------------------------------------------------|
|                     [0] Salir                       |
|----                                             ----|
|    [1] Mayor = {numeros [9]}                              |  
|    [2] Menor = {numeros [0]}                              |
|    [3] Media = {media}                                |
|-----------------------------------------------------|
''')
        print('Volviendo al menú...')
        sleep (2)
