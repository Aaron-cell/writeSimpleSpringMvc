package springMvc.org.springframework.beans;

import java.lang.annotation.Annotation;

public abstract class AbstractBeanDefinition implements BeanDefinition{
    volatile Class beanClass;

    String beanName;

    volatile Annotation[] annotations;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
}
