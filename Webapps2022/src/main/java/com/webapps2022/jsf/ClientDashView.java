package com.webapps2022.jsf;

import com.webapps2022.ejb.MoneyRequestEJB;
import com.webapps2022.ejb.MoneyTransferEJB;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;


import com.webapps2022.ejb.UserEJB;
import com.webapps2022.ejb.MoneyTransferEJB;
import com.webapps2022.entity.MoneyRequest;
import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.webapps2022.jsf.JAX_RS_Client;
import java.math.RoundingMode;

@ManagedBean
@SessionScoped
public class ClientDashView implements Serializable {

    private static final long serialVersionUID = 1685823449195612778L;

    private static Logger log = Logger.getLogger(RegisterView.class.getName());
      
    @Inject
    private MoneyTransferEJB transactionsEJB; 
    @Inject
    private MoneyRequestEJB requestsEJB;
    
    private String currencysymbol;
    private String accountbalance;
    
    private String numOfNotifications;
    private String numOfRequests;
    
    // Run from eventlistener for when page loads
    public void onPageLoad(){
        // Getting the number of new money received transactions
         //String transactions = transactionsEJB.getUserTransactions(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userProfile = (User)sessionMap.get("User");
        String user = userProfile.getUsername();
        
        List<MoneyTransfer> inboundUnreadTransfers = transactionsEJB.getUnreadInboundTransfers(user);

        numOfNotifications = " ("+String.valueOf(inboundUnreadTransfers.size())+")";

        // Get Account Balance
        BigDecimal balance = transactionsEJB.getBalance(user);
        // Convert balance from GB Pound used internally for the DB
        // To user's currency
        BigDecimal convertedBalance = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), balance);
        convertedBalance = convertedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        accountbalance = String.valueOf(convertedBalance);
        
        List<MoneyRequest> moneyRequests = requestsEJB.getPendingRequestsSentToMe(user);
        numOfRequests = " ("+String.valueOf(moneyRequests.size())+")";
        
        
    }
    
    public void refresh(){
        log.info("Inside refresh");
        //return "/dashboard?faces-redirect=true";
    }
        
    public String viewNotifications(){
        log.info("Going to view notifications");
        return "hey";
    }

    public String getCurrencysymbol() {
        return currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        this.currencysymbol = currencysymbol;
    }

    public String getAccountbalance() {
        return accountbalance;
    }

    public void setAccountbalance(String accountbalance) {
        this.accountbalance = accountbalance;
    }

    public String getNumOfNotifications() {
        return numOfNotifications;
    }

    public void setNumOfNotifications(String numOfNotifications) {
        this.numOfNotifications = numOfNotifications;
    }

    public String getNumOfRequests() {
        return numOfRequests;
    }

    public void setNumOfRequests(String numOfRequests) {
        this.numOfRequests = numOfRequests;
    }
    
    
    
    
}
