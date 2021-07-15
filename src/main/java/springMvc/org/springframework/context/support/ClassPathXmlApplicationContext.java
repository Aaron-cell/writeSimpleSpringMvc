package springMvc.org.springframework.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.beans.RootBeanDefinition;
import springMvc.org.springframework.config.Configuration;
import springMvc.org.springframework.utils.Assert;
import springMvc.org.springframework.utils.BeanDefinitionUtil;
import springMvc.org.springframework.utils.XmlParseUtil;

import java.util.List;

/**
 * 自定义一个容器 用于存放保存bean信息
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

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
     * 注册BeanDefinition
     */
    private void registerBeanDefinition() throws Exception {
        List<Class> scanClassList = this.configuration.getScanClassList();
        for(Class clazz : scanClassList){
            if(BeanDefinitionUtil.hasComponent(clazz)){
                String beanName = BeanDefinitionUtil.getBeanName(clazz);
                RootBeanDefinition beanDefinition = new RootBeanDefinition().build(clazz,beanName);
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
}
