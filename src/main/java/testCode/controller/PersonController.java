package testCode.controller;

import springMvc.org.springframework.beans.factory.annotation.Autowired;
import springMvc.org.springframework.stereotype.Controller;
import springMvc.org.springframework.web.bind.annotation.RequestMapping;
import springMvc.org.springframework.web.bind.annotation.RequestMethod;
import testCode.service.PersonService;

@Controller
public class PersonController {
    @Autowired
    PersonService personService;

    @RequestMapping(value = "/test",method = {RequestMethod.GET})
    public void test(){
        personService.test();
    }

    @RequestMapping(value = "/hello",method = {RequestMethod.GET})
    public String hello(){
        return "success.jsp";
    }

}
