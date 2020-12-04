package cn.lps.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author: Yan Qiang
 * @className: AESUtils
 * @description: AES工具类
 * @date: 2018/5/9 14:49
 * @version: 1.0
 **/
@Slf4j
public class AESUtils {
    /**
     * 对AES的key进行RSA解密的私钥
     */
    public static final String AES_KEY_DECRYPT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ11NRdNPNsm6ANneWdS44eTumWn38kR" +
            "/ReqPKUGYdyU0uD3YJaHtuynRAyF1zR+mzFoT+mxYC3FIytrjGOyIy8/580LKkWVbkww9eVjXZSRDWzjvWNVdK8viSHDiBLM/LZxsBS3jy8" +
            "t1lv4zlHHohzFpzJM2mm9LfhrCVLfkjd9AgMBAAECgYBj1ZnY8WcBiPSYm/X01jBfmQIZTExuv5IafBzBgX9xDYd7jj3Wk6we9psF2aKurQuXUw" +
            "1AHe/edV0sPZ+g4qS9ZMuJa1PNFcdeXAUicImUxIAyWd/gnpqn1vTZQXx4ymKF55VY3FV+JiDbM1wb1MV20kSFbGPPi/63q7izAZRxPQJBANICQHiFr3znj" +
            "4ysRUCq9id1B944kfzq0Eti54B/b6AepvBQk/ctK8p7sZTiFGEsauyo/H7W0C+kPKq2YB0BqB8CQQC/8Mj8jU7pB6UOR/8J41YqCNL3hSKUS/Oq93Wn98kwNfej" +
            "QxlQQpo7S+u2R1BaA1pHkjtLaOCxYtk0EJeHcVzjAkBS45aku0c/inoLMPeIhbHwcu2vFS7x35BlIN10x1e8oDyNv5AXUFnnapj1xaH7lLeDP1OhkJHNLArR6nfXG" +
            "w9LAkAMfbYGwYd2INo8ALF3SkUsPSDFnPNwJTU5VhthD/4W1hxEkrROBdeVrk4rsZ5oDTnN2JVlRfEBekZaXg4OcXEzAkAJWXBCxfpc6ToqA1zZ/twhjZea8v2CzrSvGp" +
            "vwOYGE7ycIe9N5LcUmc1moF+YM7U/zYH+SpVHMGgzXsCO9PCcx";

    /**
     * 对AES的key进行RSA加密的公钥
     */
    public static final String AES_KEY_ENCRYPT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCddTUXTTzbJugDZ3lnUu" +
            "OHk7plp9/JEf0XqjylBmHclNLg92CWh7bsp0QMhdc0fpsxaE/psWAtxSMra4xjsiMvP+fNCypFlW5MMPXlY12UkQ1s471jVXSvL4khw4gSzPy2cbAUt48vLdZb+M5Rx6IcxacyTNppvS34awlS35I3fQIDAQAB";

    /**
     * @author: Yan Qiang
     * @descrption: 私有化构造器
     * @date: 2018/5/9 14:52
     * @param: []
     * @return:
     **/
    private AESUtils() {
    }


