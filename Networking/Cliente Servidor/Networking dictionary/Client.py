import socket

HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 65432  # The port used by the server

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    while True:
        datagram = s.recv(10240)
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
                s.send(message_to_server)
                pass
            # post
            elif tokens[0].lower() == "p":
                message = tokens[1:-1]
                print(message)
                pass
            elif tokens[0].lower() == 'x':
                print(f'Server Response:{tokens}, Closing client')
                break
            else:
                message = tokens
                print(message)
                pass
                
