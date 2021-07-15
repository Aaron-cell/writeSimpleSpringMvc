package springMvc.org.springframework.utils;

import org.apache.commons.lang3.StringUtils;
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

    /**
     * 如果Controller.class, Service.class,Component.class存在value 则返回beanName
     * @param clazz
     * @return
     */
    public static String getBeanName(Class clazz) {
        String beanName = null;
        for(Class annotationClass : annotations0){
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if(annotation!=null){
                if(annotation instanceof Controller && StringUtils.isNotEmpty(((Controller)annotation).value())){
                    beanName = ((Controller)annotation).value();
                }else if(annotation instanceof Service && StringUtils.isNotEmpty(((Service)annotation).value())){
                    beanName = ((Service)annotation).value();
                }else if(annotation instanceof Component && StringUtils.isNotEmpty(((Component)annotation).value())){
                    beanName = ((Component)annotation).value();
                }
            }
        }
        if(StringUtils.isEmpty(beanName)){
            String simpleName = clazz.getSimpleName();
            //驼峰命名
            String fristChar = simpleName.substring(0, 1);
            String lowerCase = fristChar.toLowerCase();
            beanName = lowerCase+simpleName.substring(1);
        }
        return beanName;
    }


}
