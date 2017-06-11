package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.beans.ChangePasswordBean;
import edu.dsiedlarz.ParkingMeterAssistant.model.User;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;

/**
 * Created by private on 11.06.2017.
 */
@ManagedBean(name = "changePasswordBean", eager = true)
@SessionScoped
public class ChangePasswordManagedBean {

    @EJB
    private ChangePasswordBean changePasswordBean;

    public void changePassword(String user) {
        String password = "";
        changePasswordBean.changePassword(user, password);
    }

    public ArrayList<User> getUsers() {
        return changePasswordBean.getUsers();
    }

}
