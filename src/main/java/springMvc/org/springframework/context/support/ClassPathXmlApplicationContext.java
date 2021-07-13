package springMvc.org.springframework.context.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.beans.BeanDefinition;
import springMvc.org.springframework.beans.RootBeanDefinition;
import springMvc.org.springframework.config.Configuration;
import springMvc.org.springframework.utils.BeanDefinitionUtil;
import springMvc.org.springframework.utils.XmlParseUtil;
import springMvc.org.springframework.web.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义一个容器 用于存放保存bean信息
 */
public class ClassPathXmlApplicationContext implements WebApplicationContext {
    public volatile List<BeanDefinition> beanDefinitionList = new ArrayList<BeanDefinition>();

    public static Logger logger = LoggerFactory.getLogger(ClassPathXmlApplicationContext.class);
    String contextConfigLocation=null;
    Configuration configuration = null;
    public ClassPathXmlApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public void onRefresh() throws Exception {
        //解析配置文件
        analyzeConfig();

        //注册beanDefinition
        registerBeanDefinition();
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
        setConfiguration(config);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
