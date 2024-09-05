package edu.escuelaing.arep;

import edu.escuelaing.arep.lab1.GetMapping;
import edu.escuelaing.arep.lab1.RequestParam;
import edu.escuelaing.arep.lab1.RestController;

import java.util.HashMap;

@RestController
public class RestServices {
    @GetMapping("/App/usuarios")
    public static String getUsers() {
        try {
            return UsuarioHandler.mostrarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/")
    public static HashMap<String, byte[]> getStatics(String resource) {
        try {
            return FileHandler.manejarArchivosEstaticos(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/App/leerArchivos")
    public static HashMap<String, byte[]> getArchivos(@RequestParam(value = "nombre") String name) {
        try {
            return FileHandler.manejarArchivosEstaticos("/"+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
