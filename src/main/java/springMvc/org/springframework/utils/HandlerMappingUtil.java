package springMvc.org.springframework.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.core.MethodParameter;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.web.bind.annotation.RequestMapping;
import springMvc.org.springframework.web.bind.annotation.ResponseBody;
import springMvc.org.springframework.web.servlet.HandlerExecutionChain;
import springMvc.org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
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
    public static void handleRequest(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain chain) throws InvocationTargetException, IllegalAccessException, IOException, ServletException {
        //参数封装
        List<MethodParameter> methodParameterList = wrapParameter(chain);
        //参数注入
        Object[] args = paramInject(chain,methodParameterList,req);
        //获取执行的返回结果
        Object result = chain.getMethod().invoke(chain.getController(),args);

        //判断是否适用@ResponseBody注解
        if(!isResponseBodyAnnotation(chain.getController().getClass(),chain.getMethod())){
            invokeNotResponseBodyHandler(req,resp,result);
        }else{
            invokeResponseBodyHandler(req,resp,result);
        }
    }

    /**
     * 封装参数
     * @param chain
     * @return
     */
    private static List<MethodParameter> wrapParameter(HandlerExecutionChain chain) throws IOException {
        Method method = chain.getMethod();
        /**
         * jdk1.8 提供了获取参数名的方法 但需要 编译时候必须有编译选项：javac -parameters打开，默认是关闭的
         * jdk1.7及以下 未提供获取参数名的方法
         * 故这里采用asm框架 实现该功能
         */
        List<String> methodParamNames = AsmUtil.getMethodParamNames(chain.getController().getClass(), chain.getMethod());
        List<MethodParameter> methodParameters = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for(int i=0;i<parameters.length;i++){
            Parameter parameter = parameters[i];
            methodParameters.add(new MethodParameter(parameter, parameter.getType(), null, methodParamNames.get(i)));
        }
        return methodParameters;
    }

    /**
     * 参数注入
     * @param chain
     * @param methodParameters
     * @param req
     * @return
     */
    private static Object[] paramInject(HandlerExecutionChain chain,List<MethodParameter> methodParameters, HttpServletRequest req) {

        return null;
    }

    /**
     * 在ResponseBody 时处理
     * @param req
     * @param resp
     * @param result
     */
    private static void invokeResponseBodyHandler(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException {
        String jsonData = JSON.toJSONString(result);
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.print(jsonData);
        writer.flush();
        writer.close();
    }

    /**
     * 不存在ResponseBody 时处理
     * @param req
     * @param resp
     * @param result
     */
    private static void invokeNotResponseBodyHandler(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException, ServletException {
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

    /**
     * 判断该方法是否使用 @ResponseBody注解
     * @param clazz
     * @Param method
     * @return
     */
    private static boolean isResponseBodyAnnotation(Class clazz,Method method) {
        boolean flag = false;
        Annotation annotation = clazz.getAnnotation(ResponseBody.class);
        if(annotation!=null){
            flag = true;
        }else {
            annotation = method.getAnnotation(ResponseBody.class);
            if(annotation!=null){
                flag = true;
            }
        }
        return flag;
    }
}
