package springMvc.org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerExecutionChain getHandler(HttpServletRequest request);

    void setHandler(HandlerExecutionChain handlerExecutionChain);
}
