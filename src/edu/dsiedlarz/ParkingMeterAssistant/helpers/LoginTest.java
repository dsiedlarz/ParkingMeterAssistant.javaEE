package edu.dsiedlarz.ParkingMeterAssistant.helpers;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Created by private on 05.06.2017.
 */
public class LoginTest {

    public static void main(String[] args) throws LoginException {
        LoginContext lc = new LoginContext("MyExample");
        try {
            lc.login();
        } catch (LoginException e ){
            e.printStackTrace();
//            System.out.println("")
            // Authentication failed.
        }
        // Authentication successful, we can now continue.
        // We can use the returned Subject if we like.
        Subject sub = lc.getSubject();
//        Subject.doAs(sub, new MyPrivilegedAction());
    }
}
