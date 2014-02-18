package sfm.control;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {
//	public static void main(String[] args) {
//		AesUtil test = new AesUtil();
//		String sKey = "123";
//		String shaString = test.sha1(sKey);
//		String enString = test.encryptString(shaString, sKey);
//		String deString = test.decryptString(test.parseHexStr2Byte(enString), sKey);
//		System.out.println(shaString);
//		System.out.println(enString);
//		System.out.println(deString);
//		
//		
//	}
	/**
	 * 初始化 AES Cipher
	 * 
	 * @param sKey
	 * @param cipherMode
	 * @return
	 */
	public Cipher initAESCipher(String sKey, int cipherMode) {
		// 创建Key gen
		KeyGenerator keyGenerator = null;
		Cipher cipher = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] codeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
			cipher = Cipher.getInstance("AES");
			// 初始化
			cipher.init(cipherMode, key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (NoSuchPaddingException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (InvalidKeyException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return cipher;
	}

	
	/**
	 * 对文件进行AES加密
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param sKey
	 * @return
	 */
	public File encryptFile(File sourceFile, String fileType, String sKey) {
		// 新建临时加密文件
		File encrypfile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
			encrypfile = File.createTempFile(sourceFile.getName(), fileType);
			outputStream = new FileOutputStream(encrypfile);
			Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
			// 以加密流写入文件
			CipherInputStream cipherInputStream = new CipherInputStream(
					inputStream, cipher);
			byte[] cache = new byte[1024];
			int nRead = 0;
			while ((nRead = cipherInputStream.read(cache)) != -1) {
				outputStream.write(cache, 0, nRead);
				outputStream.flush();
			}
			cipherInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		return encrypfile;
	}

	/**
	 * AES方式解密文件
	 * 
	 * @param sourceFile
	 * @return
	 */
	public File decryptFile(File sourceFile, String fileType, String sKey) {
		File decryptFile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			decryptFile = File.createTempFile(sourceFile.getName(), fileType);
			Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(decryptFile);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(
					outputStream, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = inputStream.read(buffer)) >= 0) {
				cipherOutputStream.write(buffer, 0, r);
			}
			cipherOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		return decryptFile;
	}
	

	/**
	 * 对字符串进行AES加密
	 * 
	 * @param sourceString
	 * @param sKey
	 * @return 十六进制的字符串result
	 */
	public String encryptString(String sourcesString, String sKey) {
		Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
		byte[] result = null;
		byte[] btyeSourceString = null;
		try {
			btyeSourceString = sourcesString.getBytes("utf-8");
			result = cipher.doFinal(btyeSourceString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseByte2HexStr(result);
	}
	
	/**
	 * 对字符串进行AES解密
	 * 
	 * @param sourceByte
	 * @param sKey
	 * @return
	 */
	public String decryptString(byte[] sourceByte, String sKey) {
		Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
		byte[] result = null;
		try {
			result = cipher.doFinal(sourceByte);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
		return new String(result);
	}
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                    String hex = Integer.toHexString(buf[i] & 0xFF);
                    if (hex.length() == 1) {
                            hex = '0' + hex;
                    }
                    sb.append(hex.toUpperCase());
            }
            return sb.toString();
    }
    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                    return null;
            byte[] result = new byte[hexStr.length()/2];
            for (int i = 0;i< hexStr.length()/2; i++) {
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                    result[i] = (byte) (high * 16 + low);
            }
            return result;
    }
    /**sha1离散实现
     * @param sourceString
     * @return
     */
    public String sha1(String sourceString){
    	String result = null;
    	try {
			MessageDigest sha = MessageDigest.getInstance("SHA1");
			sha.update(sourceString.getBytes());
			result = parseByte2HexStr(sha.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
}
