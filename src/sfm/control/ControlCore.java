package sfm.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import sfm.model.MainUser;
import sfm.model.SafeFile;
import sfm.view.MainFrame;

import org.dom4j.*;

public class ControlCore {
	private MainUser mainUser = new MainUser();
	public ArrayList<SafeFile> files = new ArrayList<SafeFile>();
	public AesUtil aesUtil = new AesUtil();
	private DefaultTableModel dtm;
	private String dafautSavePathString = "D:\\saveFile";
	private String donloadPathString = "D:\\";

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public void setDtm(DefaultTableModel dtm) {
		this.dtm = dtm;
	}

	// 读取文件存储的用户名和密码
	public ControlCore() {
		readXmlUserData();
	}

	/**
	 * 登陆验证 
	 * return 0  登陆失败
	 * return 1  登陆成功
	 */
	public int loginJudge(String userName, String password) {
		String unameSha1 = aesUtil.sha1(userName);
		String pdSha1 = aesUtil.sha1(password);

		String otherUnameSha1 = aesUtil.decryptString(aesUtil.parseHexStr2Byte(mainUser
				.getUserNameFinalString()), password);
		String otherPdSha1 = aesUtil.decryptString(aesUtil.parseHexStr2Byte(mainUser
				.getUserPasswordFinalString()), password);
		if (unameSha1.equals(otherUnameSha1)&&pdSha1.equals(otherPdSha1)) {
			mainUser.setUserName(userName);
			mainUser.setUserPassword(password);
			return 1;
		}  else {
			return 0;
		}
	}

	// 存储jtable的数据到xml文件当中
	public void saveXmlData() throws IOException {
		/** 建立document对象 */
		Document document = DocumentHelper.createDocument();
		/** 建立PassworManager根节点 */
		Element pmElement = document.addElement("SafeFileManager");
		Element pmUserElement = pmElement.addElement("SMTUser");
		pmUserElement.addAttribute("userName", encryptMsg(mainUser.getUserName(), mainUser.getUserPassword()));
		// 此处应该将用户密码hash存储
		pmUserElement.addAttribute("password", encryptMsg(mainUser.getUserPassword(), mainUser.getUserPassword()));
		for (SafeFile i : files) {
			Element accountElement = pmUserElement.addElement("SafeFile");
			accountElement.addAttribute("fileName", i.getFileName());
			accountElement.addAttribute("fileSize", i.getFileSize());
			accountElement.addAttribute("storeTime", i.getStoreTime());
			accountElement.addAttribute("storeName", i.getStoreName());
		}
		File directory = new File("");
		XmlUtil.doc2XmlFile(document, directory.getAbsolutePath()+"\\msgSave.xml");
	}

