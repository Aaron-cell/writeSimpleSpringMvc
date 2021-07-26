package springMvc.org.springframework.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

public class CommonUtil {

    /**
     * <h1>是否是基本数据类型</h1>
     *
     * @param clazz
     * @return boolean
     */
    public final static boolean isBaseType(Class clazz) {
        if (clazz.isPrimitive()
                || clazz == String.class
                || clazz == Integer.class
                || clazz == Double.class
                || clazz == Float.class
                || clazz == Long.class
                || clazz == Boolean.class
                || clazz == Byte.class
                || clazz == Short.class) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据Clazz 做对应类型转换
     * @param clz
     * @param o
     * @param <T>
     * @return
     */
    public static  <T> T typeConversion(Class<T> clz,Object o) throws Exception {
        try{
            if(clz.isInstance(o)){
                //判断能否做直接转换
                return clz.cast(o);
            }else if(isBaseType(clz)){
                //硬编码
                if(clz == Integer.class || clz==int.class){
                    o = Integer.parseInt(o.toString());
                }else if(clz == Double.class || clz==double.class){
                    o = Double.parseDouble(o.toString());
                }else if(clz == Float.class || clz==float.class){
                    o = Float.parseFloat(o.toString());
                }else if(clz == Long.class || clz==long.class){
                    o = Long.parseLong(o.toString());
                }else if(clz == Boolean.class || clz == boolean.class){
                    o = Boolean.parseBoolean(o.toString());
                }else if(clz == Byte.class || clz == byte.class){
                    o = Byte.parseByte(o.toString());
                }else if(clz == Short.class || clz == short.class){
                    o = Short.parseShort(o.toString());
                }
            }
            return (T)o;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("param value "+o.toString()+" can not cast "+clz.getName());
        }
    }

    /**
     * 为引用类型注入属性(暂不支持父类的属性注入)
     * @param clz
     * @param req
     * @return
     */
    public static Object setReferenceTypeAttr(Class<?> clz, HttpServletRequest req) throws Exception {
        Field[] declaredFields = clz.getDeclaredFields();
        Object instance = clz.newInstance();
        for(Field field : declaredFields){
            String fieldName = field.getName();
            String fieldValue = req.getParameter(fieldName);
            if(StringUtils.isNotEmpty(fieldValue)){
                Class<?> fieldType = field.getType();
                Object value = typeConversion(fieldType, fieldValue);
                field.setAccessible(true);
                field.set(instance,value);
            }
        }
        return instance;
    }
}
