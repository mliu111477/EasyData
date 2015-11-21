package com.easydata.core.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 *
 * */

/**
 * 对称和非对对称加密<br />
 * DES,PBE,RSA,DH,DSA,ECC<br />
 * 对称加密算法des/aes<br />
 * 非对称加密RSA<br />
 *
 * @author Mr.Pro
 */
public class SymmetryEncryptUtils {
	public static final String ALGORITHM = "DES"; 
	/**
	 * DES_转换密钥
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key desToKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}
	/**
	 * DES_解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] desDecrypt(byte[] data, String key) throws Exception {
		Key k = desToKey(OneWayEncryptUtils.decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * DES_加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] desEncrypt(byte[] data, String key) throws Exception {
		Key k = desToKey(OneWayEncryptUtils.decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * DES_生成密钥
	 * @return
	 * @throws Exception
	 */
	public static String desInitKey() throws Exception {
		return desInitKey(null);
	}
	/**
	 * DES_生成密钥
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String desInitKey(String seed) throws Exception {
		SecureRandom secureRandom = null;
		if (seed != null) {
			secureRandom = new SecureRandom(OneWayEncryptUtils.decryptBASE64(seed));
		} else {
			secureRandom = new SecureRandom();
		}
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);
		SecretKey secretKey = kg.generateKey();
		return OneWayEncryptUtils.encryptBASE64(secretKey.getEncoded());
	}
	public static void main(String[] arge) throws Exception{
		//des加密与解密
		String inputStr = "up366";
		String key = desInitKey();
		System.err.println("原文:\t" + inputStr);
		System.err.println("密钥:\t" + key);
		byte[] inputData = inputStr.getBytes();
		inputData = desEncrypt(inputData, key);
		System.err.println("加密后:\t" + OneWayEncryptUtils.encryptBASE64(inputData));
		byte[] outputData =desDecrypt(inputData, key);
		String outputStr = new String(outputData);
		System.err.println("解密后:\t" + outputStr);
	}
}
