import networkx as nx
import matplotlib.pyplot as plt

# Crea el grafo
grafo = nx.Graph()

grafo.add_node('A', aforo='6')
grafo.add_node('B', aforo='6')
grafo.add_node('C', aforo='6')
grafo.add_node('D', aforo='6')
grafo.add_node('E', aforo='3')
grafo.add_node('F', aforo='3')
grafo.add_node('G', aforo='30')
grafo.add_node('H', aforo='25')
grafo.add_node('I', aforo='25')
grafo.add_node('J', aforo='30')

grafo.add_edge('A', 'B', distancia='1')
grafo.add_edge('B', 'C', distancia='1')
grafo.add_edge('C', 'D', distancia='1')
grafo.add_edge('G', 'H', distancia='1')
grafo.add_edge('H', 'E', distancia='1')
grafo.add_edge('H', 'I', distancia='1')
grafo.add_edge('I', 'F', distancia='1')
grafo.add_edge('E', 'F', distancia='1')
grafo.add_edge('I', 'J', distancia='1')
grafo.add_edge('G', 'A', distancia='4')
grafo.add_edge('J', 'D', distancia='4')


# Dibuja el grafo
nx.draw(grafo, with_labels=True)

# Muestra el dibujo del grafo
plt.show()

print('Tamaño del grafo:', grafo.size())
print('Orden del grafo:', grafo.order())
for act_node in grafo.nodes():
    print('Grado del nodo', act_node, ':', grafo.degree(act_node))
