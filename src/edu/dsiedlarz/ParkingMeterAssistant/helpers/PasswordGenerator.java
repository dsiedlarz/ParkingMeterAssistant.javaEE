package edu.dsiedlarz.ParkingMeterAssistant.helpers;

import org.jboss.security.Util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by private on 11.06.2017.
 */
public class PasswordGenerator {

    public static void main(String[] args) {

        String hashedPassword = Util.createPasswordHash("MD5",
                Util.BASE64_ENCODING,
                null,
                null,
                "test");

        System.out.println("skrót hasła: "+hashedPassword);
    }
}
