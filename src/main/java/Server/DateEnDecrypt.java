package Server;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DateEnDecrypt {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static byte[]  encryptFileToBytes(String key, File inputFile)
            throws Exception {
        return doCrypto(Cipher.ENCRYPT_MODE, key, inputFile);
    }

    public static byte[] decryptFileToBytes(String key, File inputFile)
            throws Exception {
        return doCrypto(Cipher.DECRYPT_MODE, key, inputFile);
    }
    public static void decryptBytesToFile(String key, File outputFile ,byte[] inputBytes) throws Exception{
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        writeBytesToFile(outputBytes,outputFile);
    }

    public static byte[] doCrypto(int cipherMode, String key, File inputFile
                                 ) throws Exception {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);
            inputStream.close();
            return outputBytes;


        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
    }
    public static void writeBytesToFile(byte[] outputBytes,File outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);
        outputStream.close();
    }
    public static void main(String [] args) throws Exception {
        byte[] result =encryptFileToBytes("secretKey@123456",new File("C:\\Users\\Majdi-pc\\Desktop\\a.txt"));
        decryptBytesToFile("secretKey@123456",new File("C:\\Users\\Majdi-pc\\Desktop\\b.txt"),result);
    }
}
