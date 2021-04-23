package Management;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHash {

    public static byte[] generateSalt(){
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    public static String generateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        //if (salt == null) salt = generateSalt();
        digest.update(salt);
        byte[] hashInBytes = digest.digest(password.getBytes());

        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[hashInBytes.length * 2];
        for (int i = 0; i < hashInBytes.length; i++) {
            int v = hashInBytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}

// NOTE - ADDING SALT DID NOT WORK AS EXPECTED. HENCE, I HAD TO REMOVE THAT.
