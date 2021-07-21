package springMvc.org.springframework.web.servlet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springMvc.org.springframework.context.support.ClassPathXmlApplicationContext;
import springMvc.org.springframework.web.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    public static final String  SERVLET_CONTEXT_PREFIX=DispatcherServlet.class.getName();

    public static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

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
        }catch (Exception e){
            throw new ServletException(e.getMessage());
        }
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
        doDispatcher(req, resp);
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
        doDispatcher(req, resp);
    }

    /**
     * 请求分发
     * @param req
     * @param resp
     */
    public void doDispatcher(HttpServletRequest req, HttpServletResponse resp) {

    }

}
