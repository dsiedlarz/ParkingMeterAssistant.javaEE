package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.bean.ChangePasswordBean;
import edu.dsiedlarz.ParkingMeterAssistant.model.User;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by private on 11.06.2017.
 */
@ManagedBean(name = "changePasswordBean", eager = true)
@SessionScoped
public class ChangePasswordManagedBean {

    @EJB
    private ChangePasswordBean changePasswordBean;

    public void changePassword(String user, String password) {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        changePasswordBean.changePassword(user, password);
    }

    public ArrayList<User> getUsers() {
        return changePasswordBean.getUsers();
    }
}
