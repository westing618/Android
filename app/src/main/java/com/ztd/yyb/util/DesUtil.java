package com.ztd.yyb.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密解密
 * Created by chenjh on 2015/9/24.
 */
public class DesUtil {
    private static final String DESEncoding = "UTF-8";

    private static final String DES = "DES";

    private static final String MODEL = "DES/CBC/PKCS5Padding";

    private static final byte[] IV = { 1, 2, 3, 4, 5, 9, 7, 8 };// 加密的初始向量。

    private static final IvParameterSpec IV_PARAM = new IvParameterSpec(IV);

    /**
     * 解密，注意密码只能是8字节，是64位。
     *
     * @param decryptString
     *            要解密的密文,不能是空。
     * @param decryptKey
     *            密钥8个字节，是64位。
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] decryptedData = null;
        String resultString=null;
        try {
            byte[] byteMi = Base64Util.decode(decryptString);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(DESEncoding), DES);
            Cipher cipher = Cipher.getInstance(MODEL);
            cipher.init(Cipher.DECRYPT_MODE, key, IV_PARAM);
            decryptedData = cipher.doFinal(byteMi);
            resultString=new String(decryptedData,DESEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 加密，注意密码只能是8字节，是64位,算法是DES,模式是CBC,填充是PKCS5Padding。
     *
     * @param encryptString
     *            要加密的明文，不能是空。
     * @param encryptKey
     *            密钥8个字节，是64位。
     * @return
     * @throws Exception
     */
    public static String encryptDES(String encryptString, String encryptKey)throws Exception {
        byte[] encryptedData = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(IV);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(DESEncoding), DES);
            Cipher cipher = Cipher.getInstance(MODEL);
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            encryptedData = cipher.doFinal(encryptString.getBytes(DESEncoding));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64Util.encode(encryptedData);
    }

    public static void main(String[] arg){
        String text = "phpjYg2Q/cq2PVB4ZGpZsimzKxT4UbGP1ZbPVzFHLDitrPHyhsj3Gk4yP7D1vBQWWd96PH0Svdi18QPHVfgv2PU+OCpHyBvyDK0j0Jhq6MlXAijcEpsxSsL4Mp80PK4FL6CljYTssGC37Zx3/CYHrr9pml44QmnU4eKDpH5v0x3GtQYx7PxeWTZRF61Ja8SOVm/ZKAzrSFPOi3BJuY32uN9jp2WYRu9zDwtmFxGRkQykNuuvuYG2ZVA7UlsGkIUx+sx9PsD1qxuE9B6MWkau/g==";

        try {
            System.out.print(decryptDES(text, "ucJwldG5"));

            String encryptStr = encryptDES("TEST123", "ucJwldG5");
            System.out.print("加密后："+encryptStr);
            System.out.print("解密后："+decryptDES(encryptStr,"ucJwldG5"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
