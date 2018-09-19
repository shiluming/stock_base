package com.dsxx.base.util;

import com.dsxx.base.annotaion.NoReflection;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil
{
	/**
	 * 填充数据
	 * @param clazz
	 * @param crs
	 * @return
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	public static <T> T reflectionFillData(Class clazz, CachedRowSet crs)
	{
		try
		{
			T t = (T) clazz.newInstance();
			Field[] fields = getClassFields(clazz, true);
			String fieldsName;
			Object fieldsType;
			for (int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
				NoReflection nr = fields[i].getAnnotation(NoReflection.class);
				if(nr!=null)continue;
				try
				{
					fieldsName = fields[i].getName();
					fieldsType = fields[i].getType();
					if(fieldsType.equals(Date.class)){
						//Date
						fields[i].set(t,crs.getDate(fieldsName));
					}else if(fieldsType.equals(Double.class)){
						//Double
						fields[i].set(t,crs.getDouble(fieldsName));
					}else if(fieldsType.equals(Integer.class) || fieldsType.equals(int.class)){
						//Integer
						fields[i].set(t,crs.getInt(fieldsName));
					}else if(fieldsType.equals(Short.class)){
						//Shout
						fields[i].set(t,crs.getShort(fieldsName));
					}else if(fieldsType.equals(String.class)){
						//String
						String str = crs.getString(fieldsName);
						if("null".equals(str) || str==null){
							str = "";
						}
						fields[i].set(t,str);
					}else if(fieldsType.equals(Float.class)){
						//Float
						fields[i].set(t,crs.getFloat(fieldsName));
					}else if(fieldsType.equals(Long.class)){
						//Long
						fields[i].set(t,crs.getLong(fieldsName));
					}else if(fieldsType.equals(Byte.class)){
						//Byte
						fields[i].set(t,crs.getByte(fieldsName));
					}else if(fieldsType.equals(BigDecimal.class)){
						//BigDecimal
						fields[i].set(t,crs.getBigDecimal(fieldsName));
					}else if(fieldsType.equals(byte[].class)){
						//byte[]
						fields[i].set(t,crs.getBytes(fieldsName));
					}else if(fieldsType.equals(Boolean.class)){
						//Boolean
						fields[i].set(t,crs.getBoolean(fieldsName));
					}else if(fieldsType.equals(BigInteger.class)){
						//BigInteger
						fields[i].set(t,crs.getInt(fieldsName));
					}else{
						fields[i].set(t, crs.getObject(fieldsName));
					}
				}
				catch (Exception e)
				{
					System.out.println("报错字段名："+fields[i].getName()+" 类型："+fields[i].getType());
					
					e.printStackTrace();
					return null;
				}
			}
			return t;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 生成update语句 , 通过id修改
	 * @param o
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static String reflectionCreateUpdateSql(Object o,String tableName) throws Exception{
		Class c = o.getClass();
		String sql = "update "+ tableName +" set ";
		Field[] fields =getClassFields(c, true);
		String updateField = "";
		for (Field field : fields)
		{
			if(field.getName().equals("id"))continue;
			
			NoReflection nr = field.getAnnotation(NoReflection.class);
			if(nr!=null)continue;
			
			field.setAccessible(true);
			Object value = field.get(o);
			if(value != null){
				
				updateField +=field.getName()+"=";
				updateField = divisionFieldType(field,value,updateField);
			}
		}
		sql+=updateField.substring(0,updateField.length()-1);
		Field field = c.getDeclaredField("id");
		field.setAccessible(true);
		sql+=" where id ="+field.get(o);
		return sql;
	}

	/**
	 * 生成update语句 , 通过指定的条件字段修改 如 {"id","userid"}
	 * @param o
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static String reflectionCreateUpdateSql(Object o,String tableName,String[] conditions) throws Exception{
		Class c = o.getClass();
		String sql = "update "+ tableName +" set ";
		Field[] fields =getClassFields(c, true);
		String updateField = "";
		for (Field field : fields)
		{
			if(field.getName().equals("id"))continue;
			
			NoReflection nr = field.getAnnotation(NoReflection.class);
			if(nr!=null)continue;
			
			field.setAccessible(true);
			Object value = field.get(o);
			if(value != null){
				updateField +=field.getName()+"=";
				updateField = divisionFieldType(field,value,updateField);
			}
		}
		sql+=updateField.substring(0,updateField.length()-1);
		sql+=" where 1=1 ";
		for (int i = 0; i < conditions.length; i++)
		{
			Field field = c.getDeclaredField(conditions[i]);;
			field.setAccessible(true);
			if(field.getType().equals(Integer.class)){
				sql+=" and "+conditions[i]+" ="+field.get(o);
			}else{
				sql+=" and "+conditions[i]+" ='"+field.get(o)+"'";
			}
			
		}
		return sql;
	}
	 
	/**
	 * 生成add语句 ,
	 * @param o
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static String reflectionCreateAddSql(Object o,String tableName) throws Exception{
		Class c = o.getClass();
		String sql = "insert INTO "+ tableName +"  ";
		Field[] fields = getClassFields(c, true);
		String updateField = "";
		String updateFieldValue = "";
		for (Field field : fields)
		{
			if(field.getName().equals("id"))continue;
			
			NoReflection nr = field.getAnnotation(NoReflection.class);
			if(nr!=null)continue;
			
			field.setAccessible(true);
			Object value = field.get(o);
			if(value != null){
				updateField +=field.getName()+",";
				updateFieldValue = divisionFieldType(field,value,updateFieldValue);
			}
		}
		sql+=" ("+updateField.substring(0,updateField.length()-1)+") values";
		
		sql+=" ("+updateFieldValue.substring(0,updateFieldValue.length()-1)+") ";
		return sql;
	}
	
	/**
	 * 根据字段组装json数据
	 * @return
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	public static Map<String,Object> reflectionJsonData(Object o)
	{
		try
		{
			Class c = o.getClass();
			Field[] fields = getClassFields(c, true);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,Object> map = new HashMap<String,Object>();
			
			String fieldsName;
			Object fieldsType;
			for (int i = 0; i < fields.length; i++)
			{
				if(fields[i].getName().equals("serialVersionUID"))continue;
				
				fields[i].setAccessible(true);
				try
				{
					fieldsName = fields[i].getName();
					fieldsType = fields[i].getType();
					if(fieldsType.equals(Date.class)){

						if(fields[i].get(o) != null){
							map.put(fieldsName, sdf.format((Date)fields[i].get(o)));
						}else{
							map.put(fieldsName, "");
						}

					}else{
						map.put(fieldsName, fields[i].get(o));
					}
				}
				catch (Exception e)
				{
					System.out.println("reflectionJsonData 报错字段名："+fields[i].getName()+" 类型："+fields[i].getType());
					e.printStackTrace();
				}
			}
			return map;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @Title reflectionJsonDataList
	 * @author slm
	 * @param <T>
	 * @date 2017年6月23日 下午2:54:47 
	 * @Description TODO 封装reflectionJsonData()方法
	 * @param list
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public static <T> List<Map<String,Object>> reflectionJsonDataList(List<T> list){
		List<Map<String,Object>> result = new ArrayList<>();
		for(T obj:list){
			result.add(ReflectionUtil.reflectionJsonData(obj));
		}
		return result;
	}
	
	/**
	 * 区分不同类型，拼接不同语句
	 * @param field 字段
	 * @param value 值
	 * @param updateFieldValue 拼接串
	 * @return
	 */
	private static String divisionFieldType(Field field,Object value,String updateFieldValue){
		
		if(field.getType().equals(Integer.class)){
			updateFieldValue+=value.toString()+",";
		}else if(field.getType().equals(Double.class)){
			updateFieldValue+=value.toString()+",";
		}else if(field.getType().equals(Date.class)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			updateFieldValue+="'"+sdf.format((Date)value)+"',";
		}else{
			updateFieldValue+="'"+value.toString()+"',";
		}
		
		return updateFieldValue;
	}
	
	public static Object getClassInstance(Class<?> type) {
        try {
            Method method = type.getMethod("getInstance");
            // System.out.println(method.invoke(obj));
            return method.invoke(type);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 获取类实例的属性值
	 * @param clazz
	 *            类名
	 * @param includeParentClass
	 *            是否包括父类的属性值
	 * @return 属性数组
	 */
	public static Field[] getClassFields ( Class clazz, boolean includeParentClass )
	{
		Field[] fields = clazz.getDeclaredFields ( );
		if ( includeParentClass ){
			return getParentClassFields ( fields, clazz.getSuperclass ( ) );
		}
		return fields;
	}
	
	/**
	 * 获取类实例的父类的属性值
	 * @param map
	 *            类实例的属性值Map
	 * @param clazz
	 *            类名
	 * @return 属性数组
	 */
	public static Field[] getParentClassFields ( Field[] firstFields, Class clazz )
	{
		Field[] fields = clazz.getDeclaredFields ( );
		Field[] both = (Field[]) ArrayUtils.addAll(firstFields, fields);
		if ( clazz.getSuperclass ( ) == null )
		{
			return both;
		}
		return getParentClassFields ( both, clazz.getSuperclass ( ) );
	}
	
	/**
	 * 根据请求参数封装条件
	 * @param o
	 * @param request
	 * @return
	 */
	public static String getConditionByRequest(Class clazz,HttpServletRequest request){
		StringBuffer condition = new StringBuffer("");
		try
		{
			Field[] fields = getClassFields(clazz, true);
			
			String fieldsName;
			Object fieldsType;
			String fieldsValue;
			for (int i = 0; i < fields.length; i++)
			{
				if(fields[i].getName().equals("serialVersionUID"))continue;
				
				NoReflection nr = fields[i].getAnnotation(NoReflection.class);
				if(nr!=null)continue;
				
				fields[i].setAccessible(true);
				try
				{
					fieldsName = fields[i].getName();
					fieldsType = fields[i].getType();
					fieldsValue = request.getParameter(fieldsName);
					if(StringUtils.isNotBlank(fieldsValue)){
						if(!condition.toString().equals("")){
							condition.append(" and ");
						}
						if(fieldsType.equals(Date.class)){
							condition.append(fieldsName).append("=").append(fieldsValue);
						}else if(fieldsType.equals(Integer.class) 
								|| fieldsType.equals(Double.class)
								|| fieldsType.equals(Float.class)
								|| fieldsType.equals(Long.class)){
							condition.append(fieldsName).append("=").append(fieldsValue);
						}else if(fieldsType.equals(String.class)){
							condition.append(fieldsName).append(" like '").append(fieldsValue+"%'");
						}else{
							condition.append(fieldsName).append("=").append(fieldsValue);
						}
					}
				}
				catch (Exception e)
				{
					System.out.println("getConditionByRequest 报错字段名："+fields[i].getName()+" 类型："+fields[i].getType());
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return condition.toString();
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * @return
	 * */
	public static Object getter(Object obj, String att) {
		try {
			Method method = obj.getClass().getMethod("get" + Character.toUpperCase(att.charAt(0))+att.substring(1));
			//LogUtils.log.info(method.invoke(obj));
			return method.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * @param value
	 *            设置的值
	 * @param type
	 *            参数的属性
	 * */
	public static void setter(Object obj, String att, Object value,
							  Class<?> type) {
		try {
			Method method = obj.getClass().getMethod("set" + Character.toUpperCase(att.charAt(0))+att.substring(1), type);
			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
