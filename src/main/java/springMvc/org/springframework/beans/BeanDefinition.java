package springMvc.org.springframework.beans;

import java.lang.annotation.Annotation;

public interface BeanDefinition {

    Object getBeanClass();

    void setBeanClass(Object beanClass);

    String getBeanName();

    void setBeanName(String beanName);

    Annotation[] getAnnotations();

    void setAnnotations(Annotation[] annotations);
}
