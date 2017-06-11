package edu.dsiedlarz.ParkingMeterAssistant.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by private on 11.06.2017.
 */
public class PasswordGenerator {

    public static void main(String[] args) {

        String password = "test";
//        String hashedPassword = Util.createPasswordHash("SHA",
//                Util.BASE16_ENCODING,
//                null,
//                null,
//                password);
//
//        System.out.println("skrót hasła: "+hashedPassword);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
            md.update(password.getBytes());


        String newpassword2 = String.format("%016x", new BigInteger(1, md.digest()));
        System.out.println("skrót hasła 1: "+ newpassword2);
        System.out.println("skrót hasła 2: "+ Base64.getEncoder().encodeToString(md.digest()));
        System.out.println("skrót hasła 3: "+encryptPassword(password));

    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(java.security.NoSuchAlgorithmException missing) {
            return "Error.";
        }
    }
}
