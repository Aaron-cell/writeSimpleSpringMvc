package springMvc.org.springframework.web.servlet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.context.support.AbstractApplicationContext;
import springMvc.org.springframework.context.support.ClassPathXmlApplicationContext;
import springMvc.org.springframework.utils.HandlerMappingUtil;
import springMvc.org.springframework.web.WebApplicationContext;
import springMvc.org.springframework.web.servlet.mvc.method.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    public static final String  SERVLET_CONTEXT_PREFIX=DispatcherServlet.class.getName();

    public static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    public static HandlerMapping handlerMapping = new RequestMappingHandlerMapping();

    public DispatcherServlet() {

    }

    public String getServletContextAttributeName() {
        return SERVLET_CONTEXT_PREFIX + this.getServletName();
    }

    /**
     * 重写Servlet初始化方法
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        logger.info("start initialztion");
        try{
            //获取SpringMvc配置文件位置
            String contextConfigLocation = this.getServletConfig().getInitParameter("contextConfigLocation");
            //初始化一个容器
            WebApplicationContext applicationContext = new ClassPathXmlApplicationContext(contextConfigLocation);
            applicationContext.onRefresh();

            //Servlet容器和Spring容器双向绑定
            ServletContext servletContext = super.getServletContext();
            String attributeName = this.getServletContextAttributeName();
            servletContext.setAttribute(attributeName,applicationContext);
            applicationContext.setParentContext(servletContext);

            //初始化handleMapping
            initHandlerMapping();
            logger.info("initialztion success");
        }catch (Exception e){
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * /初始化handleMapping
     */
    private void initHandlerMapping() {
        logger.info("HandlerMaping initialztion start");
        String attributeName = this.getServletContextAttributeName();
        AbstractApplicationContext ac = (AbstractApplicationContext) super.getServletContext().getAttribute(attributeName);
        Map<String, Object> singletonObjects = ac.getSingletonObjects();
       for(Map.Entry<String, Object> entry : singletonObjects.entrySet()){
           HandlerMappingUtil.analyseRequestMapping(entry,this.handlerMapping);
       }
        logger.info("HandlerMaping initialztion success");
    }

    /**
     * 重写Get方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写Post方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求分发
     * @param req
     * @param resp
     */
    public void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取Handler
        HandlerExecutionChain chain = handlerMapping.getHandler(req);
        if(chain==null){
            resp.getWriter().print("<h1>404 NOT  FOUND!</h1>");
        }else{
            HandlerMappingUtil.handleRequest(req,resp,chain);
        }
    }

}
