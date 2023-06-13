import socket
from _thread import *
import tictactoe as ttt
import time

host = '127.0.0.1'
port = 1234
partidas = list
usuarios = dict


def client_handler(connection):
    new_user = True
    connection.send(str.encode('Estas conectado el servidor... Si quieres salir pulsa p'))
    while True:
        if new_user:
            connection.sendall(str.encode(f'gBievenido a las tres en raya, antes de nada introduce tu nombre de usuario'))
            data = connection.recv(2048)
            usuario = data.decode()
            usuarios[usuario] = connection
            new_user = False
            if len(usuarios) %2 == 0:
                connection.sendall(str.encode(f''))
            else:
                connection.sendall(str.encode(f'Esperando a otro jugador...'))
                time.sleep(5)
        else:
            if len(usuarios) %2 == 0:
                connection.sendall(str.encode(f''))
            else:
                connection.sendall(str.encode(f'Esperando a otro jugador...'))
                time.sleep(5)   
            
    connection.close()

def accept_connections(ServerSocket):
    Client, address = ServerSocket.accept()
    print('Conectado a: ' + address[0] + ':' + str(address[1]))
    start_new_thread(client_handler, (Client, ))

def start_server(host, port):
    ServerSocket = socket.socket()
    try:
        ServerSocket.bind((host, port))
    except socket.error as e:
        print(str(e))
    print(f'Server is listing on the port {port}...')
    ServerSocket.listen()

    while True:
        accept_connections(ServerSocket)
start_server(host, port)