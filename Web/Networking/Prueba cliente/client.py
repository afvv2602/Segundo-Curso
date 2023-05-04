import socket

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serverAddress = ('localhost', 5000)

clientSocket.connect(serverAddress)

print('Se ha conectado al servidor {} en el puerto {}'.format(*serverAddress))

try:
    message = 'Hola servidor!'
    
    clientSocket.sendall(message.encode())

    bytesReceived = 0
    bytesExpected = len(message)

    while bytesReceived < bytesReceived:
        data = clientSocket.recv(16)
        bytesReceived += len(data)
        print('El servidor dice: {}'.format(data.decode()))

finally:
    clientSocket.close()