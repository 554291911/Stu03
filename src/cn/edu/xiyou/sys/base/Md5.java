package cn.edu.xiyou.sys.base;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
/**
 * @功能 进行密码的MD5加密及验证
 * @author lzp
 *
 */
public class Md5 {
	private static String salt = "xiyou";
	/**
	 * 密码加密
	 * 
	 * @param rawPass
	 *            未加密密码
	 * @return 加密后密码
	 */
	public static String encodePassword(String rawPass) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
		MessageDigest messageDigest = getMessageDigest();
		byte[] digest;
		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
		return new String(Hex.encodeHex(digest));
	}
	/**
	 * 验证密码是否正确
	 * 
	 * @param encPass
	 *            加密密码
	 * @param rawPass
	 *            未加密密码
	 * @return
	 */
	public static boolean isPasswordValid(String encPass, String rawPass) {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass);
		return pass1.equals(pass2);
	}

	private static final MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	private static String mergePasswordAndSalt(String password, Object salt,
			boolean strict) {
		if (password == null) {
			password = "";
		}
		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1)
					|| (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException(
						"Cannot use { or } in salt.toString()");
			}
		}
		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}
}