    /**
     * @author: Yan Qiang
     * @descrption: AES加密
     * @date: 2018/5/9 14:52
     * @param: [content, secretKey]
     * @return: java.lang.String
     **/
    public static String encrypt(String content, String secretKey) {
        try {
            byte[] key = secretKey.getBytes("utf-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");//设置key模式为AES
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);//设置当前模式为加密
            byte[] input = cipher.doFinal(content.getBytes("utf-8"));//加密并获取加密结果
            String result = Base64.getEncoder().encodeToString(input); // 使用Base64做转码功能，能起到2次加密的作用
            return result;
        } catch (Exception e) {
            log.error("AES加密异常", e);
        }
        return null;
    }

    /**
     * @author: Yan Qiang
     * @descrption: AES解密
     * @date: 2018/5/9 14:52
     * @param: [content, secretKey]
     * @return: java.lang.String
     **/
    public static String decrypt(String content, String secretKey) {
        try {
            byte[] key = secretKey.getBytes("utf-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");//设置key模式为AES
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);//设置当前模式为解密
            byte[] input = Base64.getDecoder().decode(content);// base64解码
            byte[] data = cipher.doFinal(input);//解密并获取结果
            String result = new String(data, "utf-8");//将结果转为字符串
            return result;
        } catch (Exception e) {
            log.error("AES解密异常", e);
        }
        return null;
    }

    /**
     * @author: Yan Qiang
     * @descrption: 对AES密钥进行RSA加密
     * @date: 2018/5/9 14:53
     * @param: [secretKey]
     * @return: java.lang.String
     **/
    public static String encryptAESKey(String secretKey, String publicKeyStr) {
        try {
            PublicKey publicKey = RSAUtils.loadPublicKey(publicKeyStr);//转为公钥对象
            byte[] b = RSAUtils.encrypt(secretKey.getBytes(), publicKey);//利用公钥加密
            return Base64.getEncoder().encodeToString(b);//base64编码并返回字符串
        } catch (Exception e) {
            log.error("RSA加密AES密钥异常", e);
        }
        return null;
    }

    /**
     * @author: Yan Qiang
     * @descrption: 对AES密钥进行RSA解密
     * @date: 2018/5/9 14:53
     * @param: [secretKey]
     * @return: java.lang.String
     **/
    public static String decryptAESKey(String secretKey, String privateKeystr) {
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(privateKeystr);//获取私钥对象
            byte[] b = Base64.getDecoder().decode(secretKey);//base64解码
            return new String(RSAUtils.decrypt(b, privateKey));//私钥解码并返回字符串
        } catch (Exception e) {
            log.error("RSA解密AES密钥异常", e);
        }
        return null;
    }


    /**
     * @author: Yan Qiang
     * @descrption: 生成AES密钥
     * @date: 2018/5/9 14:53
     * @param: []
     * @return: java.lang.String
     **/
    public static String getKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");//指定为AES模式，获取密钥生成器实例
            SecureRandom random = new SecureRandom();//随机生成器
            keyGenerator.init(128, random);//生成密钥
            SecretKey secretKey = keyGenerator.generateKey();//得到密钥
            return bytesToHex(secretKey.getEncoded());//转换为字符串
        } catch (Exception e) {
            log.error("生成AES密钥异常", e);
        }
        return null;
    }


    /**
     * @author: Yan Qiang
     * @descrption: 字节数组转字符串
     * @date: 2018/5/9 14:54
     * @param: [src]
     * @return: java.lang.String
     **/
    private static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * @author: Yan Qiang
     * @descrption: 测试方法
     * @date: 2018/5/9 14:54
     * @param: [args]
     * @return: void
     **/
    public static void main(String[] args) throws Exception {
        String secretKey = getKey();
        System.out.println("生成的密钥：" + secretKey);
        StringBuilder content = new StringBuilder("1234567{}}[][[{P_P_P+zFIEwhfeoidqhi!!!@#@@#@$@#892u3345345346743");
        System.out.println("待加密的明文：" + content.toString());
        Long startTime = System.currentTimeMillis();
        String encryptResult = encrypt(content.toString(), secretKey);
        Long endTime = System.currentTimeMillis();
        System.out.println("加密耗时：" + (endTime - startTime) + "ms");
        System.out.println("加密后的秘文：" + encryptResult);
        startTime = System.currentTimeMillis();
        String decryptResult = decrypt(encryptResult, secretKey);
        endTime = System.currentTimeMillis();
        System.out.println("解密耗时：" + (endTime - startTime) + "ms");
        System.out.println("解密后的明文：" + decryptResult);
        if (content.toString().equals(decryptResult)) {
            System.out.println("解密成功！");
        } else {
            System.out.println("解密失败");
        }
    }
}

