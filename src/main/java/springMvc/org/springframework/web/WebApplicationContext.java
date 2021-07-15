package springMvc.org.springframework.web;

import springMvc.org.springframework.beans.BeanDefinition;

public interface WebApplicationContext {
    void onRefresh() throws Exception;

    BeanDefinition getBeanDefinitionForType(Class clazz);

    Object createBean(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException;

}
