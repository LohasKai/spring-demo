package com.mhz.demo.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用反射生产对象
 */
public class BeanFactory {

    /**
     * 任务1 读取解析xml 通过反射技术实例化对象并且存储（map）
     * 任务2 对外提供实例对象接口（根据id获取）
     */
    private static Map<String, Object> map = new HashMap<>();

    static {
        //加载xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        //解析xml
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(resourceAsStream);
        Element rootElement = document.getRootElement();
        List<Element> beanList = rootElement.selectNodes("//bean");
        for (int i = 0; i < beanList.size(); i++) {
            Element element = beanList.get(i);
            //处理每个bean元素 获取到该元素的id和class
            String id = element.attributeValue("id");
            String aClass = element.attributeValue("class");
            //通过反射技术实例化对象
            Class<?> clazz = Class.forName(aClass);
            Object o = clazz.newInstance();

            //存储到map待用
            map.put(id, o);
        }

            //实例化对象后维护对象的依赖关系 检查哪些对象需要传值进入 根据配置  传值
            //有property子元素的bean有传值需求
            List<Element> list = rootElement.selectNodes("//property");
            for (int j = 0; j < list.size(); j++) {
                Element element = list.get(j);
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                //找到当前需要被处理的bean
                Element parent = element.getParent();

                //调用父元素的反射功能
                String parentId = parent.attributeValue("id");
                Object parentObject = map.get(parentId);
                //遍历父对象中的所有方法，找到“set”+name
                Method[] methods = parentObject.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("set"+name)){
                        method.invoke(parentObject, map.get(ref));
                    }
                }
                map.put(parentId, parentObject);
            }

            } catch (DocumentException | InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String id){
        return map.get(id);
    }
}
