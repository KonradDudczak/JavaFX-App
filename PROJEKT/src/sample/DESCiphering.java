package sample;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.lang.*;
import java.util.Base64;


public class DESCiphering extends Controller {
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static Cipher decryptCipherwithKey;
    private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };
    private static final String desktop = System.getProperty("user.home") + "\\Desktop";

    public static String  prepare() {

    try {
        SecretKey key = KeyGenerator.getInstance("DESede").generateKey();
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);


        decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        System.out.println(desktop);

        FileWriter fileWriter = new FileWriter(desktop +"\\key.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(encodedKey);

        printWriter.close();

        return encodedKey;
    }

    catch(InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
        e.printStackTrace();
        File file3 = new File(desktop+"\\key.txt");
        file3.delete();
    }
    return "err";
    }

    static int encryptmessage(String message)  {

        String clearTextFile =  desktop +"\\" + message + ".txt";
        String cipherTextFile = desktop +"\\encrypt.txt";

        try {
            encrypt(new FileInputStream(clearTextFile), new FileOutputStream(cipherTextFile));
            return 1;
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    static int decryptmessage()  {

        String cipherTextFile = desktop +"\\encrypt.txt";
        String clearTextNewFile = desktop+"\\deliver.txt";

        try {
            decrypt(new FileInputStream(cipherTextFile), new FileOutputStream(clearTextNewFile));
            System.out.println("DONE");
            return 1;
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    static int decryptmessagewithkey(String name, SecretKey newkey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {

        String cipherTextFile =desktop +"\\" + name+".txt";
        String clearTextNewFile = desktop +"\\deliver.txt";
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        decryptCipherwithKey = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decryptCipherwithKey.init(Cipher.DECRYPT_MODE, newkey, paramSpec);

        try {
            decryptwithkey(new FileInputStream(cipherTextFile), new FileOutputStream(clearTextNewFile));
            System.out.println("DONE");
            return 1;
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private static void encrypt(InputStream is, OutputStream os) throws IOException {

        os = new CipherOutputStream(os, encryptCipher);
        writeData(is, os);
    }

    private static void decrypt(InputStream is, OutputStream os) throws IOException {

        is = new CipherInputStream(is, decryptCipher);
        writeData(is, os);
        String contentToAppend = "\n\nDecrypted by 3DES";
        Files.write(
                Paths.get(desktop +"\\deliver.txt"),
                contentToAppend.getBytes(),
                StandardOpenOption.APPEND);
    }

    private static void decryptwithkey(InputStream is, OutputStream os) throws IOException {

        is = new CipherInputStream(is, decryptCipherwithKey);
        writeData(is, os);
        String contentToAppend = "\n\nDecrypted by 3DES";
        Files.write(
                Paths.get(desktop +"\\deliver.txt"),
                contentToAppend.getBytes(),
                StandardOpenOption.APPEND);
    }


    private static void writeData(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int numRead = 0;

        while ((numRead = is.read(buf)) >= 0) {
            os.write(buf, 0, numRead);
        }
        os.close();
        is.close();
    }



}