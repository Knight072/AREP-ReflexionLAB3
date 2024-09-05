package edu.escuelaing.arep;

import edu.escuelaing.arep.lab1.RequestParam;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private final int port;
    private String baseStaticFileURI;
    private static HttpServer instance;

    public HttpServer() {
        this.port = 8080;
    }

    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor escuchando en el puerto " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            run(clientSocket);
        }
    }

    private void run(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            // Parsear la solicitud
            System.out.println(requestLine);
            String method = requestLine.split(" ")[0];
            String resource = requestLine.split(" ")[1];

            callMethod(out, method, resource);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void callMethod(OutputStream out, String method, String resource) throws IOException {
        // Lógica de manejo de solicitudes
        try {
            if (method.equals("GET")) {
                if (resource.startsWith("/App/leerArchivos")) {
                    // Extraer la parte de la URL después del '?'
                    String[] parts = resource.split("\\?");
                    String path = parts[0];
                    String queryString = (parts.length > 1) ? parts[1] : "";

                    // Construir un mapa de parámetros de consulta
                    Map<String, String> queryParams = new HashMap<>();
                    for (String param : queryString.split("&")) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2) {
                            queryParams.put(keyValue[0], keyValue[1]);
                        } else if (keyValue.length == 1) {
                            queryParams.put(keyValue[0], "");
                        }
                    }

                    // Obtener el método de servicio correspondiente
                    Method methodService = PathServices.findMappingMethod(path, method);

                    // Crear los argumentos para el método controlador
                    Object[] args = new Object[methodService.getParameterCount()];
                    Parameter[] parameters = methodService.getParameters();

                    for (int i = 0; i < parameters.length; i++) {
                        if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                            String paramName = requestParam.value();
                            String paramValue = queryParams.getOrDefault(paramName, requestParam.defaultValue());
                            args[i] = paramValue;
                        }
                    }

                    // Invocar el método de servicio con los parámetros
                    HashMap<String, byte[]> response = (HashMap<String, byte[]>) methodService.invoke(null, args);
                    System.out.println(response);
                    // Enviar la respuesta
                    for (String key : response.keySet()) {
                        if (response != null) sendResponse(out, "200 OK", key, response.get(key));
                    }
                } else if (resource.equals("/App/usuarios")) {
                    Method methodService = PathServices.findMappingMethod(resource, method);
                    String response = (String) methodService.invoke(null, null);
                    if (response != null) sendResponse(out, "200 OK", "application/json", response.getBytes());
                } else {

                    HashMap<String, byte[]> file = FileHandler.manejarArchivosEstaticos(resource);
                    for (String key : file.keySet()) {
                        sendResponse(out, "200 OK", key, file.get(key));
                    }
                }

            } else {
                sendResponse(out, "405 Method Not Allowed", "text/plain", "Método no permitido".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();

        out.write(content);
        out.flush();
    }

    public void setStaticFileLocation(String path) {
        baseStaticFileURI = path;
    }
}
