package edu.escuelaing.arep;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UsuarioHandler {

    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("Juan", 28, "juan@example.com"));
        usuarios.add(new Usuario("Ana", 25, "ana@example.com"));
        usuarios.add(new Usuario("Luis", 30, "luis@example.com"));
    }

    public static String mostrarUsuarios() throws IOException {
        Respuesta respuesta = new Respuesta("success", usuarios.toArray(new Usuario[0]));
        return toJson(respuesta);
    }

    private static String toJson(Object obj) {
        if (obj instanceof Respuesta) {
            Respuesta resp = (Respuesta) obj;
            StringBuilder sb = new StringBuilder();
            sb.append("{\"status\":\"").append(resp.status).append("\", \"usuarios\":[");
            for (Usuario usuario : resp.usuarios) {
                sb.append(toJson(usuario)).append(",");
            }
            if (resp.usuarios.length > 0) sb.setLength(sb.length() - 1); // Eliminar la última coma
            sb.append("]}");
            return sb.toString();
        } else if (obj instanceof Usuario) {
            Usuario usuario = (Usuario) obj;
            return String.format("{\"nombre\":\"%s\", \"edad\":%d, \"email\":\"%s\"}", usuario.nombre, usuario.edad, usuario.email);
        }
        return "{}";
    }
}
