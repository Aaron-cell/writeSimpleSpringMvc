package springMvc.org.springframework.context.support;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.beans.AbstractBeanDefinition;
import springMvc.org.springframework.beans.BeanDefinition;
import springMvc.org.springframework.beans.RootBeanDefinition;
import springMvc.org.springframework.config.Configuration;
import springMvc.org.springframework.utils.BeanDefinitionUtil;
import springMvc.org.springframework.utils.XmlParseUtil;
import springMvc.org.springframework.web.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义一个容器 用于存放保存bean信息
 */
public class ClassPathXmlApplicationContext implements WebApplicationContext {
    public volatile List<BeanDefinition> beanDefinitionList = new ArrayList<BeanDefinition>();

    public volatile Map<String,Object> singletObjectMap = new HashMap<String, Object>();

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
            createBean((AbstractBeanDefinition) beanDefinition);
        }
    }

    private void createBean(AbstractBeanDefinition abstractBeanDefinition) throws IllegalAccessException, InstantiationException {
        //1.实例化
        Object bean = instanceBeanDefinition(abstractBeanDefinition);
        if(bean == null){
            throw new InstantiationError(abstractBeanDefinition.getBeanClass().getClass().getName()+" instance failed");
        }
        //2.属性赋值
        propertyVoluation(bean);

    }

    /**
     * 属性赋值
     * @param bean
     */
    private void propertyVoluation(Object bean) {

    }

    /**
     * 实例化Bean
     * @param beanDefinition
     */
    private Object instanceBeanDefinition(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        Object object = null;
        Class beanClass = (Class) beanDefinition.getBeanClass();
        object = beanClass.newInstance();
        return object;
    }

    /**
     * 注册BeanDefinition
     */
    private void registerBeanDefinition() {
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
        if(StringUtils.isEmpty(contextConfigLocation)){
            logger.error("contextConfigLocation must not null");
            throw new Exception("contextConfigLocation must not null");
        }
        String xmlName = contextConfigLocation.substring(contextConfigLocation.indexOf(":") + 1);
        Configuration config= new XmlParseUtil().build(xmlName);
        this.configuration = config;
    }


    public BeanDefinition getBeanDefinition(String beanName) {
        if(StringUtils.isEmpty(beanName)){
            throw new IllegalArgumentException("beanName this field must not be empty");
        }
        for(BeanDefinition beanDefinition : this.beanDefinitionList){
            if(beanName.equals(beanDefinition.getBeanName())){
                return beanDefinition;
            }
        }
        return null;
    }
}
