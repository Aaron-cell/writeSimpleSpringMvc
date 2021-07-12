package springMvc.org.springframework.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Xml解析工具类
 */
public class XmlParseUtils {
    public static Logger logger = LoggerFactory.getLogger(XmlParseUtils.class);

    public Configuration build(String xmlName) throws Exception {
        InputStream resourceAsStream = null;
        try{
            resourceAsStream = XmlParseUtils.class.getClassLoader().getResourceAsStream(xmlName);
            if(resourceAsStream == null){
                logger.error("not found springMvc configFile,initialztion failed");
                throw new IOException("not found springMvc configFile,initialztion failed");
            }
            SAXReader saxParser = new SAXReader();
            Document document = saxParser.read(resourceAsStream);
            return parseXml(document);
        }catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }finally {
            if(resourceAsStream!=null){
                resourceAsStream.close();
            }
        }
    }

    /**
     * 解析配置文件
     * @param document
     * @return
     */
    private Configuration parseXml(Document document) throws ClassNotFoundException {
        Configuration configuration = new Configuration();
        Element rootElement = document.getRootElement();
        //解析 component-scan
        Element componentScanElement = rootElement.element("component-scan");
        parseComponentScan(componentScanElement,configuration);

        return configuration;
    }

    /**
     * 解析component
     * 支持多个包路径 逗号分割
     * 支持路径 testCode.*
     * @param element
     * @param configuration
     */
    private void parseComponentScan(Element element, Configuration configuration) throws ClassNotFoundException {
        if(element == null){
            return;
        }

        Set<String> classFileRelativePath = new HashSet<String>();
        Attribute attribute = element.attribute("base-package");
        String componentScan = attribute.getText();
        //根据逗号切割
        String[] componentScans = componentScan.split(",");
        for(String path : componentScans){
            path = path.replaceAll("\\.","/");
            if(path.endsWith("*")){
                path = path.substring(0,path.lastIndexOf("/"));
            }

            URL url = this.getClass().getClassLoader().getResource(path);
            String urlPath = url.getFile();
            File file = new File(urlPath);
            if(file.isFile()){
                logger.warn(path+" is File,it should is a directory");
            }else{
                getClassFile(file,classFileRelativePath,path);
            }
        }

        //加载class类
        for(String loadClassName : classFileRelativePath){
            Class<?> clazz = Class.forName(loadClassName);
            configuration.setScanClassList(clazz);
        }
    }

    /**
     * 递归寻找class
     * @param file
     * @param set
     * @param suffix
     */
    private void getClassFile(File file,Set<String> set, String suffix) {
        if(file.isFile()){
            if(file.getName().endsWith(".class")){
                String filePath = file.getPath();
                String classPath = filePath.substring(filePath.indexOf(suffix), filePath.indexOf(".class"));
                String os = System.getProperty("os.name");
                if(os.toLowerCase().startsWith("win")){
                    classPath = classPath.replaceAll("\\\\","\\.");
                }else{
                    classPath = classPath.replaceAll("/","\\.");
                }
                set.add(classPath);
            }
            return;
        }else{
            File[] files = file.listFiles();
            for(File file1:files){
                getClassFile(file1,set,suffix);
            }
        }
    }
}
