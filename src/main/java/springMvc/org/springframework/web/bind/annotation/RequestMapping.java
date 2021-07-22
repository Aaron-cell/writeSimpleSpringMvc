package springMvc.org.springframework.web.bind.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {

    String[] value() default {};

    RequestMethod[] method() default {RequestMethod.GET};
}
