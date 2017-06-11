package edu.dsiedlarz.ParkingMeterAssistant.security2;

import edu.dsiedlarz.ParkingMeterAssistant.api.rest.TicketsApi;
import edu.dsiedlarz.ParkingMeterAssistant.security.PassiveCallbackHandler;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by private on 05.06.2017.
 */
@ManagedBean(name = "security", eager = true)
@SessionScoped
public class TestBean {
    final static Logger logger = Logger.getLogger(TestBean.class);

    public static void main() {

        String username = "dawid";
        String password = "dawid321";
        LoginContext lc = null;
        try {
//            lc = new LoginContext("gsageewrewqsfad", new JAASCallbackHandler(username, password));
            CallbackHandler cbh = new JAASCallbackHandler(username, password);
             lc = new LoginContext("example-jaas-realm", cbh);

            logger.info("context created");
            lc.login();
            logger.info("after login");
            //get the subject.
            Subject subject = lc.getSubject();
            //get principals
            subject.getPrincipals();
            logger.info("established new logincontext");
        } catch (LoginException e) {
            logger.error("Authentication failed " + e);
        }
    }
        }
