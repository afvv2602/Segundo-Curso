
class Tictactoe():   
    
    def __init__(self):
        # Tablero del tic tac toe
        self.tablero = {"t1" : " ","t2" : " ","t3" : " ","m1" : " ","m2" : " ","m3" : " ","b1" : " ","b2" : " ","b3" : " "} 
        # Tenemos el control de que casillas estan ocupadas en el tablero 0 vacias y 1 ocupadas por O y 4 por X                 
        self.control_partida = {"t1" : 0,"t2" : 0,"t3" : 0,"m1" : 0,"m2" : 0,"m3" : 0,"b1" : 0,"b2" : 0,"b3" : 0}  
    
    def pintar_estado(self):
        # Carga los valores del estado de la partida
        self.estado_partida = self.tablero.values()   
        # Cogemos los valores de las posiciones y los imprimimos por consola 
        print("\t %s | %s | %s \n\t---+---+---\n\t %s | %s | %s \n\t---+---+---\n\t %s | %s | %s " % tuple(self.estado_partida))
    
    def ready(self):
        # Se imprime al principio una pequeña guia con las posiciones
        print("**** Tic Tac Toe Game ****")
        self.status = ("T1", "T2", "T3", "M1", "M2", "M3", "B1", "B2", "B3")
        print("\t%s |%s |%s \n\t---+---+---\n\t%s |%s |%s \n\t---+---+---\n\t%s |%s |%s "% self.status)
    
    def jugador1(self):
        # Si el jugador 1 pone bien la posicion se le asignara el valor 1 a esa posición
        # En caso contrario seguira en el bucle hasta que ponga la posicion bien
        while True:
            op = input("Jugador 1>>>>> ").lower()
            if op in self.control_partida.keys():
                if self.tablero[str(op)] == " ":
                    self.tablero[str(op)]="O"
                    self.pintar_estado()
                    self.control_partida[str(op)] = 1
                    break
                else:
                    print("Esta posicion esta ocupada.\nSeleccione otra.")
                    continue
            else:
                print("Has puesto mal la posicion")    
                continue

    def jugador2(self):
        # Si el jugador 2 pone bien la posicion se le asignara el valor 4 a esa posición
        # En caso contrario seguira en el bucle hasta que ponga la posicion bien
        while True:
            op = input("Jugador 2>>>>> ").lower()
            if op in self.tablero.keys():
                if self.tablero[str(op)] == " ":
                    self.tablero[str(op)]="X"
                    self.basic()
                    self.control_partida[str(op)] = 4
                    break
                else:
                    print("Esta posicion esta ocupada.\nSeleccione otra.")
                    continue
            else:
                print("Has puesto mal la posicion")    
                continue

    def play(self):
        print("\nLet's start the game!")
        print("Jugador1 : O     Jugador2 : X")
        print("write a position!\n")
        for i in range(1,10):
            win = self.win()
            if 3 in win: # jugador1's each position has value 1
                print("jugador1 WIN!")
                break
            elif 12 in win: # jugador2'x each position has value 4
                print("jugador2 WIN!")
                break
            elif i == 9:
                if i % 2 == 0:
                    self.jugador2()
                else:
                    self.jugador1()
                print("The game ended in a tie.")
            else:
                if i % 2 == 0:
                    self.jugador2()
                else:
                    self.jugador1()
    
    def win(self):
        self.p = list(self.control_partida.values()) # for convenience made p 
        
        # row match case
        self.r_match = [self.p[i]+self.p[i+1]+self.p[i+2] for i in [0,3,6]]

        # column match case
        self.c_match = [self.p[i]+self.p[i+3]+self.p[i+6] for i in [0,1,2]]

        # diagonal match case
        self.d_match = [self.p[0]+self.p[4]+self.p[8], self.p[2]==self.p[4]==self.p[6]]

        self.result = self.r_match + self.c_match+ self.d_match
        return self.result


