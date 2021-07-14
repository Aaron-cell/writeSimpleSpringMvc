package springMvc.org.springframework.beans;

import java.lang.annotation.Annotation;

public interface BeanDefinition {

    Class getBeanClass();

    void setBeanClass(Class beanClass);

    String getBeanName();

    void setBeanName(String beanName);

    Annotation[] getAnnotations();

    void setAnnotations(Annotation[] annotations);
}
