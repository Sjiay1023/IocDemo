package com.ioc.framework;

/**
 * 属性 Bean
 * 用来封装 applicationContext.xml 文件中的<bean> 节点下的<property>节点信息
 */
public class PropertyBean {
    private String name;
    private String ref;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyBean [name=" + name + ", ref=" + ref + ", value="
                + value + "]";
    }
}
