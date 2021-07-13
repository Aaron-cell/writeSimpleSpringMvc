package springMvc.org.springframework.beans;

import java.lang.annotation.Annotation;

public class RootBeanDefinition extends AbstractBeanDefinition{
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
