package springMvc.org.springframework.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

    public void registerSingleton(String beanName, Object singletonObject) {

    }

    public Object getSingleton(String beanName) {
        return null;
    }

    public String[] getSingletonNames() {
        return new String[0];
    }

    public boolean containsSingleton(String beanName) {
        return false;
    }
}
