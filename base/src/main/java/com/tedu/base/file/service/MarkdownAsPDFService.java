package com.tedu.base.file.service;

import java.io.IOException;

public interface MarkdownAsPDFService {
	/**
	 * markdown转html
	 * @param inputUrl   markdown的路径
	 * @param outUrl	html的输出路径
	 * @return
	 * @throws IOException 
	 */
	public  String mdToHtml(String inputUrl,String outUrl) throws IOException;
	/**
	 * html转pdf
	 * @param srcPath	html的url
	 * @param destPath		pdf的输出路径
	 * @return
	 */
	public  boolean convert(String srcPath, String destPath);
}
