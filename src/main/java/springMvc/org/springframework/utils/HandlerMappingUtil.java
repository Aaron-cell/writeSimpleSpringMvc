package springMvc.org.springframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.web.bind.annotation.RequestMapping;
import springMvc.org.springframework.web.servlet.HandlerExecutionChain;
import springMvc.org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerMappingUtil {

    public static Logger logger = LoggerFactory.getLogger(HandlerMappingUtil.class);

    /**
     * 解析RequestMapping
     * @param entry
     * @param handlerMapping
     */
    public static void analyseRequestMapping(Map.Entry<String, Object> entry, HandlerMapping handlerMapping){
        Object value = entry.getValue();
        Class<?> clazz = value.getClass();
        if(clazz.isAnnotationPresent(Controller.class)){
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                if(annotation!=null){
                    String[] urls = annotation.value();
                    Assert.notNull(urls,"@RequestMapping attribute 'value' must not be empty");
                    HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(urls, value, method);
                    handlerMapping.setHandler(handlerExecutionChain);
                }
            }
        }
    }

    /**
     * 处理请求
     * @param req
     * @param resp
     * @param chain
     */
    public static void handleRequest(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain chain) throws InvocationTargetException, IllegalAccessException, ServletException, IOException {
        //获取执行的返回结果
        Object result = chain.getMethod().invoke(chain.getController());
        if(result!=null){
            if(result instanceof String){
                String str = result.toString();
                if(str.contains(":")){
                    String[] split = str.split(":");
                    String viewType = split[0];
                    String viewPage = split[1];
                    if(viewType.equals("forward")){
                        req.getRequestDispatcher(viewPage).forward(req,resp);
                    }else if(viewType.equals("redirect")){
                        resp.sendRedirect(viewPage);
                    }
                }else{
                    //默认请求转发
                    req.getRequestDispatcher(str).forward(req,resp);
                }

            }
        }
    }
}
