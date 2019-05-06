package com.ynw.system.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;


public class JsonUtils {

	/**
	 * 对象转字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String objectToString(Object object) {
		return JSON.toJSONString(object,SerializerFeature.WriteNullStringAsEmpty);
	}

	/**
	 * 字符串转对象
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object stringToObject(String jsonString, Class cla) {
		JSONObject jsonObject = JSON.parseObject(jsonString);
		Object obj = JSON.toJavaObject(jsonObject, cla);
		return obj;
	}

	/**
	 * 从标准Json字符串中获取String值
	 * 
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static String getStringValue(String jsonString, String key) {
		JSONObject jsonObject = JSON.parseObject(jsonString);
		boolean b = jsonObject.containsKey(key);
		if (b) {
			return jsonObject.getString(key);
		} else {
			return "";
		}
	}

	/**
	 * json字符串转成List<T>对象
	 * 
	 * @param jsonString
	 * @param cla
	 * @return
	 */
	public static <T> List<T> stringToList(String jsonString, Class cla) {
		List<T> list = new ArrayList<T>();
		list = JSON.parseArray(jsonString, cla);
		return list;
	}

	/**
	 * list集合转json字符串
	 * 
	 * @param list
	 * @return
	 */
	public static <T> String listToJsonString(List<T> list) {
		String jsonString = JSONArray.toJSONString(list);
		return jsonString;

	}

	/**
	 * 从Json字符串中获取int值
	 * 
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static int getIntValue(String jsonString, String key) {
		JSONObject jsonObject = JSON.parseObject(jsonString);
		boolean b = jsonObject.containsKey(key);
		if (b) {
			return jsonObject.getIntValue(key);
		} else {
			return 10000;
		}
	}

	/**
	 * 构建单个键值对的Json字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String keyValueToString(String key, Object value) {
		JSONObject json = new JSONObject();
		json.put(key, value);
		return json.toJSONString();
	}

	/**
	 * 构建含有两个键值对的Json字符串
	 * 
	 * @param key1
	 * @param value1
	 * @param key2
	 * @param value2
	 * @return
	 */
	public static String keyValueToString2(String key1, Object value1, String key2, Object value2) {
		JSONObject json = new JSONObject();
		json.put(key1, value1);
		json.put(key2, value2);
		return json.toJSONString();
	}

	public static String addToJson(String jsonString, String key, Object date) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		jsonObject.put(key, date);
		return jsonObject.toJSONString();
	}
}
