package edu.redwoods.rbachibernate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Utilities {
    public static String convertByteToHexadecimal(byte[] byteArray)
    {
        StringBuilder hex = new StringBuilder();
        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex.append(String.format("%02X", i));
        }
        return hex.toString();
    }

    public static byte[] convertHexadecimalToByteArray(String hex)
    {
        // Initializing the hex string and byte array
        byte[] ans = new byte[hex.length() / 2];

        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            // Using parseInt() method of Integer class
            int val = Integer.parseInt(hex.substring(index, index + 2), 16); // Note radix 16 for hex!
            ans[i] = (byte)val;
        }
        return ans;
    }

    public static String genHexPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        // Converting byte arrays to hex-strings for easier storage.
        return Utilities.convertByteToHexadecimal(hash);
    }

    public static boolean isAuthenticated(String providedPassword, User u) {
        byte[] salt = Utilities.convertHexadecimalToByteArray(u.getSalt());
        String ppHash = null;
        try {
            ppHash = Utilities.genHexPassword(providedPassword, salt);
            return ppHash.equals(u.getPasswordHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Failed to hash provided password.");
        }
        return false;
    }
}