	// 读取xml文件的数据到内存和jtable当中
	public void readXmlFileData() {
		Document doc = XmlUtil.load(getClass().getResource("/msgSave.xml").toString());
		try {
			List list = doc.selectNodes("/SafeFileManager/SMTUser/SafeFile");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element saveFileElement = (Element) it.next();
				SafeFile file = new SafeFile();
				file.setFileName(saveFileElement.attribute("fileName")
						.getValue());
				file.setFileSize(saveFileElement.attribute("fileSize")
						.getValue());
				file.setStoreTime(saveFileElement.attribute("storeTime")
						.getValue());
				file.setStoreName(saveFileElement.attribute("storeName")
						.getValue());
				files.add(file);
				// 设置显示单位
				String fileSize = saveFileElement.attribute("fileSize")
						.getValue();
				fileSize = fileSizeStringTraslate(fileSize);

				dtm.addRow(new Object[] {
						saveFileElement.attribute("fileName").getValue(),
						fileSize,
						saveFileElement.attribute("storeTime").getValue(),
						saveFileElement.attribute("storeName").getValue() });
			}
		} catch (Exception e) {

		}
	}

	public void readXmlUserData() {
		File directory = new File("");
		File judgeXMLExist = new File(directory.getAbsolutePath()+"\\msgSave.xml");
		if (!judgeXMLExist.exists()) {

			/** 建立document对象 */
			Document document = DocumentHelper.createDocument();
			/** 建立PassworManager根节点 */
			Element pmElement = document.addElement("SafeFileManager");
			Element pmUserElement = pmElement.addElement("SMTUser");
			pmUserElement.addAttribute("userName","F539F9C17C0A66484A08B7C9CB5D3C839E09777B2C107EA9032AD3BD224565BA2B6954AF8AFBE250403325BD2B07D386");
			// 此处应该将用户密码hash存储
			pmUserElement.addAttribute("password","F539F9C17C0A66484A08B7C9CB5D3C839E09777B2C107EA9032AD3BD224565BA2B6954AF8AFBE250403325BD2B07D386");
			XmlUtil.doc2XmlFile(document, directory.getAbsolutePath()
					+ "\\msgSave.xml");

		}
		Document doc = XmlUtil.load(getClass().getResource("/msgSave.xml").toString());
		try {
			List list = doc.selectNodes("/SafeFileManager/SMTUser");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element userElement = (Element) it.next();
				String unameFinalString = userElement.attribute("userName")
						.getValue();
				String pdFinalString = userElement.attribute("password")
						.getValue();
				mainUser.setUserNameFinalString(unameFinalString);
				mainUser.setUserPasswordFinalString(pdFinalString);
			}
		} catch (Exception e) {

		}
	}

	// 将字节单位的大小转换为合适大小的单位
	public String fileSizeStringTraslate(String fileSize) {
		if (Integer.parseInt(fileSize) > 1024) {
			fileSize = String.valueOf(Integer.parseInt(fileSize) / 1024) + "KB";
		} else {
			fileSize = fileSize + "B";
		}
		return fileSize;
	}

	// 上传文件到默认文件夹，并加密（外部调用方法）
	public void updateFile(String filePathsString, String storeNameString) {
		// 判断默认文件夹是否存在，如果不存在就创建
		File defautPathFile = new File(dafautSavePathString);
		if (!defautPathFile.exists()) {
			defautPathFile.mkdir();
		}
		String storepathString = dafautSavePathString + "\\" + storeNameString;
		// copyFileFinishUpdate(filePathsString, storepathString);
		try {
			File sourceFile = new File(filePathsString);
			// 进行加密
			// AesUtil testAes = new AesUtil();
			File restulFile = aesUtil.encryptFile(sourceFile, ".temp",
					mainUser.getUserPassword());
			// 将加密得到的结果输出到savefile文件夹
			FileInputStream in;
			in = new FileInputStream(restulFile);
			File storeFile = new File(storepathString);// 创建一个空的内存文件
			if (!storeFile.exists()) // 如果文件不存在就先创建文件
				storeFile.createNewFile();
			FileOutputStream out = new FileOutputStream(storeFile);// 创建一个文件输出流，就是写文件的管道
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				out.write(buffer);
			}
			in.close();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 下载文件时(外部调用方法)
	public void donloadFile(int fileId) {
		SafeFile safeFile = files.get(fileId);
		String fileNameString = safeFile.getFileName();
		String fileStoreName = safeFile.getStoreName();

		String fileStoreNamePathString = dafautSavePathString + "\\"
				+ fileStoreName;
		String fileNamePathString = donloadPathString + fileNameString;

		// copyFileFinishDoload(fileStoreNamePathString,fileNamePathString);
		try {
			// AESUtils.decryptFile(mainUser.getUserPassword(),
			// fileStoreNamePathString, fileNamePathString);
			File sourceFile = new File(fileStoreNamePathString);
			// 进行加密
			// AesUtil testAes = new AesUtil();
			File restulFile = aesUtil.decryptFile(sourceFile, ".temp",
					mainUser.getUserPassword());
			// 将加密得到的结果输出到savefile文件夹
			FileInputStream in;
			in = new FileInputStream(restulFile);
			File storeFile = new File(fileNamePathString);// 创建一个空的内存文件
			if (!storeFile.exists()) // 如果文件不存在就先创建文件
				storeFile.createNewFile();
			FileOutputStream out = new FileOutputStream(storeFile);// 创建一个文件输出流，就是写文件的管道
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				out.write(buffer);
			}
			in.close();
			out.close();
			MainFrame.alertMsg("下载完成", "文件已下载到D盘根目录。");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加密传入的消息（先sha1哈希,再用Aes加密哈希后的内容）
	 * 
	 * @param sourceString
	 * @param key
	 * @return
	 */
	public String encryptMsg(String sourceString, String key) {
		String sha1sString = aesUtil.sha1(sourceString);
		String enString = aesUtil.encryptString(sha1sString, key);
		return enString;
	}

	/**
	 * 解密传入的消息（直接用Aes解密传入内容）
	 * 
	 * @param sourceString
	 * @param key
	 * @return
	 */
	public String decryptMsg(String sourceString, String key) {
		String deString = aesUtil.decryptString(sourceString.getBytes(), key);
		return deString;
	}

	public String getUserName() {
		return mainUser.getUserName();
	}

	public String getDafautSavePathString() {
		return dafautSavePathString;
	}

	public void setDafautSavePathString(String dafautSavePathString) {
		this.dafautSavePathString = dafautSavePathString;
	}

}
