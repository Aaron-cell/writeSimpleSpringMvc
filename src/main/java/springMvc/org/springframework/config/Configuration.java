package springMvc.org.springframework.config;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    /*
       保存
     */
    private List<Class> scanClassList = new ArrayList<Class>();

    public List<Class> getScanClassList() {
        return scanClassList;
    }

    public void setScanClassList(Class clazz) {
        this.scanClassList.add(clazz);
    }
}
