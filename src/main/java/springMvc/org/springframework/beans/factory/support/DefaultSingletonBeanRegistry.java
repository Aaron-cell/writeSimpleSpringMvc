package springMvc.org.springframework.beans.factory.support;

import springMvc.org.springframework.utils.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

    public void registerSingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName,"Bean name must not be null");
        Assert.notNull(singletonObject, "Singleton object must not be null");
        synchronized (this.singletonObjects){
            Object oldObject = this.singletonObjects.get(beanName);
            if(oldObject!=null){
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
        }
    }

    public Object getSingleton(String beanName) {
        Assert.notNull(beanName,"Bean name must not be null");
        Object bean = null;
        synchronized (this.singletonObjects) {
            bean = this.singletonObjects.get(beanName);
        }
        return bean;
    }

    public String[] getSingletonNames() {
        return new String[0];
    }

    public boolean containsSingleton(String beanName) {
        return false;
    }
}
