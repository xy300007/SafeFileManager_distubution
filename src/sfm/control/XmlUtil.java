package sfm.control;

import java.io.*;

import org.dom4j.*;
import org.dom4j.io.*;

public class XmlUtil {
	/**
	 * doc2String ��xml�ĵ�����תΪString
	 * 
	 * @return �ַ���
	 * @param document
	 */
	public static String doc2String(Document document) {
		String s = "";
		try {
			// ʹ�������������ת��
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// ʹ��GB2312����
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
	 * string2Document ���ַ���תΪDocument
	 * 
	 * @return
	 * @param s
	 *            xml��ʽ���ַ���
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
	 * doc2XmlFile ��Document���󱣴�Ϊһ��xml�ļ�������
	 * 
	 * @return true:����ɹ� flase:ʧ��
	 * @param filename
	 *            ������ļ���
	 * @param document
	 *            ��Ҫ�����document����
	 */
	public static boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			/* ��document�е�����д���ļ��� */
			// Ĭ��ΪUTF-8��ʽ��ָ��Ϊ"GB2312"
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
	 * string2XmlFile ��xml��ʽ���ַ�������Ϊ�����ļ�������ַ�����ʽ������xml�����򷵻�ʧ��
	 * 
	 * @return true:����ɹ� flase:ʧ��
	 * @param filename
	 *            ������ļ���
	 * @param str
	 *            ��Ҫ������ַ���
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
	 * load ����һ��xml�ĵ�
	 * 
	 * @return �ɹ�����Document����ʧ�ܷ���null
	 * @param uri
	 *            �ļ�·��
	 */
	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(filename);
		} catch (Exception ex) {
			// ����Ҳ����ļ��ͳ��Դ���һ���ļ�
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

	// ����һ��xml���������ʾ����
	public void xmlWriteDemoByDocument() {
		/** ����document���� */
		Document document = DocumentHelper.createDocument();
		/** ����config���ڵ� */
		Element configElement = document.addElement("config");
		/** ����ftp�ڵ� */
		configElement.addComment("����ftp����");
		Element ftpElement = configElement.addElement("ftp");
		ftpElement.addAttribute("name", "DongDian");
		/** ftp �������� */
		Element hostElement = ftpElement.addElement("ftp-host");
		hostElement.setText("127.0.0.1");
		(ftpElement.addElement("ftp-port")).setText("21");
		(ftpElement.addElement("ftp-user")).setText("cxl");
		(ftpElement.addElement("ftp-pwd")).setText("longshine");
		ftpElement.addComment("ftp��ೢ�����Ӵ���");
		(ftpElement.addElement("ftp-try")).setText("50");
		ftpElement.addComment("ftp���������ӳ�ʱ��");
		(ftpElement.addElement("ftp-delay")).setText("10");
		/** ����Document */
		doc2XmlFile(document, "src/xmlWriteDemoByDocument.xml");
	}
}
