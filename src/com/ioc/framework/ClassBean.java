package com.ioc.framework;


import java.util.HashMap;
import java.util.Map;

/**
 * 用来封装解析 applicationContext.xml 文件中配置的<bean>节点及子节点<property信息>
 */
public class ClassBean {
    private String id;
    private String className;
    private Map<String,PropertyBean> propMap = new HashMap<String, PropertyBean>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, PropertyBean> getPropMap() {
        return propMap;
    }

    public void setPropMap(Map<String, PropertyBean> propMap) {
        this.propMap = propMap;
    }

    @Override
    public String toString() {
        return "ClassBean [id=" + id + ", className=" + className
                + ", propMap=" + propMap + "]";
    }
}
