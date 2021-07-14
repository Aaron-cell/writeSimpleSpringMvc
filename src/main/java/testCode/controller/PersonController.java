package testCode.controller;

import springMvc.org.springframework.beans.factory.annotation.Autowired;
import springMvc.org.springframework.stereotype.Controller;
import testCode.service.PersonService;

@Controller
public class PersonController {
    @Autowired
    PersonService personService;

    public void test(){
        personService.test();
    }
}
