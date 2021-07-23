package springMvc.org.springframework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private volatile Parameter parameter;

    private volatile Class<?> parameterType;

    private volatile Annotation[] parameterAnnotations;

    private volatile String parameterName;

    public MethodParameter(Parameter parameter, Class<?> parameterType, Annotation[] parameterAnnotations, String parameterName) {
        this.parameter = parameter;
        this.parameterType = parameterType;
        this.parameterAnnotations = parameterAnnotations;
        this.parameterName = parameterName;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(Annotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
