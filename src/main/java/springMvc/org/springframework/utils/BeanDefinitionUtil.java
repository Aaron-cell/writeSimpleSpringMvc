package springMvc.org.springframework.utils;

import springMvc.org.springframework.stereotype.Component;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

public class BeanDefinitionUtil {
    static Class[] annotations0 = {Controller.class, Service.class,Component.class};

    public static boolean hasComponent(Class clazz) throws Exception {
        boolean isComponent = false;
        Class annotationClass1 = null;
        for(Class annotationClass : annotations0){
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if(annotation!=null){
                isComponent = true;
                annotationClass1 = annotationClass;
                break;
            }
        }

        if(isComponent && clazz.isInterface()){
            throw new Exception(clazz.getName()+" can not exist "+annotationClass1.getName());
        }
        return isComponent;
    }

}
