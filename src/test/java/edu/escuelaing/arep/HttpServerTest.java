package edu.escuelaing.arep;

import edu.escuelaing.arep.lab1.RequestParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpServerTest {

    private HttpServer httpServer;

    @BeforeEach
    void setUp() {
        httpServer = HttpServer.getInstance();
    }

    // Clase simulada con el método de prueba
    public static class TestController {
        public static HashMap<String, byte[]> leerArchivos(@RequestParam(value = "name", defaultValue = "defaultName") String name) {
            HashMap<String, byte[]> response = new HashMap<>();
            response.put("text/html", ("Hello, " + name).getBytes());
            return response;
        }
    }

    @Test
    void testCallMethodWithValidQueryParams() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String method = "GET";
        String resource = "/App/leerArchivos?name=John";

        // Ejecutar el método del servidor directamente con la lógica esperada
        httpServer.callMethod(out, method, resource);

        // Convertir la respuesta de bytes a cadena
        String response = out.toString();

        // Validar la respuesta
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));
    }

    @Test
    void testCallMethodWithStaticFile() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String method = "GET";
        String resource = "/index.html";

        httpServer.callMethod(out, method, resource);

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));
    }

    @Test
    void testCallMethodWithUnsupportedMethod() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String method = "POST";
        String resource = "/App/leerArchivos";

        httpServer.callMethod(out, method, resource);

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 405 Method Not Allowed"));
        assertTrue(response.contains("Content-Type: text/plain"));
        assertTrue(response.contains("Método no permitido"));
    }

    @Test
    void testSendResponse() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        httpServer.sendResponse(out, "200 OK", "text/plain", "Hello".getBytes());

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/plain"));
        assertTrue(response.contains("Content-Length: 5"));
        assertTrue(response.contains("Hello"));
    }
}

