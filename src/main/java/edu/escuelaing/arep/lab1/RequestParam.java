package edu.escuelaing.arep.lab1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // La anotación se aplicará a los parámetros de métodos
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución
public @interface RequestParam {
    String value(); // El valor que representará el nombre del parámetro
    String defaultValue() default ""; // Un valor por defecto si no se proporciona el parámetro
}
