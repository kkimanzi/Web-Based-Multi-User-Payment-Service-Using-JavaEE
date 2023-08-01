package com.webapps2022.jsf;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.webapps2022.entity.User;

import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.ejb.MoneyTransferEJB;
import com.webapps2022.ejb.UserEJB;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.webapps2022.jsf.JAX_RS_Client;

@ManagedBean
@SessionScoped
public class SeeUsersView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());

    
    @Inject
    private UserEJB userEJB;
    
    private List<User> allUsers;
    // Parameter passed for current user
    private String username;
    
    // Refresh the notifications on page load
    public void onPageLoad(){
        
        allUsers = userEJB.getAllUsers();
        
    }

    // Redirect to page to view selected ser's transactions
    public String viewTransactions(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        // Get parameters passed from JSF
        Map<String,String> params = externalContext.getRequestParameterMap();
        
        username = params.get("username");
        log.info("From view transactions");
        log.info(username);
        return ("user_transactions?username="+username+"&faces-redirect=true");
    }
    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
 }