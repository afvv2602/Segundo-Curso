import socket

serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serverAddress = ('localhost', 5000)
serverSocket.bind(serverAddress)

serverSocket.listen()
print('Se ha conectado el servidor {} en el puerto {}'.format(*serverAddress))


while True:
    print('Esperando a un cliente')
    connection, client_address = serverSocket.accept()
    try:
        print('Se ha conectado el cliente', client_address)

        while True:
            data = connection.recv(1024)
            if data:
                print('El cliente dice: {}'.format(data.decode()))
                connection.sendall(data)
            else:
                break

    finally:
        connection.close()