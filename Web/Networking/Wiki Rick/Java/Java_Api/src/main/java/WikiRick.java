import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class WikiRick {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        while (true) {
            mostrarMenu();
            System.out.print("Selecciona una opcion en la wiki: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 1) {
                mostrarOpciones("character");
                System.out.print("Introduce el ID del personaje que deseas conocer: ");
                int idPersonaje = scanner.nextInt();
                scanner.nextLine();
                obtenerPersonaje(idPersonaje);
            } else if (opcion == 2) {
                mostrarOpciones("episode");
                System.out.print("Introduce el ID del episodio que deseas conocer: ");
                int idEpisodio = scanner.nextInt();
                scanner.nextLine();
                obtenerEpisodio(idEpisodio);
            } else if (opcion == 3) {
                mostrarOpciones("location");
                System.out.print("Introduce el ID de la ubicacion que deseas conocer: ");
                int idUbicacion = scanner.nextInt();
                scanner.nextLine();
                obtenerUbicacion(idUbicacion);
            } else if (opcion == 4) {
                System.out.println("Hasta luego!!");
                break;
            } else {
                System.out.println("Opcion invalida. Por favor, selecciona una opcion del menu.");
            }
        }

        scanner.close();
    }

    public static void mostrarMenu() {
        System.out.println("\nMenu:\n");
        System.out.println("1. Buscar personaje");
        System.out.println("2. Buscar episodio");
        System.out.println("3. Buscar ubicacion");
        System.out.println("4. Salir\n");
    }
    
    public static void obtenerPersonaje(int idPersonaje) {
        String url = "https://rickandmortyapi.com/api/character/" + idPersonaje;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject personaje = new JSONObject(response.body());
                System.out.println("Nombre del personaje: " + personaje.getString("name"));
                System.out.println("Especie: " + personaje.getString("species"));
                System.out.println("Genero: " + personaje.getString("gender"));
                System.out.println("Origen: " + personaje.getJSONObject("origin").getString("name"));
            } else {
                System.out.println("Error al obtener el personaje con ID " + idPersonaje + ". Codigo de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void obtenerEpisodio(int idEpisodio) {
        String url = "https://rickandmortyapi.com/api/episode/" + idEpisodio;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject episodio = new JSONObject(response.body());
                System.out.println("Nombre del episodio: " + episodio.getString("name"));
                System.out.println("Temporada: " + episodio.getString("episode").substring(0, 2));
                System.out.println("Numero de episodio: " + episodio.getString("episode").substring(3));
                System.out.println("Fecha de emision: " + episodio.getString("air_date"));
            } else {
                System.out.println("Error al obtener el episodio con ID " + idEpisodio + ". Codigo de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void obtenerUbicacion(int idUbicacion) {
        String url = "https://rickandmortyapi.com/api/location/" + idUbicacion;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject ubicacion = new JSONObject(response.body());
                System.out.println("Nombre de la ubicacion: " + ubicacion.getString("name"));
                System.out.println("Tipo: " + ubicacion.getString("type"));
                System.out.println("Dimension: " + ubicacion.getString("dimension"));
            } else {
                System.out.println("Error al obtener la ubicacion con ID " + idUbicacion + ". Codigo de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarOpciones(String tipo) {
        String url = "https://rickandmortyapi.com/api/" + tipo;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject resultado = new JSONObject(response.body());
                JSONArray results = resultado.getJSONArray("results");
                System.out.println("\nIDs disponibles para " + tipo + ":");

                if (tipo.equals("episode")) {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject elemento = results.getJSONObject(i);
                        System.out.println("ID: " + elemento.getInt("id") + ", Nombre: " + elemento.getString("name") + ", Episodio: " + elemento.getString("episode"));
                    }
                } else {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject elemento = results.getJSONObject(i);
                        System.out.println("ID: " + elemento.getInt("id") + ", Nombre: " + elemento.getString("name"));
                    }
                }

                System.out.println();
            } else {
                System.out.println("Error al obtener los IDs disponibles para " + tipo + ". Codigo de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}