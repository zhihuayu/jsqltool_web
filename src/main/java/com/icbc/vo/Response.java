package com.icbc.vo;

public class Response {

	public final static String OK = "200";
	public final static String SERVER_ERROR = "500";
	public final static String DATA_ERROR = "400";

	private String code;
	private String msg;
	private Object data;

	public static Response OK(Object data) {
		Response re = new Response();
		re.setCode(OK);
		re.setData(data);
		return re;
	}

	public static Response FAIL(String code, String msg) {
		Response re = new Response();
		re.setCode(code);
		re.setMsg(msg);
		return re;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
