package com.icbc.utli;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ExportUtil {
	private ExportUtil() {
	}

	public static void ExportTxt(String fileName, StringBuilder content, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 采用中文文件名需要在此处转码
		fileName = new String((fileName+".txt").getBytes("UTF-8"), "ISO_8859_1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.getWriter().append(content.toString());
		response.flushBuffer();
	}

}
