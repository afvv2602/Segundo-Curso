import socket
import time

s = socket.socket()
HOST = "127.0.0.1" 
PORT = 65432 

def mostrar_partida(partida):
    for i in range(3):
        for j in range(3):
            act = "_"
            # Jugador 1
            if partida[i][j] == 1:
                act = "X"
            # Jugador 2
            elif partida[i][j] == 2:
                act = "O"
            print(act, end = "\t")
        print("")

def empezar_cliente():
    try:
        s.connect((HOST, PORT))
        print("Conectado a :", HOST, ":", PORT)
        comenzar_partida()
        s.close()
    except socket.error as e:
        print("No se ha podido establecer la conexion con el socket:", e) 

def comenzar_partida():
    msg = s.recv(2048 * 10)
    print(msg.decode())

    user = input("Introduce tu nombre:")
    s.send(user.encode())
    while True:
        try: 
            msg = s.recv(2048 * 10).decode()
            if msg == "Input":
                op = 1
                while op:
                    try:
                        print("\n**** Tres en raya ****")
                        status = ("X0Y0", "X0Y1", "X0Y2", "X1Y0", "X1Y1", "X1Y2", "X2Y0", "X2Y1", "X2Y2")
                        print("\t%s |%s |%s \n\t---+---+---\n\t%s |%s |%s \n\t---+---+---\n\t%s |%s |%s "% status)
                        x = int(input("\nIntroduce una cordenada x: "))
                        y = int(input("Introduce una cordenada y: "))
                        posiciones = str(x)+"," + str(y)
                        s.send(posiciones.encode())
                        op = 0
                    except:
                        print("Ha ocurrido un error intentelo de nuevo")
                

            elif msg == "Error":
                print("Ha ocurrido un error intentelo otra vez")
            
            elif  msg == "Partida":
                print(msg)
                partida = s.recv(2048 * 100)
                partidaDecoded = partida.decode("utf-8")
                mostrar_partida(eval(partidaDecoded))

            elif msg == "":
                time.sleep(10)
                break

            else:
                print(msg)
        except KeyboardInterrupt:
            print("\nCerrando conexion...")
            time.sleep(1)
            break

empezar_cliente()