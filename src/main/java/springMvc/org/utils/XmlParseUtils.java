package springMvc.org.utils;

import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Xml解析工具类
 */
public class XmlParseUtils {
    public static Logger logger = LoggerFactory.getLogger(XmlParseUtils.class);

    public static String getPackageScan(String xmlFileName) throws Exception{
        InputStream resourceAsStream = XmlParseUtils.class.getClassLoader().getResourceAsStream(xmlFileName);
        if(resourceAsStream == null){
            logger.error("not found springMvc configFile,initialztion failed");
            throw new Exception("not found springMvc configFile,initialztion failed");
        }
        SAXReader saxParser = new SAXReader();
        saxParser.read(resourceAsStream);
        return null;
    }
}
