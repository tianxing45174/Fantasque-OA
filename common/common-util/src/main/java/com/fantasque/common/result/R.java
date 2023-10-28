/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.fantasque.common.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

//	public R setData(Object data) {
//		put("data",data);
//		return this;
//	}
//
//	//利用fastjson进行反序列化
//	public <T> T getData(String key,TypeReference<T> typeReference) {
//		Object data = get(key);	//默认是map
//		String jsonString = JSON.toJSONString(data);
//		T t = JSON.parseObject(jsonString, typeReference);
//		return t;
//	}

	public R() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static R error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static R error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, msg);
	}
	
	public static R error(HttpStatus status) {
		R r = new R();
		r.put("code", status.value());
		r.put("msg", status.getReasonPhrase());
		return r;
	}

	public static R error(HttpStatus status,String msg) {
		R r = new R();
		r.put("code", status.value());
		r.put("msg", msg);
		return r;
	}

	public static R error(ResultCodeEnum resultCodeEnum) {
		R r = new R();
		r.put("code", resultCodeEnum.getCode());
		r.put("msg", resultCodeEnum.getMessage());
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public Integer getCode() {
		return (Integer) this.get("code");
	}
}
