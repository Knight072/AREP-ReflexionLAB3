package edu.escuelaing.arep;

public class Respuesta {
    public String status;
    public Usuario[] usuarios;

    public Respuesta(String status, Usuario[] usuarios) {
        this.status = status;
        this.usuarios = usuarios;
    }

}
