package springMvc.org.springframework.utils;

import springMvc.org.springframework.beans.factory.annotation.Autowired;
import springMvc.org.springframework.stereotype.Component;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class BeanDefinitionUtil {
    static Class[] annotations0 = {Controller.class, Service.class,Component.class};

    static Class[] annotations1 = {Autowired.class};

    public static boolean hasComponent(Class clazz) throws Exception {
        if(clazz.isInterface()){
            throw new Exception(clazz.getName()+" must not is a interface");
        }
        for(Class annotationClass : annotations0){
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if(annotation!=null){
                return true;
            }
        }
        return false;
    }

}
