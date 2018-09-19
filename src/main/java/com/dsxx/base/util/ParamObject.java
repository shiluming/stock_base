package com.dsxx.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数对象，在SQL设值的时候可以用到
 * 
 * 
 */
public class ParamObject {
	private List<Object> list;

	public ParamObject() {
		list = new ArrayList<Object>();
	}

	public void add(Object object) {
		list.add(object);
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		if (list != null) {
			for (Object obj : list) {
				strBuf.append(obj.toString()).append(",");
			}
		}
		return strBuf.toString();
	}

	/**
	 * 给参数前后加上百分号
	 * 
	 * @param param
	 * @param prefix
	 *            前面是否加百分号
	 * @param end
	 *            后面是否加百分号
	 * @return
	 */
	public static String addParamLike(String param, boolean prefix, boolean end) {
		if (param == null) {
			return param;
		}
		if (prefix) {
			param = "%" + param;
		}
		if (end) {
			param += "%";
		}
		return param;
	}

}
