package springMvc.org.springframework.web.servlet.mvc.method;

import springMvc.org.springframework.web.servlet.HandlerExecutionChain;
import springMvc.org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class RequestMappingHandlerMapping implements HandlerMapping {

    List<HandlerExecutionChain> handlerExecutionChainList = new ArrayList<HandlerExecutionChain>();

    public HandlerExecutionChain getHandler(HttpServletRequest request) {
        HandlerExecutionChain chain = null;
        String uri = request.getRequestURI();
        first:for(HandlerExecutionChain handlerExecutionChain : handlerExecutionChainList){
            String[] urls = handlerExecutionChain.getUrl();
            for(String url : urls){
                if(uri.equals(url)){
                    chain=handlerExecutionChain;
                    break first;
                }
            }
        }
        return chain;
    }

    public void setHandler(HandlerExecutionChain handlerExecutionChain) {
        handlerExecutionChainList.add(handlerExecutionChain);
    }
}
