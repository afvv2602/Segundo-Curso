import socket
import time

s = socket.socket()
HOST = "127.0.0.1"
PORT = 65432
partida = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]

jugador1 = 1
jugador2 = 2

jugadoresConectados = list()
jugadoresIp = list()
jugadoresNombre = list()         

#Se valida si la entrada del jugador ha sido correcta
def validar(x, y, conn):
    if x > 3 or y > 3:
        print("\nFuera del rango, introduce otra posicion\n")
        conn.send("Error".encode())
        return False
    elif partida[x][y] != 0:
        print("\nYa esta ocupada, introduce otra posicion\n")
        conn.send("Error".encode())
        return False
    return True

def recoger_datos(jugadorAct):
    if jugadorAct == jugador1:
        jugador = "Turno del jugador 1"
        conn = jugadoresConectados[0]
    else:
        jugador = "Turno del jugador 2"
        conn = jugadoresConectados[1]
    print(jugador)
    enviarMensajes(jugador)
    op = 1
    while op:
        try:
            conn.send("Input".encode())
            data = conn.recv(2048 * 10)
            conn.settimeout(20)
            data = data.decode().split(",")
            x = int(data[0])
            y = int(data[1])
            if validar(x, y, conn):
                partida[x][y] = jugadorAct
                op = 0  
                enviarMensajes("Partida")
                enviarMensajes(str(partida))
        except:
            conn.send("Error".encode())
            print("Ha ocurrido un error intentalo de nuevo")
  
def check_filas():
    res = 0
    for i in range(3):
        if partida[i][0] == partida[i][1] and partida[i][1] == partida[i][2]:
            res = partida[i][0]
            if res != 0:
                break
    return res

def check_columnas():
    res = 0
    for i in range(3):
        if partida[0][i] == partida[1][i] and partida[1][i] == partida[2][i]:
            res = partida[0][i]
            if res != 0:
                break
    return res

def check_diagonales():
    res = 0
    if partida[0][0] == partida[1][1] and partida[1][1] == partida[2][2]:
        res = partida[0][0]
    elif partida[0][2] == partida[1][1] and partida[1][1] == partida[2][0]:
        res = partida[0][2]
    return res

def check_ganador():
    res = 0
    res = check_filas()
    if res == 0:
        res = check_columnas()
    if res == 0:
        res = check_diagonales()
    return res

def empezar_server():
    try:
        s.bind((HOST, PORT))
        print("Servidor de tres en raya iniciado. \nEsuchando en el puerto: ", PORT)
        s.listen(2) 
        aceptar_jugadores()
    except socket.error as e:
        print("Error de conexion con el servidor:", e)
    
#Se acepta un jugador
#Se le envia un mensaje de bienvenida
#Y recogemos el nombre del militar
def aceptar_jugadores():
    try:
        msg = "Bienvenido al servidor de tres en raya"
        for i in range(2):
            conn, addr = s.accept()
            conn.send(msg.encode())
            name = conn.recv(2048 * 10).decode()
            jugadoresConectados.append(conn)
            jugadoresIp.append(addr)
            jugadoresNombre.append(name)
            conn.send("Encantado {}, tu eres el jugador {}".format(name, str(i+1)).encode())
        empezar_partida()
        s.close()
    except socket.error as e:
        print("jugador: ", e)   

def empezar_partida():
    res = 0
    i = 0
    while res == 0 and i < 9 :
        if (i%2 == 0):
            recoger_datos(jugador1)
        else:
            recoger_datos(jugador2)
        res = check_ganador()
        i = i + 1
    msg = list
    if res == 1:
        msg = "Has ganado {} enhorabuena!!!".format(jugadoresNombre[0])
    elif res == 2:
        msg = "Has ganado {} enhorabuena!!!".format(jugadoresNombre[1])
    else:
        msg = "Empate"
    enviarMensajes(msg)
    time.sleep(5)
    for conn in jugadoresConectados:
        conn.close()
    
def enviarMensajes(text:str):
    jugadoresConectados[0].send(text.encode())
    jugadoresConectados[1].send(text.encode())
    time.sleep(1)

empezar_server()