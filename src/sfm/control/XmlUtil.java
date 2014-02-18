package sfm.control;

import java.io.*;

import org.dom4j.*;
import org.dom4j.io.*;

public class XmlUtil {
	/**
	 * doc2String 将xml文档内容转为String
	 * 
	 * @return 字符串
	 * @param document
	 */
	public static String doc2String(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 使用GB2312编码
			OutputFormat format = new OutputFormat("  ", true, "GB2312");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("GB2312");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	/**
	 * string2Document 将字符串转为Document
	 * 
	 * @return
	 * @param s
	 *            xml格式的字符串
	 */
	public static Document string2Document(String s) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}

	/**
	 * doc2XmlFile 将Document对象保存为一个xml文件到本地
	 * 
	 * @return true:保存成功 flase:失败
	 * @param filename
	 *            保存的文件名
	 * @param document
	 *            需要保存的document对象
	 */
	public static boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			/* 将document中的内容写入文件中 */
			// 默认为UTF-8格式，指定为"GB2312"
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter writer = new XMLWriter(
					new FileWriter(new File(filename)), format);
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * string2XmlFile 将xml格式的字符串保存为本地文件，如果字符串格式不符合xml规则，则返回失败
	 * 
	 * @return true:保存成功 flase:失败
	 * @param filename
	 *            保存的文件名
	 * @param str
	 *            需要保存的字符串
	 */
	public static boolean string2XmlFile(String str, String filename) {
		boolean flag = true;
		try {
			Document doc = DocumentHelper.parseText(str);
			flag = doc2XmlFile(doc, filename);
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * load 载入一个xml文档
	 * 
	 * @return 成功返回Document对象，失败返回null
	 * @param uri
	 *            文件路径
	 */
	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(filename);
		} catch (Exception ex) {
			// 如果找不到文件就尝试创建一个文件
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			try {
				XMLWriter writer = new XMLWriter(new FileWriter(new File(
						filename)), format);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//ex.printStackTrace();
		}
		return document;
	}

	// 新增一个xml并保存的演示代码
	public void xmlWriteDemoByDocument() {
		/** 建立document对象 */
		Document document = DocumentHelper.createDocument();
		/** 建立config根节点 */
		Element configElement = document.addElement("config");
		/** 建立ftp节点 */
		configElement.addComment("东电ftp配置");
		Element ftpElement = configElement.addElement("ftp");
		ftpElement.addAttribute("name", "DongDian");
		/** ftp 属性配置 */
		Element hostElement = ftpElement.addElement("ftp-host");
		hostElement.setText("127.0.0.1");
		(ftpElement.addElement("ftp-port")).setText("21");
		(ftpElement.addElement("ftp-user")).setText("cxl");
		(ftpElement.addElement("ftp-pwd")).setText("longshine");
		ftpElement.addComment("ftp最多尝试连接次数");
		(ftpElement.addElement("ftp-try")).setText("50");
		ftpElement.addComment("ftp尝试连接延迟时间");
		(ftpElement.addElement("ftp-delay")).setText("10");
		/** 保存Document */
		doc2XmlFile(document, "src/xmlWriteDemoByDocument.xml");
	}
}
