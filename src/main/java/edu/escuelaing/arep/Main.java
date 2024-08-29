package edu.escuelaing.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap<>();
        //Cargar componentes
        if (c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);
                }
            }
        }
        urlTest(services);
    }

    private static void urlTest(Map<String, Method> services) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        //1
        URL servicesurl = new URL("http://localhost:8080/App/Estudiantes");
        String path = servicesurl.getPath();
        String servicename = path.substring(4);
        Method ms  = services.get(servicename);
        System.out.println(ms.invoke(null, null));
        //2
        servicesurl = new URL("http://localhost:8080/App/GetPi");
        path = servicesurl.getPath();
        servicename = path.substring(4);
        ms  = services.get(servicename);
        System.out.println(ms.invoke(null, null));
        //3
        servicesurl = new URL("http://localhost:8080/App/Hello");
        path = servicesurl.getPath();
        servicename = path.substring(4);
        ms  = services.get(servicename);
        System.out.println(ms.invoke(null, null));
    }
}