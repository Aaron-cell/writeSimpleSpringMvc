package springMvc.org.springframework.web.servlet;

import java.lang.reflect.Method;

public class HandlerExecutionChain {
    String[] url;
    Object controller;
    Method method;

    public HandlerExecutionChain(String[] url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
