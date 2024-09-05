package edu.escuelaing.arep;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    public static HashMap<String, byte[]> manejarArchivosEstaticos(String resource) throws IOException {
        HashMap<String, byte[]> parts = new HashMap<>();
        // Definir el archivo por defecto como index.html si la solicitud es "/"
        String filePath = "target/classes/public" + resource;
        if (resource.equals("/")) {
            filePath += "index.html";
        }

        // Crear un Path para el archivo
        Path path = Paths.get(filePath);

        System.out.println(filePath);
        // Verificar si el archivo existe
        if (Files.exists(path)) {
            parts.put(getContentType(path), Files.readAllBytes(path));
            return parts;
        }
        return null;
    }

    public static String getContentType(Path resource) throws IOException {
        // Determinar el tipo MIME del archivo
        return Files.probeContentType(resource);
    }

}
