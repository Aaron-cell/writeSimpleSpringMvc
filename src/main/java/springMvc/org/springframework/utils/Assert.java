package springMvc.org.springframework.utils;

public class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(String[] str, String message) {
        if (str == null || str.length==0) {
            throw new IllegalArgumentException(message);
        }
    }
}
