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
public class RegisterView implements Serializable {

    private static final long serialVersionUID = 1685823449195612778L;
    private static final int STARTING_AMOUNT = 1000;
    
    private static Logger log = Logger.getLogger(RegisterView.class.getName());
    
    @Inject
    private UserEJB userEJB;
    @Inject
    private SendMoneyEJB sendMoneyEJB;
    
    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String currency;
    private String starting_balance = String.valueOf(STARTING_AMOUNT);
    
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
        UIInput uiUsername = (UIInput) components.findComponent("username");
        String usernameStr = uiUsername.getLocalValue() == null ? "" : uiUsername.getLocalValue().toString();
        
        log.info(usernameStr);
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

        // Check if username already exists
        if (userEJB.findUserById(usernameStr) != null) {
            FacesMessage msg = new FacesMessage("User with this username already exists");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(passwordId, msg);
            facesContext.renderResponse();
        }

    }

    // Function to register user
    public String register() {
        User user = new User(username, password, currency);
        
        userEJB.createUser(user, "users");
        log.info("New user created with username: " + username + " and password: " + password);
        
        // Transfer initial amount to this account
        if (!initializeAccountBalance()){
            log.info("Faailed to send initializing amount");
        }
        return "regdone";
    }
    
    // Function to convert from GB Pound to whichever currency of choice of the user
    public void currencyChangeListener(AjaxBehaviorEvent e){
        String response = String.valueOf(JAX_RS_Client.convertCurrency("GB Pound", getCurrency(), new BigDecimal(1000)));
        log.info(response);
        
        setStarting_balance(response);
    }

    // Function to send the original 1000 GBP to user account
    public boolean initializeAccountBalance(){
        // Current account
        User user = userEJB.findUserById(username);
        // Admin account
        User admin = userEJB.findUserById("admin1");
        
        BigDecimal initAmount = new BigDecimal(1000);
        
        // Transfer funds from admin to user account
        if (user != null && admin != null){
            MoneyTransfer transaction = new MoneyTransfer(
                admin.getUsername(), user.getUsername(), initAmount);
            sendMoneyEJB.sendMoney(transaction);
            return true;
        } else {
            return false;
        }
    }
    
    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        RegisterView.log = log;
    }

    public UserEJB getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(UserEJB userEJB) {
        this.userEJB = userEJB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }   

    public String getStarting_balance() {
        return starting_balance;
    }

    public void setStarting_balance(String starting_balance) {
        this.starting_balance = starting_balance;
    }
    
    
    
}
