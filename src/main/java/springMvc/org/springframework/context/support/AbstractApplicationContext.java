package springMvc.org.springframework.context.support;

import springMvc.org.springframework.beans.BeanDefinition;
import springMvc.org.springframework.beans.factory.annotation.Autowired;
import springMvc.org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import springMvc.org.springframework.utils.Assert;
import springMvc.org.springframework.web.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractApplicationContext extends DefaultSingletonBeanRegistry implements WebApplicationContext {

    public volatile List<BeanDefinition> beanDefinitionList = new ArrayList<BeanDefinition>();

    private Object context;

    /**
     * 根据Class类型 获取对应的beanDefinition
     */
    public BeanDefinition getBeanDefinitionForType(Class clazz){
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            Class<?> beanClass = beanDefinition.getBeanClass();
            if(!clazz.isInterface()){
                if(beanClass.equals(clazz)){
                    return beanDefinition;
                }
            }else{
                Class[] clazzInterfaces = beanClass.getInterfaces();
                for(Class clazzInterface : clazzInterfaces){
                    if(clazzInterface.equals(clazz)){
                        return beanDefinition;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 实例化Bean
     * @param beanDefinition
     */
    public Object newInstance(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        Class beanClass = beanDefinition.getBeanClass();
        return beanClass.newInstance();
    }

    /**
     * 实例化bean
     */
    public void createBean() throws InstantiationException, IllegalAccessException {
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            createBean(beanDefinition);
        }
    }

    public Object createBean(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        Assert.notNull(beanDefinition,"beanDefinition must not be empty");
        Object bean = this.getSingleton(beanDefinition.getBeanName());
        if(bean==null){
            //1.实例化
            bean = newInstance(beanDefinition);
            //2.属性赋值
            propertyVoluation(bean);
            this.registerSingleton(beanDefinition.getBeanName(),bean);
        }
        return bean;
    }

    /**
     * 属性赋值
     * 暂时采用硬编码
     * @param bean
     */
    public void propertyVoluation(Object bean) throws IllegalAccessException, InstantiationException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for(Field field : declaredFields){
            Autowired annotation = field.getAnnotation(Autowired.class);
            if(annotation !=null){
                Class<?> fieldType = field.getType();
                BeanDefinition beanDefinition = getBeanDefinitionForType(fieldType);
                //获取是否注入字段类型的bean
                Object singleton = getSingleton(beanDefinition.getBeanName());
                if(singleton!=null){
                    field.setAccessible(true);
                    field.set(bean,singleton);
                }else{
                    Object autoWiredObject = createBean(beanDefinition);
                    if(autoWiredObject!=null){
                        field.setAccessible(true);
                        field.set(bean,autoWiredObject);
                    }else{
                        if(annotation.required()!=false){
                            throw new InstantiationException(beanClass.getName()+" can not complete propertyVoluation cause by: not found "+fieldType.getName());
                        }
                    }
                }
            }
        }
    }

    public void setParentContext(Object context){
        this.context=context;
    }

    public Object getParentContext(){
        return this.context;
    }
}
