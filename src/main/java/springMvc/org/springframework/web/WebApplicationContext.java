package springMvc.org.springframework.web;

public interface WebApplicationContext {

    String contextConfigLocation = null;

    void onRefresh() throws Exception;
}
