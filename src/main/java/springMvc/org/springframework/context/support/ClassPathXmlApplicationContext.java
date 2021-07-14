package springMvc.org.springframework.context.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.beans.BeanDefinition;
import springMvc.org.springframework.beans.RootBeanDefinition;
import springMvc.org.springframework.beans.factory.annotation.Autowired;
import springMvc.org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import springMvc.org.springframework.config.Configuration;
import springMvc.org.springframework.utils.Assert;
import springMvc.org.springframework.utils.BeanDefinitionUtil;
import springMvc.org.springframework.utils.XmlParseUtil;
import springMvc.org.springframework.web.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义一个容器 用于存放保存bean信息
 */
public class ClassPathXmlApplicationContext extends DefaultSingletonBeanRegistry implements WebApplicationContext {
    public volatile List<BeanDefinition> beanDefinitionList = new ArrayList<BeanDefinition>();


    public static Logger logger = LoggerFactory.getLogger(ClassPathXmlApplicationContext.class);

    String contextConfigLocation;

    Configuration configuration = null;
    public ClassPathXmlApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public void onRefresh() throws Exception {
        //解析配置文件
        analyzeConfig();

        //注册beanDefinition
        registerBeanDefinition();

        //创建Bean
        createBean();
    }

    /**
     * 实例化bean
     */
    private void createBean() throws InstantiationException, IllegalAccessException {
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            createBean(beanDefinition);
        }
    }

    /**
     * 实例化bean
     */
    private Object createBean(Class clazz) throws InstantiationException, IllegalAccessException {
        Object bean = null;
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            Class<?> beanClass = beanDefinition.getBeanClass();

            if(!clazz.isInterface()){
                if(beanClass.equals(clazz)){
                    bean = createBean(beanDefinition);
                }
            }else{
                Class[] clazzInterfaces = beanClass.getInterfaces();
                for(Class clazzInterface : clazzInterfaces){
                    if(clazzInterface.equals(clazz)){
                        bean = createBean(beanDefinition);
                    }
                }
            }
        }
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        //1.实例化
        Object bean = instanceBeanDefinition(beanDefinition);
        if(bean == null){
            throw new InstantiationError(beanDefinition.getBeanClass().getClass().getName()+" instance failed");
        }
        //2.属性赋值
        propertyVoluation(beanDefinition.getBeanName(),bean);
        this.registerSingleton(beanDefinition.getBeanName(),bean);
        return bean;
    }

    /**
     * 属性赋值
     * 暂时采用硬编码
     * @param bean
     */
    private void propertyVoluation(String beanName,Object bean) throws IllegalAccessException, InstantiationException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for(Field field : declaredFields){
            Autowired annotation = field.getAnnotation(Autowired.class);
            if(annotation !=null){
                Object singleton = getSingleton(beanName);
                if(singleton!=null){
                    field.setAccessible(true);
                    field.set(bean,singleton);
                }else{
                    Class<?> fieldType = field.getType();
                    Object autoWiredObject = createBean(fieldType);
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

    /**
     * 实例化Bean
     * @param beanDefinition
     */
    private Object instanceBeanDefinition(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        Object object = null;
        Class beanClass = beanDefinition.getBeanClass();
        object = beanClass.newInstance();
        return object;
    }

    /**
     * 注册BeanDefinition
     */
    private void registerBeanDefinition() throws Exception {
        List<Class> scanClassList = this.configuration.getScanClassList();
        for(Class clazz : scanClassList){
            if(BeanDefinitionUtil.hasComponent(clazz)){
                RootBeanDefinition beanDefinition = new RootBeanDefinition().build(clazz);
                this.beanDefinitionList.add(beanDefinition);
            }
        }
    }

    /**
     * 解析配置文件
     * 暂时只支持部分功能
     * example:classpath:springmvc.xml
     */
    public void analyzeConfig() throws Exception{
        Assert.notNull(contextConfigLocation,"contextConfigLocation must not null");
        String xmlName = contextConfigLocation.substring(contextConfigLocation.indexOf(":") + 1);
        Configuration config= new XmlParseUtil().build(xmlName);
        this.configuration = config;
    }


    public BeanDefinition getBeanDefinition(String beanName) {
        Assert.notNull(beanName,"beanName this field must not be empty");
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            if(beanName.equals(beanDefinition.getBeanName())){
                return beanDefinition;
            }
        }
        return null;
    }
}
