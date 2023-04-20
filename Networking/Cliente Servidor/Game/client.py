import socket

host = '127.0.0.1'
port = 1234

ClientSocket = socket.socket()
print('Waiting for connection')
try:
    ClientSocket.connect((host, port))
except socket.error as e:
    print(str(e))
Response = ClientSocket.recv(2048)
while True:
    while True:
        datagram = ClientSocket.recv(10240)
        if datagram:

            tokens = datagram.decode()
            # print('------------')
            # print(tokens)
            # print('------------')
            
            # get
            if tokens[0].lower() == "g":
                message = tokens[1:-1]
                print(message)
                message_to_server = input('').encode()
                ClientSocket.send(message_to_server)
                pass
            # post
            elif tokens[0].lower() == "p":
                message = tokens[1:-1]
                print(message)
                pass
            elif tokens[0].lower() == 'x':
                print(f'Server Response:{tokens}, Closing client')
                ClientSocket.close()
                break
            else:
                message = tokens
                print(message)
                pass
            
    # Input = input('Your message: ')
    # ClientSocket.send(str.encode(Input))
    # Response = ClientSocket.recv(2048)
    # print(Response.decode('utf-8'))
