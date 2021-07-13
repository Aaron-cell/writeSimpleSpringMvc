package springMvc.org.springframework.beans;

import java.lang.annotation.Annotation;

public class RootBeanDefinition implements BeanDefinition{

    private volatile Object beanClass;

    private String beanName;

    private volatile Annotation[] annotations;

    public Object getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Object beanClass) {
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

    public RootBeanDefinition build(Class beanClass){
        Annotation[] annotations = beanClass.getAnnotations();
        return build(beanClass,beanClass.getName(),annotations);
    }

    public RootBeanDefinition build(Class beanClass,String beanName,Annotation[] annotations){
        this.beanClass = beanClass;
        this.beanName = beanName;
        this.annotations = annotations;
        return this;
    }
}
