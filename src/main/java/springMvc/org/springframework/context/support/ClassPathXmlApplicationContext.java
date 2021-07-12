package springMvc.org.springframework.context.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.web.WebApplicationContext;

import java.util.Collections;

/**
 * 自定义一个容器 用于存放保存bean信息
 */
public class ClassPathXmlApplicationContext implements WebApplicationContext {
    public static Logger logger = LoggerFactory.getLogger(ClassPathXmlApplicationContext.class);
    String contextConfigLocation=null;
    public ClassPathXmlApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public void onRefresh() throws Exception {
        //解析配置文件
        analyzeConfig();
    }

    /**
     * 解析配置文件
     * 暂时只支持下面方式
     * example:classpath:springmvc.xml
     */
    public void analyzeConfig() throws Exception{
        if(StringUtils.isEmpty(contextConfigLocation)){
            logger.error("contextConfigLocation must not null");
            throw new Exception("contextConfigLocation must not null");
        }

        String configFileName = contextConfigLocation.substring(contextConfigLocation.indexOf(":") + 1);
    }
}
