package springMvc.org.springframework.beans.factory.support;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    String[] getSingletonNames();

    boolean containsSingleton(String beanName);
}
