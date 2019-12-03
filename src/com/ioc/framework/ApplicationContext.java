package com.ioc.framework;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IoC容器上下文
 */
public class ApplicationContext {
    private String filePath;

    private Map<String,ClassBean> classBeanMap = new HashMap<String, ClassBean>();

    private Map<String,Object> beanMap = new HashMap<String, Object>();

    public ApplicationContext(String filePath) {
        this.filePath = filePath;
        try {
            //1.解析spring.xml文件，读取 bean 的配置信息
            parseXml();
            //2.根据spring.xml的配置，实例化 bean 对象
            instanceBean();
            //3.根据spring.xml中属性注入的配置，完成对bean对象属性的注入
            beanToProperty();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据Bean的Id获取bean对象
     * @param beanId
     * @param <T>
     * @return
     */
    public <T> T getBean(String beanId){
        return (T) beanMap.get(beanId);
    }


    /**
     * 解析xml并把信息存入ClassBean对象中，以便后面通过反射实例和封装属性
     * 一个 ClassBean 保留了一个<bean> 节点的完整的数据信息
     */
    private void parseXml(){
        InputStream is = this.getClass().getResourceAsStream(filePath);
        SAXReader reader = new SAXReader();

        try {
            Document doc = reader.read(is);
            Element root = doc.getRootElement();

            //获取所有的<baen>节点元素
            List<Element> beanList = root.elements("bean");
            for(Element element : beanList){
                //实例化
                ClassBean bean = new ClassBean();

                String id = element.attributeValue("id");
                String clsName = element.attributeValue("class");
                //保存类的完整限定名
                bean.setClassName(clsName);
                //保存id
                bean.setId(id);

                Map<String,PropertyBean> propMap  = new HashMap<String, PropertyBean>();
                //<bean>节点下的<property>节点放入对应的bean中
                bean.setPropMap(propMap );

                // 把解析的classbean 放入map 映射
                classBeanMap.put(id,bean);

                //解析Property
                List<Element> propList = element.elements("property");
                for (Element prop : propList){
                   String name = prop.attributeValue("name");
                   String ref = prop.attributeValue("ref");
                   String value = prop.attributeValue("value");

                    // 实例化PropertyBean
                    PropertyBean propBean = new PropertyBean();
                    propBean.setName(name);
                    propBean.setRef(ref);
                    propBean.setValue(value);

                    //属性名和属性对象 映射
                    propMap.put(name,propBean);
                }
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }

    /**
     * 实例化bean对象
     */
    private void instanceBean(){
        //取出所有的key
        Set<String> ids = classBeanMap.keySet();
        for (String beanid : ids){
            ClassBean classBean = classBeanMap.get(beanid);
            String clsName = classBean.getClassName();

            //实例化类对象
            try{
                Object bean = Class.forName(clsName).newInstance();
                //放入beanmap
                beanMap.put(beanid,bean);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void beanToProperty()throws Exception{
        //准备遍历Map集合
        Set<Map.Entry<String,ClassBean>> set = classBeanMap.entrySet();
        for (Map.Entry<String,ClassBean> entry : set){
            String key = entry.getKey();//id
            ClassBean val = entry.getValue();//bean结构的类对象

            //取出classbean对应的bean对象，目前对象属性都为null
            Object obj = beanMap.get(key);

            // 从classBean 中取出  obj 需要赋值的属性
            Map<String,PropertyBean> propMap = val.getPropMap();
            Set<Map.Entry<String,PropertyBean>> propSet = propMap.entrySet();
            for (Map.Entry<String,PropertyBean> propNode : propSet){
                //属性名
                String propName = propNode.getKey();

                PropertyBean propBean = propNode.getValue();

                String value = propBean.getValue();//XML中的value
                String ref = propBean.getRef();

                // 根据属性名获取属性类型
                Field property = obj.getClass().getDeclaredField(propName);
                Class<?> cls = property.getType();

                //通过反射 把属性赋值给对象
                String setMethodName = "set"
                        + propName.substring(0,1).toUpperCase()
                        + propName.substring(1);
                //通过方法名 反射创建方法对象
                Method setMethod = obj.getClass().getDeclaredMethod(setMethodName,cls);

                //判断注入的是对象还是基本类型值
                if (ref != null){
                    //注入对象
                    setMethod.invoke(obj,beanMap.get(ref));
                }else{
                    //注入普通属性值
                    if (cls == int.class || cls == Integer.class){
                        Integer v = Integer.parseInt(value);
                        setMethod.invoke(obj,v);
                    }else if (cls == String.class){
                        setMethod.invoke(obj,value);
                    }else if (cls == float.class || cls == Float.class){
                        Float v = Float.parseFloat(value);
                        setMethod.invoke(obj,v);
                    }

                }
            }

        }
    }
}
