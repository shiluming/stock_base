package com.dsxx.base.aspect;

import com.dsxx.base.annotaion.FieldsFilter;
import com.dsxx.base.annotaion.NoReflection;
import com.dsxx.base.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现Controller @FieldsFilter注解的过滤字段功能
 * @author slm
 * @date 2017/9/29
 */
@Order(1)
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyResponseBodyAdvice.class);

    /**
     * 包含项
     */
    private List<String> includes = new ArrayList<>();
    /**
     * 排除项
     */
    private List<String> excludes = new ArrayList<>();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        //重新初始化为默认值
        includes = new ArrayList<>();
        excludes = new ArrayList<>();
        if (methodParameter.getMethod().isAnnotationPresent(FieldsFilter.class)) {
            //获取注解配置的包含和去除字段
            FieldsFilter fieldsFilter = methodParameter.getMethodAnnotation(FieldsFilter.class);
            includes = new ArrayList<>(Arrays.asList(fieldsFilter.includes()));
            excludes = new ArrayList<>(Arrays.asList(fieldsFilter.excludes()));
        } else {//没有加注解的则不做处理!!!!!!!!!
            return obj;
        }

        Object tmp;
        ResultBean resultBean = null;

        //如果返回的对象是ResultBean则特殊处理，只过滤ResultBean内的Data对象中的属性
        if (obj instanceof ResultBean){
            resultBean = (ResultBean) obj;
            tmp = resultBean.getData();
            if (tmp == null){
                return resultBean;
            }
        }else {
            tmp = obj;
        }

        //判断返回的对象是单个对象，还是list，还是map
        if (tmp == null) {
            return null;
        }

        Object retObj = null;

        try {
            if (tmp instanceof List) {
                //List
                List list = (List) tmp;
                retObj = handleList(list);
            }else if (tmp instanceof Enum){
                //Enum
                retObj = tmp;
            }else {
                //Single Object
                retObj = handleSingleObject(tmp);
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
        if (obj instanceof ResultBean){
          resultBean.setData(retObj);
          return resultBean;
        }
        return retObj;
    }

    /**
     * 处理返回值是单个enity对象
     *
     * @param o
     * @return
     */
    private Object handleSingleObject(Object o) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(16);
        if (o instanceof Map) {
            //如果是Map需要特殊处理
            Map m = (Map) o;
            m.forEach((key, value) -> {
                if (includes.size() == 0 && excludes.size() == 0) {
                    map.put(key.toString(), value);
                } else if (includes.size() > 0) {
                    if (includes.contains(key.toString())) {
                        map.put(key.toString(), value);
                    }
                } else {
                    if (excludes.size() > 0) {
                        if (!excludes.contains(key.toString())) {
                            map.put(key.toString(), value);
                        }
                    }
                }
            });
        } else {
            List<Field> fieldList = new ArrayList<>() ;
            Class tmpClass = o.getClass();
            String objClassName = "java.lang.Object";
            // 获取所有字段，包括父类的，除了Object父类
            while (tmpClass !=null && !objClassName.equalsIgnoreCase(tmpClass.getName())){
                fieldList.addAll(new ArrayList<>(Arrays.asList(tmpClass.getDeclaredFields())));
                tmpClass = tmpClass.getSuperclass();
            }
            for (Field field : fieldList) {
                //设置可以访问private修饰的字段
                field.setAccessible(true);
                // 静态字段去掉
                if (Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                // 加了这个注解的不序列化
                NoReflection nr = field.getAnnotation(NoReflection.class);
                if(nr!=null){
                    continue;
                }
                //如果未配置表示全部的都返回
                if (includes.size() == 0 && excludes.size() == 0) {
                    map.put(field.getName(), field.get(o));
                } else if (includes.size() > 0) {
                    //有限考虑包含字段
                    if (includes.contains(field.getName())) {
                        map.put(field.getName(), field.get(o));
                    }
                } else {
                    //去除字段
                    if (excludes.size() > 0) {
                        if (!excludes.contains(field.getName())) {
                            map.put(field.getName(), field.get(o));
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 处理返回值是列表
     *
     * @param list
     * @return
     */
    private List<Map> handleList(List list) throws IllegalAccessException {
        List<Map> retList = new ArrayList<>();
        for (Object o : list) {
            Map map = (Map) handleSingleObject(o);
            retList.add(map);
        }
        return retList;
    }
}
