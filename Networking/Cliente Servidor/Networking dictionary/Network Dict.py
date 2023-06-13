import definiciones
from random import randint,shuffle
import socket

acronyms = ['Lan','Wan','Wlan','Man','San','Vpn','Ip','Tcp','Udp','Http','https','Ssh','Dns','NIC']
HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)


class Socket(): 
    def __init__(self,conn,addr):
        self.conn = conn
        self.addr = addr
   
    
def server_program():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT)) #Bindeamos el socket a una direccion y a un puerto
        s.listen(1)
        conn, addr = s.accept()
        t = Socket(conn,addr)
        with conn:
            print(f"Connected by {addr}")
            main(t)

def main(t:Socket):
    t.conn.send('pChoose language / Selecciona idioma\n'.encode())
    t.conn.send('g1.English \n2.Español \n3.Exit / Salir\n'.encode())
    data = t.conn.recv(1024)
    match(int(data.decode())):
        case 1:
            menu_eng(t)
        case 2:
            menu_esp(t)
        case 3:
            t.conn.send('xClosing server connection'.encode())
            print('Closing server...')

def menu_eng(t:Socket):
    t.conn.sendall('pWelcome to the networking trivial! \nChoose an option\n'.encode())
    t.conn.sendall('g1.Study (Choose one acronym to get the definition)\n2.Play trivial\n3.Exit\n'.encode())
    data = t.conn.recv(1024)
    match(int(data.decode())):
        case 1:
            first_option(True,t)
        case 2:
            second_option(True,t)
        case 3:
            t.conn.sendall('xClosing server connection'.encode())
            print('Closing server...')

def menu_esp(t:Socket):
    t.conn.send('pBienvenido al trivial del networking\nSelecciona una opción\n'.encode())
    t.conn.send('g1.Estudiar (Elige el termino para recibir su definicion)\n2.Jugar al trivial\n3.Salir\n'.encode())
    data = t.conn.recv(1024)
    match(int(data.decode())):
        case 1:
            first_option(False,t)
        case 2:
            second_option(False,t)
        case 3:
            t.conn.send('xCerrando la conexion del servidor'.encode())
            print('Closing server...')

# If the value is true the function will go to english language, if is false will take the spanish language
def first_option(sw : bool,t:Socket):
    if sw:
        while True:
            t.conn.sendall('pWelcome to the study area, first we will show you all the acronyms, then choose one to see the definition.\n'.encode())
            t.conn.sendall('gWhat definition do you want to see?\n'.encode())
            for index,cadena in enumerate(acronyms,start=1):
                t.conn.sendall(f'{index}.{cadena.capitalize()}\n'.encode()) 
            data = t.conn.recv(1024)
            data = acronyms[int(data.decode())-1]
            t.conn.sendall(f'p{data}: {definiciones.Trivia_Eng[data].value}\n'.encode())
            t.conn.sendall('gWhat do you want to do?:\n1.See another definition\n2.Back to menu\n3.Exit\n'.encode())
            data = t.conn.recv(1024)
            match(int(data.decode())):
                case 1:
                    pass
                case 2:
                    break
                case 3:
                    t.conn.sendall('xClosing server connection'.encode())
                    print('Closing server...')
                    break
        menu_eng(t) 
    else:
        while True:
            t.conn.sendall('pBienvenido a la zona de estudio, primero te mostraremos todas las definiciones despues elige la que quieras ver.\n'.encode())
            t.conn.sendall('g¿Que definicion quieres ver?\n'.encode())
            for index,cadena in enumerate(acronyms,start=1):
                t.conn.sendall(f'{index}.{cadena.capitalize()}\n'.encode())
            data = t.conn.recv(1024)
            data = acronyms[int(data.decode())-1]
            t.conn.sendall(f'p{data}: {definiciones.Trivia_Esp[data].value}\n'.encode())
            t.conn.sendall('g¿Qué quieres hacer?:\n1.Ver otra definicion\n2.Volver al menu\n3.Salir\n'.encode())
            data = t.conn.recv(1024)
            match(int(data.decode())):
                case 1:
                    pass
                case 2:
                    break
                case 3:
                    t.conn.sendall('xCerrando la conexion del servidor'.encode())
                    print('Closing server...')
                    break
        menu_esp(t)    
            
# If the value is true the function will go to english language, if is false will take the spanish language
def second_option(sw : bool,t:Socket):
    if sw:
        t.conn.sendall('pWelcome to the trivial, we are going to show you definitions and you have to guess the acronym GoodLuck!.\n'.encode())
        while True:
            choices = []
            correct_choice = randint(0,len(acronyms)-1)
            definition = acronyms[correct_choice]
            choices.append(definition.capitalize())
            t.conn.sendall(f'g{(definiciones.Trivia_Eng[definition].value)}\n'.encode())
            while True:
                if len(choices) == 4:
                    break
                else:
                    wrong_choice = randint(0,len(acronyms)-1)
                    if wrong_choice != correct_choice:
                        choices.append(acronyms[wrong_choice].capitalize())
            shuffle(choices)
            shuffle(choices)
            t.conn.sendall('Choose the correct answer\n'.encode())
            for index,option in enumerate(choices,start=1):
                t.conn.sendall(f'{index}.{option}\n'.encode())
            data = t.conn.recv(1024)
            if choices[int(data.decode())-1] == acronyms[correct_choice].capitalize():
                t.conn.sendall('pCongratulations you have won!\n'.encode())
                t.conn.sendall('gDo you want to continue playing? \n1.Yes\n2.No\n'.encode())
                data = t.conn.recv(1024)
                if int(data.decode()) != 1:
                    t.conn.sendall('xServer closed!.'.encode())
                    print('Closing server...')
                    break
                else:
                    second_option(True,t)
            else:
                t.conn.sendall(f'pYou have failed :( , the correct answer was {acronyms[correct_choice]} dont worry you have another chance. \n'.encode())
        menu_eng(t)
    else:
        t.conn.send('pBienvenido al trivial, Te iremos mostrando deficiones y tienes que adivinar el termino ¡Suerte!.\n'.encode())
        while True:
            opciones = []
            opcion_correcta = randint(0,len(acronyms)-1)
            definicion = acronyms[opcion_correcta]
            opciones.append(definicion.capitalize())
            t.conn.sendall(f'g{(definiciones.Trivia_Esp[definicion].value)}\n'.encode())
            while True:
                if len(opciones) == 4:
                    break
                else:
                    opcion_falsa = randint(0,len(acronyms)-1)
                    if opcion_falsa != opcion_correcta:
                        opciones.append(acronyms[opcion_falsa].capitalize())
            shuffle(opciones)
            shuffle(opciones)
            t.conn.sendall('Selecciona la respuesta correcta\n'.encode())
            for index,opcion in enumerate(opciones,start=1):
                t.conn.sendall(f'{index}.{opcion}\n'.encode())
            data = t.conn.recv(1024)
            if opciones[int(data.decode())-1] == acronyms[opcion_correcta].capitalize():
                t.conn.sendall('p¡Enhorabuena has ganado!\n'.encode())
                t.conn.sendall('g¿Quieres seguir jugando? 1.Si\n2.No\n'.encode())
                data = t.conn.recv(1024)
                if int(data.decode()) != 1:
                    t.conn.send('xCerrando la conexion del servidor'.encode())
                    print('Closing server...')
                    break
                else:
                    second_option(False,t)
            else:
                print(f'pHas fallado :( , la respuesta correcta era {acronyms[opcion_correcta]} no te preocupes tienes otra oportunidad. \n'.encode())
        menu_esp()
                
if __name__ == '__main__':
    server_program()