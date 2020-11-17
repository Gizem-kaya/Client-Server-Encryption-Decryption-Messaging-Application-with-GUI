import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class Crypto 
{
    private static Crypto instance = null;
    private Key AES_key;
    private Key DES_key;
    private String AES_key_base64;
    private String DES_key_base64;
    private String AES_IV_base64;
    private String DES_IV_base64;
    private byte[] DES_IV;
    private byte[] AES_IV;
    Cipher des_cbc_cipher;
    Cipher des_ofb_cipher;
    Cipher aes_cbc_cipher;
    Cipher aes_ofb_cipher;

    public Crypto() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException
    {
        // key generation for DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecureRandom secRandom = new SecureRandom();
        keyGen.init(secRandom);
        DES_key = keyGen.generateKey();

        // DES/CBC mode
        des_cbc_cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        des_cbc_cipher.init(Cipher.ENCRYPT_MODE, DES_key);
        // creates initialization vector for DES mode
        DES_IV = des_cbc_cipher.getIV();
 
        // DES/OFB mode
        des_ofb_cipher = Cipher.getInstance("DES/OFB/PKCS5Padding");
        des_ofb_cipher.init(Cipher.ENCRYPT_MODE, DES_key);

        // key generation for AES algorithm
        KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
        SecureRandom secRandom2 = new SecureRandom();
        keyGen2.init(secRandom2);
        AES_key = keyGen2.generateKey();

        // AES/CBC mode
        aes_cbc_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes_cbc_cipher.init(Cipher.ENCRYPT_MODE, AES_key);
        // AES mode initialization vector
        AES_IV = aes_cbc_cipher.getIV();

        // DES/OFB mode
        aes_ofb_cipher = Cipher.getInstance("AES/OFB/PKCS5Padding");
        aes_ofb_cipher.init(Cipher.ENCRYPT_MODE, AES_key);

        // gets base64 encoded version of the keys and IV's
        DES_key_base64 = Base64.getEncoder().encodeToString(DES_key.getEncoded());
        DES_IV_base64 = Base64.getEncoder().encodeToString(DES_IV);
        AES_key_base64 = Base64.getEncoder().encodeToString(AES_key.getEncoded());
        AES_IV_base64 = Base64.getEncoder().encodeToString(AES_IV);

    }    

    public static Crypto getInstance() throws InvalidKeyException, 
                                            NoSuchAlgorithmException, NoSuchPaddingException 
    {
        if (instance == null)
            instance = new Crypto();
        return instance;
    }

    public String get_DES_key() { return DES_key_base64; }
    public String get_AES_key() { return AES_key_base64; }
    public String get_DES_IV() { return DES_IV_base64; }
    public String get_AES_IV() { return AES_IV_base64; }

    /** Encryption */
    public String encrypt(String text, boolean isDES, boolean isCBC)
            throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        Cipher cipher = null;

        if (isDES) // DES algorithm
        {
            if (isCBC)
                cipher = des_cbc_cipher;
            else
                cipher = des_ofb_cipher;
        }
        else // AES algorithm
        {
            if (isCBC)
                cipher = aes_cbc_cipher;
            else
                cipher = aes_ofb_cipher;
        }
        
        // encodes given string; stores the result as bytes
        byte[] encoded = cipher.doFinal(text.getBytes("UTF-8"));
        // encodes the text to readable base64 form
        String ciphertext = Base64.getEncoder().encodeToString(encoded);
        return ciphertext;
        
    }

    /** Decryption */
    public String decrypt(String text, boolean isDES, boolean isCBC) throws UnsupportedEncodingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Key key = null;
        byte[] iv = null;
        Cipher cipher = null;

        if (isDES) // DES algorithm
        {
            key = DES_key;
            iv = DES_IV;
            if (isCBC) // CBC mode
                cipher = des_cbc_cipher;
            else // OFB mode
                cipher = des_ofb_cipher;
        }
        else // AES algorithm
        {
            key = AES_key;
            iv = AES_IV;
            if (isCBC) // CBC mode
                cipher = aes_cbc_cipher;
            else // OFB mode
                cipher = aes_ofb_cipher;
        }

        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        byte[] plainbyte;
        // decode given text
        plainbyte = cipher.doFinal(Base64.getDecoder().decode(text.getBytes("UTF-8")));
        // write in UTF-8 format
        String decrypted = new String(plainbyte, "UTF-8");
        return decrypted;
    }
    
}
