package com.icbc.utli;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ExportUtil {
	private ExportUtil() {
	}

	/**
	 *  导出文本类型文件，使用UTF-8编码
	* @author yzh
	* @date 2020年5月20日
	* @Description: 
	*  @param fileName 文件名称
	*  @param fileType 文件类型
	*  @param content  内容
	*  @param response 响应体
	*  @throws IOException    参数
	* @return void    返回类型
	* @throws
	 */
	public static void exportPlain(String fileName, String fileType, StringBuilder content,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 采用中文文件名需要在此处转码
		fileName = new String((fileName + "." + fileType).getBytes("UTF-8"), "ISO_8859_1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		ServletOutputStream out = response.getOutputStream();
		out.write(content.toString().getBytes("UTF-8"));
		out.close();
	}

}
