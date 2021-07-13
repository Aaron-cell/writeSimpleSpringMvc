package springMvc.org.springframework.utils;

import springMvc.org.springframework.stereotype.Component;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

public class BeanDefinitionUtil {
    static Class[] annotations = {Controller.class, Service.class,Component.class};

    public static boolean hasComponent(Class clazz){
        for(Class annotationClass : annotations){
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if(annotation!=null){
                return true;
            }
        }
        return false;
    }
}
