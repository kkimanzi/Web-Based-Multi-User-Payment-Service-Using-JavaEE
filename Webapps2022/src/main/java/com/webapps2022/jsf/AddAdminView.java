package com.webapps2022.jsf;

import com.webapps2022.ejb.SendMoneyEJB;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;


import com.webapps2022.ejb.UserEJB;
import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.entity.User;
import java.math.BigDecimal;
import javax.faces.event.AjaxBehaviorEvent;

import com.webapps2022.jsf.JAX_RS_Client;

@ManagedBean
@SessionScoped
public class AddAdminView implements Serializable {

    private static final long serialVersionUID = 1685823449195612778L;
    private static final int STARTING_AMOUNT = 1000;
    
    private static Logger log = Logger.getLogger(AddAdminView.class.getName());
    
    @Inject
    private UserEJB adminEJB;
    @Inject
    private SendMoneyEJB sendMoneyEJB;
    
    private String name;
    private String adminname;
    private String password;
    private String confirmPassword;
    
    // Function to validate fields of for
    public void validatePassword(ComponentSystemEvent event) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();

        // get password
        UIInput uiPassword = (UIInput) components.findComponent("password");
        String password = uiPassword.getLocalValue() == null ? "" : uiPassword.getLocalValue().toString();
        String passwordId = uiPassword.getClientId();

        // get confirm password
        UIInput uiConfirmPassword = (UIInput) components.findComponent("confirmpassword");
        String confirmPassword = uiConfirmPassword.getLocalValue() == null ? ""
                : uiConfirmPassword.getLocalValue().toString();
        
        // get password
        UIInput uiAdminname = (UIInput) components.findComponent("adminname");
        String adminnameStr = uiAdminname.getLocalValue() == null ? "" : uiAdminname.getLocalValue().toString();
        
        log.info(adminnameStr);
        log.info(password);
        log.info(confirmPassword);
        
        // Handled by required
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage("Passwords do not match");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(passwordId, msg);
            facesContext.renderResponse();
        }

        // Check if adminname already exists
        if (adminEJB.findUserById(adminnameStr) != null) {
            FacesMessage msg = new FacesMessage("Admin with this adminname already exists");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(passwordId, msg);
            facesContext.renderResponse();
        }

    }

    // Function to register admin
    public void register() {
        User admin = new User(adminname, password);
        
        adminEJB.createUser(admin, "admins");
        log.info("New admin created with adminname: " + adminname + " and password: " + password);
   
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("New Admin Successfulyy Created");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        facesContext.addMessage("password", msg);
        facesContext.renderResponse();
    }
    
    
    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        AddAdminView.log = log;
    }

    public UserEJB getAdminEJB() {
        return adminEJB;
    }

    public void setAdminEJB(UserEJB adminEJB) {
        this.adminEJB = adminEJB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    
    
}
